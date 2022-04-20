package info.nemoworks.udo.service.eventHandler;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.event.SyncEvent;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import info.nemoworks.udo.storage.UdoNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncEventHandler {

    @Autowired
    UdoService udoService;

    @Subscribe
    public void syncEvent(SyncEvent syncEvent) throws UdoServiceException, UdoNotExistException {
        Udo udo = (Udo) syncEvent.getSource();
        JsonElement udoData = udo.getData();
        Udo udo1 = udoService.getUdoById(udo.getId());
        if (udo1.getData() == null || udo.getData() == null) {
            return;
        }
//        if (new String(syncEvent.getPayload()).equals("reject")) {
//            return;
//        }
        JsonObject dataToUpdate = udoData.getAsJsonObject();
        JsonObject dataOrigin = udo1.getData().getAsJsonObject();
        if (dataToUpdate.has("last_updated")) {
            dataToUpdate.remove("last_updated");
        }
        if (dataToUpdate.has("last_changed")) {
            dataToUpdate.remove("last_changed");
        }
        if (dataToUpdate.has("state")) {
            if (dataOrigin.has("state")) {
                if (dataToUpdate.get("state").getAsString()
                    .equals(dataOrigin.get("state").getAsString())) {
                    return;
                }
            }
        }

        if (!udo1.getData().equals(dataToUpdate)) {
            System.out.println("detect udo changing...");
            System.out.println("udo origin: " + udo1.getData().getAsJsonObject().toString());
            System.out.println("udo to up: " + dataToUpdate.toString());
            udo1.setData(dataToUpdate);
            udoService.saveOrUpdateUdo(udo1);
        }
    }


}
