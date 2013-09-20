package com.putable.siteriter;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * A SiteRiter SDL parser. Provides the ability to {@link #load(Reader)} an SDL
 * program from an arbitrary Reader, and then to {@link #makePage(String, Map)}
 * based on the loaded SDL program.
 */
public interface SDLParser
{
    /**
     * Load an SDL program from a {@link Reader}. Stores defined rules in some
     * form allowing {@link #makePage(String, Map)} to work subsequent to
     * calling this method. This method may be called multiple times, in which
     * case only the <i>last</i> SDL program loaded has any effect on the
     * SDLParser's behavior.
     * 
     * @param reader
     *            An arbitrary non-null Reader. Note this reader may not be
     *            buffered, and may not support {@link Reader#mark(int).}
     * 
     * @throws SDLParseException
     *             if the program read is syntactically incorrect in any way. If
     *             {@link SDLParseException} is thrown, the internal state of
     *             the {@link SDLParser}, and the behavior of
     *             {@link #makePage(String, Map)}, is undefined, unless and
     *             until a subsequent call to {@link #load(Reader)} finishes
     *             successfully.
     * @throws NullPointerException
     *             if reader is null
     */
    void load(Reader reader) throws IOException;

    /**
     * Generate a page based on a previously-loaded SDL program.
     * 
     * @param key
     *            A string value (typically an URL) used to determine which
     *            possible page is generated.
     * @param selectors
     *            A map from Strings to Integers that provides selector values
     *            for all, some, or none of the selector values referenced in
     *            the most recently {@link #load(Reader)} SDL program.
     * @return The entire generated page as a single String.
     * @throws IllegalStateException
     *             if {@link #load(Reader)} has not yet successfully completed
     *             on this {@link SDLParser}
     * @throws NullPointerException
     *             if either argument is null.
     * 
     */
    String makePage(String key, Map<String, Integer> selectors);
}
