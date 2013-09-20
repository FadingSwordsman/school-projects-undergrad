package com.putable.siteriter.server;

/**
 * An interface for flexibly recording the activities of a
 * {@link Server#Server(ConnectionFactory)}.
 * 
 * @author ackley
 * 
 */
public interface Auditor {
    /**
     * Called by the server to record an arbitrary text message. An Auditor
     * makes whatever use of this information it desires.
     * 
     * @param text
     *            The message that should be recorded in the audit files
     */
    void msg(String text);

    /**
     * Called once by the server to record each web hit. The Auditor makes
     * whatever use of this information it desires.
     * 
     * @param ip
     *            A String representation of the IP (Internet Protocol) address
     *            that originated the server request
     * @param path
     *            The suffix of the URL that was requested (after removing the
     *            protocol and host information).
     */
    void hit(String ip, String path);
}
