package de.quinscape.automaton.tooling.model.graphql;

import java.util.List;

/**
 * Configuration for a relation based on a view / without formal database foreign key.
 */
public class ViewRelation
{
    private String id;

    private String sourcePojo;

    private List<String> sourceFields;

    private String targetPojo;

    private List<String> targetFields;

    private SourceField sourceField = SourceField.NONE;

    private TargetField targetField = TargetField.NONE;

    private String leftSideObjectName;

    private String rightSideObjectName;


    /**
     * Optional explicit relation id (must be unique). If not defined, a unique id is generated.
     */
    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Name of the POJO on the left side of the relation.
     */
    public String getSourcePojo()
    {
        return sourcePojo;
    }


    public void setSourcePojo(String sourcePojo)
    {
        this.sourcePojo = sourcePojo;
    }


    /**
     * Fields within the source table pointing to the target.
     */
    public List<String> getSourceFields()
    {
        return sourceFields;
    }


    public void setSourceFields(List<String> sourceFields)
    {
        this.sourceFields = sourceFields;
    }


    /**
     * Name of the POJO on the right side of the relation
     */
    public String getTargetPojo()
    {
        return targetPojo;
    }


    public void setTargetPojo(String targetPojo)
    {
        this.targetPojo = targetPojo;
    }


    /**
     * Fields the relation is pointing to within the target type.
     */
    public List<String> getTargetFields()
    {
        return targetFields;
    }


    public void setTargetFields(List<String> targetFields)
    {
        this.targetFields = targetFields;
    }


    /**
     * Source field configuration. (NONE, SCALAR, OBJECT, OBJECT_AND_SCALAR )
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
     * Target field configuration. (NONE, ONE, MANY )
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
     * Name of the object field representing the relation on the left side of the relation.
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
     * Name of the object field representing the relation on the right side of the relation.
     */
    public String getRightSideObjectName()
    {
        return rightSideObjectName;
    }


    public void setRightSideObjectName(String rightSideObjectName)
    {
        this.rightSideObjectName = rightSideObjectName;
    }
}
