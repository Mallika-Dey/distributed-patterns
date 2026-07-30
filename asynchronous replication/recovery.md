```bash
docker stop pg-primary

# in replica run
SELECT pg_is_in_recovery(); # it should be t
```

# Learning from mistake😂 (multi-master)

```bash
# promote replica without stopping the primary
SELECT pg_promote();

# insert from new master
# then try to view from pg_primary
insert into employee values (2, 'alice');
```
## Timeline divergence

Initially both db like - 
```bash
A ---- B ---- C
```
Promotion creates a new branch -
``` bash
                D ---- E   (Promoted Replica)

               /
A ---- B ---- C

               \
                F ---- G   (Original Primary)
```

postgres can not automatically merge them. as both contain valid data. thats why prod system never promote manually. 
