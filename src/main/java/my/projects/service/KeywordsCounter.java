package my.projects.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordsCounter {

    public Map<String, Integer> countKeywordsOnPage(String url) throws Exception {
        if (url == null || url.trim().isEmpty())
            throw new RuntimeException("You did not provide any text as URL");

        // Get Document object after parsing the html from given url.
        Document document = Jsoup.connect(url).get();

        Map<String, Integer> result = new HashMap<>();

        // Get keywords from document object.
        Element keywordsElement = document.select("meta[name=keywords]").first();
        if (keywordsElement != null) {

            String keywordsString = keywordsElement.attr("content");
            if (keywordsString != null && !keywordsString.trim().isEmpty()) {

                String pageText = document.body().text();
                for (String keyword : keywordsString.split(",")) {
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        Pattern p = Pattern.compile(keyword.trim(), Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher(pageText);

                        int counter = 0;
                        while (m.find())
                            counter++;

                        result.put(keyword.trim(), counter);
                    }
                }
            }
        }

        return result;
    }
}
