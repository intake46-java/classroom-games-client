package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.domain.repository.InvitationRepository;
import com.iti.crg.client.infrastructure.repository.InvitationRepositoryImp;

public class SendInvitationUseCase {
    private final InvitationRepository repository;

    public SendInvitationUseCase() {
        this.repository = new InvitationRepositoryImp();
    }

    public boolean execute(String targetUser) {
        if (targetUser == null || targetUser.isEmpty()) return false;
        System.out.println(targetUser);
        return repository.sendInvite(targetUser);
    }
}