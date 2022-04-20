package info.nemoworks.udo.graphql.schemaParser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import graphql.language.ListType;
import graphql.language.Type;
import graphql.language.TypeName;
import info.nemoworks.udo.model.Link;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaTree {

    private String name;

    private Map<String, Type> typeMap = new HashMap<>();

    private Map<String, SchemaTree> childSchemas = new HashMap<>();

    private Map<String, Type> inputMap = new HashMap<>();

    private Map<String, Type> filterMap = new HashMap<>();

    private List<Link> linkList = new ArrayList<>();

    private List<String> meterList = new ArrayList<>();

    public Map<String, Type> getInputMap() {
        return inputMap;
    }

    public void setInputMap(Map<String, Type> inputMap) {
        this.inputMap = inputMap;
    }

    public Map<String, Type> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(Map<String, Type> filterMap) {
        this.filterMap = filterMap;
    }

    @Override
    public String toString() {
        return "SchemaTree{" + "typeMap=" + typeMap + ", childSchemas=" + childSchemas
            + ", linkList=" + linkList + '}';
    }

    public SchemaTree() {
        typeMap.put("udoi", new TypeName("String"));
        filterMap.put("schemaId", new TypeName("String"));
    }

    public SchemaTree createSchemaTree(JsonObject schema) {
        SchemaTree schemaTree = new SchemaTree();
        JsonObject properties = schema.get("properties").getAsJsonObject();
        schemaTree.name = schema.get("title").getAsString();
        if (properties != null) {
            HashMap<String, LinkedTreeMap> hashMap = new Gson()
                .fromJson(properties.toString(), HashMap.class);
            hashMap.forEach((key, value1) -> {
                SchemaPropertyType typeName = SchemaPropertyType
                    .valueOf(value1.get("type").toString());
                switch (typeName) {
                    case Link:
                        String typeName1 = value1.get("linkTo").toString();
                        schemaTree.linkList.add(new Link(key, Link.LinkType.OBJ, typeName1));
                        schemaTree.typeMap.put(key, new TypeName(typeName1));
                        schemaTree.inputMap.put(key, new TypeName("String"));
                        break;
                    case LinkList:
                        String typeName2 = value1.get("linkTo").toString();
                        schemaTree.linkList.add(new Link(key, Link.LinkType.OBJS, typeName2));
                        schemaTree.typeMap.put(key, new ListType(new TypeName(typeName2)));
                        schemaTree.inputMap.put(key, new ListType(new TypeName("String")));
                        break;
                    case embedded:
                        schemaTree.typeMap
                            .put(key, new TypeName(value1.get("typeName").toString()));
                        schemaTree.inputMap.put(key, new TypeName(
                            new GraphQLPropertyConstructor(value1.get("typeName").toString())
                                .inputKeyWordInQuery()));
                        break;
                    case embeddedList:
                        schemaTree.typeMap.put(key,
                            new ListType(new TypeName(value1.get("typeName").toString())));
                        schemaTree.inputMap.put(key, new ListType(new TypeName(
                            new GraphQLPropertyConstructor(value1.get("typeName").toString())
                                .inputKeyWordInQuery())));
                        break;
                    case number:
                        schemaTree.typeMap.put(key, new TypeName("Float"));
                        schemaTree.inputMap.put(key, new TypeName("Float"));
                        schemaTree.filterMap.put(key, new TypeName("Float"));
                    case integer:
                        schemaTree.typeMap.put(key, new TypeName("Int"));
                        schemaTree.inputMap.put(key, new TypeName("Int"));
                        schemaTree.filterMap.put(key, new TypeName("Int"));
                        break;
                    case object:
                        JsonObject jsonObject = new Gson().toJsonTree(value1).getAsJsonObject();
                        schemaTree.typeMap
                            .put(key, new TypeName(jsonObject.get("title").getAsString()));
                        schemaTree.inputMap.put(key,
                            new TypeName(new GraphQLPropertyConstructor(
                                jsonObject.get("title").getAsString())
                                .inputKeyWordInQuery()));
                        schemaTree.filterMap.put(key,
                            new TypeName(new GraphQLPropertyConstructor(
                                jsonObject.get("title").getAsString())
                                .filterKeyWordInQueryXxlist()));
                        schemaTree.childSchemas.put(key, this.createSchemaTree(jsonObject));
                        break;
                    case array:
                        jsonObject = new Gson().toJsonTree(value1).getAsJsonObject();
                        String s = jsonObject.get("items").getAsJsonObject().get("typeName")
                            .getAsString();
                        System.out.println(s);
                        schemaTree.typeMap.put(key, new ListType(new TypeName(s)));
                        schemaTree.inputMap.put(key, new ListType(new TypeName(
                            new GraphQLPropertyConstructor(s).inputKeyWordInQuery())));
                        break;
                    case meter:
                        schemaTree.typeMap.put(key, new TypeName("Int"));
                        schemaTree.inputMap.put(key, new TypeName("Int"));
                        schemaTree.filterMap.put(key, new TypeName("Int"));
                        schemaTree.meterList.add(key);
                        break;
                    case bool:
                        schemaTree.typeMap.put(key, new TypeName("Boolean"));
                        schemaTree.inputMap.put(key, new TypeName("Boolean"));
                        schemaTree.filterMap.put(key, new TypeName("Boolean"));
                    default:
                        schemaTree.typeMap.put(key, new TypeName("String"));
                        schemaTree.inputMap.put(key, new TypeName("String"));
                        schemaTree.filterMap.put(key, new TypeName("String"));
                }

            });
        }
        return schemaTree;
    }

    private String lowerCase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public SchemaTree createSchemaTree(JsonObject schema, String name) {
        SchemaTree schemaTree = new SchemaTree();
        JsonObject properties = schema.get("properties").getAsJsonObject();
//        schemaTree.name = schema.get("title").getAsString();
        schemaTree.name = name;
        if (properties != null) {
            HashMap<String, LinkedTreeMap> hashMap = new Gson()
                .fromJson(properties.toString(), HashMap.class);
//            System.out.println(hashMap);
//            hashMap.forEach((key, value) -> {
//                System.out.println("key: " + key + " value: " + value);
//            });
            hashMap.forEach((key, value1) -> {
                SchemaPropertyType typeName = SchemaPropertyType
                    .valueOf(value1.get("type").toString());
                switch (typeName) {
                    case Link:
                        String typeName1 = value1.get("linkTo").toString();
                        schemaTree.linkList.add(new Link(key, Link.LinkType.OBJ, typeName1));
                        schemaTree.typeMap.put(key, new TypeName(typeName1));
                        schemaTree.inputMap.put(key, new TypeName("String"));
                        break;
                    case LinkList:
                        String typeName2 = value1.get("linkTo").toString();
                        schemaTree.linkList.add(new Link(key, Link.LinkType.OBJS, typeName2));
                        schemaTree.typeMap.put(key, new ListType(new TypeName(typeName2)));
                        schemaTree.inputMap.put(key, new ListType(new TypeName("String")));
                        break;
                    case embedded:
                        schemaTree.typeMap
                            .put(key, new TypeName(value1.get("typeName").toString()));
                        schemaTree.inputMap.put(key, new TypeName(
                            new GraphQLPropertyConstructor(value1.get("typeName").toString())
                                .inputKeyWordInQuery()));
                        break;
                    case embeddedList:
                        schemaTree.typeMap.put(key,
                            new ListType(new TypeName(value1.get("typeName").toString())));
                        schemaTree.inputMap.put(key, new ListType(new TypeName(
                            new GraphQLPropertyConstructor(value1.get("typeName").toString())
                                .inputKeyWordInQuery())));
                        break;
                    case integer:
                        schemaTree.typeMap.put(key, new TypeName("Int"));
                        schemaTree.inputMap.put(key, new TypeName("Int"));
                        schemaTree.filterMap.put(key, new TypeName("Int"));
                        break;
                    case number:
                        schemaTree.typeMap.put(key, new TypeName("Float"));
                        schemaTree.inputMap.put(key, new TypeName("Float"));
                        schemaTree.filterMap.put(key, new TypeName("Float"));
                        break;
                    case object:
                        JsonObject jsonObject = new Gson().toJsonTree(value1).getAsJsonObject();
                        schemaTree.typeMap
                            .put(key, new TypeName(
                                new GraphQLPropertyConstructor(key).schemaKeyWordInGraphQL()));
                        schemaTree.inputMap.put(key,
                            new TypeName(new GraphQLPropertyConstructor(
                                key)
                                .inputKeyWordInQuery()));
                        schemaTree.filterMap.put(key,
                            new TypeName(new GraphQLPropertyConstructor(
                                key)
                                .filterKeyWordInQueryXxlist()));
                        schemaTree.childSchemas.put(key, this.createSchemaTree(jsonObject, key));
                        break;
                    case array:
                        jsonObject = new Gson().toJsonTree(value1).getAsJsonObject();
                        String s = jsonObject.get("items").getAsJsonObject().get("type")
                            .getAsString();
//                        System.out.println(s);
                        schemaTree.typeMap.put(key, new ListType(new TypeName
                            (upperCase(s))));
                        schemaTree.inputMap.put(key, new ListType(new TypeName(
                            upperCase(s))));
                        break;
                    case meter:
                        schemaTree.typeMap.put(key, new TypeName("Int"));
                        schemaTree.inputMap.put(key, new TypeName("Int"));
                        schemaTree.filterMap.put(key, new TypeName("Int"));
                        schemaTree.meterList.add(key);
                        break;
                    case bool:
                        schemaTree.typeMap.put(key, new TypeName("Boolean"));
                        schemaTree.inputMap.put(key, new TypeName("Boolean"));
                        schemaTree.filterMap.put(key, new TypeName("Boolean"));
                    default:
                        schemaTree.typeMap.put(key, new TypeName("String"));
                        schemaTree.inputMap.put(key, new TypeName("String"));
                        schemaTree.filterMap.put(key, new TypeName("String"));
                }

            });
        }
        return schemaTree;
    }

    public void traceSchemaTree(SchemaTree schemaTree) {
        schemaTree.childSchemas.forEach((key, value) -> {
            traceSchemaTree(value);
            System.out.println(key);
        });
        schemaTree.typeMap.forEach((key, value) -> {
            System.out.println(key + "   " + value);
        });
    }

    public Map<String, SchemaTree> getChildSchemas() {
        return childSchemas;
    }

    public void setChildSchemas(Map<String, SchemaTree> childSchemas) {
        this.childSchemas = childSchemas;
    }

    public List<Link> getLinkList() {
        return linkList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
    }

    public Map<String, Type> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<String, Type> typeMap) {
        this.typeMap = typeMap;
    }

    public List<String> getMeterList() {
        return meterList;
    }

    public void setMeterList(List<String> meterList) {
        this.meterList = meterList;
    }
}
