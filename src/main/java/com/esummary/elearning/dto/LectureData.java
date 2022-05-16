package com.esummary.elearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureData {
	//과목정보
	Long lectureId;
	String lectureVideoId;
	String type;
	String idx;
	String title;
	String fullTime;
	
	//사용자 개인정보
	String status; // 0 or 1 //학습 완료: 1, 학습 미완료: 0 
	String learningTime;
}