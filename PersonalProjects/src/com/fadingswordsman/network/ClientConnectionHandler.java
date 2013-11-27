package com.fadingswordsman.network;

import java.util.List;

public class ClientConnectionHandler {
	private Connection currentConnection;
	private List<ConnectionReader> registeredConnectionReaders;
	
	public ClientConnectionHandler(Connection onConnection)
	{
		this.currentConnection = onConnection;
	}
	
	public void sendToConnection(String input)
	{
		currentConnection.sendToConnection(input);
	}
	
	public void readFromConnection()
	{
		String input = currentConnection.getNextFromConnection();
		for(ConnectionReader reader : registeredConnectionReaders)
			reader.send(input);
	}
	
	public void registerConnectionReader(ConnectionReader reader)
	{
		registeredConnectionReaders.add(reader);
	}
}
