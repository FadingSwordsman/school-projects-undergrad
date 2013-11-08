package com.fadingswordsman.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import com.fadingswordsman.network.interfaces.Connectable;

public class ChatClient implements Connectable
{
    private static ChatClient instance;
    private static String url = "localhost";
    private static int port = 31337;
    
    private Socket sock;
    
    private ChatClient()
    {
    }
    
    public static ChatClient getClient()
    {
	if(instance == null)
	    instance = new ChatClient();
	return instance;
    }
    
    @Override
    public boolean receivePacket() throws IOException
    {
	return false;
    }

    @Override
    public void sendPacket() throws IOException
    {
    }

    @Override
    public Socket connect(String hostName, int port) throws IOException
    {
	sock = new Socket(hostName, port);
	return sock;
    }
    
    public static void main(String[] args)
    {
	ChatClient client = getClient();
	try
	{
	    client.connect(url, port);
	    System.setIn(client.sock.getInputStream());
	    System.setOut((PrintStream) client.sock.getOutputStream());
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	    return;
	}
    }
}
