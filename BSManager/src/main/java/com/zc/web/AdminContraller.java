package com.zc.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zc.entity.Announcement;
import com.zc.entity.Student;
import com.zc.entity.StudentTaskBookOpening;
import com.zc.entity.Teacher;
import com.zc.entity.ThesisPaper;
import com.zc.entity.ThesisTitle;
import com.zc.entity.User;
import com.zc.service.IAnnouncementService;
import com.zc.service.IDepartmentService;
import com.zc.service.IMajorService;
import com.zc.service.IStudentService;
import com.zc.service.ITeacherService;




@Controller
@RequestMapping(value="/admin")
public class AdminContraller {
	
	@Autowired
	private ITeacherService teacherService;
	
	@Autowired
	private IDepartmentService departmentService;
	
	@Autowired
	private IStudentService studentService;
	
	@Autowired
	private IMajorService majorService;
	
	@Autowired
	private IAnnouncementService announcementService;
	
	
	@RequestMapping(value="/main",method=RequestMethod.GET)
	public String adminMainForm() {
		return "admin/main.jsp";
	}
	
	@RequestMapping(value="/modifyPassword",method=RequestMethod.GET)
	public String adminModifyPassword() {
		return "admin/adminModifyPassword.jsp";
	}
	
	@RequestMapping(value="/teacherAdd",method=RequestMethod.GET)
	public String adminTeacherAdd() {
		return "admin/adminTeacherAdd.jsp";
	}
	
	@RequestMapping(value="/teacherManage",method=RequestMethod.GET)
	public String adminTeacherManage() {
		
		return "admin/adminTeacherManage.jsp";
	}
	
	@RequestMapping(value="/studentAdd",method=RequestMethod.GET)
	public String adminStudentAdd() {
		return "admin/adminStudentAdd.jsp";
	}
	
	@RequestMapping(value="/studentManage",method=RequestMethod.GET)
	public String adminStudentManage() {
		return "admin/adminStudentManage.jsp";
	}
	
	@RequestMapping(value="/shujufenxi",method=RequestMethod.GET)
	public String adminShujufenxi() {
		return "admin/adminShujufenxi.jsp";
	}
	
	@RequestMapping(value="/checkThesis",method=RequestMethod.GET)
	public String adminCheckThesis(Model model) {
		List<ThesisTitle> thesisList = teacherService.showAllThesisTitle();
		
		for(int i=0;i<thesisList.size();i++) {
			int status = thesisList.get(i).getStatus();
			if(status == 1) {
				thesisList.get(i).setStatusName("?????????");
			}else if(status == 2) {
				thesisList.get(i).setStatusName("????????????");
			}else {
				thesisList.get(i).setStatusName("?????????");
			}
		}
		
		model.addAttribute("thesisTitleList", thesisList);
		System.out.println("?????????????????????????????????"+thesisList);
		
		return "admin/adminCheckThesis.jsp";
	}
	
	@RequestMapping(value="/announcement")
	public String adminAnnouncement(Model model) {
		List<Announcement> list = announcementService.showAllAnnonucement();
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd"); 
		for(int i=0;i<list.size();i++) {
			Date time4db = list.get(i).getLastModifyTime();
			String formatTime = time.format(time4db);
			list.get(i).setTimeFormat(formatTime);
		}
		
		model.addAttribute("announcementList", list);
		
		return "admin/adminAnnouncement.jsp";
	}
	
	@RequestMapping(value="/publishAnnouncement",method=RequestMethod.GET)
	public String adminPublishAnnouncement() {
		return "admin/adminPublishAnnouncement.jsp";
	}
	
