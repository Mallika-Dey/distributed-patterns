# Transaction-outbox-pattern
## Key Concept
The **Outbox Pattern** ensures reliable event publishing:
1. Services write domain events to a local outbox table (same transaction as business logic)
2. Debezium captures changes from the outbox using PostgreSQL WAL
3. Events are published to Kafka topics
4. Other services consume events from Kafka topics

## Order Flow Example

```
User places order  ->  Order Service API      ->    Orders DB (with outbox)
                                                        |
                                                  Debezium Connector
                                                        |
                                                Kafka Topic (order-service.outbox_events)
                                                        |
                                                Inventory Service (consumes event)
                                                        |
    Order service   <-          kafka topic     <- Updates Inventory DB (with outbox)
(inventory result consumer) (inventory-service.outbox_events)
```

Key config fields
- `connector.class` — Debezium Postgres connector implementation.
- `database.*` — connection details for the Postgres database used by the service.
- `topic.prefix` — prefix for Kafka topics produced by this connector.
- `table.include.list` — table(s) to monitor (e.g. `public.outbox_events`).
- `plugin.name` — logical decoding plugin (commonly `pgoutput`).
- `slot.name` — (optional) replication slot name; keep unique per connector/DB.

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
    "plugin.name": "pgoutput",
    "slot.name": "inventory_slot"
  }
}'
```

Quick checklist if things fail
- Ensure Postgres has `wal_level = logical`.
- The replication user must have replication privileges and be reachable from Connect.
- Check Connect worker logs for connector startup/offset errors.
- Make sure replication slot names are unique and the DB names/hosts are correct.

```bash
# view list of connector
curl http://localhost:8083/connectors

# check connector status
curl http://localhost:8083/connectors/inventory-outbox-connector/status

# if needed remove broken connector
curl -X DELETE http://localhost:8083/connectors/inventory-outbox-connector
```
