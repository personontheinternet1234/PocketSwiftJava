package com.personontheinternet.pocketjava.packets.app;

import com.personontheinternet.pocketjava.network.Connection;
import com.personontheinternet.pocketjava.network.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SwiftPacket implements Packet {

    private String name;

    public SwiftPacket() {}

    public SwiftPacket(String name) {
        this.name = name;
    }

    @Override
    public void fromBytes(DataInputStream stream) throws IOException {
        this.name = stream.readLine();
    }

    @Override
    public void toBytes(DataOutputStream stream) throws IOException {
        System.out.println("This shouldn't be happening.");
    }

    @Override
    public void handle(Connection connection) {
        System.out.println("SWIFT RECIEVED: " + name);
    }
}