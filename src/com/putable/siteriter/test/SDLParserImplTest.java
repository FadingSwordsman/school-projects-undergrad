package com.putable.siteriter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.putable.siteriter.SDLParseException;
import com.putable.siteriter.SDLParser;
import com.putable.siteriter.locd011.SDLParserImpl;

/**
 * Dave's "pretty aggressive" tests of an SDLParserImpl. Includes input-output
 * functional tests only, meaning that it does <i>not</i> attempt to test the
 * required space and time efficiency requirements given in the spec.
 * 
 * <p>
 * <b>Usage notes:</b>
 * <ol>
 * <li>These are JUnit 4 tests! You'll need JUnit 4 in your build path!
 * <li>This is a <b>subset</b> of the tests that we will run on your
 * SDLParserImpl! Some tests have been held back!
 * <li>This code should be usable without modification if the class under test
 * is <code>com.putable.siteriter.test.SDLParserImpl</code>. If it isn't, see
 * the doc of {@link #buildParser()}.
 * <li>Except possibly for modifying {@link #buildParser()}, you shouldn't
 * modify these tests!
 * </ol>
 * 
 * <b>Behavior notes:</b>
 * <p>
 * All correct SDLParserImpl's will pass all tests, with two possible
 * exceptions:
 * <ol>
 * <li>Conceivably, some legal but inefficient implementations might run out of
 * space on some of the 'Big' tests. Haven't seen it happen, but it's possible
 * in principle.
 * <li>Conceivably, some legal randomization process might possibly die on
 * {@link #testMakePageRandomization()} due to sheer <i>extremely</i> bad luck.
 * Again, never have seen it happen, but in principle.
 * </ol>
 * 
 * @author ackley
 * @version 2.1
 * 
 */
public class SDLParserImplTest {
    /**
     * All tested SDLParser implementations are built by this method. Customize
     * this routine if the SDLParser implementation you want to test is not
     * called 'SDLParserImpl'.
     * 
     * @throws Exception
     */
    @Before
    public void buildParser() throws Exception {
        parser = new SDLParserImpl();
    }

    private SDLParser parser;

