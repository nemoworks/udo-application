package info.nemoworks.udo.graphql.dataFetchers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.UdoType;
import info.nemoworks.udo.model.Uri;
import info.nemoworks.udo.model.UriType;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import info.nemoworks.udo.storage.UdoNotExistException;
import java.io.StringReader;
import java.util.HashMap;
import org.springframework.stereotype.Component;


@Component
public class CreateDocumentMutation implements DataFetcher<HashMap<String, LinkedTreeMap>> {

    private final UdoService udoService;

    public CreateDocumentMutation(UdoService udoService) {
        this.udoService = udoService;
    }


    @Override
    public HashMap<String, LinkedTreeMap> get(DataFetchingEnvironment dataFetchingEnvironment) {
        //String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
//        JsonObject content = new Gson()
//            .fromJson(dataFetchingEnvironment.getArgument("content").toString()
//                .replace(":", "\\:")
//                .replace("/", "\\/"), JsonObject.class);
        String contentStr = dataFetchingEnvironment.getArgument("content").toString();
//        System.out.println("contentStr: " + contentStr);
        JsonReader reader = new JsonReader(new StringReader(contentStr));
        reader.setLenient(true);
        JsonObject content = new Gson().fromJson(reader, JsonObject.class);

        String udoTypeId = dataFetchingEnvironment.getArgument("udoTypeId").toString();
        Udo udo = new Udo();
        if (dataFetchingEnvironment.containsArgument("uri")) {
            udo = this.createNewUdo(udoTypeId, content,
                dataFetchingEnvironment.getArgument("uri").toString(),
                dataFetchingEnvironment.getArgument("uriType").toString());
        } else {
            udo = this.createNewUdo(udoTypeId, content, null, null);
        }

        assert udo != null;
        HashMap hashMap = new Gson().fromJson(udo.getData().toString(), HashMap.class);
        hashMap.put("udoi", udo.getId());
        return hashMap;
    }

    private Udo createNewUdo(String typeId, JsonObject content, String uri, String uriType) {
        UdoType type = udoService.getTypeById(typeId);
//        type.setId(typeId);
//        assert type != null;
//        content.addProperty("uri", uri);
        Udo udo = new Udo(type, content);
        if (uri == null) {
            udo.setUri(new Uri(null, UriType.NOTEXIST));
        } else {
            switch (uriType) {
                case "HTTP":
                    udo.setUri(new Uri(uri, UriType.HTTP));
                    break;
                case "MQTT":
                    udo.setUri(new Uri(uri, UriType.MQTT));
                    break;
                default:
                    break;
            }
        }
        try {
            udo = udoService.saveOrUpdateUdo(udo);
            return udo;
        } catch (UdoServiceException | UdoNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }


}
