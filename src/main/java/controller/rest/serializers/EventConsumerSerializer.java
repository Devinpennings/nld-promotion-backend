package controller.rest.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import events.EventConsumer;

import java.io.IOException;

public class EventConsumerSerializer extends JsonSerializer<EventConsumer> {

    @Override
    public void serialize(EventConsumer aClass, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("id", aClass.getId());
        jgen.writeStringField("title", aClass.getTitle());
        jgen.writeStringField("description", aClass.getDescription());
        jgen.writeEndObject();
    }

}
