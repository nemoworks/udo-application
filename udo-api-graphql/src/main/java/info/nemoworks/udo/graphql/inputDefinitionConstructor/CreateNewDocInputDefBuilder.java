package info.nemoworks.udo.graphql.inputDefinitionConstructor;

import graphql.language.InputValueDefinition;
import graphql.language.TypeName;
import info.nemoworks.udo.graphql.schemaParser.GraphQLPropertyConstructor;
import java.util.ArrayList;
import java.util.List;

public class CreateNewDocInputDefBuilder implements InputValueDefinitionBuilder {

    @Override
    public List<InputValueDefinition> inputValueDefinitionListBuilder(
        GraphQLPropertyConstructor graphQLPropertyConstructor) {
        List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();
        inputValueDefinitions.add(new InputValueDefinition("content",
            new TypeName(graphQLPropertyConstructor.inputKeyWordInQuery())));
        inputValueDefinitions.add(new InputValueDefinition("uri", new TypeName("String")));
        inputValueDefinitions.add(new InputValueDefinition("uriType", new TypeName("String")));
        inputValueDefinitions.add(new InputValueDefinition("udoTypeId", new TypeName("String")));
        return inputValueDefinitions;
    }
}
