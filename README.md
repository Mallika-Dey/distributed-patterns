# distributed-patterns
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
```bash
# if needed remove broken connector
curl -X DELETE http://localhost:8083/connectors/inventory-outbox-connector
```
