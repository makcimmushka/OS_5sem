package com.sockets;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class SocketClient {
    public void startClient(int variant) throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 7777);
        SocketChannel client = SocketChannel.open();
        client.connect(hostAddress);

        String threadName = Thread.currentThread().getName();

        byte[] bytes = {};

        if (threadName.equals("funcF")) {
            bytes = String.valueOf(this.customFuncF(variant)).getBytes();
        } else if (threadName.equals("funcG")) {
            bytes = String.valueOf(this.customFuncG(variant)).getBytes();
        }

        /* Send message to server */
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        client.write(buffer);
        buffer.clear();

        client.close();
    }

    private Integer customFuncF(int variant) throws InterruptedException {
        /* Returned value = variant * 10 */
        switch (variant) {
            case 1:
                Thread.sleep(5000);
                return 10;
            case 2:
                Thread.sleep(10000);
                return 20;
            case 3:
                Thread.sleep(5000);
                return 0;
            case 4:
            case 6:
                Thread.sleep(5000);
                return null;
            case 5:
                Thread.sleep(5000);
                return 50;
            default:
                return null;
        }
    }

    private Integer customFuncG(int variant) throws InterruptedException {
        /* Returned value = variant * 100 */
            switch (variant) {
                case 1:
                    Thread.sleep(10000);
                    return 100;
                case 2:
                    Thread.sleep(5000);
                    return 200;
                case 3:
                case 5:
                    Thread.sleep(5000);
                    return null;
                case 4:
                    Thread.sleep(5000);
                    return 0;
                case 6:
                    Thread.sleep(5000);
                    return 600;
                default:
                    return null;
            }
    }
}
