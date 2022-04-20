package info.nemoworks.udo.service.eventHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.Subscribe;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.event.EventType;
import info.nemoworks.udo.model.event.PublishByMqttEvent;
import info.nemoworks.udo.model.event.SubscribeByMqttEvent;
import info.nemoworks.udo.service.UdoService;
import info.nemoworks.udo.service.UdoServiceException;
import info.nemoworks.udo.storage.UdoNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscribeByMqttEventHandler {
    @Autowired
    UdoService udoService;

    @Autowired
    UdoEventManager udoEventManager;

    @Subscribe
    public void saveByMqttEvent(SubscribeByMqttEvent subscribeByMqttEvent) throws JsonProcessingException {
        System.out.println("In SBUri Subscribe");
        Udo udo = (Udo) subscribeByMqttEvent.getSource();
        udo.setType(udo.inferType());
        udoEventManager.post(new PublishByMqttEvent(EventType.PUBLISH_BY_MQTT,udo,null));
//        try {
//            udoService.saveOrUpdateUdo(udo);
//            udoEventManager.post(new PublishByMqttEvent(EventType.PUBLISH_BY_MQTT,udo,null));
//        } catch (UdoServiceException | UdoNotExistException e) {
//            e.printStackTrace();
//        }
    }
}
