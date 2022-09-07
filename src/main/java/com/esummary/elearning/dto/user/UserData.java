package com.esummary.elearning.dto.user;

import java.util.Map;

import com.esummary.elearning.entity.user.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
	private String studentNumber;
	private String userName;
	private Map<String, String> initialCookies; // 일단 놔둠
	
	public static UserInfo toEntity(UserData userData) {
		return UserInfo.builder()
				.studentNumber(userData.getStudentNumber())
				.name(userData.getUserName())
				.build();
	}
}
