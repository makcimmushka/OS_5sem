package com.sockets;

import com.constants.Constants;
import spos.lab1.demo.IntOps;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;


public class SocketClient {
    public void startClient(int variant) throws IOException, InterruptedException {
        try {
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 7777);
            SocketChannel client = SocketChannel.open();
            client.connect(hostAddress);

            String threadName = Thread.currentThread().getName();

            if (!Thread.currentThread().isInterrupted() || client.isRegistered()) {
                byte[] bytes = {};

                if (threadName.equals(Constants.FUNC_F)) {
//            Integer funcFResult = this.customFuncF(variant);
                    Integer funcFResult = IntOps.funcF(variant - 1);
                    if (funcFResult != null) {
                        String message = Arrays.toString(new String[]{Constants.FUNC_F, funcFResult.toString()});
                        bytes = message.getBytes();
                    }
                } else if (threadName.equals(Constants.FUNC_G)) {
//            Integer funcGResult = this.customFuncG(variant);
                    Integer funcGResult = IntOps.funcG(variant - 1);

                    if (funcGResult != null) {
                        String message = Arrays.toString(new String[]{Constants.FUNC_G, funcGResult.toString()});
                        bytes = message.getBytes();
                    }
                }

                /* Send message to server */
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                client.write(buffer);
                buffer.clear();

                client.close();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Integer customFuncF(int variant) {
        /* Returned value = variant * 10 */
        try {
            switch (variant) {
                case 1:
                    Thread.sleep(3000);
                    return 10;
                case 2:
                    Thread.sleep(6000);
                    return 20;
                case 3:
                    Thread.sleep(3000);
                    return 0;
                case 4:
                case 6:
                    Thread.sleep(6000);
                    return Integer.MAX_VALUE;
                case 5:
                    Thread.sleep(3000);
                    return 50;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    private Integer customFuncG(int variant) {
        /* Returned value = variant * 100 */
        try {
            switch (variant) {
                case 1:
                    Thread.sleep(6000);
                    return 100;
                case 2:
                    Thread.sleep(3000);
                    return 200;
                case 3:
                case 5:
                    Thread.sleep(6000);
                    return Integer.MAX_VALUE;
                case 4:
                    Thread.sleep(3000);
                    return 0;
                case 6:
                    Thread.sleep(3000);
                    return 600;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }
}