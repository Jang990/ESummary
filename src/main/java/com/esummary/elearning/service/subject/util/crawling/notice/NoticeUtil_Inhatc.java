package com.esummary.elearning.service.subject.util.crawling.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil_Inhatc;

//@Component
@Component("crawlNot")
public class NoticeUtil_Inhatc implements NoticeUtil{

	@Override
	public List<SubjectNoticeInfo> getSubjectNoticeInfo(String subjectId, Map<String, String> loginCookies) {
		List<SubjectNoticeInfo> noticeList = new ArrayList<SubjectNoticeInfo>();
		Elements notices = crawlNoticeBox(subjectId, loginCookies);
		
		for (Element element : notices) {
			SubjectNoticeInfo notice = createNotice(element, subjectId);
			if(notice != null) {
				noticeList.add(notice);
			}
		}
		
		return noticeList;
	}
	
	private Elements crawlNoticeBox(String subjectId, Map<String, String> loginCookies) {
		Document docStudyHome = SubjectUtil_Inhatc.connStudyHome(subjectId,loginCookies);
		
		//StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
		final String noticePageSelector = "#1 > ul > li:nth-child(1) > a";
		final String noticeBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
		Document docNoticePage = ELearningServiceImpl.gotoHrefPageFromHomePage(loginCookies, docStudyHome, noticePageSelector);
		return docNoticePage.select(noticeBoxSelector);
	}

	private SubjectNoticeInfo createNotice(Element element, String subjectId) {
		String id = crawlNoticeId(element);
		String title = crawlTitle(element);
		if(title.equals("") || title == null)
			return null; //크롤링오류임
		String description = crawlDescription(element);
		String author = crawlAutor(element);
		String createDate = crawlCreateDate(element);
		
		SubjectNoticeInfo createdNotice = 
				new SubjectNoticeInfo(id, title, author, createDate, description, subjectId);
		
		return createdNotice; 
	}
	
	private String crawlCreateDate(Element element) {
		final String createDateSelector = "div > dl > dd.info > ul.fr.mr10 > li:nth-child(2)";
		String createDate = element.select(createDateSelector).text().replace("작성일 :", "").trim();
		return createDate;
	}

	private String crawlAutor(Element element) {
		final String authorSelector = "div > dl > dd.info > ul.fr.mr10 > li:nth-child(1) > span";
		String author = element.select(authorSelector).text().trim();
		return author;
	}

	private String crawlDescription(Element element) {
		final String descriptionSelector = "div > dl > dd:nth-child(3) > div";
		String description = element.select(descriptionSelector).text().trim();
		return description;
	}

	private String crawlTitle(Element element) {
		final String titleSelector = "div > dl > dt > h4 > a";
		String title = element.select(titleSelector).text().trim();
		return title;
	}

	private String crawlNoticeId(Element element) {
		final String idSelector = "div > dl > dt > ul > li > button";
		String idJSCode = element.select(idSelector).attr("onclick");
		String id = SubjectUtil_Inhatc.extractDataFromJsCode(idJSCode)[0];
		return id;
	}

}
