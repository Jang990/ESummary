package com.esummary.elearning.service.vue;

import java.util.ArrayList;  
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dao.DBSubjectUtil;
import com.esummary.elearning.dao.DBUserSubjectUtil;
import com.esummary.elearning.dao.lectures.DBLectureWeekUtil;
import com.esummary.elearning.dao.lectures.lecture.DBLectureUtil;
import com.esummary.elearning.dao.notice.DBNoticeUtil;
import com.esummary.elearning.dao.task.DBTaskUtil;
import com.esummary.elearning.dao.user.DBUserInfoUtil;
import com.esummary.elearning.dao.user.DBUserLectureUtil;
import com.esummary.elearning.dao.user.DBUserTaskUtil;
import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.subject.NoticeInfoRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.crawling.SubjectCrawlingService;
import com.esummary.elearning.service.crawling.notice.NoticeCrawlingService;
import com.esummary.elearning.service.crawling.task.TaskCrawlingService;
import com.esummary.elearning.service.crawling.week.WeekCrawlingService;

@Service
public class VueServiceImpl implements VueService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private NoticeInfoRepository subjectNoticeRepository;
	
//	@Autowired
//	private ELearningService eLearningService;
	@Autowired
	private SubjectCrawlingService subjectUtil;
	@Autowired
	private NoticeCrawlingService noticeUtil;
	@Autowired
	private TaskCrawlingService taskUtil;
	@Autowired
	private WeekCrawlingService weekUtil;
	
	@Autowired
	private DBSubjectUtil dbSubjectUtil;
	@Autowired
	private DBLectureWeekUtil dbWeekUtil;
	@Autowired
	private DBLectureUtil dbLectureUtil;
	@Autowired
	private DBNoticeUtil dbNoticeUtil;
	@Autowired
	private DBTaskUtil dbTaskUtil;
	
	@Autowired
	private DBUserSubjectUtil dbUserSubjectUtil;
	@Autowired
	private DBUserLectureUtil dbUserLectureUtil;
	@Autowired
	private DBUserTaskUtil dbUserTaskUtil;
	@Autowired
	private DBUserInfoUtil dbUserInfoUtil;
	
//	@Override
//	public List<SubjectCardData> getInitCardData(String studentNumber) {
//		UserInfo user = userRepository.findWithUserSubjectsByStudentNumber(studentNumber);
//		List<SubjectCardData> cardList = new ArrayList<>();
//		
//		for (UserSubject userSubject : user.getUserSubjects()) {
//			SubjectInfo subject = userSubject.getSubjectInfo();
//			SubjectCardData card = new SubjectCardData(
//					subject.getSubjectId(), subject.getSubjectName(), subject.getSubjectOwnerName());
//			cardList.add(card);
//		}
//		
//		if(cardList.size() > 0) return cardList;
//		else return null;
//	}
	
	public List<NoticeData> getNoticeData(String subjectId) {
		List<NoticeInfo> noticeInfo = subjectNoticeRepository.findBySubjectInfo_SubjectId(subjectId);
		if(noticeInfo == null || noticeInfo.size() == 0) return null;
		
		List<NoticeData> noticeDTO = new ArrayList<>();
		for (NoticeInfo subjectNoticeInfo : noticeInfo) {
			NoticeData notice = new NoticeData(
					subjectNoticeInfo.getNoticeId(),
					subjectNoticeInfo.getTitle(), 
					subjectNoticeInfo.getDescription(),
					subjectNoticeInfo.getAuthor(), 
					subjectNoticeInfo.getCreateDate()
				);  
			noticeDTO.add(notice); 
		}
		
		if(noticeDTO.size() > 0) return noticeDTO;
		else return null;
	}

	@Override
	public boolean saveUser(UserData user) {
		if(userRepository.findByStudentNumber(user.getStudentNumber()).isEmpty()) {
			UserInfo userInfo = new UserInfo();
			userInfo.setStudentNumber(user.getStudentNumber());
			userInfo.setUserName(user.getUserName());
			userRepository.save(userInfo); 
			return true;
		}
		else
			return false;
	}

	@Override
	public List<NoticeData> crawlNotice(UserData user, String subjectId) {
		//크롤링
		List<NoticeInfo> notices = noticeUtil.getSubjectNoticeInfo(subjectId, user.getInitialCookies());
		
		//저장
		dbNoticeUtil.saveService(notices);
		
		//DTO로 변환
		List<NoticeData> noticeDTO = new ArrayList<NoticeData>();
		for (NoticeInfo subjectNoticeInfo : notices) {
			noticeDTO.add(NoticeData.convertNoticeData(subjectNoticeInfo));
		}
		
		return noticeDTO;
	}
	
	@Override
	public List<TaskData> crawlTask(UserData user, String subjectId) {
		//크롤링
		List<TaskInfo> tasks = taskUtil.getSubjectTaskInfo(subjectId, user.getInitialCookies());
		
		//저장
		dbTaskUtil.saveService(tasks); // SubjectTask 저장
		Optional<UserSubject> userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber());
		if(userSubject.isEmpty()) System.out.println("여기발생");
		List<UserTask> userTasks = convertToUserTaskList(userSubject.get(), tasks);
		dbUserTaskUtil.saveService(userTasks); //UserTask 저장
		
		//DTO로 변환
		List<TaskData> taskDTO = new ArrayList<TaskData>();
		for (TaskInfo subjectTaskInfo : tasks) {
			taskDTO.add(TaskData.convertTaskData(subjectTaskInfo));
		}
		return taskDTO;
	}
	
	private List<UserTask> convertToUserTaskList(UserSubject userSubject, List<TaskInfo> tasks) {
		List<UserTask> userTasks = new ArrayList<UserTask>();
		for (TaskInfo lecture : tasks) {
			userTasks.add(new UserTask(lecture, userSubject));
		}
		return userTasks;
	}

	@Override
	public List<LectureWeekData> crawlLecture(UserData user, String subjectId) {
//		UserSubject userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber()); 
		//크롤링하기.
		List<WeekInfo> weeks = weekUtil.getSubjectLectureWeekInfo(subjectId, user.getInitialCookies());
		
		//Lecture저장하기.
		dbWeekUtil.saveService(weeks);
		Optional<UserSubject> userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber());
