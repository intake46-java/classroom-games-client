package com.iti.crg.client.domain.repository;

public interface InvitationRepository {
    boolean sendInvite(String targetUsername);
    void acceptInvite(String originalSender);
    void rejectInvite(String originalSender);
}