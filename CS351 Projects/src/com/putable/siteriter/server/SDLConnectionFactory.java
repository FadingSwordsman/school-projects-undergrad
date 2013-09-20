package com.putable.siteriter.server;

import java.net.Socket;

import com.putable.siteriter.SDLParser;

/**
 * This implementation of a {@link ConnectionFactory} builds
 * {@link SDLServerConnection}'s and passes them the {@link SDLParser} reference
 * the factory got when it was built.
 * 
 * @author ackley
 * 
 */
public class SDLConnectionFactory implements ConnectionFactory {
    private SDLParser parser;

    public SDLConnectionFactory(SDLParser p) {
        parser = p;
    }

    @Override
    public AbstractConnection make(Server server, Socket socket) {
        return new SDLServerConnection(server, socket, parser);
    }

}
