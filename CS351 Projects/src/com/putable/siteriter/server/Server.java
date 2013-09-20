package com.putable.siteriter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ConnectionFactory connectionFactory;
	private int port;
	private boolean running;
	private static final Auditor defaultAuditor = new Auditor() {
		@Override
		public void hit(String ip, String path) { 
            System.out.println("Hit: "+ip+" "+path);
		}
		@Override
		public void msg(String text) { 
            System.err.println("Message: " +text);
		}
	};
	private Auditor auditor = defaultAuditor;

	public void setAuditor(Auditor a) {
		if (a == null)
			auditor = defaultAuditor;
		else
			auditor = a;
	}
	public Auditor getAuditor() {
		return auditor;
	}

	public Server(ConnectionFactory cf) {
		this(cf, 80);
	}

	public Server(ConnectionFactory cf, int port) {
		this.port = port;
		this.connectionFactory = cf;
	}

	public void stop() {
		running = false;
	}

	public void run() throws ServerException {
		ServerSocket theSocket = null;
		try {
			theSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new ServerException("Can't start on " + port);
		}
		auditor.msg("Listening on port " + port);
		running = true;
		try {
			ThreadGroup tg = new ThreadGroup("Server threads");
			while (running) {
				Socket socket = theSocket.accept();
				AbstractConnection cxn = connectionFactory.make(this,socket);
				Thread t = new Thread(tg, cxn);
				t.start();
			}
		} catch (IOException e) {
			throw new ServerException("IO error on connection: " + e);
		}
	}
}
