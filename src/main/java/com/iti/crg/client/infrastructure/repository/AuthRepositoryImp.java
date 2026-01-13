package com.iti.crg.client.infrastructure.repository;

import com.google.gson.Gson;
import com.iti.crg.client.domain.entities.Player;
import com.iti.crg.client.domain.repository.AuthRepository;
import com.iti.crg.client.domain.usecases.LoginResult;
import com.iti.crg.client.infrastructure.dto.AuthResponse;
import com.iti.crg.client.infrastructure.dto.Login;
import com.iti.crg.client.infrastructure.dto.Register;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;

import java.io.BufferedReader;
import java.io.PrintStream;

public class AuthRepositoryImp implements AuthRepository {

    private final Gson gson;
    private final ServerConnection connection;
    public AuthRepositoryImp() {
        this.gson = new Gson();
        this.connection = ServerConnection.getInstance();
    }

    @Override
    public LoginResult login(String username, String password) {
        Login loginDto = new Login(username, password);

        String loginJson = gson.toJson(loginDto);

        Boolean isSuccess = sendRequest("LOGIN", loginJson);

        if (isSuccess == null) return new LoginResult(false, null,null,null);

        return new LoginResult(isSuccess, connection.getSocket(),connection.getReader(),connection.getWriter());
    }

    @Override
    public LoginResult register(String username, String password, int score) {
        Register registerDto = new Register(username, password, score);

        String registerJson = gson.toJson(registerDto);

        Boolean isSuccess = sendRequest("REGISTER", registerJson);

        if (isSuccess == null) return new LoginResult(false, null,null,null);

        return new LoginResult(isSuccess, connection.getSocket(), connection.getReader(), connection.getWriter());
    }

    private Boolean sendRequest(String type, String payload) {
        connection.forceDisconnect();

        if (!connection.connect()) {
            return null;
        }

        try {
            PrintStream ps = connection.getWriter();
            BufferedReader dis = connection.getReader();

            Request request = new Request(type, payload);
            String fullJsonRequest = gson.toJson(request);

            ps.println(fullJsonRequest);
            ps.flush();

            String data = dis.readLine();
            AuthResponse response =  gson.fromJson(data, AuthResponse.class);

            System.out.println(response.getScore());

            return response.isSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
