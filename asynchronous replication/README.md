# Asynchronous physical streaming replication
WAL(Write-Ahead Log): PostgreSQL writes the log before updating the database. Suppose, 2 server primary and replica. If Postgres copied the whole db every second that would be in efficient. Intead it sends only the wal records. 

```bash
                    Primary
              ┌────────────────┐
              │                │
              │ PostgreSQL     │
              │                │
              ├────────────────┤
              │ Database Files │
              │                │
              │ WAL Files      │
              └────────────────┘
                       ▲
                       │
             WAL Sender Process
                       │
                Streaming
                       │
                       ▼
              ┌────────────────┐
              │    Replica     │
              │                │
              │ WAL Receiver   │
              │ WAL Replay     │
              └────────────────┘
```

```bash
                 PostgreSQL Server
               ┌───────────────────┐
               │                   │
               │ SQL Engine        │
Client ------> │ Query Planner     │
               │ Buffer Manager    │
               │ WAL Writer        │  
               │                   │
               └───────────────────┘
                        │
        ┌───────────────┴───────────────┐
        │                               │
 Data Files                     WAL Files



# start primary
docker compose up -d primary

# connect to postgres
docker exec -it pg-primary psql -U postgres -d demo

# list roles to verify the replication user
\du

# verify the postgres config
SHOW config_file;
SHOW hba_file;
SHOW wal_level; # expected 'replica'
SHOW max_wal_senders; # expected '10'
```

## Replication configuration
A WAL contains changes, not the entire database. So, a replica must start with exactly the same database as the primary. Only then can WAL be applied. `pg_basebackup` creates an exact copy of the primary. Replication is actually 2 phases-
1. Copy the database (one time operation)
2. keep the replica updated

```bash
          Primary
              │
              │
      pg_basebackup -R
              │
              ▼
     Copy database files
     Create standby.signal
     Write primary_conninfo
              │
              ▼
        Start PostgreSQL
              │
              ▼
     Detect standby.signal
              │
              ▼
      Become a Replica
              │
              ▼
     Connect to Primary
              │
              ▼
        Stream WAL
```

```bash
# create empty volume
docker volume create distributed-patterns_replica-data

# copy the primary's data in the volume
docker run --rm \
  --network distributed-patterns_default \
  -v distributed-patterns_replica-data:/var/lib/postgresql/data \
  postgres:16 \
  bash -c "PGPASSWORD=replpass pg_basebackup \
    -h primary \
    -U replicator \
    -D /var/lib/postgresql/data \
    -Fp \
    -Xs \
    -P \
    -R"  # automatically create standby.signal (defines as a replica)

# start replica
docker compose up -d replica
```

## verify from primary and replica

```bash
docker exec -it pg-primary psql -U postgres -d demo

# view the replica 
#
# application_name | client_addr |   state   | sync_state
# ------------------+-------------+-----------+------------
# walreceiver      | 172.31.0.3  | streaming | async
SELECT application_name,
       client_addr,
       state,
       sync_state
FROM pg_stat_replication;


# connect to replica
docker exec -it pg-replica psql -U postgres -d demo

# run. expected t
SELECT pg_is_in_recovery();

# create employee table in primary
CREATE TABLE employee ( id SERIAL PRIMARY KEY,name TEXT);

# view from replica
select * from employee;

# try to insert data in employee from replica. should be fail
# then insert data from primary. and view from replica. should be availble in replica
INSERT INTO employee(name) VALUES ('Alice');
```


