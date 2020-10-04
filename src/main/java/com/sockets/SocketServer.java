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
    private final Map<SocketChannel, List> channelMapper;
    private final InetSocketAddress listenAddress;
    private int multiplication = 1;
    private int processedClientsAmount = 0;
    private boolean isProcessingRequests = true;

    public SocketServer(String address, int port) {
        this.listenAddress = new InetSocketAddress(address, port);
        this.channelMapper = new HashMap<>();
    }

    /* Create server channel */
    public void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        /* Retrieve server socket and bind it to port */
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started ...");

        /* Accept only two sockets for funcF and funcG calculations */
        outer: while (this.processedClientsAmount < 2) {
            /* Wait for events */
            this.selector.select();

            /* Iterate through keys */
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
                }

                else if (key.isReadable()) {
                    this.read(key);

                    if (!this.isProcessingRequests) {
                        break outer;
                    }
                }
            }
        }

        serverChannel.close();
    }

    /* Accept a connection made to this channel's socket */
    private synchronized void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();

        channel.configureBlocking(false); /* NIO mode for server */
        System.out.println("Server connected with client ...");

        /* Register channel with selector for further IO read operation */
        this.channelMapper.put(channel, new ArrayList<>());
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    /* Read from the socket channel */
    private synchronized void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String[] funcCalculationData = this.getComputationData(channel);

        String funcName = funcCalculationData[0];
        String funcCalculationResult = funcCalculationData[1];

        /* If we got zero or undefined from func computation, we shouldn't wait for another one */
        if (funcCalculationResult.equals("0") || funcCalculationResult.equals(Constants.UNDEFINED)) {
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
        this.channelMapper.remove(channel);
        channel.close();
        key.cancel();
    }

    private String[] getComputationData(SocketChannel channel) throws IOException {
        Gson gson = new Gson();
        ByteBuffer buffer = ByteBuffer.allocate(128);

        int numRead = channel.read(buffer); /* Number of reading bytes */

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);

        return gson.fromJson(new String(data), String[].class);
    }

    public synchronized int getMultiplication() {
        return this.multiplication;
    }
}
