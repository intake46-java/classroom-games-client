package com.iti.crg.client.domain.usecases;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

public class LoginResult {
    private final boolean success;
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintStream writer;

    public LoginResult(boolean success, Socket socket, BufferedReader reader, PrintStream writer) {
        this.success = success;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public boolean isSuccess() { return success; }
    public Socket getSocket() { return socket; }
    public BufferedReader getReader() { return reader; }
    public PrintStream getWriter() { return writer; }
}