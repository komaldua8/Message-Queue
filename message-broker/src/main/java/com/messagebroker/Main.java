package com.messagebroker;

import com.messagebroker.server.MessageBrokerServer;

public class Main {
    public static void main(String[] args) {
        new MessageBrokerServer(5000).start();
    }
}

