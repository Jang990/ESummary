package com.esummary.elearning.service.subject.util.crawling.lectures;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface LectureWeekUtil {
	List<SubjectLectureWeekInfo> getSubjectLectureInfo(UserSubject userSubject, Document docStudyHome, Map<String, String> initialCookies);
}
