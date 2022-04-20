package info.nemoworks.udo.service;

import com.google.gson.JsonObject;
import info.nemoworks.udo.model.ContextInfo;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.UdoType;
import info.nemoworks.udo.model.Uri;
import info.nemoworks.udo.model.UriType;
import info.nemoworks.udo.model.event.EventType;
import info.nemoworks.udo.model.event.GatewayEvent;
import info.nemoworks.udo.service.eventHandler.UdoEventManager;
import info.nemoworks.udo.storage.UdoNotExistException;
import info.nemoworks.udo.storage.UdoPersistException;
import info.nemoworks.udo.storage.UdoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UdoService {

    private final UdoRepository udoRepository;

    private final UdoEventManager udoEventManager;


    public UdoService(
        @Qualifier("udoWrapperRepository") UdoRepository udoRepository,
        UdoEventManager udoEventManager) {
        this.udoRepository = udoRepository;
        this.udoEventManager = udoEventManager;
    }

    /**
     * 根据uri创建udo实例
     * @param uri
     * @param longitude
     * @param latitude
     * @param uriType
     * @param avatarUrl
     * @param name
     * @return
     * @throws UdoPersistException
     */
    public String createUdoByUri(String uri, float longitude, float latitude, String uriType,
        String avatarUrl, String name)
        throws UdoPersistException {
        Udo saved = new Udo();
        switch (uriType) {
            case "HTTP":
                saved.setUri(new Uri(uri, UriType.HTTP));
                break;
            case "MQTT":
                saved.setUri(new Uri(uri, UriType.MQTT));
                break;
            default:
                saved.setUri(new Uri(uri, UriType.NOTEXIST));
                break;
        }
//        saved.setLocation(location);
        ContextInfo contextInfo = saved.getContextInfo();
        JsonObject location = new JsonObject();
        location.addProperty("longitude", longitude);
        location.addProperty("latitude", latitude);
        contextInfo.addContext("location", location);
        contextInfo.addContext("avatarUrl", avatarUrl);
        contextInfo.addContext("name", name);
        System.out.println("location: " + location);
        saved.setContextInfo(contextInfo);
//        JsonObject data = new JsonObject();
//        data.add("location", location);
//        data.addProperty("avatarUrl", avatarUrl);
//        saved.setData(data);
        saved = udoRepository.saveUdo(saved);
        udoEventManager.post(new GatewayEvent(EventType.SAVE_BY_URI, saved, uri.getBytes()));
        return saved.getId();
    }

    public Udo saveByUri(Udo udo, byte[] payload) throws UdoServiceException {
        try {
            udoRepository.saveUdo(udo);
        } catch (UdoPersistException e) {
            throw new UdoServiceException("Udo (" + udo.getId() + ") cannot be saved");
        }
        udoEventManager.post(new GatewayEvent(EventType.SAVE, udo, payload));
        return udo;
    }

    public Udo saveOrUpdateUdo(Udo udo) throws UdoServiceException, UdoNotExistException {
        Udo saved = null;
        boolean created = false;
        if (udo.getId() != null) {
            saved = udoRepository.findUdoById(udo.getId());
            if (saved == null) {
                created = true;
            }
        } else {
            created = true;
        }
        try {
            saved = udoRepository.saveUdo(udo);
        } catch (UdoPersistException e) {
            throw new UdoServiceException("Udo (" + udo.getId() + ") cannot be saved");
        }
        if (created) {
//            System.out.println("Updating Udo: " + saved.getId());
            if (udo.getUri() != null && udo.getUri().getUriType() != UriType.NOTEXIST) {
                udoEventManager
                    .post(
                        new GatewayEvent(EventType.SAVE, saved, udo.getUri().getUri().getBytes()));
            }
//            else {
//                udoEventManager.post(new GatewayEvent(EventType.SAVE, saved, null));
//            }
        } else {
            if (udo.getUri() != null && udo.getUri().getUriType() != UriType.NOTEXIST) {
                udoEventManager
                    .post(new GatewayEvent(EventType.UPDATE, saved,
                        udo.getUri().getUri().getBytes()));
            }
//            else {
//                udoEventManager.post(new GatewayEvent(EventType.UPDATE, saved, null));
//            }
        }
        return saved;
    }

    public Udo getUdoById(String id) {
        try {
            return udoRepository.findUdoById(id);
        } catch (UdoNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Udo> getUdoByType(UdoType udoType) {
        return udoRepository.findUdosByType(udoType);
    }

    public void deleteUdoById(String id) throws UdoServiceException {
        try {
            udoRepository.deleteUdoById(id);
            udoEventManager.post(new GatewayEvent(EventType.DELETE, null, id.getBytes()));
        } catch (UdoNotExistException e) {
            throw new UdoServiceException("not exist");
        }
    }

    public UdoType saveOrUpdateType(UdoType udoType) throws UdoServiceException {
        UdoType saved = null;
        try {
            saved = udoRepository.saveType(udoType);
            return saved;
        } catch (UdoPersistException e) {
            throw new UdoServiceException("canot save/update");
        }
    }

    public UdoType getTypeById(String id) {
        try {
            return udoRepository.findTypeById(id);
        } catch (UdoNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UdoType> getAllTypes() {
        return udoRepository.findAllTypes();
    }

    public List<Udo> getAllUdos() {
        return udoRepository.findAllUdos();
    }

    public UdoType getTypeForUdo(Udo udo) {
        try {
            return udoRepository.findTypeById(udo.getType().getId());
        } catch (UdoNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTypeById(String id) throws UdoServiceException {
        try {
            udoRepository.deleteTypeById(id);

        } catch (UdoNotExistException e) {
            throw new UdoServiceException("not exist");
        }
    }

    public Udo syncUdo(Udo udo) throws UdoServiceException {
        Udo saved = null;
        try {
            saved = udoRepository.sync(udo);
            udoEventManager.post(new GatewayEvent(EventType.SYNC, udo, null));
        } catch (UdoPersistException e) {
            throw new UdoServiceException("Udo (" + udo.getId() + ") cannot be sync");
        }
        return saved;
    }
}
