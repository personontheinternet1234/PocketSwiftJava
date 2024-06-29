package com.personontheinternet.pocketjava.packets.stats;

import com.personontheinternet.pocketjava.Server;
import com.personontheinternet.pocketjava.network.Connection;
import com.personontheinternet.pocketjava.network.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerCount implements Packet {

    public int playerCount;

    public PlayerCount(){}

    public PlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public void fromBytes(DataInputStream stream) throws IOException {
        this.playerCount = stream.readInt();
    }

    @Override
    public void toBytes(DataOutputStream stream) throws IOException {
        stream.writeInt(playerCount);
    }

    @Override
    public void handle(Connection connection) {
        connection.sendPacket(new PlayerCount(Server.playerCount));
    }
}