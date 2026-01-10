package com.iti.crg.client.infrastructure.repository;

import com.google.gson.Gson;
import com.iti.crg.client.domain.repository.InvitationRepository;
import com.iti.crg.client.infrastructure.dto.InviteDto;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;

import java.io.PrintStream;

public class InvitationRepositoryImp implements InvitationRepository {

    private final ServerConnection connection;
    private final Gson gson;

    public InvitationRepositoryImp() {
        this.connection = ServerConnection.getInstance();
        this.gson = new Gson();
    }

    @Override
    public boolean sendInvite(String targetUsername) {
        return sendRequest("SEND_INVITE", targetUsername);
    }

    @Override
    public void acceptInvite(String originalSender) {
        sendRequest("INVITE_ACCEPT", originalSender);
    }

    @Override
    public void rejectInvite(String originalSender) {
        sendRequest("INVITE_REJECT", originalSender);
    }

    // Helper method to wrap data in JSON and send it
    private boolean sendRequest(String type, String username) {

        if (!connection.isConnected()) return false;
        System.out.println("Sending request to " + username);

        try {
            PrintStream ps = connection.getWriter();

            InviteDto payloadDto = new InviteDto(username);
            String payloadJson = gson.toJson(payloadDto);

            Request request = new Request(type, payloadJson);
            String fullJson = gson.toJson(request);

            ps.println(fullJson);
            ps.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}