package org.nbreval.weather_twin.gateway.test.util;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpUtils {

    /**
     * Gets a random available port in system
     * 
     * @return The available port found
     */
    public static int getRandomPort() {
        try (var socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
