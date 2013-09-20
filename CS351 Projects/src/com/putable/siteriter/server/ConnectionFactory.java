package com.putable.siteriter.server;

import java.net.Socket;

/**
 * An interface providing the ability to create AbstractConnections. A
 * {@link Server#Server(ConnectionFactory)} is passed one, which it uses to
 * produce connections in response to web hits.
 * 
 * @author ackley
 * 
 */
public interface ConnectionFactory {
    /**
     * Build a (subclass of) AbstractConnection
     * @param server
     * @param s
     * @return the constructed instance
     */
    public AbstractConnection make(Server server, Socket s);
}
