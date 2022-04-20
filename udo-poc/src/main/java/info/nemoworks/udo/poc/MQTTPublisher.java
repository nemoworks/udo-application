package info.nemoworks.udo.poc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTPublisher {

    public static void main(String[] args) throws MqttException, InterruptedException {
        String clientid = UUID.randomUUID().toString();
        MqttClient client = null;
//        MqttClient client1 = null;
        client = new MqttClient("tcp://210.28.132.168:30609", clientid);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setUserName("udo-user");
        char[] password = "123456".toCharArray();
        options.setPassword(password);
        client.connect(options);

//        client1 = new MqttClient("tcp://test.mosquitto.org:1883", clientid);
//        client1.connect(options);
//        client1.subscribe("sub/app_demo/H4863HkBn1Poxb4D_FLG",((topic, payload) -> {
//            System.out.println("subscriber=====" + new String(payload.getPayload()));
//        }));

        Map<String, Object> map = new HashMap<>();
        map.put("name", "world");
        MqttMessage mqttMessage = new MqttMessage(map.toString().getBytes());
//        client.publish("sub/app_demo/lhFIQ3oB4t4MYt1TPlcc", mqttMessage);
//        int i = 0;
//        while (i < 100) {
//            client.publish("topic/pub/mqttTest@email.com", mqttMessage);
//            System.out.println("Publishing: mqttTest@email.com");
//            Thread.sleep(1000);
//            i++;
//        }
//        System.out.println("\tMessage '" + map + "' to 'udo'");
        String dumbStr = "ThisIsNotAJsonStr";
        MqttMessage dumbStrMessage = new MqttMessage(dumbStr.getBytes());
        client.publish("sub/app_demo/aZQl7XoBkC4qS4p24Hrj", dumbStrMessage);
//        String illegalStrCreatedBy = "{CreatedBy: who}";
//        String illegalStrCreatedOn = "{CreatedOn: 1202}";
//        String correctStr = "{CreatedBy: nemoworks, CreatedOn: 2021}";
//        MqttMessage illegalCreatedByMessage = new MqttMessage(illegalStrCreatedBy.getBytes());
//        MqttMessage illegalCreatedOnMessage = new MqttMessage(illegalStrCreatedOn.getBytes());
//        MqttMessage correctMessage = new MqttMessage(correctStr.getBytes());
//        client.publish("sub/app_demo/lhFIQ3oB4t4MYt1TPlcc", illegalCreatedByMessage);
//        client.publish("sub/app_demo/lhFIQ3oB4t4MYt1TPlcc", illegalCreatedOnMessage);
//        client.publish("sub/app_demo/lhFIQ3oB4t4MYt1TPlcc", correctMessage);
    }

//    public void publishFilteringMessage(String appId, String formatString, String udoId)
//        throws MqttException {
//        String clientid = UUID.randomUUID().toString();
//        MqttClient client = null;
////        MqttClient client1 = null;
//        client = new MqttClient("tcp://test.mosquitto.org:1883", clientid);
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setAutomaticReconnect(true);
//        options.setCleanSession(true);
//        options.setConnectionTimeout(10);
//        client.connect(options);
//        MqttMessage mqttMessage = new MqttMessage(formatString.getBytes());
//        client.publish("sub/" + appId + "/" + udoId, mqttMessage);
//        System.out.println("\tMessage '" + formatString + "' to 'udo'");
//    }
}
