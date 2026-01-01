package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.infrastructure.remote.ServerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginUseCase {

    public LoginResult execute(String username, String password) {
        ServerConnection connection = ServerConnection.getInstance();

        if (!connection.connect()) {
            return new LoginResult(false, null, null, null);
        }

        try {
            PrintStream ps = connection.getWriter();
            BufferedReader dis = connection.getReader();

            ps.println(username);
            ps.println(password);
            ps.flush();

            String response = dis.readLine();
            boolean isPlayerExist = Boolean.parseBoolean(response);

            return new LoginResult(isPlayerExist, connection.getSocket(), dis, ps);

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResult(false, null, null, null);
        }
    }
}