package info.nemoworks.udo.model.event;

import info.nemoworks.udo.model.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 数据存储事件
 */
public class StorageEvent extends UdoEvent{
    private EventType contextId;
    private Identifiable source;
    private byte[] payload;
}
