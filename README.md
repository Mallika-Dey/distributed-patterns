# distributed-patterns

```bash
SHOW wal_level;
```

If it is not 'logical' follow below steps to change it to 'logical'

```bash
# Check the postgresql.conf file location
SHOW config_file;
# Edit the postgresql.conf file for ubuntu
sudo nano /etc/postgresql/12/main/postgresql.conf

# Add below lines
wal_level = logical
# recommended settings
max_replication_slots = 10
max_wal_senders = 10
```
```bash
#restart postgres
sudo systemctl restart postgresql

# grant replication privileges to postgres user
ALTER USER postgres REPLICATION;
# check if replication privileges are granted
SELECT rolname, rolreplication
FROM pg_roles
WHERE rolname = 'postgres';
```
```bash
curl -X POST http://localhost:8083/connectors \
-H "Content-Type: application/json" \
-d '{
  "name": "order-outbox-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",

    "database.hostname": "postgres",
    "database.port": "5432",
    "database.user": "postgres",
    "database.password": "postgres",
    "database.dbname": "order",

    "topic.prefix": "order-service",

    "table.include.list": "public.outbox_events",

    "plugin.name": "pgoutput"
  }
}'



# run this for connect with inventory
curl -X POST http://localhost:8083/connectors \
-H "Content-Type: application/json" \
-d '{
  "name": "inventory-outbox-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",

    "database.hostname": "postgres",
    "database.port": "5432",
    "database.user": "postgres",
    "database.password": "postgres",
    "database.dbname": "inventory",

    "topic.prefix": "inventory-service",

    "table.include.list": "public.outbox_events",

    "plugin.name": "pgoutput"
  }
}'
```
```bash
# if needed remove broken connector
curl -X DELETE http://localhost:8083/connectors/inventory-outbox-connector
```
