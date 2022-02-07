package de.quinscape.automaton.tooling.model.graphql;

import java.util.List;

/**
 * Relation based on a formal database foreign key definition.
 */
public class ForeignKeyRelation
{
    private String fkField;

    private SourceField sourceField = SourceField.NONE;

    private TargetField targetField = TargetField.NONE;

    private String leftSideObjectName;

    private String rightSideObjectName;

    private List<String> metaTags;


    /**
     * Java literal of the foreign key field (e.g. "Foo.OWNER_ID")
     * @return
     */
    public String getFkField()
    {
        return fkField;
    }


    public void setFkField(String fkField)
    {
        this.fkField = fkField;
    }


    /**
     * Source field configuration
     */
    public SourceField getSourceField()
    {
        return sourceField;
    }


    public void setSourceField(SourceField sourceField)
    {
        if (sourceField == null)
        {
            throw new IllegalArgumentException("sourceField can't be null");
        }

        this.sourceField = sourceField;
    }


    /**
     * Target field configuration
     */
    public TargetField getTargetField()
    {
        return targetField;
    }


    public void setTargetField(TargetField targetField)
    {
        if (targetField == null)
        {
            throw new IllegalArgumentException("targetField can't be null");
        }
        this.targetField = targetField;
    }


    /**
     * Object name on the left side of the relation.
     */
    public String getLeftSideObjectName()
    {
        return leftSideObjectName;
    }


    public void setLeftSideObjectName(String leftSideObjectName)
    {
        this.leftSideObjectName = leftSideObjectName;
    }


    /**
     * Object name on the right side of the relation.
     */
    public String getRightSideObjectName()
    {
        return rightSideObjectName;
    }


    public void setRightSideObjectName(String rightSideObjectName)
    {
        this.rightSideObjectName = rightSideObjectName;
    }


    public List<String> getMetaTags()
    {
        return metaTags;
    }


    public void setMetaTags(List<String> metaTags)
    {
        this.metaTags = metaTags;
    }


    @Override
    public String toString()
    {
        return super.toString() + ": "
            + "fkField = '" + fkField + '\''
            + ", sourceField = " + sourceField
            + ", targetField = " + targetField
            + ", leftSideObjectName = '" + leftSideObjectName + '\''
            + ", rightSideObjectName = '" + rightSideObjectName + '\''
            + ", metaTags = " + metaTags
            ;
    }
}
