package com.personontheinternet.pocketjava.packets.testing;

import com.personontheinternet.pocketjava.network.Connection;
import com.personontheinternet.pocketjava.network.PacketHandler;
import com.personontheinternet.pocketjava.packets.app.SwiftPacket;
import com.personontheinternet.pocketjava.packets.stats.PlayerCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class TestClient {

    public static void main(String args[]) throws IOException{

        int packetID = 1;
        PacketHandler.registerPacket(TestPacket.class, 1);
        PacketHandler.registerPacket(PlayerCount.class, 2);
        PacketHandler.registerPacket(SwiftPacket.class, 3);


        InetAddress address=InetAddress.getLocalHost();
        Socket s1 = null;
        String line = null;
        BufferedReader br = null;
        BufferedReader is = null;
        Connection connection = null;

        try {
            s1 = new Socket(address, 25501); // You can use static final constant PORT_NUM
            connection = new Connection("Client", s1);
            connection.start();

            br = new BufferedReader(new InputStreamReader(System.in));
            is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : " + address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        try{
            line=br.readLine();
            while(line.compareTo("QUIT") != 0){
                System.out.println("sending");
                connection.sendPacket(new TestPacket(line));

                line=br.readLine();
            }



        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{

            is.close();br.close();s1.close();
            System.out.println("Connection Closed");

        }

    }
}