	@RequestMapping(value="/teacherAdd",method=RequestMethod.POST)
	public String addTeacher(HttpServletRequest request, String teacherNo, String teacherName,String sex,String phone,String email,String zhicheng,String department,Model model) throws Exception {
		teacherNo = new String(teacherNo.getBytes("iso-8859-1"),"utf-8");
		teacherName = new String(teacherName.getBytes("iso-8859-1"),"utf-8");
		sex = new String(sex.getBytes("iso-8859-1"),"utf-8");
		//String inputMan = (String) session.getAttribute("currentUser");
		
		User user = (User)request.getSession().getAttribute("currentUser");
		String inputMan = user.getUserNo();
		
		// String inputMan = (String) request.getSession().getAttribute("currentUser.userNo");
		// inputMan = new String(inputMan.getBytes("iso-8859-1"),"utf-8");
		phone = new String(phone.getBytes("iso-8859-1"),"utf-8");
		email = new String(email.getBytes("iso-8859-1"),"utf-8");
		zhicheng = new String(zhicheng.getBytes("iso-8859-1"),"utf-8");
		department = new String(department.getBytes("iso-8859-1"),"utf-8");
		
		if(teacherNo == null || "".equals(teacherNo) || teacherName == null || "".equals(teacherName)|| sex == null || "".equals(sex) || phone == null || "".equals(phone) || department == null || "".equals(department)  ) {
			model.addAttribute("message", "????????????????????????");
			return "admin/main.jsp";
		}else {
			Date currentTime = new Date();
			
			Teacher teacher = new Teacher();
			teacher.setTeacherNo(teacherNo);
			teacher.setTeacherName(teacherName);
			teacher.setDepartmentId(Integer.parseInt(department));
			teacher.setSex(sex);
			teacher.setInputMan(inputMan);
			teacher.setLastModifyTime(currentTime);
			teacher.setPhone(phone);
			teacher.setEmail(email);
			teacher.setZhicheng(zhicheng);
			
			int addNum = teacherService.addTeacher(teacher);
			// System.out.println("???????????????"+addNum);
			model.addAttribute("message", "??????????????????????????????");
			return "admin/adminTeacherAdd.jsp";
		}
		
		
		
	}
	
	@RequestMapping(value="/showAllTeacher")
	public String adminShowAllTeacher(Model model,HttpServletResponse response) throws Exception {
		List<Teacher> teachers = teacherService.showAllTeacher();
		for(int i=0;i<teachers.size();i++) {
			System.out.println(teachers.get(i));
			int depmentId = teachers.get(i).getDepartmentId();
			String departmentName = departmentService.getNameById(depmentId);
			teachers.get(i).setDepartmentName(departmentName);
		}
		
		
		model.addAttribute("teacherList", teachers);
		System.out.println("?????????????????????"+teachers);
		return "admin/adminTeacherManage.jsp";
	}
	
	/*@RequestMapping(value="/shujufenxi",method=RequestMethod.GET)
	public String adminShujuFenxi(Model model,HttpServletResponse response) throws Exception {
		List<StudentTaskBookOpening> scores = teacherService.showAllInfo();
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<scores.size();i++) {
			int temp=0;
			if(scores.get(i).getOpenscore().equals("A"))
				temp++;
			if(scores.get(i).getStudentKeXingXing().equals("A"))
				temp++;
			if(scores.get(i).getXuqiuscore().equals("A"))
				temp++;
			if(scores.get(i).getGaiyaoscore().equals("A"))
				temp++;
			if(scores.get(i).getShujukuscore().equals("A"))
				temp++;
			if(temp==5) {
				list.set(4, list.get(4)+1);
			}
			if(temp==4) {
				list.set(3, list.get(3)+1);
			}
			if(temp==3) {
				list.set(2, list.get(2)+1);
			}
			if(temp==2) {
				list.set(1, list.get(1)+1);
			}
			if(temp==1) {
				list.set(0, list.get(0)+1);
			}
			System.out.println(list);
			
		}
		
		model.addAttribute("list", list);
		return "admin/adminShujufenxi.jsp";
	}*/
	
	@RequestMapping(value="/deleteTeacher")
	public String adminDeleteTeacher(int id,Model model) {
		// System.out.println(id);
		int num = teacherService.deleteTeacher(id);
		// System.out.println("?????????"+num+"????????????");
		model.addAttribute("message", "??????????????????????????????");
		return "admin/adminTeacherManage.jsp";
	}
	
	@RequestMapping(value="/modifyTeacher")
	public String adminModifyTeacher(int id,Model model) {
		Teacher teacher = teacherService.getTeacherByid(id);
		
		String teacherNo = teacher.getTeacherNo();
		String teacherName = teacher.getTeacherName();
		int depId = teacher.getDepartmentId();
		String departmentName = departmentService.getNameById(depId);
		String sex = teacher.getSex();
		String phone = teacher.getPhone();
		String email = teacher.getEmail();
		String zhicheng = teacher.getZhicheng();
		
		model.addAttribute("id", id);
		model.addAttribute("teacherNo", teacherNo);
		model.addAttribute("teacherName", teacherName);
		model.addAttribute("departmentName", departmentName);
		model.addAttribute("sex", sex);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("zhicheng", zhicheng);
		return "admin/adminTeacherModify.jsp";
	}
	
