package com.personontheinternet.pocketjava.packets.testing;

import com.personontheinternet.pocketjava.network.Connection;
import com.personontheinternet.pocketjava.network.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TestPacket implements Packet {

    private String name;

    public TestPacket() {}

    public TestPacket(String name) {
        this.name = name;
    }

    @Override
    public void fromBytes(DataInputStream stream) throws IOException {
        this.name = stream.readUTF();
    }

    @Override
    public void toBytes(DataOutputStream stream) throws IOException {
        stream.writeUTF(name);
    }

    @Override
    public void handle(Connection connection) {
        System.out.println("RECIEVED: " + name);
    }
}