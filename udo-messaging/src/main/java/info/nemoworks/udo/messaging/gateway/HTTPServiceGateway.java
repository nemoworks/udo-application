package info.nemoworks.udo.messaging.gateway;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import info.nemoworks.udo.model.Udo;
import info.nemoworks.udo.model.UriType;
import info.nemoworks.udo.model.event.EventType;
import info.nemoworks.udo.model.event.GatewayEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HTTPServiceGateway extends UdoGateway {

    private final HttpClient client;
    private final Builder httpRequestBuilder =
        HttpRequest.newBuilder()
            .header("Content-Type", "application/json")
            .timeout(Duration.ofMinutes(2));

    @Value("${httpGateway.bearer.token}")
    private String bearerToken;

    @Value("${homeassistant.host}")
    private String homeAssistantHost;

    public ConcurrentHashMap<String, URI> getEndpoints() {
        return endpoints;
    }

    private final ConcurrentHashMap<String, URI> endpoints;


    public HTTPServiceGateway() throws IOException {
        super();
        endpoints = new ConcurrentHashMap<>();
        client =
            HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public HttpResponse<String> getRequestBody(byte[] payload)
        throws IOException, InterruptedException {
        HttpRequest request =
            httpRequestBuilder
                .GET()
                .uri(URI.create(new String(payload)))
                .header("Referer", "postman")
                .header("Authorization",
                    "Bearer "+bearerToken)
                .build();

        return client.send(request, BodyHandlers.ofString());
    }

    public HttpResponse<String> postRequestBody(byte[] payload, String data)
        throws IOException, InterruptedException {
        log.info("Post Payload: " + new String(payload));
        log.info("Data: " + data);
        HttpRequest request =
            httpRequestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(new String(payload)))
                .header("Authorization",
                    "Bearer "+bearerToken)
                .build();

        return client.send(request, BodyHandlers.ofString());
    }

    @Subscribe
    public void gateWayEvent(GatewayEvent gatewayEvent) {
        try {
            Udo udo = (Udo) gatewayEvent.getSource();
            if (udo != null) {
                if (udo.getUri() == null) {
                    return;
                }
                if (udo.getUri().getUriType().equals(UriType.MQTT)) {
                    return;
                }
            }
            EventType contextId = gatewayEvent.getContextId();
            switch (contextId) {
                case SAVE_BY_URI:
                    HttpResponse<String> uriBody = getRequestBody(gatewayEvent.getPayload());
                    this.updateUdoByUri(udo.getId(), uriBody.body().getBytes(),
                        gatewayEvent.getPayload(), udo.getContextInfo(), udo.getUri().getUriType());
                    break;
                case SAVE:
                    this.register(udo.getId(), new URI(udo.uri.getUri()));
                    break;
                case UPDATE:
//                    if (gatewayEvent.getPayload() != null) {
//                        HttpResponse<String> body = getRequestBody(gatewayEvent.getPayload());
//                        this.updateUdoByPolling(udo.getId(), body.body().getBytes());
//                    }
                    JsonObject data = (JsonObject) udo.getData();
                    if (data.has("state")) {
                        JsonObject entityId = new JsonObject();
                        entityId.addProperty("entity_id", data.get("entity_id").getAsString());
                        String postBody = new Gson().toJson(entityId);
                        if (data.get("state").getAsString().equals("off")) {
                            this.postRequestBody(
                                    (homeAssistantHost+"/api/services/fan/turn_off").getBytes(),
                                postBody);
                        } else {
                            this.postRequestBody(
                                    (homeAssistantHost+"/api/services/fan/turn_on").getBytes(),
                                postBody);
                        }
                    }
                    this.updateUdoByPolling(udo.getId(), udo.getData().toString().getBytes());
                    break;
                case DELETE:
                    this.unregister(new String(gatewayEvent.getPayload()));
                    break;
                default:
                    break;
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void register(String tag, URI uri) {
        if (!endpoints.containsKey(tag)) {
            log.info(tag + " registered on httpServiceGateway");
            endpoints.put(tag, uri);
        }
    }

    public synchronized void unregister(String tag) {
        if (endpoints.containsKey(tag)) {
            log.info(tag + " unregistered on httpServiceGateway");
            endpoints.remove(tag);
        }
    }

    public void start() throws InterruptedException {
        endpoints.forEach(
            (key, value) -> {
                try {
                    log.info("fetching data...");
                    downLink(key, value.toString().getBytes());
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
    }

    // downlink: 获取资源状态，向udo发送状态更新消息
    @Override
    public void downLink(String tag, byte[] payload) throws IOException, InterruptedException {
        HttpRequest request =
            httpRequestBuilder
                .GET()
                .uri(URI.create(new String(payload)))
                .header("Authorization",
                    "Bearer "+bearerToken)
                .build();

        HttpResponse<String> body = client.send(request, BodyHandlers.ofString());
        this.updateUdoByPolling(tag, body.body().getBytes());
    }

    // updatelink: 参数为topic中监听到的更新请求 向资源发送状态更新请求
    @Override
    public void updateLink(String tag, byte[] payload, String data)
        throws IOException, InterruptedException {
        HttpRequest request = httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString(data))
            .uri(URI.create(new String(payload)))
            .header("Authorization",
                "Bearer "+bearerToken)
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        log.info(response.statusCode());
        // print response body
//        log.info(response.body());

    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
