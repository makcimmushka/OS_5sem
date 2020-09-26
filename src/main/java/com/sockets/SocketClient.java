package com.sockets;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class SocketClient {
    private Integer x;

    public void startClient(String message) throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 7777);
        SocketChannel client = SocketChannel.open();
        client.connect(hostAddress);

        this.x = Integer.parseInt(message);
        String threadName = Thread.currentThread().getName();

        byte[] bytes = {};

        if (threadName.equals("funcF")) {
            bytes = String.valueOf(this.funcF()).getBytes();
        } else if (threadName.equals("funcG")) {
            bytes = String.valueOf(this.funcG()).getBytes();
        }

        /* Send message to server */
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        client.write(buffer);
        buffer.clear();

        client.close();
    }

    private int funcF() {
        return 3 * this.x; /* Mock funcF implementation */
    }

    private int funcG() {
        return this.x * this.x; /* Mock funcG implementation */
    }
}
