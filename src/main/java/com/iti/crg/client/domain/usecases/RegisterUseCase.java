package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.domain.repository.AuthRepository;
import com.iti.crg.client.infrastructure.repository.AuthRepositoryImp;

public class RegisterUseCase {
    private final AuthRepository repository;

    public RegisterUseCase() {
        this.repository = new AuthRepositoryImp();
    }

    public LoginResult execute(String username, String password) {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()){
            return new LoginResult(false, null, null, null);
        }

        return repository.register(username, password, 0);
    }
}