package com.fadingswordsman.network;

import java.io.IOException;

public interface Connection extends Runnable
{
	public void sendToConnection(String toSend);
	public String getNextFromConnection();
	public void closeConnection(boolean error) throws IOException;
}
