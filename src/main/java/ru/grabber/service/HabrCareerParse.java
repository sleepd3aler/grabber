package ru.grabber.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.grabber.model.Post;
import ru.grabber.utils.HabrDateTimeParser;

public class HabrCareerParse implements Parse {
    private static final Logger log = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int TOTAL_PAGES = 5;

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        var dateTimeParser = new HabrDateTimeParser();
        try {
            for (int pageNumber = 1; pageNumber <= TOTAL_PAGES; pageNumber++) {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
                Connection connection = Jsoup.connect(fullLink);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element title = row.select(".vacancy-card__title").first();
                    Element linkElement = title.child(0);
                    String vacancyName = title.text();
                    String link = String.format("%s%s", SOURCE_LINK,
                            linkElement.attr("href"));
                    Element date = row.select(".vacancy-card__date").first();
                    Element dateTime = date.child(0);
                    LocalDateTime created = dateTimeParser.parse(dateTime.attr("datetime"));
                    System.out.printf("%s %s %s %n", vacancyName, link, created.toString());
                    Post post = new Post(vacancyName, link, null, created);
                    result.add(post);
                });
            }
        } catch (IOException e) {
            log.error("When load page", e);
        }
        return result;
    }

    public static void main(String[] args) {
        HabrCareerParse parse = new HabrCareerParse();
        parse.fetch();
    }
}
