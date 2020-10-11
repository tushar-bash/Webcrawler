import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tushar on 10/11/20.
 */
public class WebCrawlerTest {
    @Test
    public void testWebCrawler() {
        List<String> urls = Arrays.asList("https://www.espncricinfo.com",
                "https://www.google.com", "https://www.facebook.com");
        WebCrawler webCrawler = new WebCrawler(urls);
        assertEquals(true, webCrawler.search("sachin"));
    }
}
