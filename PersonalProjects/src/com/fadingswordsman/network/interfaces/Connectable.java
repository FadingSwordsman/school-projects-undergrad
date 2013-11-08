package com.fadingswordsman.network.interfaces;

import java.io.IOException;
import java.net.Socket;

public interface Connectable
{
    public boolean receivePacket() throws IOException;
    public void sendPacket() throws IOException;
    public Socket connect(String hostName, int port) throws IOException;
}
