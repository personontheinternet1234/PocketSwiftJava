package com.personontheinternet.pocket.network;


import com.personontheinternet.pocket.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

public class Connection extends Thread {

    public enum ConnectionType{
        SERVER,
        CLIENT
    }

    // CLIENT AND SERVER
    public boolean isAuthenticated = false;

    // CLIENT ONLY
    public UUID playerUUID;

    // SERVER ONLY
    public String token;

    public ConnectionType type;

    // Connection parts
    public Queue<Packet> packetQueue = new ArrayDeque<>();
    public Socket s;
    String prefix;
    public DataInputStream is = null;
    public DataOutputStream os = null;
    int status;

    public Connection(String prefix, Socket s){
        this.s = s;
        this.prefix = prefix;
    }

    public void closeConnection(){
        if(isAuthenticated && type == ConnectionType.CLIENT){
            Server.playerCount--;
        }
        try {
            if (is != null){
                is.close();
            }

            if(os != null){
                os.close();
            }
            if(s != null){
                s.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPacket(Packet packet){
        try {
            os.writeInt(100);
            int packetID = PacketHandler.PACKET_CLASS_TO_ID.getOrDefault(packet.getClass(), 0);
            os.writeInt(packetID); // packetID
            packet.toBytes(os); // packet data
        } catch (IOException e) {
            closeConnection();
        }
    }

    public void read() {
        try {
            int packetID = is.readInt();
            Packet packet = PacketHandler.ID_TO_PACKET_CLASS.get(packetID).newInstance();
            packet.fromBytes(is);
            packet.handle(this);

        } catch (IOException | InstantiationException | IllegalAccessException e) {
            closeConnection();
        }
    }

    public void run() {
        try {
            is = new DataInputStream(s.getInputStream());
            os = new DataOutputStream(s.getOutputStream());

            while(!packetQueue.isEmpty()){
                sendPacket(packetQueue.poll());
            }

            do{
                s.getInputStream().read();
                s.getInputStream().read();
                s.getInputStream().read();
                status = s.getInputStream().read();
                if(status == -1){
                    break;
                }
                read();
            }
            while(status != -1);
        }
        catch (IOException | NullPointerException ignored) {
        }
        closeConnection();
    }
}