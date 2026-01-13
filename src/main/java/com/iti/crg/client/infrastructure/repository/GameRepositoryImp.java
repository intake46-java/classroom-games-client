package com.iti.crg.client.infrastructure.repository;

import com.google.gson.Gson;
import com.iti.crg.client.domain.repository.GameRepository;
import com.iti.crg.client.infrastructure.dto.GameMoveDto;
import com.iti.crg.client.infrastructure.dto.InviteDto;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;
import java.io.PrintStream;

public class GameRepositoryImp implements GameRepository {
    private final ServerConnection connection;
    private final Gson gson;

    public GameRepositoryImp() {
        this.connection = ServerConnection.getInstance();
        this.gson = new Gson();
    }

    @Override
    public boolean sendMove(GameMoveDto moveDto) {
        return sendRequest("SEND_MOVE", gson.toJson(moveDto));
    }

    @Override
    public void sendWin(String opponent) {
        InviteDto data = new InviteDto(opponent);
        sendRequest("GAME_OVER_WIN", gson.toJson(data));
    }

    @Override
    public void sendTie(String opponent) {
        InviteDto data = new InviteDto(opponent);
        sendRequest("GAME_OVER_TIE", gson.toJson(data));
    }

    private boolean sendRequest(String type, String payload) {
        if (!connection.isConnected()) return false;
        try {
            PrintStream ps = connection.getWriter();
            Request request = new Request(type, payload);
            ps.println(gson.toJson(request));
            ps.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}