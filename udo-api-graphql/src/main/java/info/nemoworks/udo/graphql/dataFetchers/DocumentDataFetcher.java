package info.nemoworks.udo.graphql.dataFetchers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.service.UdoService;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
public class DocumentDataFetcher implements DataFetcher<HashMap<String, LinkedTreeMap>> {

    private String documentCollectionName;
    private String keyNameInParent;

    private final UdoService udoService;

    public DocumentDataFetcher(UdoService udoService) {
        this.udoService = udoService;
    }


    public void setDocumentCollectionName(String documentCollectionName) {
        this.documentCollectionName = documentCollectionName;
    }

    public void setKeyNameInParent(String keyNameInParent) {
        this.keyNameInParent = keyNameInParent;
    }

    @Override
    public HashMap<String, LinkedTreeMap> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = String.valueOf(dataFetchingEnvironment.getArguments().get("udoi"));
        if (id.equals("null")) {
            JsonObject jsonObject =  new Gson().fromJson(dataFetchingEnvironment.getSource().toString(), JsonObject.class);
            id = jsonObject.get(keyNameInParent).getAsString();
        }
//        String collection = dataFetchingEnvironment.getArgument("collection").toString();
        return this.getDocumentByAggregation(id, documentCollectionName);
    }

    private HashMap<String, LinkedTreeMap> getDocumentByAggregation(String id, String collection){
        Udo udo = udoService.getUdoById(id);
        if(udo == null)
            return new HashMap<>();
        HashMap hashMap = new Gson().fromJson(udo.getData().toString(), HashMap.class);
        hashMap.put("udoi",udo.getId());
        return hashMap;
    }
}
