package com.esummary.crawler;

import com.esummary.crawler.announcementcrawler.AnnouncementCrawler;
import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.dto.AssignmentDTO;
import com.esummary.crawler.dto.CourseDTO;
import com.esummary.crawler.dto.WeekDTO;
import com.esummary.crawler.logincrawler.LoginCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InhatcCrawler implements Crawler {
//    public final String MAIN_URL = "https://cyber.inhatc.ac.kr";

    private final LoginCrawler inhatcLoginCrawler;
    private final AnnouncementCrawler inhatcAnnouncementCrawler;

    @Override
    public Optional<Map<String, String>> crawlLoginSession(String id, String password) {
        try {
            return inhatcLoginCrawler.getLoginSession(id, password);
        } catch (Exception e) {
            // JWT를 제거하는 등의 작업 필요?
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CourseDTO> crawlOwnCourse(String courseId, Map<String, String> loginSessionCookie) {
        return null;
    }

    @Override
    public List<WeekDTO> crawlLectureByWeek(String courseId, Map<String, String> loginSessionCookie) {
        return null;
    }

    @Override
    public List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie) {
        inhatcAnnouncementCrawler.crawlAnnouncement(courseId, loginSessionCookie);
        return null;
    }

    @Override
    public List<AssignmentDTO> crawlAssignment(String courseId, Map<String, String> loginSessionCookie) {
        return null;
    }
}
