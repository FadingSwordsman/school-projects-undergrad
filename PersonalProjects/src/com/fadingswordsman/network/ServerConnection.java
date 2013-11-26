package com.fadingswordsman.network;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends AbstractConnection
{

	public ServerConnection(Socket connectionSocket) throws IOException
	{
		super(connectionSocket);
	}

}
