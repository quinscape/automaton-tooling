package de.quinscape.automaton.tooling;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "automaton-tooling",
    subcommands = {
        GraphQLConfigGenerator.class
    }
)
public class AutomatonTooling
{
    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new AutomatonTooling()).execute(args);
    }
}
