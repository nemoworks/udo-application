package info.nemoworks.udo.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public abstract class Identifiable {

    private String id;

    public long createdOn;
    public String createdBy;
    public long modifiedOn;
    public String modifiedBy;
    //    public String uri;
    public Uri uri;
    public String location;

    public abstract JsonObject toJsonObject();

    private ContextInfo contextInfo;

    // 当前时钟
    private String clock;
    // 极限访问距离
    private float limitDis;

    public Identifiable() {
        this.createdBy = "nemoworks";
        this.contextInfo = new ContextInfo();
    }

}