	@RequestMapping(value="/modifyTeacherToDb")
	public String adminModifyTeacherToDb(int id,Model model,HttpServletRequest request, String teacherNo,String departmentOld, String teacherName,String sex,String phone,String email,String zhicheng,String department) throws Exception {
		// System.out.println(id);
		
		int departmentId = 0;
		departmentOld = new String(departmentOld.getBytes("iso-8859-1"),"utf-8");
		departmentId =  departmentService.getIdByName(departmentOld);
		
		teacherNo = new String(teacherNo.getBytes("iso-8859-1"),"utf-8");
		teacherName = new String(teacherName.getBytes("iso-8859-1"),"utf-8");
		sex = new String(sex.getBytes("iso-8859-1"),"utf-8");
		User user = (User)request.getSession().getAttribute("currentUser");
		String inputMan = user.getUserNo();
		phone = new String(phone.getBytes("iso-8859-1"),"utf-8");
		email = new String(email.getBytes("iso-8859-1"),"utf-8");
		zhicheng = new String(zhicheng.getBytes("iso-8859-1"),"utf-8");
		department = new String(department.getBytes("iso-8859-1"),"utf-8");
		Date currentTime = new Date();
		
		Teacher teacher = new Teacher();
		teacher.setId(id);
		teacher.setTeacherNo(teacherNo);
		teacher.setTeacherName(teacherName);
		if (department == null || "".equals(department)) {
			teacher.setDepartmentId(departmentId);
		}else {
			teacher.setDepartmentId(Integer.parseInt(department));
		}
		teacher.setSex(sex);
		teacher.setInputMan(inputMan);
		teacher.setLastModifyTime(currentTime);
		teacher.setPhone(phone);
		teacher.setEmail(email);
		teacher.setZhicheng(zhicheng);
		
		int num = teacherService.updateTeacher(teacher);
		System.out.println("???????????????"+num);
		return "forward:showAllTeacher";
	}
	
	
	@RequestMapping(value="/showTeacherOne",method=RequestMethod.POST)
	public String adminShowTeacherOne(Model model,HttpServletResponse response, @RequestParam(value="teacherNo",required=false) String teacherNo,@RequestParam(value="teacherName",required=false) String teacherName) throws Exception {
		
		if( ("".equals(teacherNo) || teacherNo == null) && ("".equals(teacherName) || teacherName == null) ) {
			adminShowAllTeacher(model, response);
		}else if((!"".equals(teacherNo) || teacherNo != null) &&("".equals(teacherName) || teacherName == null) ) {
			List<Teacher> teachers = teacherService.showTeacherOne1(teacherNo);
			if(teachers.isEmpty()) {
				model.addAttribute("showMessage", "???????????????????????????????????????");
			}else {
				for(int i=0;i<teachers.size();i++) {
					
					System.out.println(teachers.get(i));
					int depmentId = teachers.get(i).getDepartmentId();
					String departmentName = departmentService.getNameById(depmentId);
					teachers.get(i).setDepartmentName(departmentName);
				}
			}
			model.addAttribute("teacherList", teachers);
			System.out.println("???????????????"+teachers);
			return "admin/adminTeacherManage.jsp";
		}else if(("".equals(teacherNo) || teacherNo == null) && (!"".equals(teacherName) || teacherName != null)) {
			teacherName = new String(teacherName.getBytes("iso-8859-1"),"utf-8");
			List<Teacher> teachers = teacherService.showTeacherOne2(teacherName);
			if(teachers.isEmpty()) {
				model.addAttribute("showMessage", "???????????????????????????????????????");
			}else {
				for(int i=0;i<teachers.size();i++) {
					System.out.println(teachers.get(i));
					int depmentId = teachers.get(i).getDepartmentId();
					String departmentName = departmentService.getNameById(depmentId);
					teachers.get(i).setDepartmentName(departmentName);
				}
			}
			model.addAttribute("teacherList", teachers);
			System.out.println("???????????????"+teachers);
			return "admin/adminTeacherManage.jsp";
		} else {
			teacherName = new String(teacherName.getBytes("iso-8859-1"),"utf-8");
			teacherNo = new String(teacherNo.getBytes("iso-8859-1"),"utf-8");
			List<Teacher> teachers = teacherService.showTeacherOne3(teacherNo, teacherName);
			if(teachers.isEmpty()) {
				model.addAttribute("showMessage", "???????????????????????????????????????");
			}else {
				for(int i=0;i<teachers.size();i++) {
					System.out.println(teachers.get(i));
					int depmentId = teachers.get(i).getDepartmentId();
					String departmentName = departmentService.getNameById(depmentId);
					teachers.get(i).setDepartmentName(departmentName);
				}
			}
			
			model.addAttribute("teacherList", teachers);
			System.out.println("???????????????"+teachers);
			return "admin/adminTeacherManage.jsp";
		}
		return "admin/adminTeacherManage.jsp";
	}
	
