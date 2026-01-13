package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.controllers.AuthController;
import com.iti.crg.client.domain.repository.AuthRepository;
import com.iti.crg.client.infrastructure.repository.AuthRepositoryImp;

public class GetPlayerScore {
    private AuthRepository repository;
    public GetPlayerScore() {
        repository = new AuthRepositoryImp();
    }
//    public int GetPlayerScore() {
//        return repository.g
//    }
}
