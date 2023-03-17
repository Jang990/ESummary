package com.esummary.crawler;

import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InhaTcCrawlerTest {
    private Crawler crawler = new InhaTcCrawler(new InhatcLoginCrawler());

    /*
    @Autowired
    private Crawler crawler;
    */

    @Test
    @DisplayName("로그인 실패")
    void crawlLoginSession() {
        Optional<Map<String, String>> loginCookie = crawler.crawlLoginSession("201845096", "1232132");
        Assertions.assertThat(loginCookie.isEmpty()).isEqualTo(true);
    }

    @Test
    void crawlOwnCourse() {
    }

    @Test
    void crawlLectureByWeek() {
    }

    @Test
    void crawlAnnouncement() {
    }

    @Test
    void crawlAssignment() {
    }
}