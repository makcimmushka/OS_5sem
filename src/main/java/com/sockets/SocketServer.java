package com.sockets;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import com.constants.Constants;
import com.google.gson.Gson;


public class SocketServer {
    private Selector selector;
    private final InetSocketAddress listenAddress;
    private Integer multiplication = 1;
    private int processedClientsAmount = 0;
    private boolean isProcessingRequests = true;
    private Integer variant = 1;
    private final List<Thread> clientsThreads = new ArrayList<>();

    public SocketServer(String address, int port) {
        this.listenAddress = new InetSocketAddress(address, port);
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

    private void prepareServer() throws IOException {
        this.selector = Selector.open();
        this.multiplication = 1;
        this.processedClientsAmount = 0;
        this.isProcessingRequests = true;
    }

    /* Create server channel */
    public void startServer() throws IOException {
        this.prepareServer();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        /* Retrieve server socket and bind it to port */
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started ...");

        Runnable client = () -> {
            try {
                new SocketClient().startClient(this.variant);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Thread threadFuncF = new Thread(client, Constants.FUNC_F);
        Thread threadFuncG = new Thread(client, Constants.FUNC_G);

        this.clientsThreads.add(threadFuncF);
        this.clientsThreads.add(threadFuncG);

        threadFuncF.start();
        threadFuncG.start();

        /* Accept only two sockets for funcF and funcG calculations */
        outer:
        while (this.processedClientsAmount < 2) {
            /* Wait for events */
            this.selector.select();

            /* Iterate through selector keys */
            Iterator keys = this.selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                /* Prevent the same key from coming up
                    again the next time around. */
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);

                    if (!this.isProcessingRequests) {
                        break outer;
                    }
                }
            }
        }

        /* Close server channel and interrupt clients threads ... */
        this.closeClientsThreads();
        serverChannel.close();
        this.selector.selectNow();
    }

    public void closeClientsThreads() {
        for (Thread clientThread : this.clientsThreads) {
            clientThread.interrupt();
        }
    }


    /* Accept a connection made to this channel's socket */
    private synchronized void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();

        channel.configureBlocking(false); /* NIO mode for server */
        System.out.println("Server connected with client ...");

        /* Register channel with selector for further IO read operation */
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    /* Read from the socket channel */
    private synchronized void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String[] funcCalculationData = this.getComputationData(channel);

        String funcName = funcCalculationData[0];
        String funcCalculationResult = funcCalculationData[1];

        /* If we got zero or undefined from func computation, we shouldn't wait for another one */
        if (funcCalculationResult.equals("0")) {
            this.multiplication *= Integer.parseInt(funcCalculationResult);
            System.out.println("Got " + funcCalculationResult + " from " + funcName + " , stop computation ...");

            this.isProcessingRequests = false;
            return;
        } else {
            System.out.println("Got " + funcCalculationResult + " from " + funcName);
        }

        this.multiplication *= Integer.parseInt(funcCalculationResult);

        /* Increased number of processed clients */
        this.processedClientsAmount++;

        /* Delete channel after receiving the result */
        key.cancel();
        this.selector.selectNow();
        channel.close();
    }

    private String[] getComputationData(SocketChannel channel) throws IOException {
        Gson gson = new Gson();
        ByteBuffer buffer = ByteBuffer.allocate(128);

        int numRead = channel.read(buffer); /* Number of reading bytes */

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);

        return gson.fromJson(new String(data), String[].class);
    }

    public Integer getMultiplication() {
        return this.multiplication;
    }

    public List<Thread> getClientsThreads() {
        return this.clientsThreads;
    }
}