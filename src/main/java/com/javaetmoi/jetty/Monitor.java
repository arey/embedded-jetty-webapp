package com.javaetmoi.jetty;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Listens for stop commands and causes jetty to stop by stopping the server instance.
 *
 * @see <a href="https://github.com/eclipse/jetty.project/tree/master/jetty-maven-plugin">https://github.com/eclipse/jetty.project/tree/master/jetty-maven-plugin</a>
 */
public class Monitor extends Thread {

    private static Logger LOGGER = LoggerFactory.getLogger(Monitor.class);

    private Server[] servers;
    ServerSocket serverSocket;

    public Monitor(int port, Server[] servers) throws UnknownHostException, IOException {
        if (port <= 0) {
            throw new IllegalStateException("Bad stop PORT");
        }
        this.servers = servers;
        setDaemon(true);
        setName("StopJettyMonitor");
        serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        serverSocket.setReuseAddress(true);
    }

    public void run() {
        while (serverSocket != null) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                socket.setSoLinger(false, 0);
                LineNumberReader lin = new LineNumberReader(new InputStreamReader(
                        socket.getInputStream()));
                String cmd = lin.readLine();
                if ("stop".equals(cmd)) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage(), e);
                    }
                    try {
                        serverSocket.close();
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage(), e);
                    }
                    serverSocket = null;

                    for (int i = 0; servers != null && i < servers.length; i++) {
                        try {
                            LOGGER.info("Stopping server " + i);
                            servers[i].stop();
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }

                } else
                    LOGGER.info("Unsupported monitor operation");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
                socket = null;
            }
        }
    }
}