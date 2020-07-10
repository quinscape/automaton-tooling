# Automaton-Tooling

Java-based command-line generation helpers for Automaton.

This project generates an multi-command "uber jar" which contains all necessary dependencies.

## Command `graphql`  

Our first command is `graphql` which is used to generate a automaton GraphQLConfiguration.java file based on a JSON 
configuration.

### Usage

```shell script 
java -jar automaton-tooling-1.0-SNAPSHOT.jar graphql -b my.package -i <JSON-input-path> -o <java-output-path>
```

The `--base-package`/`-b` option defines the base package name of the automaton application to generate the 
GraphQLConfiguration for. The tool assumes a standard automaton project layout.


![Automaton Standard Directory Layout](./standard-layout.jpg)

It is assumed that the `domain` package contains the generared JOOQ sources.

The output is supposed to be written to `runtime.config.GraphQLConfiguration` and the output file should match that.
 

## Configuration

We expect our input data as JSON matching the model types in de.quinscape.automaton.tooling.model.graphql.

The root of the file corresponds to the de.quinscape.automaton.tooling.model.graphql.GraphQLConfig class. 
