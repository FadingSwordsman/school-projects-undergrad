package com.putable.siteriter;

import java.io.IOException;

/**
 * An exception signaling an SDL parsing failure.
 */
public class SDLParseException extends IOException {
    private static final long serialVersionUID = 1L;

    public SDLParseException(String message) {
        super(message);
    }

}