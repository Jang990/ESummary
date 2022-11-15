package com.esummary.chat.dto;

import java.sql.Timestamp; 

import com.querydsl.core.annotations.QueryProjection;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatMessageDTO {
	private String senderId;
	private String message;
	private String subjectId;
	private Timestamp createdTime;
	
	public ChatMessageDTO(String senderId, String content) {
		this.senderId = senderId;
		this.message = content;
	}
	
	@Builder
	public ChatMessageDTO(String subjectId, String senderId, String message) {
		this.subjectId = subjectId;
		this.senderId = senderId;
		this.message = message;
	}
	
	@QueryProjection
	public ChatMessageDTO(String userName, String content, Timestamp createdTime) {
		this.senderId = userName;
		this.message = content;
		this.createdTime = createdTime;
	}

	public ChatMessageDTO(String senderId, String message, String subjectId, Timestamp createdTime) {
		this.senderId = senderId;
		this.message = message;
		this.subjectId = subjectId;
		this.createdTime = createdTime;
	}
	
	

}