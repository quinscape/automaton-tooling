package de.quinscape.automaton.tooling.model.graphql;

/**
 * DomainQL behavior for the target side of a foreign key. (Copy from domainql)
 */
public enum TargetField
{
    /**
     * Do nothing on target side.
     */
    NONE,
    /**
     * Assume the foreign key to represent a one-to-one relationship and embed a single object as back reference.
     */
    ONE,

    /**
     * Assume the foreign key to represent a many-to-one relationship and embed a list of back references.
     */
    MANY
}
