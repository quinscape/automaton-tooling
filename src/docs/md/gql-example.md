## Graphql Config Example

```json
{
    "additionalInputTypes": [
        "Foo",
        "Bar"
    ],
    "foreignKeyRelations": [
        {
            "fkField" : "FOO.OWNER_ID",
            "sourceField" : "OBJECT_AND_SCALAR",
            "targetField" : "MANY"
        },
        {
            "fkField" : "QUX_MAIN.QUX_B_NAME",
            "sourceField" : "OBJECT_AND_SCALAR",
            "leftSideObjectName": "quxB"
        }

    ],
    "viewRelations": [
        {
            "sourcePojo" :  "IBearbeiteteObjekteAnlagenBetriebsstaetten",
            "sourceFields" : ["beaAnlageId"],
            "targetPojo" :  "AAnlage",
            "targetFields" : ["id"],
            "sourceField" :  "OBJECT_AND_SCALAR",
            "leftSideObjectName" :  "aAnlage"
        },
        {
            "sourcePojo" :  "IFavoritenAnlagenBetriebsstaetten",
            "sourceFields" : ["favAnlageId"],
            "targetPojo" :  "AAnlage",
            "targetFields" : ["id"],
            "sourceField" :  "OBJECT_AND_SCALAR",
            "leftSideObjectName" :  "aAnlage",
            "targetField" :  "MANY",
            "rightSideObjectName" :  "anlagen"
        }

    ],
    "nameFields": ["name"],
    "nameFieldsByType": {
        "Bar" : ["name", "description"],
        "Qux" : ["asName"]
    }
}

```
