package com.esummary.crawling.service.crawling;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;

public interface SubjectCrawlingService {
	
//	List<SubjectInfo> getSubjectList(UserInfo user);
	
//	List<SubjectInfo> InitSubjectData(UserInfo user);
//	void getSubjectDetail(UserInfo user);
//	void settingUserSubject(UserInfo user, List<SubjectInfo> subjectList);

	List<SubjectInfo> crawlBasicSubjectInfo(Map<String, String> loginCookie);
}
