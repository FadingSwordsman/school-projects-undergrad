package com.putable.siteriter.server;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.putable.siteriter.SDLParser;
import com.putable.siteriter.locd011.SDLParserImpl;

/**
 * An example web server based on the com.putable.siteriter.server classes,
 * available as a example, model, and starting point for modification by UNM
 * CS351 S'10 students to build their own server code.
 * 
 * @author ackley
 * 
 */
public class SDLServer {
    /**
     * The instance of an SDLParser we will use to generate pages.
     */
    private SDLParser parser;

    /**
     * The actual web server object that will listen on the network and receive
     * connection requests.
     */
    private Server server;

    /**
     * Build an ExampleServer.
     * 
     * @param portNumber
     *            What port number to listen on. Should be greater than 1000 and
     *            must not be in use by any other program (including any other
     *            instance of an ExampleServer).
     */
    public SDLServer(int portNumber) {
        parser = new SDLParserImpl();
        if(portNumber <= 1000)
            throw new IllegalArgumentException("Invalid port specified! Use a port number greater than 1000!");
        server = new Server(new SDLConnectionFactory(parser), portNumber);
    }

    /**
     * Load the given grammar and then start serving pages based on it.
     * 
     * @param reader
     *            The source of the SDL Rules File for this SiteRiter server
     * @throws IOException
     *             If anything goes wrong.
     */
    private void loadAndRun(Reader reader) throws IOException {
        parser.load(reader);
        server.run();
    }

    /**
     * A minimal test driver for the ExampleServer. Note that because the
     * {@link ExampleSDLParserImpl#load(Reader)} method doesn't actually read
     * from its input, the contents of the reader provided here doesn't actually
     * matter. In a real implementation things would be different.
     * 
     * @param args
     *            Not used in this demo
     * @throws IOException
     *             If basically anything blows up during processing.
     */
    public static void main(String[] args) throws IOException {
	//Print 'helpful' information and exit if there isn't any argument
	if(args.length == 0)
	{
	    throw new IllegalArgumentException("Expected at least one argument.\nUsage:\n" +
	    		"\tjar SDLServer.java <filename> [port]");
	}
	Reader r = new FileReader(args[0]);
	int port = 8000;
	if(args.length > 1)
	{
	    try
	    {
		port = Integer.parseInt(args[1]);
	    }
	    catch(NumberFormatException e)
	    {
		throw new IllegalArgumentException("Illegal port value specified! Please specify a plain number greater than 1000.");
	    }
	}
        SDLServer es = new SDLServer(port);
        es.loadAndRun(r);
    }
}
