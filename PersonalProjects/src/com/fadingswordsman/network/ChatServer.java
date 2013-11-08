package com.fadingswordsman.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

public class ChatServer
{
    private static int port = 31337;
    private static ChatServer instance;
    private Collection<Socket> connections = new LinkedBlockingDeque<Socket>();
    private Collection<BufferedWriter> outputs = new LinkedBlockingDeque<BufferedWriter>();
    
    private ChatServer()
    {
	instance = this;
    }
    
    private void sendAll(String msg)
    {
	for(BufferedWriter output : outputs)
	{
	    try
	    {
		output.write(msg);
		output.flush();
	    }
	    catch(IOException e)
	    {
		e.printStackTrace();
	    }
	}
    }
    
    public static ChatServer getServer()
    {
	if(instance == null)
	    instance = new ChatServer();
	return instance;
    }
    
    private void listen()
    {
	ServerSocket blah;
	while(true)
	{
	    try
	    {
		blah = new ServerSocket(port);
		Socket sock = blah.accept();
		connections.add(sock);
		outputs.add(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
		Thread manageConnection = new Thread(new StreamListener(this, new InputStreamReader(sock.getInputStream())));
		manageConnection.start();
	    }
	    catch (IOException e)
	    {
		e.printStackTrace();
		return;
	    }
	}
    }
    
    private class StreamListener implements Runnable
    {
	private ChatServer server;
	private BufferedReader listener;
	
	public StreamListener(ChatServer server, InputStreamReader listener)
	{
	    this.server = server;
	    this.listener = new BufferedReader(listener);
	}
	
	public void run()
	{
	    while(true)
	    {
		try
		{
		    StringBuilder msg = new StringBuilder();
		    while (listener.ready())
		    {
			msg.append(listener.readLine()).append("\n");
		    }
		    server.sendAll(msg.toString());
		}
		catch(IOException e)
		{
		    e.printStackTrace();
		}
	    }
	}
    }
    
    public static void main(String[] args)
    {
	if(args.length > 0)
	    try
	    {
		port = Integer.parseInt(args[0]);
	    }
	    catch (NumberFormatException e)
	    {
		e.printStackTrace();
	    }
	ChatServer server = ChatServer.getServer();
	server.listen();
    }
}
