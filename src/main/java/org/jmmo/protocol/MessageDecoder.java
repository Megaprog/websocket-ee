package org.jmmo.protocol;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.logging.Logger;

@Singleton
public class MessageDecoder implements Decoder.Text<Message> {

    @Inject
    private Logger log;

    @Override
    public Message decode(String s) throws DecodeException {
        log.info("Decoding: " + s);

        final JsonReader reader = Json.createReader(new StringReader(s));
        final JsonObject jsonObject = reader.readObject();

        if (!jsonObject.containsKey("type")) {
            throw new DecodeException(s, "Requires type value");
        }
        final String type = jsonObject.getString("type");

        if (!jsonObject.containsKey("sequence_id")) {
            throw new DecodeException(s, "Requires sequence_id value");
        }
        final String sequenceId = jsonObject.getString("sequence_id");

        if (!jsonObject.containsKey("data")) {
            throw new DecodeException(s, "Requires data object");
        }
        final JsonObject data = jsonObject.getJsonObject("data");

        return new Message(type, sequenceId, data);
    }

    @Override
    public boolean willDecode(String msg) {
        return true;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig config) {
    }
}
