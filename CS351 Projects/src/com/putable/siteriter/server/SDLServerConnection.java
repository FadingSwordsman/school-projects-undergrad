package com.putable.siteriter.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.putable.siteriter.SDLParser;

/**
 * A sample concrete class extending {@link AbstractConnection}, which see for
 * an overview of its purpose.
 * 
 * @author ackley
 * 
 */
public class SDLServerConnection extends AbstractConnection {
    /**
     * A reference to the {@link SDLParser} to use for generating the response
     * to this connection.
     */
    private SDLParser parser;

    /**
     * Construct an ExampleConnection.
     * 
     * @param server The {@link Server} that accepted this connection
     * @param socket The {@link Socket} that (will) provide the input and output streams
     * @param parser
     */
    public SDLServerConnection(Server server, Socket socket, SDLParser parser) {
        super(server, socket);
        this.parser = parser;
    }

    /**
     * Milliseconds in one day.
     */
    private static final long MS_PER_DAY = 1000 * 60 * 60 * 24;

    private void writeResponse(String response, String type, long expires)
            throws IOException {
        Date now = new Date();
        if (expires <= 0)
            expires = now.getTime();
        else
            expires += now.getTime();
        byte[] bytes = response.getBytes();
        String header = "HTTP/1.0 200 OK\r\n" + "Date: " + formatAsRFC1123(now.getTime())
                + "\r\n" + "Server: Example Server/8.0\r\n" + "Content-Type: "
                + type + "\r\n" + "Expires: " + formatAsRFC1123(expires)
                + "\r\n" + "Content-Length: " + bytes.length + "\r\n"
                + "Last-modified: " + formatAsRFC1123(now.getTime()) + "\r\n" + "\r\n";
        getWriter().write(header);
        getWriter().write(response);
    }

    private static final String ROBOTS_TXT = "# Default robots.txt for the SiteRiter example server\n"
            + "User-agent: *\n"
            + "Disallow: /  # Don't say we didn't warn you..\n";

    @Override
    public void respond(String request) throws IOException {
        if (request.endsWith("/reload")) {
            // TODO: Extra credit component: Handle reload according to spec.
            writeResponse("Reloaded", "text/plain", 0);
            return;
        }
        if (request.equals("/robots.txt")) {
            // TODO: Extra credit component: Handle robots.txt according to spec.
            String r = null;
            if (r == null)
                r = ROBOTS_TXT;
            writeResponse(r, "text/plain", MS_PER_DAY);
            return;
        }

        // TODO: Extra credit component: Design and implement a way to load selectors based on the URL!
        Map<String,Integer> selectors = new HashMap<String,Integer>();
        String b = parser.makePage(request, selectors);
        writeResponse(b, "text/HTML", MS_PER_DAY);
    }
}
