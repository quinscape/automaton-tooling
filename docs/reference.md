# ForeignKeyRelation

Relation based on a formal database foreign key definition.

property | type | description 
---------|------|-------------
sourceField | [SourceField](#sourcefield) | Source field configuration
fkField | String | Java literal of the foreign key field (e.g. "Foo.OWNER_ID")
metaTags | List of String | 
targetField | [TargetField](#targetfield) | Target field configuration
rightSideObjectName | String | Object name on the right side of the relation.
leftSideObjectName | String | Object name on the left side of the relation.
# GraphQLConfig

Root node for our JSON data

property | type | description 
---------|------|-------------
nameFieldsByType | Map of List&lt;String&gt; | Type specific name field configuration. Maps the name of a POJO type to a list of name fields for that type. The given type will use the given name fields as representative values/name field.
nameFields | List of String | Default name fields. The first name in the list contained in a type is used as name field for that type.
foreignKeyRelations | List of [ForeignKeyRelation](#foreignkeyrelation) | List of foreign key based relation configurations.
additionalInputTypes | List of String | List of additional POJO names to use as input types.
viewRelations | List of [ViewRelation](#viewrelation) | List of view / POJO based relation configurations.
# SourceField

DomainQL behaviour for the source / left-hand side of a foreign Key. (Copy from domainql)

What kind of field is generated on the side that contains the foreign key.

value | description
------|------------
NONE | Ignore field for source type.
SCALAR | Define a scalar GraphQL field for the key itself (e.g. fooId : string)
OBJECT | Define an embedded object for the target
OBJECT_AND_SCALAR |  Define a field for the key iteself *and* define an embedded object.
 <p>
     This is useful in situtations where you want the embedded object in some cases, but in others you
     want to save one level of querying because all you need is the target id.
 </p>
# TargetField

DomainQL behavior for the target side of a foreign key. (Copy from domainql)

value | description
------|------------
NONE | Do nothing on target side.
ONE | Assume the foreign key to represent a one-to-one relationship and embed a single object as back reference.
MANY | Assume the foreign key to represent a many-to-one relationship and embed a list of back references.
# ViewRelation

Configuration for a relation based on a view / without formal database foreign key.

property | type | description 
---------|------|-------------
sourceField | [SourceField](#sourcefield) | Source field configuration. (NONE, SCALAR, OBJECT, OBJECT_AND_SCALAR )
sourcePojo | String | Name of the POJO on the left side of the relation.
sourceFields | List of String | Fields within the source table pointing to the target.
targetField | [TargetField](#targetfield) | Target field configuration. (NONE, ONE, MANY )
metaTags | List of String | 
targetPojo | String | Name of the POJO on the right side of the relation
rightSideObjectName | String | Name of the object field representing the relation on the right side of the relation.
id | String | Optional explicit relation id (must be unique). If not defined, a unique id is generated.
leftSideObjectName | String | Name of the object field representing the relation on the left side of the relation.
targetFields | List of String | Fields the relation is pointing to within the target type.
