package info.nemoworks.udo.model.event;

import info.nemoworks.udo.model.Identifiable;
import info.nemoworks.udo.model.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * udo 事件
 */
public class UdoEvent {
    private EventType contextId;
    private Identifiable source;
    private byte[] payload;

}
