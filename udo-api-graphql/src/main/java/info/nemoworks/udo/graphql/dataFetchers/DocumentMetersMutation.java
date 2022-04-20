//package info.nemoworks.udo.graphql.dataFetchers;
//
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetchingEnvironment;
//import info.nemoworks.udo.repository.PrometheusService;
//import net.sf.json.JSONObject;
//
//public class DocumentMetersMutation implements DataFetcher<JSONObject> {
//
//    private final PrometheusService prometheusService;
//
//    public DocumentMetersMutation(PrometheusService prometheusService) {
//        this.prometheusService = prometheusService;
//    }
//
//    @Override
//    public JSONObject get(DataFetchingEnvironment dataFetchingEnvironment) {
//        String start = dataFetchingEnvironment.getArgument("start").toString();
//        String end = dataFetchingEnvironment.getArgument("end").toString();
//        String query = dataFetchingEnvironment.getArgument("query").toString();
//        String step = dataFetchingEnvironment.getArgument("step").toString();
//
//        return prometheusService.fetchPrometheusMetrics(start, end, query, step);
//    }
//}
