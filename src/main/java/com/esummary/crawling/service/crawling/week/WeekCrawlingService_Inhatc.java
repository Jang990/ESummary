package com.esummary.crawling.service.crawling.week;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.crawling.service.crawling.ELearningURL;
import com.esummary.crawling.service.crawling.SubjectCrawlingService_Inhatc;
import com.esummary.crawling.service.crawling.lecture.LectureCrawlingService;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.subject.LectureInfoRepository;
import com.esummary.repository.subject.TaskInfoRepository;
import com.esummary.repository.subject.WeekInfoRepository;
import com.esummary.repository.user.UserLectureRepository;

//@Component
@Component("crawlLec")
public class WeekCrawlingService_Inhatc implements WeekCrawlingService {
	
	@Autowired
	private LectureCrawlingService lectureUtil;
	
	private WeekInfo crawlWeekDetailInfo(Element element, String subjectId) {
		Elements lectureElements = crawlLectureBox(element);
		if(lectureElements.isEmpty()) {
			return null;
		}
		
		String lectureWeekId = crawlWeekId(element);
		String titleAndDeadline = crawlTitleAndDaealine(element);
		Map<String, String> splitData = splitDataInTitleString(titleAndDeadline);
		if(splitData == null) return null;
		String title = splitData.get("title");
		Date startDate = SubjectCrawlingService_Inhatc.parseDate(splitData.get("startDate"));
		Date endDate = SubjectCrawlingService_Inhatc.parseDate(splitData.get("endDate"));
		
		List<LectureInfo> lectureList = lectureUtil.getLectureList(lectureElements, lectureWeekId);
		WeekInfo weekInfo = new WeekInfo(lectureWeekId, title, startDate, endDate, subjectId, lectureList);
		
		return weekInfo;
	}

	private String crawlWeekId(Element element) {
		final String idSelector = "div > dl > dt > h4 > span";
		String id = element.select(idSelector).attr("onclick")
				.replace("contentsInfoPop('", "")
				.replace("');return false;", "");
		return id;
	}

	private String crawlTitleAndDaealine(Element element) {
		final String titleAndDeadlineSelector = "div > dl > dt > h4";
		String titleAndDeadLine = element.select(titleAndDeadlineSelector).text();
		return titleAndDeadLine;
	}

	private Elements crawlLectureBox(Element element) {
		final String lecturesSelector = "div > dl > dd > div > table > tbody > tr";
		return element.select(lecturesSelector);
	}

	private Map<String, String> splitDataInTitleString(String titleAndDeadLine) {
		/*
		1주차 과제 2022-02-27 ~ 2022-06-17 학습목표보기
		이 데이터를 
		학습목표보기를 없애고
		title / StartDate / EndDate로 나눈다.
		1주차 과제 / 2022-02-27 / 2022-06-17
		*/
		
		titleAndDeadLine = titleAndDeadLine.replace(" 학습목표보기", "");
		String[] splitData = titleAndDeadLine.split(" ");
		int tildeIdx = SubjectCrawlingService_Inhatc.findTildeIdx(splitData);
		
		if(tildeIdx == 0) return null;
		
		String title = concatTitle(splitData, tildeIdx-1); 
		String startDate = splitData[tildeIdx - 1].trim();
		String endDate = splitData[tildeIdx + 1].trim();
		
		Map<String, String> splitMapData = new HashMap<String, String>();
		splitMapData.put("title", title);
		splitMapData.put("startDate", startDate);
		splitMapData.put("endDate", endDate);
		
		return splitMapData;
	}

	private String concatTitle(String[] splitData, int idx) {
		String title = "";
		for (int i = 0; i < idx; i++) {
			if(!splitData[i].equals("") && splitData[i] != null) 
				title += splitData[i] + " ";
		}
		return title.trim();
	}

	@Override
	public List<WeekInfo> getSubjectLectureWeekInfo(String subjectId,
			Map<String, String> loginCookies) {
		
		Document docStudyHome = SubjectCrawlingService_Inhatc.connStudyHome(subjectId, loginCookies);
		
		//강의 목록 페이지로 이동
		final String lecturePageURLSelector = "#0  > ul > li.last > a";
		Document docLecturePage = ELearningURL.gotoHrefPageFromHomePage(
				loginCookies, docStudyHome, lecturePageURLSelector);
		
		//StudyHome에서 과제 내용이 적혀있는 박스 섹션
		//ex)1주차. 자율드론프로젝트 및 드론이론 2022-02-28 ~ 2022-03-04    뭐 이렇게 써있는 박스 Element를 가져옴
		Elements lectures = crawlLectureWeekContentsBox(docLecturePage);
		
		//주차 정보 크롤링 - 이 하위에는 강의 차시 크롤링도 포함된다.
		List<WeekInfo> weekList = new ArrayList<WeekInfo>();
		for (Element element : lectures) {
			WeekInfo week = crawlWeekDetailInfo(element, subjectId);
			if(week != null) {
				weekList.add(week);
			}
		}
		
		return weekList;
	}



	private Elements crawlLectureWeekContentsBox(Document docLecturePage) {
		final String lectureBoxSelector = ".listContent";
		return docLecturePage.select(lectureBoxSelector);
	}
}
