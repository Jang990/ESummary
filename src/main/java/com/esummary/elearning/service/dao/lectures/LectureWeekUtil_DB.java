package com.esummary.elearning.service.dao.lectures;

import java.text.ParseException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectLectureRepository;
import com.esummary.elearning.repository.subject.SubjectLectureWeekRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.crawling.ELearningURL;
import com.esummary.elearning.service.crawling.SubjectCrawlingService_Inhatc;
import com.esummary.elearning.service.dao.lectures.lecture.DBLectureUtil;

@Component
public class LectureWeekUtil_DB implements DBLectureWeekUtil {
	@Autowired
	private SubjectLectureWeekRepository subjectLectureWeekRepository;
	
	@Override
	public boolean saveService(WeekInfo lectureWeek) {
		if(validateDuplicate(lectureWeek))
			return false;
		
		subjectLectureWeekRepository.save(lectureWeek);
		return true;
	}

	@Override
	public boolean saveService(List<WeekInfo> lectureWeeks) {
		List<WeekInfo> savedLectureWeeks = new ArrayList<WeekInfo>();
		
		for (WeekInfo lectureWeek : lectureWeeks) {
			if(validateDuplicate(lectureWeek)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedLectureWeeks.add(lectureWeek);
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectLectureWeekRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(WeekInfo lectureWeek) {
		Optional<WeekInfo> lectureWeekCheck = subjectLectureWeekRepository.
				findByLectureWeekIdAndSubjectInfo_subjectId(lectureWeek.getLectureWeekId(), lectureWeek.getSubjectId());
		
		if(lectureWeekCheck.isEmpty()) return false;
		else return true; //중복 맞음
	}

}