	@RequestMapping(value="/studentAdd",method=RequestMethod.POST)
	public String addStudent(HttpServletRequest request,String studentNo, String studentName,String sex,String grade,String phone,String email,String major,Model model) throws Exception {
		studentNo = new String(studentNo.getBytes("iso-8859-1"),"utf-8");
		studentName = new String(studentName.getBytes("iso-8859-1"),"utf-8");
		sex = new String(sex.getBytes("iso-8859-1"),"utf-8");
		grade = new String(grade.getBytes("iso-8859-1"),"utf-8");
		
		User user = (User)request.getSession().getAttribute("currentUser");
		String inputMan = user.getUserNo();
		
		//inputMan = new String(inputMan.getBytes("iso-8859-1"),"utf-8");
		
		
		phone = new String(phone.getBytes("iso-8859-1"),"utf-8");
		email = new String(email.getBytes("iso-8859-1"),"utf-8");
		major = new String(major.getBytes("iso-8859-1"),"utf-8");
		if(studentNo== null || "".equals(studentNo)||studentName == null || "".equals(studentName) || sex==null ||"".equals(sex) || grade == null || "".equals(grade) || phone == null || "".equals(phone) || major==null ||"".equals(major)) {
			model.addAttribute("message", "????????????????????????");
			return "admin/main.jsp";
			
		}else {
			Date currentTime = new Date();
			
			Student student = new Student();
			student.setStudentNo(studentNo);
			student.setStudentName(studentName);
			student.setSex(sex);
			student.setGrade(grade);
			student.setInputMan(inputMan);
			student.setPhone(phone);
			student.setEmail(email);
			student.setMajorId(Integer.parseInt(major));
			student.setLastModifyTime(currentTime);
			
			int addNum = studentService.addStudent(student);
			// System.out.println("???????????????"+addNum);
			
			model.addAttribute("message", "??????????????????????????????");
			
			return "admin/adminStudentAdd.jsp";
		}
		
		
		
		
	}
	
	@RequestMapping(value="/showAllStudent")
	public String adminShowAllStudent(Model model,HttpServletResponse response) throws Exception {
		List<Student> students = studentService.showAllStudent();
		for(int i=0;i<students.size();i++) {
			System.out.println(students.get(i));
			int majorId = students.get(i).getMajorId();
			String majorName = majorService.getNameById(majorId);
			students.get(i).setMajorName(majorName);
		}
		
		
		model.addAttribute("studentList", students);
		System.out.println("?????????????????????"+students);
		return "admin/adminStudentManage.jsp";
	}
	
	@RequestMapping(value="/modifyStudent")
	public String adminModifyStudent(int id,Model model) {
		
		Student student = studentService.getStudentNameById(id);
		String studentNo = student.getStudentNo();
		String studentName= student.getStudentName();
		int majorId = student.getMajorId();
		String majorName = majorService.getNameById(majorId);
		String grade = student.getGrade();
		String sex = student.getSex();
		String phone = student.getPhone();
		String email = student.getEmail();
		
		
		model.addAttribute("id", id);
		model.addAttribute("studentNo", studentNo);
		model.addAttribute("studentName", studentName);
		model.addAttribute("grade", grade);
		model.addAttribute("majorName", majorName);
		model.addAttribute("sex", sex);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		return "admin/adminStudentModify.jsp";
	}
	
