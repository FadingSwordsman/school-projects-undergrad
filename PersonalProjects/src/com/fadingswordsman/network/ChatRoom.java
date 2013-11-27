package com.fadingswordsman.network;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatRoom implements Runnable, ConnectionRegistrar
{
	private Set<Connection> connections = Collections.newSetFromMap(new ConcurrentHashMap<Connection, Boolean>());
	private AtomicBoolean isRunning = new AtomicBoolean(true);
	private BlockingQueue<String> toProcess = new LinkedBlockingQueue<String>();
	private BlockingQueue<String> toSend = new LinkedBlockingQueue<String>();
	
	public void run()
	{
		new Thread(new Sender()).start();
		while(isRunning.get())
		{
			for(Connection connection : connections)
			{
				String incoming = connection.getNextFromConnection();
				if(incoming != null)
				{
					boolean processed = false;
					while(!processed)
					{
						try
						{
							toProcess.put(incoming);
							processed = true;
						}
						catch(InterruptedException e)
						{
						}
					}
				}
			}
		}
	}
	
	private class Sender implements Runnable
	{
		public void run()
		{
			while(isRunning.get())
			{
				try
				{
					String message = toSend.take();
					for(Connection connection : connections)
						connection.sendToConnection(message);
				}
				catch(InterruptedException e)
				{
				}
			}
		}
	}
	
	public String getNextInput()
	{
		return toProcess.peek();
	}
	
	public void registerConnection(Connection connection)
	{
		connections.add(connection);
	}
	public boolean unregisterConnection(Connection connection)
	{
		if(connections.contains(connection))
		{
			connections.remove(connection);
			return true;
		}
		return false;
	}
}
