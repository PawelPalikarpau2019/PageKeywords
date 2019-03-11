package my.projects.service;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KeywordsCounterTest {

    private static KeywordsCounter keywordsCounter;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        keywordsCounter = new KeywordsCounter();
    }

    @After
    public void tearDown() {
        keywordsCounter = null;
    }

    @Test
    public void test_EmptyUrl_Exception() throws Exception {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("You did not provide any text as URL");
        keywordsCounter.countKeywordsOnPage("");
    }

    @Test
    public void test_IncorrectUrl_Exception() throws Exception {ś
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Malformed URL: incorrect url");
        keywordsCounter.countKeywordsOnPage("incorrect url");
    }

    @Test
    public void test_CountKeywordsOnPage_Empty() throws Exception {
        String url = "https://pl.wikipedia.org/wiki/ASDF";
        Map<String, Integer> result = keywordsCounter.countKeywordsOnPage(url);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void test_CountKeywordsOnPage_Success001() throws Exception {
        String url = "https://codesjava.com/jsoup-get-images-from-html-example/";
        Map<String, Integer> result = keywordsCounter.countKeywordsOnPage(url);

        assertNotNull(result);
        assertEquals(13, result.size());

        String[] expectedKeywords =
                {
                        "jsoup img src",
                        "how to use get images in jsoup",
                        "get images jsoup example code",
                        "jsoup",
                        "jsoup get image url",
                        "java jsoup get images from html example",
                        "get images in jsoup example",
                        "jsoup get image",
                        "jsoup get images example",
                        "get images in jsoup",
                        "get images program in jsoup",
                        "jsoup get image src",
                        "jsoup download image"
                };
        Integer[] expectedAmounts = {0, 1, 1, 42, 0, 0, 1, 3, 1, 3, 1, 0, 0};

        int i = 0;
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            assertEquals(expectedKeywords[i], entry.getKey());
            assertEquals(expectedAmounts[i], entry.getValue());
            i++;
        }
    }

    @Test
    public void test_CountKeywordsOnPage_Success002() throws Exception {
        String url = "https://www.w3schools.com/tags/tag_meta.asp";
        Map<String, Integer> result = keywordsCounter.countKeywordsOnPage(url);

        assertNotNull(result);
        assertEquals(27, result.size());

        String[] expectedKeywords =
                {
                        "demos",
                        "references",
                        "primer",
                        "W3C",
                        "Web development",
                        "HTML",
                        "training",
                        "learning",
                        "colors",
                        "tips",
                        "exercises",
                        "JavaScript",
                        "PHP",
                        "Bootstrap",
                        "programming",
                        "Python",
                        "quiz",
                        "Java",
                        "CSS",
                        "DOM",
                        "source code",
                        "SQL",
                        "jQuery",
                        "examples",
                        "tutorials",
                        "XML",
                        "lessons",
                };
        Integer[] expectedAmounts = {0, 3, 0, 0, 1, 75, 1, 1, 3, 2, 10,
                16, 14, 13, 0, 9, 20, 20, 27, 4, 0, 8, 11, 15, 4, 24, 0};

        int i = 0;
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            assertEquals(expectedKeywords[i], entry.getKey());
            assertEquals(expectedAmounts[i++], entry.getValue());
        }
    }
}