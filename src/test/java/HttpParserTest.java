import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tushar on 10/10/20.
 */
public class HttpParserTest {

    @Test
    public void testHttpParser() {
        HttpParser parser = new HttpParser();
        assertEquals(true, parser.parse("https://www.espncricinfo.com"));

        assertTrue(parser.getLinks().size() > 0);

        assertEquals(true, parser.searchWord("kohli"));
    }
}
