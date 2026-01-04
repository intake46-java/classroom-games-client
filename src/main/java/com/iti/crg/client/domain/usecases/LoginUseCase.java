package com.iti.crg.client.domain.usecases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iti.crg.client.domain.repository.AuthRepository;
import com.iti.crg.client.infrastructure.remote.ServerConnection;

import java.io.BufferedReader;
import java.io.PrintStream;


public class LoginUseCase {
    private final AuthRepository repository;

    public LoginUseCase(AuthRepository repository) {
        this.repository = repository;
    }

    public LoginResult execute(String username, String password) {
        return repository.login(username, password);
    }


}