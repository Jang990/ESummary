package com.esummary.repository.querydsl;

import java.util.List;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.entity.user.UserSubject;

public interface UserSubjectRepositoryCustom {
	List<UserSubject> findLectureInfo(String studentId,String subjectId);
	List<LectureWeekData> findUserLectureList(String studentId, String subjectId);
}
