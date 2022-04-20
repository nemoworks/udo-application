package info.nemoworks.udo.service.eventHandler;


import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonElement;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.event.AfterCheckEvent;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import info.nemoworks.udo.storage.UdoNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AfterCheckEventHandler {

    @Autowired
    UdoService udoService;

    @Subscribe
    public void afterCheckEvent(AfterCheckEvent afterCheckEvent)
        throws UdoServiceException, UdoNotExistException {
        Udo udo = (Udo) afterCheckEvent.getSource();
        JsonElement udoData = udo.getData();
        Udo udo1 = udoService.getUdoById(udo.getId());
        if (udo1.getData() == null) {
            return;
        }
        if (!udo1.getData().equals(udo.getData())) {
            System.out.println("detect udo changing...");
            System.out.println("udo origin: " + udo1.getData().getAsJsonObject().toString());
            System.out.println("udo to up: " + udo.getData().getAsJsonObject().toString());
            udo1.setData(udoData);
            udoService.saveOrUpdateUdo(udo1);
        }
    }
}
