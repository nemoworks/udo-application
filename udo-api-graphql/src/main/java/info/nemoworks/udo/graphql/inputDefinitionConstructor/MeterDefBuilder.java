package info.nemoworks.udo.graphql.inputDefinitionConstructor;

import graphql.language.InputValueDefinition;
import graphql.language.TypeName;
import info.nemoworks.udo.graphql.schemaParser.GraphQLPropertyConstructor;

import java.util.ArrayList;
import java.util.List;

public class MeterDefBuilder implements InputValueDefinitionBuilder {
    @Override
    public List<InputValueDefinition> inputValueDefinitionListBuilder(GraphQLPropertyConstructor graphQLPropertyConstructor) {
        List<InputValueDefinition> inputValueDefinitions = new ArrayList<>();
        inputValueDefinitions.add(new InputValueDefinition("start", new TypeName("String")));
        inputValueDefinitions.add(new InputValueDefinition("end", new TypeName("String")));
        inputValueDefinitions.add(new InputValueDefinition("step", new TypeName("String")));
        inputValueDefinitions.add(new InputValueDefinition("query", new TypeName("String")));
        return inputValueDefinitions;
    }
}
