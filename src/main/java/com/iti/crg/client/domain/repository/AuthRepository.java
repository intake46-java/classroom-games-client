package com.iti.crg.client.domain.repository;

import com.iti.crg.client.domain.usecases.LoginResult;

public interface AuthRepository {
    LoginResult login(String username, String password);
    LoginResult register(String username, String password, int score);
}
