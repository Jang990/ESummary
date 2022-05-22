package com.esummary.elearning.service.vue;

import java.util.List;

import com.esummary.elearning.dto.LectureData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;

public interface VueService {
	List<SubjectCardData> getInitCardData(String studentNumber);
	List<NoticeData> getNoticeData(String subjectId);
	List<TaskData> getTaskData(String subjectId, String studentNumber);
	List<LectureWeekData> getLectureeData(String subjectId, String studentNumber);
	boolean saveUser(UserData user);
	boolean isExistUserSubjectInDB(String studentNumber);
	
	List<NoticeData> crawlNotice(UserData user, String subjectId);
	List<TaskData> crawlTask(UserData user, String subjectId);
	List<LectureWeekData> crawlLecture(UserData user, String subjectId);
	
	
}
