package info.nemoworks.udo.model.event;

import info.nemoworks.udo.model.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 向mqtt server发送的事件
 */
public class PublishByMqttEvent extends UdoEvent {
    private EventType contextId;
    private Identifiable source;
    private byte[] payload;
}
