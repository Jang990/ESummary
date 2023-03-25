package com.esummary.crawler.assignment;

import com.esummary.crawler.InhatcCrawlerConfig;
import com.esummary.crawler.dto.AssignmentDTO;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import com.esummary.crawler.logincrawler.LoginCrawler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InhatcAssignmentCrawlerTest {

    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
    private InhatcAssignmentCrawler crawler = new InhatcAssignmentCrawler();

    private String id = InhatcCrawlerConfig.id;
    private String password = InhatcCrawlerConfig.password;
    private String courseId = InhatcCrawlerConfig.courseId;
    private Map<String, String> failSessionCookie = InhatcCrawlerConfig.failSessionCookie;

    @BeforeAll
    static void beforeAll() {
        if(InhatcCrawlerConfig.password.equals(InhatcCrawlerConfig.state.EMPTY.toString())) {
            throw new IllegalArgumentException("InhatcLoginCrawlerTest 설정 정보가 모두 필요합니다.");
        }
    }
    @Test
    @DisplayName("과제 크롤링 정상 작동")
    public void crawlAssignment() throws Exception {
        //given
        Map<String, String> loginSession =
                loginCrawler.getLoginSession(id, password)
                        .orElseThrow(() -> new IllegalArgumentException());

        //when
        List<AssignmentDTO> assignmentDTOList = crawler.crawlAssignment(courseId, loginSession);

        //then
        Assertions.assertThat(assignmentDTOList.size()).isEqualTo(14);
    }
}