package com.esummary;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.esummary.configuration.security.jwt.elearninglogin.ElearningLoginService;
import com.esummary.elearning.dto.LoginCheck_DTO;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.repository.subject.SubjectInfoRepository;
import com.esummary.elearning.service.crawling.user.UserCrawlingUtil;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.subject.SubjectDBService;
import com.esummary.elearning.service.vue.VueService;
import com.esummary.repository.TestRepository;

@SpringBootTest
class EsummaryApplicationTests {
	
	@Autowired
	private SubjectDBService subjectDBService;
	@Autowired
	private VueService vueService;
	@Autowired
	private ElearningLoginService elearningLoginService;
	@Autowired
	private TestRepository testRepository;
	@Autowired
	private SubjectInfoRepository subjectInfoRepository;
	@Autowired
	private UserCrawlingUtil userCrawlingUtil;
	
	
//	@Test
	public void loadDB() {
		/*
		String num = "201845096";
		String id = "202214043C4846";
		UserData ud = new UserData(num, "장현우", new HashMap<String,String>());
		List<LectureWeekData> wd = subjectDBService.getLectureData(ud, id);
		System.out.println("==============>여기");
		System.out.println(wd);
		*/
		
		System.out.println("시작");
		SubjectInfo si = subjectInfoRepository.findBySubjectId("202214043C4846");
		System.out.println("=====>여기: "+si.getLectureList().get(0).getTitle());
		System.out.println("여기까지");
	}
	
	
	@Test
	public void CrawlWeekAndLecture() {
		String studentNumber = "201845096";
		String password = "..."; //비밀번호로 변경할 것
		String subjectId = "202211141LLA104";
		LoginCheck_DTO loginCheck = new LoginCheck_DTO();
		loginCheck.setStudentNumber(studentNumber);
		loginCheck.setPassword(password);
		
		Map<String, String> loginCookies = elearningLoginService.getLoginCookies(loginCheck);
		System.out.println("=========>"+loginCookies);
	}
	
}
