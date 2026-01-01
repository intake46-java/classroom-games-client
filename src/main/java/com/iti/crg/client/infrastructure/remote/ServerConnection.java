package com.iti.crg.client.infrastructure.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerConnection {

    private static final String SERVER_IP = "127.0.0.1"; // Or load from config
    private static final int SERVER_PORT = 5005;

    private Socket socket;
    private BufferedReader reader;
    private PrintStream writer;

    private ServerConnection() {
        // Optional: You could auto-connect here, or keep it manual
    }

    // 2. Static Inner Class - The "Holder"
    // This class is not loaded into memory until getInstance() is called.
    private static class ServerConnectionHolder {
        private static final ServerConnection INSTANCE = new ServerConnection();
    }

    // 3. Global Access Point
    public static ServerConnection getInstance() {
        return ServerConnectionHolder.INSTANCE;
    }

    // --- Infrastructure Logic ---

    /**
     * Connects to the server if not already connected.
     * @return true if connection successful or already connected
     */
    public boolean connect() {
        if (socket != null && !socket.isClosed() && socket.isConnected()) {
            return true;
        }

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());
            System.out.println("Connected to server.");
            return true;
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket = null;
            reader = null;
            writer = null;
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintStream getWriter() {
        return writer;
    }

    public Socket getSocket() {
        return socket;
    }
}