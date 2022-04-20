package info.nemoworks.udo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {

    public enum LinkType {
        OBJ, OBJS
    }

    private String name;
    private LinkType linkType;
//    private UdoType from;
    private String to;

}
