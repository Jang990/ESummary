package com.esummary.crawling.service;

import com.esummary.crawling.dto.InhatcUserDTO;

/**
 * 크롤링하고 db에 저장 이 두개를 나누기엔 너무 멀리옴
 * @author User
 *
 */
public interface CrawlingService {
	/**
	 * 사용자정보를 받아서 로그인페이지 정보(수강하는 과목들 정보, 학번, 이름) 크롤링
	 * @param userDTO
	 */
	boolean crawlLoginPage(InhatcUserDTO userDTO);
}
