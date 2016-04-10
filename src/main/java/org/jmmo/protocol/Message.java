package org.jmmo.protocol;

import javax.json.JsonObject;
import java.util.Objects;

public class Message {
    private final String type;
    private final String sequenceId;
    private final JsonObject data;

    public Message(String type, String sequenceId, JsonObject data) {
        this.type = type;
        this.sequenceId = sequenceId;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public JsonObject getData() {
        return data;
    }

    public Message reply(String type, JsonObject data) {
        return new Message(type, getSequenceId(), data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(type, message.type) &&
                Objects.equals(sequenceId, message.sequenceId) &&
                Objects.equals(data, message.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sequenceId, data);
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", sequenceId='" + sequenceId + '\'' +
                ", data=" + data +
                '}';
    }
}
