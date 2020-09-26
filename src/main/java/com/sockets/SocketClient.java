package com.sockets;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class SocketClient {
    public void startClient(String message)
            throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 7777);
        SocketChannel client = SocketChannel.open();
        client.connect(hostAddress);

        System.out.println(Thread.currentThread().getName() + " started ...");

        /* Send message to server */
        byte[] bytes = message.getBytes();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        client.write(buffer);
        System.out.println("Argument X was written");
        buffer.clear();
        Thread.sleep(1500);

        client.close();
    }
}
