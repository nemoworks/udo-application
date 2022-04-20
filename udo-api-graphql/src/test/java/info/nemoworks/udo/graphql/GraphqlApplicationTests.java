package info.nemoworks.udo.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import info.nemoworks.udo.graphql.graphqlBuilder.TypeRegistryBuilder;
import info.nemoworks.udo.graphql.schemaParser.SchemaTree;
import info.nemoworks.udo.model.UdoType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ElasticsearchConfig.class)
@SpringBootTest(classes = {info.nemoworks.udo.graphql.schemaParser.SchemaTree.class
        ,info.nemoworks.udo.graphql.graphqlBuilder.TypeRegistryBuilder.class})
class GraphqlApplicationTests {

    @Autowired
    private TypeRegistryBuilder typeRegistryBuilder;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateSchemaTree() throws JsonProcessingException {
        String s = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"title\": \"VirtualMachineInstance\",\n" +
                "  \"properties\": {\n" +
                "    \"CPU\": {\n" +
                "      \"properties\": {\n" +
                "        \"cores\": {\n" +
                "          \"type\": \"meter\"\n" +
                "        },\n" +
                "        \"sockets\": {\n" +
                "          \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"threads\": {\n" +
                "          \"type\": \"integer\"\n" +
                "        },\n" +
                "        \"model\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"features\": {\n" +
                "          \"items\": {\n" +
                "            \"type\": \"embedded\",\n" +
                "            \"typeName\": \"CPUFeature\"\n" +
                "          },\n" +
                "          \"type\": \"array\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false,\n" +
                "      \"title\": \"CPU\",\n" +
                "      \"type\": \"object\"\n" +
                "    },\n" +
                "    \"CPUFeature\": {\n" +
                "      \"required\": [\n" +
                "        \"name\"\n" +
                "      ],\n" +
                "      \"properties\": {\n" +
                "        \"name\": {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"policy\": {\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"additionalProperties\": false,\n" +
                "      \"title\": \"CPUFeature\",\n" +
                "      \"type\": \"object\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JsonObject data = new Gson().fromJson(s,JsonObject.class);
        UdoType type = new UdoType(data);
        SchemaTree schemaTree = new SchemaTree().createSchemaTree(new Gson().fromJson(type.getSchema().toString(), JsonObject.class));
        typeRegistryBuilder.initSchemaDefinition();
        typeRegistryBuilder.initTypeDefinition();
        typeRegistryBuilder.addSchema(schemaTree);
        typeRegistryBuilder.buildTypeRegistry();
    }


}
