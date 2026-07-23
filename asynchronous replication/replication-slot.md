# Replication Slot
1 replication slot -> 1 replica
Because, slot has 1 restart point. So, it can't accurate track multiple replica. 

## Configure a replica slot
```bash
# in primary check the slots
SELECT slot_name,
       slot_type,
       active,
       restart_lsn
FROM pg_replication_slots;

# create replicatin slot in primary
SELECT pg_create_physical_replication_slot('replica1_slot');

# in replica update the primary slot
docker exec -it pg-replica psql -U postgres
ALTER SYSTEM SET primary_slot_name = 'replica1_slot';
\q

# restart replica
docker restart pg-replica
```