package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.domain.repository.InvitationRepository;
import com.iti.crg.client.infrastructure.repository.InvitationRepositoryImp;

public class RespondToInviteUseCase {
    private final InvitationRepository repository;

    public RespondToInviteUseCase() {
        this.repository = new InvitationRepositoryImp();
    }

    public void accept(String senderName) {
        repository.acceptInvite(senderName);
    }

    public void reject(String senderName) {
        repository.rejectInvite(senderName);
    }
}