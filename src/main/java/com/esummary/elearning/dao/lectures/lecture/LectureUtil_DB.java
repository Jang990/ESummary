package com.esummary.elearning.dao.lectures.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.dao.user.UserLectureUtil_DB;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.repository.subject.LectureInfoRepository;
import com.esummary.repository.user.UserLectureRepository;

@Component
public class LectureUtil_DB implements DBLectureUtil {
	
	@Autowired
	private LectureInfoRepository subjectLectureRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;
	@Autowired
	private UserLectureUtil_DB userLectureUtil_DB; 

	@Override
	public List<LectureInfo> getLectureList(WeekInfo weekInfo) {
		List<LectureInfo> lectureList = new ArrayList<LectureInfo>();
		lectureList = subjectLectureRepository.findByWeekInfo(weekInfo);
		
		return lectureList;
	}
	
	@Override
	public Optional<UserLecture> getUserLecture(int usId, LectureInfo lecture) {
		Optional<UserLecture> ul = userLectureUtil_DB.getUserLecture(usId, lecture.getLectureId());
		return ul;
	}

	@Override
	public boolean saveService(LectureInfo lecture) {
		if(validateDuplicate(lecture))
			return false;
		
		subjectLectureRepository.save(lecture);
		return true;
	}

	@Override
	public boolean saveService(List<LectureInfo> lectures) {
		List<LectureInfo> savedLectureWeeks = new ArrayList<LectureInfo>();
		
		for (LectureInfo lecture : lectures) {
			if(validateDuplicate(lecture)) // 중복 확인, 중복일시 예외발생
				continue;
			else {
				savedLectureWeeks.add(lecture);
			}
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectLectureRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(LectureInfo lecture) {
		Optional<LectureInfo> lectureCheck = getLecture(lecture.getWeekId(), lecture.getIdx());
		
		if(lectureCheck.isEmpty()) return false; //중복 아님
		if(!equalEntityValue(lecture, lectureCheck.get())) return false; //중복 아님 
		
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean equalEntityValue(LectureInfo lecture1, LectureInfo lecture2) {
		if(!lecture1.getTitle().equals(lecture2.getTitle())) return true;
		if(!(lecture1.getLectureVideoId() != null)) {
			if(!lecture1.getLectureVideoId().equals(lecture2.getLectureVideoId())) return true;
		}
		if(!lecture1.getLearningTime().equals(lecture2.getLearningTime())) return true;
		
		return false;
		/*
		if(lecture1.getFullTime().equals(lecture2.getFullTime()) &&
				lecture1.getTitle().equals(lecture2.getTitle())) {
			if(!lecture1.getType().equals("온라인")) return true; // 실제 값이 같음.
			if(lecture1.getLectureVideoId() == null) return true;
			return false;
		}
		else
			return false;
		*/
	}

	@Override
	public Optional<LectureInfo> getLecture(String weekId, String idx) {
		return subjectLectureRepository.
				findByWeekInfo_WeekIdAndIdx(weekId, idx);
	}
}