	@RequestMapping(value="/modifyStudentToDb")
	public String adminModifyStudentToDb(int id,Model model,HttpServletRequest request, String studentNo,String majorOld, String studentName,String sex,String phone,String email,String major,String grade) throws Exception {
		// System.out.println(id);
		
		int majorId = 0;
		majorOld = new String(majorOld.getBytes("iso-8859-1"),"utf-8");
		majorId = majorService.getIdByName(majorOld);
		
		studentNo = new String(studentNo.getBytes("iso-8859-1"),"utf-8");
		studentName = new String(studentName.getBytes("iso-8859-1"),"utf-8");
		sex = new String(sex.getBytes("iso-8859-1"),"utf-8");
		grade = new String(grade.getBytes("iso-8859-1"),"utf-8");
		User user = (User)request.getSession().getAttribute("currentUser");
		String inputMan = user.getUserNo();
		phone = new String(phone.getBytes("iso-8859-1"),"utf-8");
		email = new String(email.getBytes("iso-8859-1"),"utf-8");
		major = new String(major.getBytes("iso-8859-1"),"utf-8");
		Date currentTime = new Date();
		
		/*Teacher teacher = new Teacher();
		teacher.setId(id);
		teacher.setTeacherNo(teacherNo);
		teacher.setTeacherName(teacherName);
		if (major == null || "".equals(major)) {
			teacher.setDepartmentId(departmentId);
		}else {
			teacher.setDepartmentId(Integer.parseInt(department));
		}
		teacher.setSex(sex);
		teacher.setInputMan(inputMan);
		teacher.setLastModifyTime(currentTime);
		teacher.setPhone(phone);*/
		
		Student student = new Student();
		student.setId(id);
		student.setStudentNo(studentNo);
		student.setStudentName(studentName);
		if(major==null || "".equals(major)) {
			student.setMajorId(majorId);
		} else {
			student.setMajorId(Integer.parseInt(major));
		}
		student.setSex(sex);
		student.setInputMan(inputMan);
		student.setLastModifyTime(currentTime);
		student.setPhone(phone);
		student.setEmail(email);
		student.setGrade(grade);
		
		int num = studentService.updateStudent(student);
		
		System.out.println("???????????????"+num);
		return "forward:showAllStudent";
	}
	
	@RequestMapping(value="/deleteStudent")
	public String adminDeleteStudent(int id,Model model) {
		// System.out.println(id);
		int num = studentService.deleteStudent(id);
		//System.out.println("?????????"+num+"????????????");
		model.addAttribute("message", "??????????????????????????????");
		return "admin/adminStudentManage.jsp";
	}
	
	@RequestMapping(value="/showStudentOne",method=RequestMethod.POST)
	public String adminShowStudentOne(Model model,HttpServletResponse response, @RequestParam(value="studentNo",required=false) String studentNo,@RequestParam(value="studentName",required=false) String studentName) throws Exception {
		
		if( ("".equals(studentNo) || studentNo == null) && ("".equals(studentName) || studentName == null) ) {
			adminShowAllStudent(model, response);
		}else if((!"".equals(studentNo) || studentNo != null) &&("".equals(studentName) || studentName == null) ) {
			List<Student> students = studentService.showStudentOne1(studentNo);
			if(students.isEmpty()) {
				model.addAttribute("showMessage", "???????????????????????????????????????");
			}else {
				for(int i=0;i<students.size();i++) {
					
					int mojarId = students.get(i).getMajorId();
					String majorName= majorService.getNameById(mojarId);
					students.get(i).setMajorName(majorName);
				}
			}
			model.addAttribute("studentList", students);
			System.out.println("???????????????"+students);
			return "admin/adminStudentManage.jsp";
		}else if(("".equals(studentNo) || studentNo == null) && (!"".equals(studentName) || studentName != null)) {
			studentName = new String(studentName.getBytes("iso-8859-1"),"utf-8");
			List<Student> students = studentService.showStudentOne2(studentName);
			if(students.isEmpty()) {
				model.addAttribute("showMessage", "???????????????????????????????????????");
			}else {
				for(int i=0;i<students.size();i++) {
					int mojarId = students.get(i).getMajorId();
					String majorName= majorService.getNameById(mojarId);
					students.get(i).setMajorName(majorName);
				}
			}
			model.addAttribute("studentList", students);
			System.out.println("???????????????"+students);
			return "admin/adminStudentManage.jsp";
		} else {
			studentName = new String(studentName.getBytes("iso-8859-1"),"utf-8");
			studentNo = new String(studentNo.getBytes("iso-8859-1"),"utf-8");
			List<Student> students = studentService.showStudentOne3(studentNo, studentName);
			if(students.isEmpty()) {
				model.addAttribute("showMessage", "???????????????????????????????????????");
			}else {
				for(int i=0;i<students.size();i++) {
					int mojarId = students.get(i).getMajorId();
					String majorName= majorService.getNameById(mojarId);
					students.get(i).setMajorName(majorName);
				}
			}
			
			model.addAttribute("studentList", students);
			System.out.println("???????????????"+students);
			return "admin/adminStudentManage.jsp";
		}
		return "admin/adminStudentManage.jsp";
	}
	
	
	@RequestMapping(value="/agreeThesis")
	public String agreeThesis(int id,Model model) {
		
		int num = teacherService.agreeThesisTitle(id);
		System.out.println("???????????????");
		adminCheckThesis(model);
		return "admin/adminCheckThesis.jsp";
	}
	
