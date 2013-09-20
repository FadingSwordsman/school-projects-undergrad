package com.putable.siteriter.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AbstractConnection defines the common characteristics of all connection
 * handlers in a SiteRiter implementation.
 * 
 * AbstractConnection handles almost all of connection processing. Essentially
 * all that concrete classes have to do is implement the
 * {@link #respond(String)} method, which outputs a web page appropriate to the
 * String that it is given.
 * 
 * @author ackley
 * 
 */
public abstract class AbstractConnection implements Runnable {
    private static final Pattern requestPattern = Pattern
            .compile("^GET (.+) HTTP/1.[01]$");

    private static SimpleDateFormat makeRFC1123Format() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf;
    }

    public String formatAsRFC1123(long when) {
        return makeRFC1123Format().format(new Date(when));
    }

    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private Writer writer;

    public AbstractConnection(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Output the response to a web server request. The output must be written
     * to the Writer returned by {@link #getWriter()}, and the output must
     * include everything -- the http header, and the web page -- needed to
     * provide the response.
     * 
     * @param request
     *            The URL of the request, with the leading 'http://' and the
     *            hostname and the optional port number all removed. So, an URL
     *            like 'http://foo.com:1234/search?q=zot' will generate a
     *            request argument of '/search?q=zot'.
     * @throws IOException
     *             if an I/O error occurs while generating or writing the
     *             response.
     */
    abstract public void respond(String request) throws IOException;

    @Override
    public void run() {
        try {
            handle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle() throws IOException {
        String ip = "unknown";
        String request = "unknown";

        ip = socket.getInetAddress().getHostAddress();
        reader = new BufferedReader(new InputStreamReader(socket
                .getInputStream()));
        writer = new OutputStreamWriter(socket.getOutputStream());

        request = reader.readLine();
        if (request == null) {
            return;
        }

        Matcher m = requestPattern.matcher(request);
        if (!m.matches()) {
            return;
        }
        String path = m.group(1);

        Map<String, String> headers = new HashMap<String, String>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("")) {
                break;
            }
            int colonPos = line.indexOf(":");
            if (colonPos > 0) {
                String key = line.substring(0, colonPos);
                String value = line.substring(colonPos + 1);
                headers.put(key, value.trim());
            }
        }
        server.getAuditor().hit(ip, path);
        respond(path);
    }

    /**
     * @param theWriter
     *            the theWriter to set
     */
    public void setWriter(BufferedWriter theWriter) {
        this.writer = theWriter;
    }

    /**
     * @return the theWriter
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * @param theReader
     *            the theReader to set
     */
    public void setReader(BufferedReader theReader) {
        this.reader = theReader;
    }

    /**
     * @return the theReader
     */
    public BufferedReader getReader() {
        return reader;
    }
}
