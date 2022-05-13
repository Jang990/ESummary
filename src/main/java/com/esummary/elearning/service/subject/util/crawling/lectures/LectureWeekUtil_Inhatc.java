package com.esummary.elearning.service.subject.util.crawling.lectures;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.subject.SubjectLectureRepository;
import com.esummary.elearning.repository.subject.SubjectLectureWeekRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil_Inhatc;
import com.esummary.elearning.service.subject.util.crawling.lectures.lecture.LectureUtil;

//@Component
@Component("crawlLec")
public class LectureWeekUtil_Inhatc implements LectureWeekUtil {
	
	@Autowired
	private LectureUtil lectureUtil;
	
	@Autowired
	private SubjectLectureWeekRepository subjectLectureWeekRepository;
	
	//UserLecture에 넣어야하는데 계속 함수간에 전달하기 귀찮아서 그냥 여기 많듦
	private UserSubject userSubject;
	
	public List<SubjectLectureWeekInfo> getSubjectLectureInfo(
			UserSubject userSubject, Document docStudyHome, 
			Map<String, String> initialCookies) {
		this.userSubject = userSubject;
		List<SubjectLectureWeekInfo> weekList = new ArrayList<SubjectLectureWeekInfo>();
		
		//StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
		final String lecturePageSelector = "#0  > ul > li.last > a";
		final String lectureBoxSelector = ".listContent";
		
		Document docNoticePage = ELearningServiceImpl.gotoHrefPageFromHomePage(
				initialCookies, docStudyHome, lecturePageSelector);
		Elements lectures = docNoticePage.select(lectureBoxSelector);
		
		for (Element element : lectures) {
			SubjectLectureWeekInfo week = getWeekInfo(element, userSubject.getSubjectInfo());
			if(week != null) {
				weekList.add(week);
			}
		}
		
		return weekList;
	}
	
	private SubjectLectureWeekInfo getWeekInfo(Element element, SubjectInfo subjectInfo) {
		Elements lectureElements = crawlLectureBox(element);
		if(lectureElements.isEmpty()) {
			return null;
		}
		
		String titleAndDeadline = crawlTitleAndDaealine(element);
		Map<String, String> splitData = splitDataInTitleString(titleAndDeadline);
		if(splitData == null) return null;
		String id = crawlWeekId(element);
		
		SubjectLectureWeekInfo weekInfo = new SubjectLectureWeekInfo();
		String title = splitData.get("title");
		Date startDate = SubjectUtil_Inhatc.parseDate(splitData.get("startDate"));
		Date endDate = SubjectUtil_Inhatc.parseDate(splitData.get("endDate"));
		
		weekInfo.setLectureWeekId(id);
		weekInfo.setTitle(title);
		weekInfo.setStartDate(startDate);
		weekInfo.setEndDate(endDate);
		weekInfo.setSubjectInfo(subjectInfo);
		subjectLectureWeekRepository.save(weekInfo); //DB에 저장 - 여기서 저장하는 이유는 lecture가 외래키로 사용해야하기 때문.
		
		List<SubjectLecture> lectureList = lectureUtil.getLectureList(lectureElements, weekInfo, this.userSubject);
		weekInfo.setLectures(lectureList);
		
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
		int tildeIdx = SubjectUtil_Inhatc.findTildeIdx(splitData);
		
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
}
