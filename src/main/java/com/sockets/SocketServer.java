package com.sockets;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class SocketServer {
    private Selector selector;
    private final Map<SocketChannel, List> dataMapper;
    private final InetSocketAddress listenAddress;

    public SocketServer(String address, int port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);
        dataMapper = new HashMap<>();
    }

    /* Create server channel */
    public void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        /* Retrieve server socket and bind it to port */
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started...");

        while (true) {
            /* Wait for events */
            this.selector.select();

            /* Work on selected keys */
            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                /* This is necessary to prevent the same key from coming up
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
                }
            }
        }
    }

    /* Accept a connection made to this channel's socket */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        System.out.println("Server connected with client");

        /* Register channel with selector for further IO */
        dataMapper.put(channel, new ArrayList<>());
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int numRead = -1;
        numRead = channel.read(buffer); /* Number of bytes read */

        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        }

        System.out.println("Server got number from client: " + new String(buffer.array()));
    }
}
