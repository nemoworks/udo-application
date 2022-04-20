package info.nemoworks.udo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Uri {

    private String uri;
    private UriType uriType;
}