    private static final ArrayList<Character> start = new ArrayList<Character>();
    private static final ArrayList<Character> part = new ArrayList<Character>();
    private static final ArrayList<Character> white = new ArrayList<Character>();
    static {
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; ++i) {
            char ch = (char) i;
            if (Character.isJavaIdentifierStart(ch))
                start.add(ch);
            if (Character.isJavaIdentifierPart(ch))
                part.add(ch);
            if (Character.isWhitespace(ch))
                white.add(ch);
        }
    }

    /* A simple 'non-scary' random legal name of length len. */
    private static String makeLatinName(int len, Random r) {
        final String lc = "abcdefghijklmnopqrstuvwxyz";
        final String alphabet = lc + lc.toUpperCase();
        final String start = "$_" + alphabet;
        final String part = start + "0123456789";

        StringBuffer sb = new StringBuffer();
        sb.append(start.charAt(r.nextInt(start.length())));
        while (sb.length() < len)
            sb.append(part.charAt(r.nextInt(part.length())));
        return sb.toString();
    }

    /* A full-on 'letter of the law' random legal name of length len. */
    private static String makeName(int len, Random r) {
        StringBuffer sb = new StringBuffer();
        sb.append(start.get(r.nextInt(start.size())));
        while (sb.length() < len)
            sb.append(part.get(r.nextInt(part.size())));
        return sb.toString();
    }

    private static String makeSLiteral(int len, Random r) {
        return makeLiteral('\'', len, r);
    }

    private static String makeDLiteral(int len, Random r) {
        return makeLiteral('"', len, r);
    }

    private static String makeLiteral(char delim, int len, Random r) {
        StringBuffer sb = new StringBuffer();
        sb.append(delim);
        while (sb.length() < len + 1) {
            char ch;
            do {
                ch = (char) r.nextInt(Character.MAX_VALUE);
            } while (ch == delim || Character.isHighSurrogate(ch) // avoid
                    || Character.isLowSurrogate(ch)); // weirdo half-chars
            sb.append(ch);
        }
        sb.append(delim);
        return sb.toString();
    }

    private static String makeWS(int len, Random r) {
        StringBuffer sb = new StringBuffer();
        while (sb.length() < len)
            sb.append(white.get(r.nextInt(white.size())));
        return sb.toString();
    }

    /* A weak, custom, private Reader to fight instanceof's in code. */
    private class UnknownUnbufferedMarklessReader extends Reader {
        private StringReader source;

        public UnknownUnbufferedMarklessReader(String input) {
            source = new StringReader(input);
        }

        @Override
        public void close() throws IOException {
            source.close();
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            return source.read(cbuf, off, len);
        }

        @Override
        public boolean markSupported() {
            return false; // Reader does this anyway, but just to drive home the
                          // point..
        }
    }

    private Reader makeReader(String input) {
        return new UnknownUnbufferedMarklessReader(input);
    }

    private void testALegalGrammar(String g) throws IOException {
        try {
            parser.load(makeReader(g));
        } catch (SDLParseException e) {
            fail("On grammar \"" + g + "\" load threw " + e);
        }
    }

    @Test
    public final void testLoadBasic1() throws IOException {
        testALegalGrammar("a=;"); // shortest
        testALegalGrammar("\n \n \na=;"); // leading w/s no problem
        testALegalGrammar("foo='bar';"); // try an sliteral
        testALegalGrammar("foo=\"bar\";"); // and a dliteral
        testALegalGrammar("foo='a\"b';"); // and nest them
        testALegalGrammar("foo=\"a'b\";"); // both ways
        testALegalGrammar("foo=\"a\"'b';"); // and put them adjacent
        testALegalGrammar("foo='b'\"a\";"); // both ways
    }

    @Test
    public final void testLoadBasic2() throws IOException {
        testALegalGrammar("foo='weird=stuff|in;sliterals:OK';"); 
        testALegalGrammar("foo=\"weird=stuff|in;dliterals:OK\";"); 
    }

    @Test
    public final void testLoadBasic3() throws IOException {
        testALegalGrammar("foo='bar';\nfoo='gah' 'hop';\n"); // redef okay
        testALegalGrammar("foo:a='bar'\n;"); // selector okay
        testALegalGrammar(":foo='bar'\n;"); // secondary start okay
        testALegalGrammar(":foo:a='bar'\n;"); // secondary start and selector
                                              // okay
    }

    @Test
    public final void testLoadLegalNames() throws IOException {
        final String[] funnyNames = { "_", "$", "$1", "$$$", "_$_" };
        for (String f : funnyNames) {
            testALegalGrammar(f + "='f';");
            testALegalGrammar(":" + f + "='f';");
            testALegalGrammar(":" + f + ":" + f + "='f';");
            testALegalGrammar(f + ":" + f + "='f';");
        }
    }

    @Test
    public final void testLoadRandomLegalNames() throws IOException {
        Random r = new Random(2);
        for (int i = 0; i < 1000; ++i) {
            int len = 1 + i / 10;
            String n1 = makeName(len, r), n2 = makeName(len, r);
            String grammar = n1 + ":" + n2 + "='bar';";
            try {
                parser.load(makeReader(grammar));
            } catch (SDLParseException e) { // Catch to give better message
        	e.printStackTrace();
                fail("Legal names (" + i + "): Legal grammar: \"" + grammar
                        + "\": threw " + e);
            }
        }
    }

    @Test
    public final void testLoadLegalWhitespace() throws IOException {
        Random r = new Random(3);
        for (int i = 0; i < 100; ++i) {
            int len = 1 + i / 5;
            String n1 = makeName(len, r), n2 = makeName(len, r);
            String grammar = makeWS(len, r) + n1 + // Lots of ws..
                    makeWS(len, r) + ":" + // ws everywhere
                    makeWS(len, r) + n2 + // for
                    makeWS(len, r) + "=" + // everyb
                    makeWS(len, r) + "'bar'" + // o
                    makeWS(len, r) + ";" + // d
                    makeWS(len, r); // y
            try {
                parser.load(makeReader(grammar));
            } catch (SDLParseException e) { // Catch to give better message
                fail("Whitespace test (" + i + "): Legal grammar: \"" + grammar
                        + "\": threw " + e);
            }
        }
    }

    @Test
    public final void testLoadBigLiterals() throws IOException {
        Random r = new Random(17);
        for (int i = 0; i < 100; ++i) {
            String g = "foo = " + makeSLiteral(i * 20, r)
                    + makeDLiteral(i * 25, r) + ";";
            try {
                parser.load(makeReader(g));
            } catch (SDLParseException e) {
                fail("LoadBigLiterals (" + i + ") Threw on '" + g + "': " + e);
            }
        }
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie0() throws IOException {
        parser.load(makeReader("")); // no rules
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie1() throws IOException {
        parser.load(makeReader("foo")); // no =
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie2() throws IOException {
        parser.load(makeReader("=;")); // no name
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie3() throws IOException {
        parser.load(makeReader("foo=")); // no ;
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie4() throws IOException {
        parser.load(makeReader("foo=bar")); // no ;
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie5() throws IOException {
        // illegal token '=' in seq
        parser.load(makeReader("foo=bar\nbar='hop'"));
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie6() throws IOException {
        // Missing close '
        parser.load(makeReader("foo='hop;"));
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie7() throws IOException {
        // Missing close "
        parser.load(makeReader("foo=\"hop;"));
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie8() throws IOException {
        // Missing close "
        parser.load(makeReader("foo=bar hop \"gah' bop \"hop\";"));
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie9() throws IOException {
        // Selector without name
        parser.load(makeReader("foo:=;"));
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie10() throws IOException {
        // Two selectors
        parser.load(makeReader("foo:a:b=;"));
    }

    @Test(expected = SDLParseException.class)
    public final void testLoadDie11() throws IOException {
        // Two names before =
        parser.load(makeReader("foo a=;"));
    }

    private void shouldBe(String input, String output) throws IOException {
        shouldBe(input, output, null);
    }

    private void shouldBe(String input, String output, String key)
            throws IOException {
        Map<String, Integer> sels = new HashMap<String, Integer>();
        parser.load(makeReader(input));
        for (int i = 0; i < 100; i += 17) {
            sels.put("zero", 0);
            sels.put("one", 1);
            sels.put("two", 2);
            sels.put("nine", 9);
            sels.put("ten", 10);
            String k = key == null ? "" + i : key;
            String ret = parser.makePage(k, sels);
            assertEquals("For key: " + k + " in grammar: " + input, output, ret);
        }
    }

    @Test
    public final void testMakePage1() throws IOException {
        shouldBe("foo='bar';", "bar");
        shouldBe("\nfoo\n=\n\n'bar'\n;\n\n\n", "bar");
        shouldBe("foo=\"bar\";", "bar");
        shouldBe("foo='bar\"foo';", "bar\"foo");
        shouldBe("foo=\"bar'foo\";", "bar'foo");
        shouldBe("foo='bar' \"'\" 'foo' ;", "bar'foo");
        shouldBe("foo='bar'\"'\"'foo' ;", "bar'foo");
        shouldBe("foo=bar;\ngah='hop'\n;bar=gah;", "hop");
        shouldBe("foo=bar bar\t\nbar\n\t \n;\ngah='hop'\n;bar=gah;",
                "hophophop");
    }

    @Test
    public final void testMakePage2() throws IOException {
        shouldBe("a=b|b|b|b|b|b|b|b;b='pop';", "pop");
        shouldBe("a=b c|b c|b c;c='pod';b='pid';", "pidpod");
        shouldBe("a=a|b|c|a|a;b='zong';c='zong';", "zong");
    }

    @Test
    public final void testMakePage3() throws IOException {
        shouldBe("a=b;b='pop';c='pin';a=c;", "pin");
        shouldBe("a=a;b='a';a='pop';a='pin';a=a;a=b;", "a");
    }

    @Test
    public final void testMakePage4() throws IOException {
        shouldBe("a='b|c|d';b='hop';c='on';d='pop';", "b|c|d");
        shouldBe("a=\"b;c='d|e\";b='hop';c='on';d='pop';", "b;c='d|e");
    }

    @Test
    public final void testMakePageSelectors1() throws IOException {
        shouldBe("a:zero=b|c;b='pop';c='pin';", "pop");
        shouldBe("a:one=b|c;b='pop';c='pin';", "pin");
        shouldBe("a:two=b|c;b='pop';c='pin';", "pop");
        shouldBe("a:nine=b|c;b='pop';c='pin';", "pin");
        shouldBe("a:ten=b|c;b='pop';c='pin';", "pop");
        shouldBe("a:zero=b|c;b:zero=d|e;d:one=f|g;g='zot';", "zot");
    }

    public final void testMakePageUndefs() throws IOException {
        shouldBe("a=b;", "b?");
        shouldBe("a=b;b=c;c=d|d|d|d|d;", "d?");
    }

    @Test
    public final void testMakePageBigGrammar() throws IOException {
        StringBuffer sb = new StringBuffer();
        Set<String> used = new HashSet<String>();
        Random r = new Random(4);
        String last = "page";
        for (int i = 0; i < 1000; ++i) {
            used.add(last); // Mark last name used
            String n;
            do {
                n = makeLatinName(3, r);
            } while (used.contains(n));
            sb.append(" " + last + " = " + n + "|" + n + "|" + n + "|" + n
                    + "|" + n + "|" + n + ";");
            last = n;
        }
        sb.append(" " + last + " = 'zot';");
        shouldBe(sb.toString(), "zot");
    }

    @Test
    public final void testMakePageBigSelectorGrammar() throws IOException {
        StringBuffer sb = new StringBuffer("\n");
        Set<String> used = new HashSet<String>();
        Random r = new Random(11);
        String last = "page";
        final String[] sels = { "zero", "one", "two" };
        for (int i = 0; i < 1000; ++i) {
            used.add(last); // Mark last name used
            String n;
            do {
                n = makeLatinName(3, r);
            } while (used.contains(n));
            sb.append(" " + last + ":");
            int pick = r.nextInt(sels.length);
            sb.append(sels[pick] + " = "); // Stick on chosen selector
            for (int p = 0; p < 3; ++p) {
                if (p > 0)
                    sb.append("|");
                if (p == pick)
                    sb.append(n); // Hide the path forward
                else
                    sb.append("'bzzt'"); // All other choices are rejects
            }
            sb.append(";\n");
            last = n;
        }
        sb.append(" " + last + " = 'zoobid';");
        shouldBe(sb.toString(), "zoobid");
    }

    @Test
    public final void testMakePageRandomization() throws IOException {
        testALegalGrammar("p=q;q=|a|b;a='f';b='g';");
        Map<String, Integer> counts = new HashMap<String, Integer>();
        Map<String, Integer> selectors = new HashMap<String, Integer>();
        for (int i = 0; i < 1000; ++i) {
            String key = "k" + i;
            String res = parser.makePage(key, selectors);
            assertEquals(res, parser.makePage(key, selectors)); // Do the same
                                                                // thing twice?
            Integer c = counts.get(res);
            if (c == null)
                c = 0;
            counts.put(res, c + 1);
        }
        assertEquals(3, counts.size());
        assertTrue(counts.get("") > 300); // About a third of
        assertTrue(counts.get("f") > 300); // results land on
        assertTrue(counts.get("g") > 300); // each choice?
    }

    @Test
    public final void testIndependence() throws IOException {
        SDLParser parser2 = new SDLParserImpl();
        parser.load(makeReader("p=a;a=b;b=c;c='foo';"));
        parser2.load(makeReader("a=d;p='not';d=c;c='zot';"));
        Map<String, Integer> sel1 = new HashMap<String, Integer>();
        Map<String, Integer> sel2 = new HashMap<String, Integer>();
        assertEquals("foo", parser.makePage("1", sel1));
        assertEquals("zot", parser2.makePage("1", sel2));

        assertEquals("foo", parser.makePage("2", sel1));
        assertEquals("zot", parser2.makePage("2", sel2));

        parser.load(makeReader("p=a;a=b;b=c;c='goo';"));
        assertEquals("goo", parser.makePage("3", sel1));
        assertEquals("zot", parser2.makePage("3", sel2));

        parser2.load(makeReader("a=d;p='not';d=c;c='rot';"));
        assertEquals("goo", parser.makePage("4", sel1));
        assertEquals("rot", parser2.makePage("4", sel2));
    }
}
