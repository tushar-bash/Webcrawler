import java.util.*;

public class WebCrawler {
    private static final int MAX_PAGES_TO_SEARCH = 300;
    private Set<String> pagesVisited = new HashSet<>();
    private List<String> pagesToVisit = new LinkedList<>();

    public WebCrawler(List<String> urlList) {
        this.pagesToVisit.addAll(urlList);
    }

    public boolean search(String searchWord)
    {
        boolean found = false;
        while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
        {
            HttpParser parser = new HttpParser();
            String currentUrl = this.nextUrl();
            if (parser.parse(currentUrl)) {
                if (parser.searchWord(searchWord)) {
                    System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                    found= true;
                }
                this.pagesToVisit.addAll(parser.getLinks());
            }
        }
        System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
        return found;
    }


    private String nextUrl()
    {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);

        return nextUrl;
    }

}
