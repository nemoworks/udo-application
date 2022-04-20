package info.nemoworks.udo.graphql.graphqlBuilder;

import graphql.language.FieldDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.InputValueDefinition;
import graphql.language.ListType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.SchemaDefinition;
import graphql.language.Type;
import graphql.language.TypeDefinition;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;
import info.nemoworks.udo.graphql.inputDefinitionConstructor.CreateNewDocInputDefBuilder;
import info.nemoworks.udo.graphql.inputDefinitionConstructor.FilterDocListInputDefBuilder;
import info.nemoworks.udo.graphql.inputDefinitionConstructor.IdInputDefBuilder;
import info.nemoworks.udo.graphql.inputDefinitionConstructor.MeterDefBuilder;
import info.nemoworks.udo.graphql.inputDefinitionConstructor.UpdateDocInputDefBuilder;
import info.nemoworks.udo.graphql.schemaParser.GraphQLPropertyConstructor;
import info.nemoworks.udo.graphql.schemaParser.SchemaTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TypeRegistryBuilder {

    private TypeDefinitionRegistry typeDefinitionRegistry;
    public static Map<String, Map<String, Type>> typeDefinitionsMap = new HashMap<>();

    private final List<FieldDefinition> fieldDefinitionListInQuery = new ArrayList<>();

    @Autowired
    TypeRegistryBuilder() {
        this.typeDefinitionRegistry = new TypeDefinitionRegistry();
    }

    TypeDefinitionRegistry getTypeDefinitionRegistry() {
        return typeDefinitionRegistry;
    }

    public void initSchemaDefinition() {
        SchemaDefinition.Builder builder = SchemaDefinition.newSchemaDefinition();
        OperationTypeDefinition operationTypeDefinition = new OperationTypeDefinition("query",
            new TypeName("Query"));
        SchemaDefinition schemaDefinition = builder.operationTypeDefinition(operationTypeDefinition)
            .build();
        typeDefinitionRegistry.add(schemaDefinition);
    }

    public void initTypeDefinition() {

        Map<String, Type> deleteTypeMap = new HashMap<>();
        deleteTypeMap.put("deleteResult", new TypeName("String"));
        typeDefinitionRegistry
            .add(newObjectTypeDefinition("DeleteResult", newFieldDefinitions(deleteTypeMap)));

        Map<String, Type> metersType = new HashMap<>();
        metersType.put("status", new TypeName("String"));
        metersType.put("data", new TypeName("String"));
        typeDefinitionRegistry
            .add(newObjectTypeDefinition("Meter", newFieldDefinitions(metersType)));

//        Map<String, Type> itemTypeMap = new HashMap<>();
//        itemTypeMap.put("item", new TypeName("Object"));
//        typeDefinitionRegistry
//            .add(newObjectTypeDefinition("Object", newFieldDefinitions(itemTypeMap)));
    }

    public void buildTypeRegistry() {
        typeDefinitionRegistry.getType("Query").ifPresent(typeDefinition -> {
            if (typeDefinition instanceof ObjectTypeDefinition) {
                typeDefinitionRegistry.remove(typeDefinition);
            }
        });
        ObjectTypeDefinition query = ObjectTypeDefinition.newObjectTypeDefinition().name("Query")
            .fieldDefinitions(fieldDefinitionListInQuery).build();
        typeDefinitionRegistry.add(query);
    }


    public void addSchema(SchemaTree schemaTree) {
        schemaTree.getChildSchemas().forEach((key, value) -> addSchema(value));

        if (!typeDefinitionsMap.keySet().contains(schemaTree.getName())) {

            typeDefinitionsMap.put(schemaTree.getName(), schemaTree.getTypeMap());
            GraphQLPropertyConstructor graphQLPropertyConstructor = new GraphQLPropertyConstructor(
                schemaTree.getName());
            this.addTypeDefinition(graphQLPropertyConstructor.schemaKeyWordInGraphQL(),
                schemaTree.getTypeMap());

            this.addDocumentTypeInQuery(graphQLPropertyConstructor);

            this.addDocumentListTypeInQuery(graphQLPropertyConstructor, schemaTree.getFilterMap());

            this.addCreateNewDocumentTypeInQuery(graphQLPropertyConstructor,
                schemaTree.getInputMap());

            this.addUpdateDocumentByIdInQuery(graphQLPropertyConstructor);

            this.addDeleteDocumentByIdInQuery(graphQLPropertyConstructor);

            this.addDocumentCommitsInQuery(graphQLPropertyConstructor);

            //   this.addDocumentCommitsInQuery(graphQLPropertyConstructor);
            this.addDocumentMetersInQuery(graphQLPropertyConstructor);

        }
    }

    public void deleteSchema(SchemaTree schemaTree) {
        schemaTree.getChildSchemas().forEach((key, value) -> {
            deleteSchema(value);
        });
        if (typeDefinitionsMap.containsKey(schemaTree.getName())) {
            GraphQLPropertyConstructor graphQLPropertyConstructor = new GraphQLPropertyConstructor(
                schemaTree.getName());

            this.deleteTypeDefinitionInQuery(graphQLPropertyConstructor);
            this.deleteTypeDefinition(graphQLPropertyConstructor);
            this.deleteTypeFilterDefinition(graphQLPropertyConstructor);
            this.deleteTypeInputDefinition(graphQLPropertyConstructor);
            this.deleteTypeCommitsDefinition(graphQLPropertyConstructor);
        }
        typeDefinitionsMap.remove(schemaTree.getName());
    }

    private void deleteTypeDefinitionInQuery(
        GraphQLPropertyConstructor graphQLPropertyConstructor) {
        List<String> keyWordsInQuery = Arrays
            .asList(graphQLPropertyConstructor.createNewXxKeyWord(),
                graphQLPropertyConstructor.updateXxKeyWord()
                , graphQLPropertyConstructor.deleteXxKeyWord(),
                graphQLPropertyConstructor.queryXxKeyWord()
                , graphQLPropertyConstructor.queryXxlistKeyWord(),
                graphQLPropertyConstructor.commitsXxKeyWord()
                , graphQLPropertyConstructor.metersXxKeyWord());
        typeDefinitionRegistry.getType("Query").ifPresent(typeDefinition -> {
            if (typeDefinition instanceof ObjectTypeDefinition) {
                ((ObjectTypeDefinition) typeDefinition).getFieldDefinitions()
                    .forEach(fieldDefinition -> {
                        if (keyWordsInQuery.contains(fieldDefinition.getName())) {
                            fieldDefinitionListInQuery.remove(fieldDefinition);
                        }
                    });
            }
        });
    }

    private void deleteTypeDefinition(GraphQLPropertyConstructor graphQLPropertyConstructor) {
        typeDefinitionRegistry
            .remove(new ObjectTypeDefinition(graphQLPropertyConstructor.schemaKeyWordInGraphQL()));
    }

    private void deleteTypeFilterDefinition(GraphQLPropertyConstructor graphQLPropertyConstructor) {
        typeDefinitionRegistry.remove(InputObjectTypeDefinition.newInputObjectDefinition()
            .name(graphQLPropertyConstructor.filterKeyWordInQueryXxlist()).build());
    }

    private void deleteTypeInputDefinition(GraphQLPropertyConstructor graphQLPropertyConstructor) {
        typeDefinitionRegistry.remove(InputObjectTypeDefinition.newInputObjectDefinition()
            .name(graphQLPropertyConstructor.inputKeyWordInQuery()).build());
    }

    private void deleteTypeCommitsDefinition(
        GraphQLPropertyConstructor graphQLPropertyConstructor) {
        typeDefinitionRegistry
            .remove(new ObjectTypeDefinition(graphQLPropertyConstructor.commitsTypeInGraphQL()));
    }

    //在GraphQL的Schema中的Query类中增加一个访问定义的对象的字段
    private void addDocumentTypeInQuery(GraphQLPropertyConstructor graphQLPropertyConstructor) {
        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.getSchemaName(),
            new TypeName(graphQLPropertyConstructor.schemaKeyWordInGraphQL()),
            new IdInputDefBuilder().inputValueDefinitionListBuilder(graphQLPropertyConstructor));
    }

    private void addDocumentListTypeInQuery(GraphQLPropertyConstructor graphQLPropertyConstructor,
        Map<String, Type> filterMap) {
        // input filter Type in Type
        this.addInputObjectTypeDefinition(graphQLPropertyConstructor.filterKeyWordInQueryXxlist(),
            filterMap);
        // orderDocumentList:[OrderDocument]
        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.queryXxlistKeyWord(),
            new ListType(new TypeName(graphQLPropertyConstructor.schemaKeyWordInGraphQL())),
            new FilterDocListInputDefBuilder()
                .inputValueDefinitionListBuilder(graphQLPropertyConstructor));
    }

    private void addCreateNewDocumentTypeInQuery(
        GraphQLPropertyConstructor graphQLPropertyConstructor,
        Map<String, Type> inputMap) {

        this.addInputObjectTypeDefinition(graphQLPropertyConstructor.inputKeyWordInQuery(),
            inputMap);
        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.createNewXxKeyWord(),
            new TypeName(graphQLPropertyConstructor.schemaKeyWordInGraphQL()),
            new CreateNewDocInputDefBuilder()
                .inputValueDefinitionListBuilder(graphQLPropertyConstructor));
    }

    private void addUpdateDocumentByIdInQuery(
        GraphQLPropertyConstructor graphQLPropertyConstructor) {
        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.updateXxKeyWord(),
            new TypeName(graphQLPropertyConstructor.schemaKeyWordInGraphQL()),
            new UpdateDocInputDefBuilder()
                .inputValueDefinitionListBuilder(graphQLPropertyConstructor));
    }

    private void addDeleteDocumentByIdInQuery(
        GraphQLPropertyConstructor graphQLPropertyConstructor) {
        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.deleteXxKeyWord(),
            new TypeName("DeleteResult"),
            new IdInputDefBuilder().inputValueDefinitionListBuilder(graphQLPropertyConstructor));
    }

    private void addDocumentCommitsInQuery(GraphQLPropertyConstructor graphQLPropertyConstructor) {
        // register DocumentCommits Type
        Map<String, Type> commitsTypeMap = new HashMap<>();
        commitsTypeMap.put("commitId", new TypeName("String"));
        commitsTypeMap.put("commitDate", new TypeName("String"));
        commitsTypeMap
            .put("data", new TypeName(graphQLPropertyConstructor.schemaKeyWordInGraphQL()));
        this.addTypeDefinition(graphQLPropertyConstructor.commitsTypeInGraphQL(), commitsTypeMap);
        // init documentCommits(id:String,name:String):[DocumentCommits] in Query
        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.commitsXxKeyWord(),
            new ListType(new TypeName(graphQLPropertyConstructor.commitsTypeInGraphQL())),
            new IdInputDefBuilder().inputValueDefinitionListBuilder(graphQLPropertyConstructor));

    }

    private void addDocumentMetersInQuery(GraphQLPropertyConstructor graphQLPropertyConstructor) {

        this.addFieldDefinitionsInQueryType(graphQLPropertyConstructor.metersXxKeyWord(),
            new TypeName("Meter"),
            new MeterDefBuilder().inputValueDefinitionListBuilder(graphQLPropertyConstructor));
    }

    void addInputObjectTypeDefinition(String name, Map<String, Type> typeMap) {
        List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();
        typeMap.forEach(
            (key, value) -> inputValueDefinitions.add(new InputValueDefinition(key, value)));
        InputObjectTypeDefinition inputObjectTypeDefinition = InputObjectTypeDefinition
            .newInputObjectDefinition().name(name)
            .inputValueDefinitions(inputValueDefinitions).build();
        typeDefinitionRegistry.add(inputObjectTypeDefinition);
    }

    void addTypeDefinition(String name, Map<String, Type> typeMap) {
        typeDefinitionRegistry.add(newObjectTypeDefinition(name, newFieldDefinitions(typeMap)));
    }


    void addFieldDefinitionsInQueryType(String name, Type type) {
        typeDefinitionRegistry.getType("Query").ifPresent(queryType -> {
            if (queryType instanceof ObjectTypeDefinition) {
                ((ObjectTypeDefinition) queryType).getFieldDefinitions()
                    .add(new FieldDefinition(name, type));
            }
        });
    }

    void deleteFieldDefinitionsInQueryType(String name) {
        typeDefinitionRegistry.getType("Query").ifPresent(queryType -> {
            if (queryType instanceof ObjectTypeDefinition) {
                ((ObjectTypeDefinition) queryType).getFieldDefinitions()
                    .remove(this.getFieldDefinitionInQueryType(name));
            }
        });
    }

    void updateFieldDefinitionsInQueryType(String name, Type type) {
        typeDefinitionRegistry.getType("Query").ifPresent(queryType -> {
            if (queryType instanceof ObjectTypeDefinition) {
                ((ObjectTypeDefinition) queryType).getFieldDefinitions()
                    .remove(this.getFieldDefinitionInQueryType(name));
                ((ObjectTypeDefinition) queryType).getFieldDefinitions()
                    .add(new FieldDefinition(name, type));
            }
        });
    }

    void updateFieldDefinitions(String name, Map<String, Type> typeMap) {
        typeDefinitionRegistry.getType(name).ifPresent(typeDefinition -> {
            if (typeDefinition instanceof ObjectTypeDefinition) {
                ((ObjectTypeDefinition) typeDefinition).getFieldDefinitions().clear();
                ((ObjectTypeDefinition) typeDefinition).getFieldDefinitions()
                    .addAll(newFieldDefinitions(typeMap));
            }
        });
    }

    private FieldDefinition getFieldDefinitionInQueryType(String name) {
        if (typeDefinitionRegistry.getType("Query").get() instanceof ObjectTypeDefinition) {
            for (FieldDefinition fieldDefinition : ((ObjectTypeDefinition) typeDefinitionRegistry
                .getType("Query")
                .get()).getFieldDefinitions()) {
                if (fieldDefinition.getName().equals(name)) {
                    return fieldDefinition;
                }
            }
        }
        return null;
    }

    private List<FieldDefinition> getFieldDefinitionsInType(String name) {
        if (typeDefinitionRegistry.getType(name).get() instanceof ObjectTypeDefinition) {
            return ((ObjectTypeDefinition) typeDefinitionRegistry.getType(name).get())
                .getFieldDefinitions();
        }
        return null;
    }

    Map<String, TypeDefinition> getFieldDefinitionsInMyTypeRegistry() {
        return typeDefinitionRegistry.types();
    }

    void addFieldDefinitionsInQueryType(String name, Type type,
        List<InputValueDefinition> inputValueDefinitions) {
        FieldDefinition definition = FieldDefinition.newFieldDefinition()
            .inputValueDefinitions(inputValueDefinitions).name(name).type(type).build();
        fieldDefinitionListInQuery.add(definition);
    }

    private List<FieldDefinition> newFieldDefinitions(Map<String, Type> typeMap) {
        List<FieldDefinition> fieldDefinitions = new ArrayList<>();
        typeMap.forEach((name, Type) -> fieldDefinitions.add(new FieldDefinition(name, Type)));
        return fieldDefinitions;
    }

    private ObjectTypeDefinition newObjectTypeDefinition(String name,
        List<FieldDefinition> fieldDefinitions) {
        ObjectTypeDefinition.Builder builder = ObjectTypeDefinition.newObjectTypeDefinition();
        return builder.name(name).fieldDefinitions(fieldDefinitions).build();
    }
}
