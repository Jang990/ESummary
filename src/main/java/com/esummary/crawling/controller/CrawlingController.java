package com.esummary.crawling.controller;

import java.util.ArrayList; 
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.exception.DeniedElearningCookieException;
import com.esummary.auth.service.login.CustomUserDetails;
import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.tofront.SubjectCardDTO;
import com.esummary.crawling.service.CrawlingService;

import lombok.RequiredArgsConstructor;


/**
 * 인하공전 이러닝에서 여러 정보를 크롤링
 * @author User
 *
 */
@RestController
@RequestMapping("/api/inhatc")
@RequiredArgsConstructor
public class CrawlingController {
	
	private final CrawlingService crawlingService;
	
	/**
	 * 로그인화면에서 학번, 이름, 수강리스트 기본정보 크롤링
	 * 
	 * 이전 /getInitSubject과 같은 기능
	 * 
	 * @param studentId
	 * @return 크롤링한 정보들을 리턴
	 */
	@PostMapping("/login-info")
	public ResponseEntity<SubjectCardDTO> getInitData(@AuthenticationPrincipal CustomUserDetails customUser) {
		//이러닝이 안되서 추가한 테스트코드
		/*
		InitalPageData testInitPageData = new InitalPageData(new ArrayList<SubjectInfo>(), "장현우", "201845096");
		testCode(testInitPageData.getSubjectCardData());
		return testInitPageData;
		*/
		InhatcUserDTO userDto = new InhatcUserDTO(customUser.getUsername(), customUser.getInhaTcSessionId());
		
		List<InhatcSubjectCardDTO> subjects;
		try {
			subjects=  crawlingService.crawlLoginPage(userDto);
			return new ResponseEntity<SubjectCardDTO>(new SubjectCardDTO(subjects), HttpStatus.OK);
		} catch(DeniedElearningCookieException e) {
			subjects = new ArrayList<>();
			// 일단 OK로 해놓음
			return new ResponseEntity<SubjectCardDTO>(new SubjectCardDTO(subjects), HttpStatus.OK);
		}
		
	}
	
	private void testCode(List<InhatcSubjectCardDTO> subjectCardData) {
		/*
		만약 이러닝에 1학기 정보가 내려갔을때 테스트를 위한 코드
		SubjectCardData(subjectId=CORS_1703071437557d610794, subjectName=서버프로그래밍, owner=조규철), 
		SubjectCardData(subjectId=CORS_1704050920510f7c44c0, subjectName=프로그래밍, owner=관리자), 
		SubjectCardData(subjectId=202214001LLA117, subjectName=[1학년L반] 발명과특허, owner=김영준), 
		SubjectCardData(subjectId=202214001LLA138, subjectName=[1학년L반] 빅데이터의이해, owner=용승림), 
		SubjectCardData(subjectId=202211141LLA104, subjectName=[1학년L반] 자기개발과직업윤리, owner=김성진), 
		SubjectCardData(subjectId=202214043CMP389, subjectName=[3학년C반] 빅데이터, owner=권두순), 
		SubjectCardData(subjectId=202214043C4846, subjectName=[3학년C반] 소프트웨어분석설계, owner=김철진), 
		SubjectCardData(subjectId=202214043C5019, subjectName=[3학년C반] 웹보안, owner=용승림), 
		SubjectCardData(subjectId=202214043CMP743, subjectName=[3학년C반] 자율드론, owner=박병섭), 
		SubjectCardData(subjectId=202214043DMP250, subjectName=[3학년D반] 캡스톤디자인, owner=황수철)])
		*/
		subjectCardData.add(new InhatcSubjectCardDTO("202214001LLA117", "[1학년L반] 발명과특허", "김영준", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202214001LLA138", "[1학년L반] 빅데이터의이해", "용승림", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202211141LLA104", "[1학년L반] 자기개발과직업윤리", "김성진", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202214043CMP389", "[3학년C반] 빅데이터", "권두순", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202214043C4846", "[3학년C반] 소프트웨어분석설계", "김철진", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202214043C5019", "[3학년C반] 웹보안", "용승림", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202214043CMP743", "[3학년C반] 자율드론", "박병섭", ""));
		subjectCardData.add(new InhatcSubjectCardDTO("202214043DMP250", "[3학년D반] 캡스톤디자인", "황수철", ""));
	}

}
