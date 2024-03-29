package com.esummary.elearning.dao.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.esummary.crawling.service.crawling.ELearningURL;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.SubjectInfo;

public interface DBNoticeUtil {
	
	//일단 Subject쪽 할때 전부 바꿀꺼라 남겨둠
	List<NoticeInfo> getSubjectNoticeInfo(SubjectInfo subjectInfo);

	boolean saveService(NoticeInfo notice);

	boolean saveService(List<NoticeInfo> notices);

	boolean validateDuplicate(NoticeInfo notice);
}
