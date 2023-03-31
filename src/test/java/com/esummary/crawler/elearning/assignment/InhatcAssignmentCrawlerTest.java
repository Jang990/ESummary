package com.esummary.crawler.elearning.assignment;

import com.esummary.crawler.elearning.InhatcCrawlerConfig;
import com.esummary.crawler.elearning.assignment.dto.AssignmentDTO;
import com.esummary.crawler.elearning.login.InhatcLoginCrawler;
import com.esummary.crawler.elearning.login.LoginCrawler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

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
                loginCrawler.getLoginSession(id, password);

        //when
        List<AssignmentDTO> assignmentDTOList = crawler.crawlAssignment(courseId, loginSession);

        //then
        assertThat(assignmentDTOList.size()).isEqualTo(14);
    }

    @Test
    @DisplayName("잘못된 세션으로 과제 크롤링 시도")
    public void crawlAssignmentFail() throws Exception {
        //when
        List<AssignmentDTO> assignmentList = crawler.crawlAssignment(courseId, failSessionCookie);

        //then
        assertThat(assignmentList.size()).isEqualTo(0);

    }
}