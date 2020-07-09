package de.quinscape.automaton.tooling.model;

import org.svenson.JSONTypeHint;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Root node for our JSON data
 */
public class GraphQLConfig
{
    private List<String> additionalInputTypes;
    private List<ForeignKeyRelation> foreignKeyRelations;
    private List<ViewRelation> viewRelations;
    private List<String> nameFields;
    private Map<String,List<String>> nameFieldsByType;


    /**
     * List of additional POJO names to use as input types.
     */
    public List<String> getAdditionalInputTypes()
    {
        if (additionalInputTypes == null)
        {
            return Collections.emptyList();
        }
        return additionalInputTypes;
    }


    public void setAdditionalInputTypes(List<String> additionalInputTypes)
    {
        this.additionalInputTypes = additionalInputTypes;
    }


    /**
     * List of foreign key based relation configurations.
     */
    public List<ForeignKeyRelation> getForeignKeyRelations()
    {
        if (foreignKeyRelations == null)
        {
            return Collections.emptyList();
        }
        return foreignKeyRelations;
    }


    @JSONTypeHint(ForeignKeyRelation.class)
    public void setForeignKeyRelations(List<ForeignKeyRelation> foreignKeyRelations)
    {
        this.foreignKeyRelations = foreignKeyRelations;
    }


    /**
     * List of view / POJO based relation configurations.
     */
    public List<ViewRelation> getViewRelations()
    {
        if (viewRelations == null)
        {
            return Collections.emptyList();
        }
        return viewRelations;
    }


    @JSONTypeHint(ViewRelation.class)
    public void setViewRelations(List<ViewRelation> viewRelations)
    {
        this.viewRelations = viewRelations;
    }


    /**
     * Default name fields. The first name in the list contained in a type is used as name field for that type.
     */
    public List<String> getNameFields()
    {
        if (nameFields == null)
        {
            return Collections.emptyList();
        }

        return nameFields;
    }


    public void setNameFields(List<String> nameFields)
    {
        this.nameFields = nameFields;
    }


    /**
     * Type specific name field configuration. Maps the name of a POJO type to a list of name fields for that type.
     * The given type will use the given name fields as representative values / name field.
     */
    public Map<String, List<String>> getNameFieldsByType()
    {
        if (nameFieldsByType == null)
        {
            return Collections.emptyMap();
        }
        return nameFieldsByType;
    }


    public void setNameFieldsByType(Map<String, List<String>> nameFieldsByType)
    {
        this.nameFieldsByType = nameFieldsByType;
    }


    @Override
    public String toString()
    {
        return super.toString() + ": "
            + "additionalInputTypes = " + additionalInputTypes
            + ", foreignKeyRelations = " + foreignKeyRelations
            + ", viewRelations = " + viewRelations
            + ", nameFields = " + nameFields
            + ", nameFieldsByType = " + nameFieldsByType
            ;
    }
}
