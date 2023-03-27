package com.esummary.crawler.lecture;

import com.esummary.crawler.InhatcCrawlerConfig;
import com.esummary.crawler.lecture.dto.WeekDTO;
import com.esummary.crawler.login.InhatcLoginCrawler;
import com.esummary.crawler.login.LoginCrawler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InhatcLectureCrawlerTest {
    LectureCrawler crawler = new InhatcLectureCrawler();
    LoginCrawler loginCrawler = new InhatcLoginCrawler();

    String courseId = InhatcCrawlerConfig.courseId;
    String id = InhatcCrawlerConfig.id;
    String password = InhatcCrawlerConfig.password;
    Map<String, String> failSession = InhatcCrawlerConfig.failSessionCookie;

    @Test
    @DisplayName("수업정보 정상 크롤링")
    public void crawlLectureTest() throws Exception {
        //given
        Map<String, String> loginSession = loginCrawler.getLoginSession(id, password);

        //when
        List<WeekDTO> weekList = crawler.crawlLecture(courseId, loginSession);

        //then

    }
}