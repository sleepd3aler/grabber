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
import ru.grabber.utils.DateTimeParser;
import ru.grabber.utils.HabrDateTimeParser;

public class HabrCareerParse implements Parse {
    private static final Logger log = Logger.getLogger(HabrCareerParse.class);
    private  String sourceLink;
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int TOTAL_PAGES = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> fetch(String link) {
        sourceLink = link;
        List<Post> result = new ArrayList<>();
        for (int pageNumber = 1; pageNumber <= TOTAL_PAGES; pageNumber++) {
            Objects.requireNonNull(parseLink(pageNumber)).forEach(row -> {
                Post post = parsePost(row);
                result.add(post);
            });
        }
        return result;
    }

    private Post parsePost(Element element) {
        Element tittle = element.select(".vacancy-card__title").first();
        Element linkElement = tittle.child(0);
        String vacancyName = tittle.text();
        String link = String.format("%s%s", sourceLink, linkElement.attr("href"));
        Element date = element.select(".vacancy-card__date").first();
        Element dateTime = date.child(0);
        String description = parseDescription(link);
        LocalDateTime created = dateTimeParser.parse(dateTime.attr("datetime"));
        System.out.printf("%s %s %s %n", link, created.toString(), description);

        return new Post(vacancyName, link, created);
    }

    private Elements parseLink(int pageNumber) {
        String fullLink = "%s%s%d%s".formatted(sourceLink, PREFIX, pageNumber, SUFFIX);
        Document document = loadDocument(fullLink);
        return document.select(".vacancy-card__inner");
    }

    private String parseDescription(String link) {
        Document document = loadDocument(link);
        Element element = document.select(".vacancy-description__text").first();
        return element.text();

    }

    private Document loadDocument(String link) {
        try {
            Connection connection = Jsoup.connect(link);
            return connection.get();
        } catch (IOException e) {
            log.error("When load page", e);
        }
        return null;
    }

    public static void main(String[] args) {
        HabrCareerParse parse = new HabrCareerParse(new HabrDateTimeParser());
        parse.fetch("https://career.habr.com");
    }
}
