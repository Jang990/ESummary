package com.esummary.elearning.service.subject.util.db.user;

import com.esummary.elearning.entity.user.UserInfo;

public interface DBUserInfoUtil {
	boolean saveUserService(UserInfo user);
	boolean validateDuplicate(UserInfo user);
}
