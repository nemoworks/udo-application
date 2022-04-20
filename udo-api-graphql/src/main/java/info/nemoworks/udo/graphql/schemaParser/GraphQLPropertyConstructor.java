package info.nemoworks.udo.graphql.schemaParser;

public class GraphQLPropertyConstructor {

    private String schemaName;

    private static final String COLLECTION_NAME_PRE = "";
    private static final String QUERY_XXLIST_SUFFIX = "Documents";
    private static final String QUERY_XX_SUFFIX = "";
    private static final String CREATE_NEW_XX_PRE = "new";
    private static final String UPDATE_XX_PRE = "update";
    private static final String DELETE_XX_PRE = "delete";
    private static final String FILTER_XX_SUFFIX = "Filters";
    private static final String INPUT_XX_SUFFIX = "Inputs";
    private static final String COMMITS_XX_SUFFIX = "Commits";
    private static final String METER_XX_SUFFIX = "Meters";

    public GraphQLPropertyConstructor(String schemaName) {
        this.schemaName = schemaName;
    }


    public String metersXxKeyWord() {
        return schemaName + METER_XX_SUFFIX;
    }

    public String collectionName() {
        return COLLECTION_NAME_PRE + schemaName;
    }

    public String queryXxlistKeyWord() {
        return schemaName + QUERY_XXLIST_SUFFIX;
    }

    public String queryXxKeyWord() {
        return schemaName + QUERY_XX_SUFFIX;
    }

    public String createNewXxKeyWord() {
        return CREATE_NEW_XX_PRE + schemaName;
    }

    public String updateXxKeyWord() {
        return UPDATE_XX_PRE + schemaName;
    }

    public String deleteXxKeyWord() {
        return DELETE_XX_PRE + schemaName;
    }

    public String schemaKeyWordInQuery() {
        return lowerCase(schemaName);
    }

    public String schemaKeyWordInGraphQL() {
        return upperCase(schemaName);
    }

    public String commitsXxKeyWord() {
        return schemaName + COMMITS_XX_SUFFIX;
    }

    public String commitsTypeInGraphQL() {
        return upperCase(schemaName) + COMMITS_XX_SUFFIX;
    }

    public String filterKeyWordInQueryXxlist() {
        return schemaName + FILTER_XX_SUFFIX;
    }

    public String inputKeyWordInQuery() {
        return schemaName + INPUT_XX_SUFFIX;
    }

    private String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String lowerCase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public String getSchemaName() {
        return schemaName;
    }
}
