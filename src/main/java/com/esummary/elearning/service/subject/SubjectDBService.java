package com.esummary.elearning.service.subject;

import java.util.List;
import java.util.Map;

import com.esummary.elearning.dto.subject.LectureWeekData;
import com.esummary.elearning.dto.subject.NoticeData;
import com.esummary.elearning.dto.subject.SubjectDetailDataWithCnt_DTO;
import com.esummary.elearning.dto.subject.TaskData;
import com.esummary.elearning.dto.user.UserData;


public interface SubjectDBService {
	//데이터베이스 조회
	List<SubjectDetailDataWithCnt_DTO> getSubjectDataWithAllDetail(UserData user, String studentNumber);
	List<LectureWeekData> getLectureData(UserData user, String studentNumber);
	List<NoticeData> getNoticeData(String subjectId);
	List<TaskData> getTaskData(UserData user, String studentNumber);
}
