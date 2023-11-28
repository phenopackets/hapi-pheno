This package is an interim setup for generating the set of interfaces that represent the "hub model". The interfaces are generated under `src-gen/main/java`.

The `model.modeldsl` is a simple DSL for declaring our "entities". See the file, it should be easy to understand until there is more detailed documentation.  Once that file is updated, run the following from the commandline/terminal and the core module's directory:

```
mvn exec:java \
    -Dexec.mainClass=com.essaid.model.dsl.Main \
    -Dexec.args="--dsl-source src/main/java/org/monarchinitiative/phenoex/model/model.modeldsl --out-dir src-gen/main/java"
```

This will delete and re-generate the `src-gen` to contain the Java model interfaces based on the entities defined in `src/main/java/org/monarchinitiative/phenoex/model/model.modeldsl`
