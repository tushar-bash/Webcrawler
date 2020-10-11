import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Tushar on 10/8/20.
 */
public class HttpParser {

    final static Pattern urlPat = Pattern.compile("https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private List<String> links = new LinkedList<String>();
    private StringBuilder document;

    private BufferedReader Get(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");

        // pretend that we are a new-ish browser. current user agent is actually from 2015.
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        con.setInstanceFollowRedirects(true);

        int statusCode = con.getResponseCode();

        boolean redirect = false;
        if (statusCode != HttpURLConnection.HTTP_OK) {
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                    || statusCode == HttpURLConnection.HTTP_SEE_OTHER)
                redirect = true;
        }

        if (redirect) {
            // get redirect url from "location" header field
            String newUrl = con.getHeaderField("Location");
            // get the cookie if need
            String cookies = con.getHeaderField("Set-Cookie");

            return Get(new URL(newUrl));
        }

        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    public boolean parse(String strUrl) {
        URL url;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            System.out.println("bad url " + strUrl + ": " + e);
            return false;
        }

        BufferedReader reader;
        try {
            reader = Get(url);
        } catch (IOException e) {
            System.out.println("IOException Http.Get " + ": " + e);
            return false;
        }

        String lineBuf = "";
        LinkedList<String> urls = new LinkedList<>();
        document = new StringBuilder();
        do {
            try {
                lineBuf = reader.readLine();
            } catch (IOException e) {
                System.out.println("error parsing: " + e);
                this.links.addAll(urls);
                return false;
            }
            document.append(lineBuf);

            if (lineBuf == null) {
                this.links.addAll(urls);
                return true;
            }

            Matcher m = urlPat.matcher(lineBuf);
            while(m.find()) {
                urls.add(m.group(0));
            }
        } while(lineBuf != null);

        return true;
    }

    //get distinct list of urls
    public List<String> getLinks()
    {
        return this.links.stream().distinct().collect(Collectors.toList());
    }

    public boolean searchWord(String str) {
        if (document == null)
            return false;

        String body = document.toString();
        if (body.toLowerCase().contains(str.toLowerCase()))
            return true;

        return false;
    }
}
