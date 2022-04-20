package info.nemoworks.udo.model.event;

import info.nemoworks.udo.model.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 消息网关接收事件
 */
public class GatewayEvent extends UdoEvent{

    private EventType contextId;
    private Identifiable source;
    private byte[] payload;
}