//		if(userSubject.isEmpty()) System.out.println("UserSubject 존재하지 않음");
		for (WeekInfo week : weeks) {
			dbLectureUtil.saveService(week.getLectures());
			List<UserLecture> userLectures = convertToUserLectureList(userSubject.get(), week.getLectures());
			dbUserLectureUtil.saveService(userLectures);
		}
		
		//DTO로 바꾸기.
		List<LectureWeekData> lecturesDTO = new ArrayList<LectureWeekData>();
		for (WeekInfo subjectLectureWeekInfo : weeks) {
			lecturesDTO.add(new LectureWeekData(subjectLectureWeekInfo));
		}
		
		return lecturesDTO;
	}

	private List<UserLecture> convertToUserLectureList(UserSubject userSubject, List<LectureInfo> lectureList) {
		/*
		 *  예외사례
		 * 사용자 A의 첫 사이트 이용인 경우 saveService부분에서 userLecture에 ul_Id가 무조건 들어간다.
		 * 근데 사용자 A가 재접속을 한다면? save를 하지않음 -> setLectureId를 하지않음 -> 즉 ul_Id는 null이 된다.
		 *  
		 *  해결방안
		 * convertToUserLectureList 부분에서 SubjectLecture를 검색해서 넣어준다.
		 */
		List<UserLecture> userLectures = new ArrayList<UserLecture>();
		for (LectureInfo lecture : lectureList) {
			if(lecture.getLectureId() == null) { 
				Optional<LectureInfo> dbLecture = dbLectureUtil.getLecture(lecture.getWeekId(), lecture.getIdx()); //여기 최적화 필요. 전부 쿼리를 보냄. 한번에 불러오고 키값으로 찾는게 좋을 듯
				if(dbLecture.isEmpty()) System.out.println("======= 오류위치: convertToUserLectureList ");
				else {
					lecture.setLectureIdForCrawlingObject(dbLecture.get().getLectureId()); // 크롤링을 했기때문에 MySQL에 ID값은 들어가 있지 않다.
				}
			}
			userLectures.add(new UserLecture(lecture, userSubject));
		}
		return userLectures;
	}

	@Override
	public InitalPageData crawlInitDataService(UserData userDTO) {
		List<SubjectInfo> crawledBasicSubjectData = crawlInitData(userDTO); //크롤링
		if(crawledBasicSubjectData == null) return null;
		saveInitData(userDTO, crawledBasicSubjectData); //UserSubject와 Subject 저장
		
		InitalPageData initPageData = new InitalPageData(
					crawledBasicSubjectData, userDTO.getUserName(), userDTO.getStudentNumber()
				);
		return initPageData;
	}

	@Override
	public boolean saveUserService(UserData userData) {
		UserInfo user = new UserInfo(userData);
		return dbUserInfoUtil.saveUserService(user);
	}
	
	private List<SubjectInfo> crawlInitData(UserData userDTO) {
		List<SubjectInfo> crawlingBasicSubjectData = subjectUtil.crawlBasicSubjectInfo(userDTO.getInitialCookies()); //크롤링 정보 가져오기
		if(crawlingBasicSubjectData.isEmpty() || crawlingBasicSubjectData == null) return null;
		else return crawlingBasicSubjectData;
	}
	
	//UserSubject와 Subject 저장
	private boolean saveInitData(UserData userDTO, List<SubjectInfo> crawledBasicSubjectData) {
		UserInfo userEntity = new UserInfo(userDTO);
		dbSubjectUtil.saveService(crawledBasicSubjectData);
		List<UserSubject> usList = new ArrayList<UserSubject>();
		for (SubjectInfo subjectInfo : crawledBasicSubjectData) {
			usList.add(new UserSubject(userEntity, subjectInfo));
		}
		dbUserSubjectUtil.saveService(usList);
		
		return true;
	}
	
}
