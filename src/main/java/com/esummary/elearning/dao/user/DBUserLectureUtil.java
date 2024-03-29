package com.esummary.elearning.dao.user;

import java.util.List;
import java.util.Optional;

import com.esummary.entity.user.UserLecture; 


public interface DBUserLectureUtil {
	//저장 서비스 (중복 체크 + 저장)
	boolean saveService(UserLecture subject);
	boolean saveService(List<UserLecture> subjects);
	boolean validateDuplicate(UserLecture subject);
	
	Optional<UserLecture> getUserLecture(long usId, long lectureId);
}
