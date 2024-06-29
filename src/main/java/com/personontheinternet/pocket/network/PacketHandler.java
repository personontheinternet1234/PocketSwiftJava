package com.personontheinternet.pocket.network;

import java.util.HashMap;

/**
 * Created by iKeirNez on 18/04/2014.
 */
public class PacketHandler {

    public static HashMap<Integer, Class<? extends Packet>> ID_TO_PACKET_CLASS = new HashMap<>();
    public static HashMap<Class<? extends Packet>, Integer> PACKET_CLASS_TO_ID = new HashMap<>();

    public static void registerPacket(Class<? extends Packet> packetClass, int packetID){
       ID_TO_PACKET_CLASS.put(packetID, packetClass);
       PACKET_CLASS_TO_ID.put(packetClass, packetID);
    }
}
