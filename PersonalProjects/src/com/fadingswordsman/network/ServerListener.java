package com.fadingswordsman.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ServerListener implements Runnable
{

	private static ServerListener instance;
	
	private ServerSocket listener;
	private int connectionNumber = 0;
	
	private ServerListener(String scope, int port) throws IOException
	{
			listener = new ServerSocket(port, 50, InetAddress.getByName(scope));
	}
	
	@Override
	public void run() {
		while(true)
		{
			try
			{
				Connection newConnection = new ServerConnection(listener.accept());
				new Thread(newConnection, "Connection" + ++connectionNumber);
			}
			catch(IOException e)
			{
				System.err.println("Connection failed to open");
			}
		}
	}
	
	public static ServerListener getInstance() throws IOException
	{
		if(instance == null)
			instance = new ServerListener("*", 8035);
		return instance;
	}
}
