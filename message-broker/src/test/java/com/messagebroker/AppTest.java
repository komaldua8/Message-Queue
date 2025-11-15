package com.messagebroker;

import com.messagebroker.server.MessageBrokerServer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testServerInitialization() {
        MessageBrokerServer server = new MessageBrokerServer();
        assertNotNull(server, "Server instance should be created");
    }
}
