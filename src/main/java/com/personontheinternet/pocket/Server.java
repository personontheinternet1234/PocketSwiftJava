package com.personontheinternet.pocket;

import com.personontheinternet.pocket.network.Connection;
import com.personontheinternet.pocket.network.PacketHandler;
import com.personontheinternet.pocket.packets.app.SwiftPacket;
import com.personontheinternet.pocket.packets.stats.PlayerCount;
import com.personontheinternet.pocket.packets.testing.TestPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static int playerCount = 0;

    public static String url = "localhost";
    public static String superUser = "root";
    public static String password = "password";


    public static void main(String args[]){

        int packetID = 1;
        PacketHandler.registerPacket(TestPacket.class, 1);
        PacketHandler.registerPacket(PlayerCount.class, 2);
        PacketHandler.registerPacket(SwiftPacket.class, 3);

        ServerSocket serverSocket = null;
        System.out.println("Server Listening......");
        try{
            serverSocket = new ServerSocket(25501);
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error.");
        }

//        // SQL setup
//        new SQLConnectionManager();
//        SQLConnectionManager.INSTANCE.connect();
//
//        if(SQLConnectionManager.INSTANCE.connection.isClosed()){
//            System.out.println("Failed to connect to DB.");
//            System.exit(-1);
//        }

        while(true){
            try{
                Socket socket = serverSocket.accept();
                //System.out.println("Connection Established: " + socket.getInetAddress() + " " + socket.getPort());
                Connection connection = new Connection("Server", socket);
                connection.start();
            }

            catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }


    }
}

