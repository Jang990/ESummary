package com.esummary.elearning.service.subject.util.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.repository.subject.SubjectRepository;

@Component
public class SubjectUtil_DB implements DBSubjectUtil {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Override
	public boolean saveService(SubjectInfo subject) {
		if(validateDuplicate(subject))
			return false;
		
		subjectRepository.save(subject);
		return true;
	}
	
	@Override
	public boolean saveService(List<SubjectInfo> subjects) {
		List<SubjectInfo> savedSubjects = new ArrayList<SubjectInfo>();
		
		for (SubjectInfo subject : subjects) {
			if(validateDuplicate(subject)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedSubjects.add(subject);
		}
		
		if(savedSubjects.size() == 0) return false;
		
		subjectRepository.saveAll(savedSubjects);
		return true;
	}

	@Override
	public boolean validateDuplicate(SubjectInfo subject) {
		SubjectInfo us = subjectRepository.findBySubjectId(subject.getSubjectId());
		
		if(us == null) return false;
		else return true; //중복 맞음
	}
	
	
}
