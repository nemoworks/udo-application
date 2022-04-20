package info.nemoworks.udo.storage;

import info.nemoworks.udo.model.UdoException;

public class UdoNotExistException extends UdoException {

    public UdoNotExistException(String message) {
        super(message);
    }

}
