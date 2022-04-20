package info.nemoworks.udo.service.eventHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.UdoType;
import info.nemoworks.udo.model.event.SaveByUriEvent;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveByUriEventHandler {

    @Autowired
    UdoService udoService;

    @Subscribe
    public void saveByUriEvent(SaveByUriEvent saveByUriEvent)
        throws JsonProcessingException, UdoServiceException {
        System.out.println("In SBUri Subscribe");
        Udo udo = (Udo) saveByUriEvent.getSource();
//        udo.setType(udo.inferType());
        JsonObject data = (JsonObject) udo.getData();
//        JsonObject subData = data.getAsJsonObject("data");
        data.add("location", (JsonObject) udo.getContextInfo().getContext("location"));
        data.addProperty("avatarUrl", udo.getContextInfo().getContext("avatarUrl").toString());
//        data.add("data", subData);
//        data.addProperty("uri", new String(saveByUriEvent.getPayload()));
        udo.setData(data);
        UdoType udoType = udo.inferType();
        JsonObject schema = udoType.getSchema();
        schema.addProperty("title", udo.getContextInfo().getContext("name").toString());
        udoType.setSchema(schema);
        udoService.saveOrUpdateType(udoType);
        udo.setType(udoType);
        udoService.saveByUri(udo, saveByUriEvent.getPayload());
    }
}
