package info.nemoworks.udo.model;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Meter {
    private AtomicInteger value;

}
