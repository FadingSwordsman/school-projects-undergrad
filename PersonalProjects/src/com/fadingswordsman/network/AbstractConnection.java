package com.fadingswordsman.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class AbstractConnection implements Connection
{
	private Socket connectionSocket;
	
	private Reader socketIn;
	private Writer socketOut;
	
	private AtomicBoolean isOpen = new AtomicBoolean(true);
	private BlockingQueue<String> toSend = new LinkedBlockingQueue<String>();
	private BlockingQueue<String> toReceive = new LinkedBlockingQueue<String>();
	
	public AbstractConnection(Socket connectionSocket) throws IOException
	{
		this.connectionSocket = connectionSocket;
		this.socketIn = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		this.socketOut = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
	}
	
	@Override
	public void run()
	{
		new Thread(new Listener()).start();
		while(isOpen.get())
		{
			boolean isSent = false;
			while(!isSent)
			try
			{
				String message = toSend.take();
				socketOut.write(message);
				socketOut.flush();
				isSent = true;
			}
			catch(InterruptedException e)
			{}
			catch(IOException e)
			{
				System.err.println("Error while writing to connection");
				System.err.println(e.getLocalizedMessage());
				isSent = true;
				isOpen.set(false);
			}
		}
	}

	@Override
	public void sendToConnection(String toSend)
	{
		boolean sent = false;
		
		while (!sent)
			try
			{
				this.toSend.put(toSend);
				sent = true;
			}
			catch (InterruptedException e)
			{
			}
	}

	@Override
	public String getNextFromConnection()
	{
		String value = null;
		boolean sent = false;
		
		while(!sent)
			try
			{
				value = toReceive.take();
				sent = true;
			}
			catch(InterruptedException e)
			{
				
			}
		
		
		return value;
	}

	@Override
	public void closeConnection(boolean error) throws IOException
	{
		socketIn.close();
		socketOut.close();
		connectionSocket.close();
	}
	
	private class Listener implements Runnable
	{

		@Override
		public void run()
		{
			while(isOpen.get())
			{
				boolean hasOutput = false;
				
				String output = null;
				while(!hasOutput)
				{
					try
					{
						output = toSend.take();
						hasOutput = true;
					}
					catch(InterruptedException e)
					{
						
					}
				}
				
				try
				{
					socketOut.append(output);
					socketOut.flush();
				}
				catch(IOException e)
				{
					System.err.println();
				}
			}
		}
	}
}
