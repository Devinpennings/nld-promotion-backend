package util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import controller.rest.serializers.EventConsumerSerializer;
import events.EventConsumer;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private ObjectMapper objectMapper;

    public JacksonConfig() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("mail-service", new Version(1, 0, 0, null));
        module.addSerializer(EventConsumer.class, new EventConsumerSerializer());
        objectMapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return this.objectMapper;
    }

}
