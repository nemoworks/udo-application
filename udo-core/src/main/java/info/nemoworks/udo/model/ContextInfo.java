package info.nemoworks.udo.model;

import java.util.HashMap;
import java.util.Map;

public class ContextInfo {

    private Map<String, Object> contexts = null;

    public ContextInfo() {

    }

    public Object getContext(String ctxid) {
        if (contexts != null) {
            return contexts.get(ctxid);
        }
        return null;
    }

    public void addContext(String ctxid, Object ctxObj) {
        if (contexts == null) {
            contexts = new HashMap<>();
        }

        contexts.put(ctxid, ctxObj);
    }

    public void removeContext(String ctxid) {
        if (contexts != null) {
            contexts.remove(ctxid);
        }
    }
}
