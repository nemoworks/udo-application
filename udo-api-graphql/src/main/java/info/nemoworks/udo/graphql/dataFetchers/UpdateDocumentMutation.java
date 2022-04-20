package info.nemoworks.udo.graphql.dataFetchers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import info.nemoworks.udo.storage.UdoNotExistException;
import java.io.StringReader;
import java.util.HashMap;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class UpdateDocumentMutation implements DataFetcher<HashMap<String, LinkedTreeMap>> {

    private final UdoService udoService;

    private String documentCollectionName;

    public UpdateDocumentMutation(UdoService udoService) {
        this.udoService = udoService;
    }

    public void setDocumentCollectionName(String documentCollectionName) {
        this.documentCollectionName = documentCollectionName;
    }

    @SneakyThrows
    @Override
    public HashMap<String, LinkedTreeMap> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
//        JsonObject content = new Gson()
//            .fromJson(dataFetchingEnvironment.getArgument("content").toString()
//                .replace(":", "\\:")
//                .replace("/", "\\/"), JsonObject.class);
        String contentStr = dataFetchingEnvironment.getArgument("content").toString();
//        contentStr = contentStr.replace("=", "=\"");
//        contentStr = contentStr.replace(",", "\",");
        System.out.println("contentStr: " + contentStr);
        JsonReader reader = new JsonReader(new StringReader(contentStr));
        reader.setLenient(true);
        JsonObject content = new Gson().fromJson(reader, JsonObject.class);
//        String udoTypeId = dataFetchingEnvironment.getArgument("udoTypeId").toString();
//        System.out.println(content.getAsJsonObject());
        Udo udo = this.updateDocumentById(udoi, content, documentCollectionName);
        HashMap hashMap = new Gson().fromJson(udo.getData().toString(), HashMap.class);
        hashMap.put("udoi", udo.getId());
        return hashMap;
    }

    private Udo updateDocumentById(String id, JsonObject content, String collection) {
        Udo udo = udoService.getUdoById(id);
        assert udo != null;
        udo.setData(content);
        try {
            return udoService.saveOrUpdateUdo(udo);
        } catch (UdoServiceException | UdoNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

}
