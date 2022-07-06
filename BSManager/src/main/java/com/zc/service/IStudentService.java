package com.zc.service;

import java.util.List;
import java.util.Map;

import com.zc.entity.Announcement;
import com.zc.entity.Doubt;
import com.zc.entity.Student;
import com.zc.entity.StudentTaskBookOpening;
import com.zc.entity.TeacherProgress;
import com.zc.entity.TeacherTaskBookOpening;
import com.zc.entity.ThesisInformation;
import com.zc.entity.ThesisTitle;
import com.zc.entity.Topic;
import com.zc.entity.Zhiyuan;


public interface IStudentService {
	
	int addStudent(Student student);
	
	List<Student> showAllStudent();
	
	Student getStudentNameById(int id);
	
	int updateStudent(Student student);
	
	int deleteStudent(int id);
	
	List<Student> showStudentOne1(String studentNo);
	
	List<Student> showStudentOne2(String studentName);
	
	List<Student> showStudentOne3(String studentNo,String studentName);
	
	Student getStudentByNO(String studentNo);
	
	List<ThesisTitle> availableTopic();
	
	Topic chosenThesisTitle(int studentId); 
	
	int addTopicToDb(Topic topic);
	
	int deleteTopic(int studentId);
	
	TeacherTaskBookOpening getFilePathByThesisId(int thesisId);
	
	StudentTaskBookOpening getSTBOInfoById(int studentId);
	
	int uploadTaskBook(int studentId,String filePath);
	
	int uploadOpening(int studentId,String filePath);
	
	int uploadKexing(int studentId,String filePath);
	
	int uploadXuqiu(int studentId,String filePath);
	
	int uploadGaiyao(int studentId,String filePath);
	
	int uploadShujuku(int studentId,String filePath);
	
	Map<String, String> getTaskBookOpeningToMap(int studentId);
	
	int resetTaskBook(int studentId);
	
	int resetOpening(int studentId); 
	
	StudentTaskBookOpening getInfoByTaskBookPath(String taskBookPath);
	
	StudentTaskBookOpening getInfoByOpeningPath(String openingPath);
	
	
	ThesisTitle getThesisInfoByid(int thesisId);
	
	List<TeacherProgress> getTeacherProgressByStudentId(int studentId);
	
	List<Announcement> showAllAnnouncement();
	
	int uploadThesisInformation(int studentId,String filePath);
	
	ThesisInformation getInfoByStudentId(int studentId);
	
	ThesisInformation getInfoByFilePath(String filePath);
	
	int deleteThesisInformation(int studentId);
	
	
	ThesisInformation getThesisInforInfoByStudentId(int studentId);
	
	
	int addDoubt(Doubt doubt);
	
	List<Doubt> getAllDoubtByStudent(int studentId);
	
	
	String getThesisDesc(int thesisId);

	int addZhiyuanToDb(Zhiyuan zhiyuan);

	Zhiyuan chosenZhiyuan(int studentId);

	
}

