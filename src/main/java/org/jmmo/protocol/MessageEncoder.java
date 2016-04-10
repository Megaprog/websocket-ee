package org.jmmo.protocol;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder.Text;
import javax.websocket.EndpointConfig;
import java.io.StringWriter;
import java.util.logging.Logger;

@Singleton
public class MessageEncoder implements Text<Message> {

    @Inject
    private Logger log;

    @Override
    public String encode(Message message) throws EncodeException {
        log.info("Encoding: " + message);

        final JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("type", message.getType())
                .add("sequence_id", message.getSequenceId())
                .add("data", message.getData());

        final StringWriter stringWriter = new StringWriter();
        try (final JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(jsonBuilder.build());
        }

        return stringWriter.toString();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig config) {
    }
}
