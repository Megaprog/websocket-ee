package org.jmmo.controller;

import org.javatuples.Triplet;
import org.jmmo.protocol.Message;
import org.jmmo.protocol.MessageDecoder;
import org.jmmo.protocol.MessageEncoder;
import org.jmmo.service.CustomerService;
import org.jmmo.util.Localization;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonParsingException;
import javax.websocket.CloseReason;
import javax.websocket.DecodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@ServerEndpoint(value = "/websocket/login", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class LoginController {
    public static final String LOGIN_CUSTOMER = "LOGIN_CUSTOMER";

    @Inject
    private Logger log;

    @Inject
    private CustomerService customerService;

    @Inject
    private Localization localization;

    @OnMessage
    public Message onMessage(Message message, Session session) {
        log.info("onMessage: " + message);

        if (LOGIN_CUSTOMER.equals(message.getType())) {
            final Triplet<String, String, Instant> result = customerService.login(
                    message.getData().getString("email", "fake@mail.com"),
                    message.getData().getString("password", ""));

            if (result.getValue0() == null) {
                return message.reply("CUSTOMER_API_TOKEN", Json.createObjectBuilder()
                        .add("api_token", result.getValue1())
                        .add("api_token_expiration_date", DateTimeFormatter.ISO_INSTANT.format(result.getValue2())).build());
            }
            else {
                return message.reply("CUSTOMER_ERROR", Json.createObjectBuilder()
                        .add("error_description", localization.localize(result.getValue0()))
                        .add("error_code", result.getValue0()).build());
            }
        }

        return message;
    }

    @OnOpen
    public void helloOnOpen(Session session) {
        log.info("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void helloOnClose(CloseReason reason) {
        log.info("WebSocket connection closed with CloseCode: " + reason.getCloseCode());
    }

    @OnError
    public void error(Session session, Throwable t) {
        log.warning("Error with session " + session.getId());
        t.printStackTrace();

        if (t instanceof JsonParsingException || t instanceof DecodeException) {
            session.getAsyncRemote().sendText("{\"data\":{\n" +
                    "    \"error_description\": \"" + t.getMessage() + "\"\n" +
                    "    \"error_code\":\"error.badProtocol\"\n" +
                    "  }\n" +
                    "}");
        }
    }
}
