package info.nemoworks.udo.graphql.inputDefinitionConstructor;

import graphql.language.InputValueDefinition;
import info.nemoworks.udo.graphql.schemaParser.GraphQLPropertyConstructor;

import java.util.List;

public interface InputValueDefinitionBuilder {
    public List<InputValueDefinition> inputValueDefinitionListBuilder(GraphQLPropertyConstructor graphQLPropertyConstructor);
}