	@RequestMapping(value="/disagreeThesis")
	public String disgreeThesis(int id,Model model) {
		
		int num = teacherService.disagreeThesisTitle(id);
		System.out.println("?????????????????????");
		adminCheckThesis(model);
		return "admin/adminCheckThesis.jsp";
	}
	
	@RequestMapping(value="/publishAnnouncement")
	public String adminPublishAnnouncement(HttpServletRequest request,Model model,String announcement) throws Exception {
		announcement = new String(announcement.getBytes("iso-8859-1"),"utf-8");
		
		if(announcement == null|| "".equals(announcement)) {
			
			model.addAttribute("message", "???????????????????????????");
			return "admin/main.jsp";
		}else {
			Announcement an = new Announcement();
			an.setContext(announcement);
			an.setInputMan("admin");
			Date time = new Date();
			an.setLastModifyTime(time);
			
			int num = announcementService.addAnnouncement(an);
			System.out.println("????????????"+num+"???");
			model.addAttribute("message", "??????????????????????????????");
			adminAnnouncement(model);
			
			return "admin/adminAnnouncement.jsp";
		}
		
	}
	
	@RequestMapping(value="/deleteAnnouncement")
	public String adminDeleteAnnouncement(Model model,int id) throws Exception {
		int num = announcementService.deleteAnnouncement(id);
		System.out.println("????????????"+num+"???");
		
		model.addAttribute("message", "?????????????????????");
		adminAnnouncement(model);
		return "admin/adminAnnouncement.jsp";
	}
	
	@RequestMapping(value="/showThesisPaper")
	public String adminShowThesisPaper(Model model) {
		List<ThesisPaper> paperList = teacherService.getAllPaper();
		List<Student> studentList = new ArrayList<Student>();
		Student s = new Student();
		for(int i=0;i<paperList.size();i++) {
			int studentId = paperList.get(i).getStudentId();
			String studentName = studentService.getStudentNameById(studentId).getStudentName();
			int thesisId = teacherService.getTopicInfoByStudentId(studentId).getThesisId();
			String thesisName = teacherService.getThesisById(thesisId).getThesisName();
			String filePath = paperList.get(i).getPaperInfo();
			String[] str = filePath.split("\\\\");
			String fileName = str[str.length-1].toString();
			s.setStudentName(studentName);
			s.setThesisName(thesisName);
			s.setFileName(fileName);
			s.setFilePath(filePath);
			studentList.add(s);
		}
		model.addAttribute("studentList", studentList);
		return "admin/adminThesisPaperResult.jsp";
	}
	
	@RequestMapping(value="/fileDownload")
	public ResponseEntity<byte[]> fileDownload(HttpServletRequest request, @RequestParam("filePath") String filePath,@RequestParam("fileName") String fileName, Model model) throws Exception {
		fileName = new String(fileName.getBytes("iso-8859-1"),"utf-8");
		filePath = new String(filePath.getBytes("iso-8859-1"),"utf-8");
		File file = new File(filePath);
		HttpHeaders headers = new HttpHeaders();
		String downloadFile = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		headers.setContentDispositionFormData("attachment", downloadFile);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED); 
	}
	
}
