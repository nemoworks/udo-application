package info.nemoworks.udo.poc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirQuality {
    private int temperature;
    private int humidity;
    private int aqi;
}
