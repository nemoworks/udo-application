package info.nemoworks.udo.graphql.dataFetchers;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteDocumentMutation implements DataFetcher<Map<String, String>> {
    private final UdoService udoService;

    private String documentCollectionName;

    public DeleteDocumentMutation(UdoService udoService) {
        this.udoService = udoService;
    }

    public void setDocumentCollectionName(String documentCollectionName) {
        this.documentCollectionName = documentCollectionName;
    }

    @SneakyThrows
    @Override
    public Map<String, String> get(DataFetchingEnvironment dataFetchingEnvironment) {
        String udoi = dataFetchingEnvironment.getArgument("udoi").toString();
//        String collection = dataFetchingEnvironment.getArgument("collection").toString();
        return deleteDocumentById(udoi, documentCollectionName);
    }

    private Map<String, String> deleteDocumentById(String id, String collection)  {
        try {
            udoService.deleteUdoById(id);
        } catch (UdoServiceException e) {
            e.printStackTrace();
        }
        Map<String,String> res = new HashMap<>();
        res.put("deleteResult", "document has been deleted.");
        return res;
    }
}
