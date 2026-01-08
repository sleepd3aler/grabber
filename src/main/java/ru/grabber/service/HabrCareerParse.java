package ru.grabber.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final HabrDateTimeParser dateTimeParser = new HabrDateTimeParser();

    @Override
    public List<Post> fetch() {
        List<Post> result = new ArrayList<Post>();
        for (int pageNumber = 1; pageNumber <= TOTAL_PAGES; pageNumber++) {
            Objects.requireNonNull(parseLink(pageNumber)).forEach(row -> {
                Post post = parsePost(row);
                result.add(post);
            });
        }
        return result;
    }

    private Elements parseLink(int pageNumber) {
        try {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            return document.select(".vacancy-card__inner");
        } catch (IOException e) {
            log.error("When load page", e);
        }
        return null;
    }

    private Post parsePost(Element element) {
        Element tittle = element.select(".vacancy-card__title").first();
        Element linkElement = tittle.child(0);
        String vacancyName = tittle.text();
        String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        Element date = element.select(".vacancy-card__date").first();
        Element dateTime = date.child(0);
        LocalDateTime created = dateTimeParser.parse(dateTime.attr("datetime"));
        System.out.printf("%s %s %s %n", vacancyName, link, created.toString());
        return new Post(vacancyName, link, created);
    }

    public static void main(String[] args) {
        HabrCareerParse parse = new HabrCareerParse();
        parse.fetch();
    }
}
