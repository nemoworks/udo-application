package info.nemoworks.udo.model.event;

import info.nemoworks.udo.model.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AfterCheckEvent extends StorageEvent {

    /**
     * 确认后事件
     *
     * @param contextId 场景id
     * @param source    实体标识
     * @param payload   实体内容
     */
    public AfterCheckEvent(EventType contextId, Identifiable source, byte[] payload) {
        super(contextId, source, payload);
    }

}