package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.domain.repository.AuthRepository;




public class LoginUseCase {
    private final AuthRepository repository;

    public LoginUseCase(AuthRepository repository) {

        this.repository = repository;
    }

    public LoginResult execute(String username, String password) {
        return repository.login(username, password);
    }


}