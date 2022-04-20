package info.nemoworks.udo.poc;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AirPurifier {

    private String entityId;
    private String state;
    private String mode;
    private int temperature;
    private int humidity;
    private int aqi;
}
