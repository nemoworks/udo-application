package info.nemoworks.udo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.saasquatch.jsonschemainferrer.AdditionalPropertiesPolicies;
import com.saasquatch.jsonschemainferrer.EnumExtractors;
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer;
import com.saasquatch.jsonschemainferrer.RequiredPolicies;
import com.saasquatch.jsonschemainferrer.SpecVersion;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Udo extends Identifiable {

    private UdoType type;

    private JsonElement data;

    public Udo(JsonElement data) throws JsonProcessingException {
        super();
        this.data = data;
        this.type = this.inferType();
    }

    public Udo() {
    }

    public Udo(UdoType type, JsonElement data) {
        super();
        this.type = type;
        this.data = data;
    }

    @Override
    public JsonObject toJsonObject() {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(this);
        return (JsonObject) jsonElement;
    }

    public UdoType inferType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = new Gson().toJson(data);

        JsonSchemaInferrer inferrer =
            JsonSchemaInferrer.newBuilder()
                .setSpecVersion(SpecVersion.DRAFT_06)
                .setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed())
                .setRequiredPolicy(RequiredPolicies.nonNullCommonFields())
                .addEnumExtractors(
                    EnumExtractors.validEnum(java.time.Month.class),
                    EnumExtractors.validEnum(java.time.DayOfWeek.class))
                .build();

        JsonNode jsonNode = mapper.readTree(jsonStr);
        JsonNode res = inferrer.inferForSample(jsonNode);
        String resStr = res.toPrettyString();
        resStr = resStr.replace("boolean", "bool");
        return new UdoType(new Gson().fromJson(resStr, JsonObject.class));
    }
}
