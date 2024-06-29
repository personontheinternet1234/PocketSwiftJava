package com.personontheinternet.pocket.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Objects being sent and received must implement this class.
 *
 * @author iKeirNez
 */
public interface Packet {
    void fromBytes(DataInputStream stream) throws IOException;
    void toBytes(DataOutputStream stream) throws IOException;
    void handle(Connection connection);
}
