package com.zc.web;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zc.entity.Announcement;
import com.zc.entity.Doubt;
import com.zc.entity.Student;
import com.zc.entity.StudentScore;
import com.zc.entity.StudentTaskBookOpening;
import com.zc.entity.TeacherProgress;
import com.zc.entity.TeacherTaskBookOpening;
import com.zc.entity.ThesisInformation;
import com.zc.entity.ThesisTitle;
import com.zc.entity.Topic;
import com.zc.entity.User;
import com.zc.entity.Zhiyuan;
import com.zc.service.IMajorService;
import com.zc.service.IStudentService;
import com.zc.service.ITeacherService;



@Controller
@RequestMapping(value="/student")
public class StudentController {

	@Autowired
	private IStudentService studentService;
	
	@Autowired
	private IMajorService majorService;
	
	@Autowired
	private ITeacherService teacherService;
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String addStudentForm() {
		return "student/addStudent.jsp";
	}
	
	private String realTimeTopicMessageOn = "";
	
	// ?????????
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addStudent(HttpServletRequest request,String studentNo, String studentName,String sex,String grade ,String inputMan,String phone,String major,Model model) throws Exception {
		studentNo = new String(studentNo.getBytes("iso-8859-1"),"utf-8");
		studentName = new String(studentName.getBytes("iso-8859-1"),"utf-8");
		sex = new String(sex.getBytes("iso-8859-1"),"utf-8");
		grade = new String(grade.getBytes("iso-8859-1"),"utf-8");
		inputMan = new String(inputMan.getBytes("iso-8859-1"),"utf-8");
		phone = new String(phone.getBytes("iso-8859-1"),"utf-8");
		major = new String(major.getBytes("iso-8859-1"),"utf-8");
		Date currentTime = new Date();
		
		Student student = new Student();
		student.setStudentNo(studentNo);
		student.setStudentName(studentName);
		student.setSex(sex);
		student.setGrade(grade);
		student.setInputMan(inputMan);
		student.setPhone(phone);
		student.setMajorId(Integer.parseInt(major));
		student.setLastModifyTime(currentTime);
		
		int addNum = studentService.addStudent(student);
		System.out.println("???????????????"+addNum);
		
		return "student/addSuccess.jsp";
	}
	
	@RequestMapping(value="/info",method=RequestMethod.GET)
	public String studentInfo() {
		return "student/studentPersionalInformation.jsp";
	}
	
	@RequestMapping(value="/main",method=RequestMethod.GET)
	public String studentMainForm() {
		return "student/main.jsp";
	}
	
	@RequestMapping(value="/modifyInfo",method=RequestMethod.GET)
	public String studentMofidyInfoForm() {
		return "student/studentModifyInfo.jsp";
	}
	
	@RequestMapping(value="/modifyPassword",method=RequestMethod.GET)
	public String studentMofidyPasswordForm() {
		return "student/studentModifyPassword.jsp";
	}
	
	@RequestMapping(value="/thesis",method=RequestMethod.GET)
	public String studentThesis(HttpServletResponse response,HttpServletRequest request,Model model) {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		String userNo = currentUser.getUserNo();
		//??????????????????
		List<ThesisTitle> thesisList = teacherService.showAllThesisTitle();
		
		Student student = studentService.getStudentByNO(userNo);
		int studentId = student.getId();
		Topic topic = studentService.chosenThesisTitle(studentId);
		//ThesisTitle title = teacherService.getThesisInfoByThesisId(topic.getThesisId());
		
		//ThesisInformation topic2 = studentService.getInfoByStudentId(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("thesisTitleList", thesisList);
			System.out.println("????????????????????????"+thesisList);
			return "student/studentThesis.jsp";
		}else {
			System.out.println(topic);
			model.addAttribute("topicMessage", "?????????????????????????????????");
			//model.addAttribute("Message", title.getThesisName());
			
			return "student/main.jsp";
		}
		
		
		
	}
	
	@RequestMapping(value="/thesisResult",method=RequestMethod.GET)
	public String studentThesisResult(HttpServletResponse response,HttpServletRequest request,Model model) {
		User currnetUser = (User)request.getSession().getAttribute("currentUser");
		String studentNo = currnetUser.getUserNo();
		Student student = studentService.getStudentByNO(studentNo);
		int studentId = student.getId();
		Topic topic = studentService.chosenThesisTitle(studentId);
		Zhiyuan zhiyuan = studentService.chosenZhiyuan(studentId);
		if(topic == null || "".equals(topic)) {
			if(zhiyuan == null || "".equals(zhiyuan)) {
				model.addAttribute("topicMessage", "??????????????????");
				model.addAttribute("realTimeTopicMessage", realTimeTopicMessageOn);
			} else {
				model.addAttribute("topicMessage", "???????????????????????????????????????");
				model.addAttribute("realTimeTopicMessage", realTimeTopicMessageOn);
			}
			
			return "student/main.jsp";
		}else {
			int thesisId = topic.getThesisId();
			
			ThesisTitle theisTitle = teacherService.getThesisById(thesisId);
			String topicName = theisTitle.getThesisName();
			String inputMan = theisTitle.getInputMan();
			String nandu = theisTitle.getNandu();
			String liang = theisTitle.getLiang();
			String from = theisTitle.getFrom();
			String leixing = theisTitle.getLeixing();
			String description = theisTitle.getDescription();
			
			model.addAttribute("topicName", topicName);
			model.addAttribute("inputMan", inputMan);
			model.addAttribute("nandu", nandu);
			model.addAttribute("liang", liang);
			model.addAttribute("from", from);
			model.addAttribute("leixing", leixing);
			model.addAttribute("description", description);
			
			model.addAttribute("realTimeTopicMessage", realTimeTopicMessageOn);
			
			return "student/studentThesisResult.jsp";
		}
		
	}
	
	@RequestMapping(value="/viewTaskOpening")
	public String studentViewTaskOpening(HttpServletRequest request,Model model) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "??????????????????");
			return "student/studentViewTaskBookAndOpening.jsp";
		}else {
			int thesisId = topic.getThesisId();
			TeacherTaskBookOpening ttbo = studentService.getFilePathByThesisId(thesisId);
			if(ttbo==null || "".equals(ttbo)) {
				return "student/studentViewTaskBookAndOpening.jsp";
			}else {
				// ????????????????????????????????????
				String taskBookPath = ttbo.getTaskBook();
				String openingPath = ttbo.getOpeningReport();
				String keXingXingPath = ttbo.getKeXingXing();
				String xuQiuPath = ttbo.getXuQiu();
				String gaiYaoPath = ttbo.getGaiYao();
				String shuJuKuPath = ttbo.getShuJuKu();
				
				Map<String, String> fileList = new HashMap<String, String>();

				if(taskBookPath == null || "".equals(taskBookPath)) {
					
				}else {
					String[] str1 = taskBookPath.split("\\\\");
					String taskBookName = str1[str1.length-1].toString();
					fileList.put(taskBookName, taskBookPath);
				}
				
				if(openingPath == null || "".equals(openingPath)) {
					
				}else {
					String[] str2 = openingPath.split("\\\\");
					String openingName = str2[str2.length-1].toString();
					fileList.put(openingName, openingPath);
				}
				
				if(keXingXingPath == null || "".equals(keXingXingPath)) {
					
				}else {
					String[] str2 = keXingXingPath.split("\\\\");
					String keXingXingName = str2[str2.length-1].toString();
					fileList.put(keXingXingName, keXingXingPath);
				}
				
				if(xuQiuPath == null || "".equals(xuQiuPath)) {
					
				}else {
					String[] str2 = xuQiuPath.split("\\\\");
					String xuQiuName = str2[str2.length-1].toString();
					fileList.put(xuQiuName, xuQiuPath);
				}
				
				if(gaiYaoPath == null || "".equals(gaiYaoPath)) {
					
				}else {
					String[] str2 = gaiYaoPath.split("\\\\");
					String gaiYaoName = str2[str2.length-1].toString();
					fileList.put(gaiYaoName, gaiYaoPath);
				}
				
				if(shuJuKuPath == null || "".equals(shuJuKuPath)) {
					
				}else {
					String[] str2 = shuJuKuPath.split("\\\\");
					String shuJuKuName = str2[str2.length-1].toString();
					fileList.put(shuJuKuName, shuJuKuPath);
				}
				
				model.addAttribute("fileList", fileList);
				
				return "student/studentViewTaskBookAndOpening.jsp";
			}
			
		}
		
		
	}
	
	@RequestMapping(value="/sectionTask")
	public String studentSectionTask(HttpServletRequest request,Model model) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		List<TeacherProgress> progresses = studentService.getTeacherProgressByStudentId(studentId);
		
		for(int i=0;i<progresses.size();i++) {
			int state = progresses.get(i).getState();
			if(state ==0) {
				progresses.get(i).setStateName("??????????????????");
			}else if(state ==1) {
				progresses.get(i).setStateName("?????????");
			}else {
				progresses.get(i).setStateName("?????????");
			}
			
		}
		
		
		model.addAttribute("progressList", progresses);
		
		return "student/studentSectionTask.jsp";
	}
	
	@RequestMapping(value="/uploadFile",method=RequestMethod.GET)
	public String studentUploadFile() {
		return "student/studentUploadFile.jsp";
	}
	
	@RequestMapping(value="/resourcesDownload")
	public String studentResourcesDownload(HttpServletRequest request,Model model) {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		
		Map<String, String> fileList = studentService.getTaskBookOpeningToMap(studentId);
		if(fileList.isEmpty()) {
			return "student/studentResourcesDownload.jsp";
		}else {
			ThesisInformation thesisInformation4Db = studentService.getInfoByStudentId(studentId);
			if(thesisInformation4Db ==null) {
				
			}else {
				String filePath = thesisInformation4Db.getThesisText();
				String[] str = filePath.split("\\\\");
				String fileName = str[str.length-1].toString();
				fileList.put(fileName, filePath);
			}
			
			model.addAttribute("fileList", fileList);
			return "student/studentResourcesDownload.jsp";
		}
	}
	
	@RequestMapping(value="/announcement")
	public String studentAnnouncement(Model model) {
		List<Announcement> list = studentService.showAllAnnouncement();
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd"); 
		for(int i=0;i<list.size();i++) {
			Date time4db = list.get(i).getLastModifyTime();
			String formatTime = time.format(time4db);
			list.get(i).setTimeFormat(formatTime);
		}
		
		model.addAttribute("announcementList", list);
		
		return "student/studentAnnouncement.jsp";
	}
	
	@RequestMapping(value="/score")
	public String studentScore(HttpServletRequest request,Model model) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		StudentScore dbScore = teacherService.showInfoByStudentId(studentId);
		if(dbScore == null || "".equals(dbScore)) {
			model.addAttribute("message", "????????????");
			return "student/main.jsp";
			
		}else {
			String studentName = teacherService.getStudentInfoByStudentId(studentId).getStudentName();
			int thesisId = teacherService.getTopicInfoByStudentId(studentId).getThesisId();
			String thesisName = teacherService.getThesisById(thesisId).getThesisName();
			int score = teacherService.showInfoByStudentId(studentId).getThesisResult();
			Student student = new Student();
			student.setStudentName(studentName);
			student.setThesisName(thesisName);
			student.setThesisScore(score);
			
			model.addAttribute("studentList", student);
			
			return "student/studentScore.jsp";
		}
		
	}
	
	@RequestMapping(value="/modifyInfoToDb",method=RequestMethod.POST)
	public String studentModifyInfoToDb(Model model,HttpServletRequest request,int id,String studentNo, String studentName,String sex,String majorOld,String major,String grade, String phone, String email) throws Exception {
		
		// ???????????????????????????????????????
		int majorId = 0;
		majorOld = new String(majorOld.getBytes("iso-8859-1"),"utf-8");
		//departmentId =  departmentService.getIdByName(departmentOld);
		majorId = majorService.getIdByName(majorOld);
		studentNo = new String(studentNo.getBytes("iso-8859-1"),"utf-8");
		studentName = new String(studentName.getBytes("iso-8859-1"),"utf-8");
		grade = new String(grade.getBytes("iso-8859-1"),"utf-8");
		sex = new String(sex.getBytes("iso-8859-1"),"utf-8");
		User user = (User)request.getSession().getAttribute("currentUser");
		String inputMan = user.getUserNo();
		phone = new String(phone.getBytes("iso-8859-1"),"utf-8");
		email = new String(email.getBytes("iso-8859-1"),"utf-8");
		major = new String(major.getBytes("iso-8859-1"),"utf-8");
		Date currentTime = new Date();
		
		
		Student student = new Student();
		student.setId(id);
		student.setStudentNo(studentNo);
		student.setStudentName(studentName);
		
		if (major == null || "".equals(major)) {
			student.setMajorId(majorId);
		}else {
			student.setMajorId(Integer.parseInt(major));
		}
		student.setGrade(grade);
		student.setSex(sex);
		student.setInputMan(inputMan);
		student.setLastModifyTime(currentTime);
		student.setPhone(phone);
		student.setEmail(email);
		
		int num = studentService.updateStudent(student);
		System.out.println("?????????????????????"+num);
		
		// ?????? ??????id ????????????name
		int majId = student.getMajorId();
		String majorNameNew = majorService.getNameById(majId);
		student.setMajorName(majorNameNew);
		
		HttpSession session = request.getSession();
		session.setAttribute("student", student);
		
		return "student/main.jsp";
	}
	
	@RequestMapping(value="/getAllAvailableTopic")
	public void studentGetAllAvailableTopicForm(HttpServletResponse response,HttpServletRequest request) throws Exception {
		
		List<ThesisTitle> thesisList = studentService.availableTopic();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter write = response.getWriter();
		write.write(JSONArray.toJSONString(thesisList));
		write.close();
	}
	
	@RequestMapping(value="/selectTopic")
	public String studentSelectTopic(HttpServletResponse response,HttpServletRequest request,Model model,int id,int topic) throws Exception {
		
		/*System.out.println("id:"+id);
		System.out.println("topic:"+topic);*/
		Topic top = new Topic();
		top.setStudentId(id);
		top.setThesisId(topic);
		Date time = new Date();
		top.setSelectTime(time);
		
		int num = studentService.addTopicToDb(top);
		System.out.println("?????????"+num+"?????????");
		
		Student student = studentService.getStudentNameById(id);
		String studentNo = student.getStudentNo();
		
		ThesisTitle thesis = studentService.getThesisInfoByid(topic);
		String thesisName = thesis.getThesisName();
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("?????????");
		sb.append(studentNo);
		sb.append("?????????????????????");
		sb.append(thesisName);
		
		
		String infoMessage = sb.toString();
		
		this.realTimeTopicMessageOn = infoMessage;
		model.addAttribute("realTimeTopicMessage", realTimeTopicMessageOn);
		
		studentThesisResult(response, request, model);
		return "student/studentThesisResult.jsp";
	}
	
	@RequestMapping(value="/selectThesis")
	public String studentSelectThesis(HttpServletResponse response,HttpServletRequest request,Model model,int id,int thesis) throws Exception {
		
		/*System.out.println("id:"+id);
		System.out.println("topic:"+topic);*/
		Zhiyuan zhi = studentService.chosenZhiyuan(id);
		if(zhi == null || "".equals(zhi)) {
			Zhiyuan zhiyuan = new Zhiyuan();
			zhiyuan.setStudentId(id);
			zhiyuan.setThesisId(thesis);
			Date time = new Date();
			zhiyuan.setSelectTime(time);
			
			int num = studentService.addZhiyuanToDb(zhiyuan);
			System.out.println("?????????"+num+"?????????");
			
			Student student = studentService.getStudentNameById(id);
			String studentNo = student.getStudentNo();
			
			ThesisTitle thesis1 = studentService.getThesisInfoByid(thesis);
			String thesisName = thesis1.getThesisName();
			
			
			StringBuffer sb = new StringBuffer();
			sb.append("?????????");
			sb.append(studentNo);
			sb.append("?????????????????????");
			sb.append(thesisName);
			
			
			String infoMessage = sb.toString();
			
			this.realTimeTopicMessageOn = infoMessage;
			model.addAttribute("message", realTimeTopicMessageOn);
			
			studentThesisResult(response, request, model);
			return "student/main.jsp";
		}else {
			model.addAttribute("message", "????????????????????????????????????");
			return "student/main.jsp";
		}
		
	}
	
	@RequestMapping(value="/deleteChosenTopic")
	public String studentDeleteChosenTopic(Model model,int studentId) throws Exception {
		
		// System.out.println(studentId);
		StudentTaskBookOpening stbo = studentService.getSTBOInfoById(studentId);
		if(stbo==null||"".equals(stbo)) {
			int num = studentService.deleteTopic(studentId); 
			System.out.println("???????????? :"+num+"?????????");
			model.addAttribute("message", "????????????");
			
			return "student/main.jsp";
		}else {
			model.addAttribute("message", "????????????????????????????????????");
			return "student/main.jsp";
		}
	}
	
	@RequestMapping(value="/fileDownload")
	public ResponseEntity<byte[]> fileDownload(HttpServletRequest request, @RequestParam("filePath") String filePath,@RequestParam("fileName") String fileName, Model model) throws Exception {
		System.out.println(fileName);
		System.out.println(filePath);
		//fileName = new String(fileName.getBytes("iso-8859-1"),"utf-8");
		//filePath = new String(filePath.getBytes("iso-8859-1"),"utf-8");
		File file = new File(filePath);
		HttpHeaders headers = new HttpHeaders();
		String downloadFile = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		headers.setContentDispositionFormData("attachment", downloadFile);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED); 
	}
	
	@RequestMapping(value="/uploadTaskBook")
	public String studentUploadTaskBook(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		if(!file.isEmpty()) {
			
			File fileRoot = new File("E:\\BSM\\student");
			File fileDb = new File(fileRoot, studentIdToString);
			String fileName = file.getOriginalFilename();
			
			File filePath = new File(fileDb, fileName);
			
			if(!filePath.getParentFile().exists()) {
				filePath.getParentFile().mkdirs();
			}
			
			file.transferTo(new File(fileDb+File.separator+fileName));
			
			int num = studentService.uploadTaskBook(studentId, filePath.toString());
			System.out.println("?????????"+num+"?????????");
			model.addAttribute("message", "?????????????????????");
			return "student/main.jsp";
		}else {
			model.addAttribute("message", "?????????????????????");
			return "error.jsp";
		}
	}
	
	@RequestMapping(value="/uploadOpening")
	public String studentUploadOpening(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "????????????????????????");
			return "student/main.jsp";
		}else {
			if(!file.isEmpty()) {
				
				File fileRoot = new File("E:\\BSM\\student");
				File fileDb = new File(fileRoot, studentIdToString);
				String fileName = file.getOriginalFilename();
				
				File filePath = new File(fileDb, fileName);
				
				if(!filePath.getParentFile().exists()) {
					filePath.getParentFile().mkdirs();
				}
				
				file.transferTo(new File(fileDb+File.separator+fileName));
				
				int num = studentService.uploadOpening(studentId, filePath.toString());
				System.out.println("?????????"+num+"?????????");
				
				model.addAttribute("message", "????????????????????????");
				return "student/main.jsp";
			}else {
				model.addAttribute("message", "????????????????????????");
				return "student/main.jsp";
			}
		}
	}
	
	@RequestMapping(value="/uploadKexing")
	public String studentUploadKexing(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "?????????????????????????????????");
			return "student/main.jsp";
		}else {
			if(!file.isEmpty()) {
				
				File fileRoot = new File("E:\\BSM\\student");
				File fileDb = new File(fileRoot, studentIdToString);
				String fileName = file.getOriginalFilename();
				
				File filePath = new File(fileDb, fileName);
				
				if(!filePath.getParentFile().exists()) {
					filePath.getParentFile().mkdirs();
				}
				
				file.transferTo(new File(fileDb+File.separator+fileName));
				
				int num = studentService.uploadKexing(studentId, filePath.toString());
				System.out.println("?????????"+num+"?????????");
				
				model.addAttribute("message", "?????????????????????????????????");
				return "student/main.jsp";
			}else {
				model.addAttribute("message", "?????????????????????????????????");
				return "student/main.jsp";
			}
		}
	}
	
	@RequestMapping(value="/uploadXuqiu")
	public String studentUploadXuqiu(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "??????????????????????????????");
			return "student/main.jsp";
		}else {
			if(!file.isEmpty()) {
				
				File fileRoot = new File("E:\\BSM\\student");
				File fileDb = new File(fileRoot, studentIdToString);
				String fileName = file.getOriginalFilename();
				
				File filePath = new File(fileDb, fileName);
				
				if(!filePath.getParentFile().exists()) {
					filePath.getParentFile().mkdirs();
				}
				
				file.transferTo(new File(fileDb+File.separator+fileName));
				
				int num = studentService.uploadXuqiu(studentId, filePath.toString());
				System.out.println("?????????"+num+"?????????");
				
				model.addAttribute("message", "??????????????????????????????");
				return "student/main.jsp";
			}else {
				model.addAttribute("message", "??????????????????????????????");
				return "student/main.jsp";
			}
		}
	}
	
	@RequestMapping(value="/uploadGaiyao")
	public String studentUploadGaiyao(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "??????????????????????????????");
			return "student/main.jsp";
		}else {
			if(!file.isEmpty()) {
				
				File fileRoot = new File("E:\\BSM\\student");
				File fileDb = new File(fileRoot, studentIdToString);
				String fileName = file.getOriginalFilename();
				
				File filePath = new File(fileDb, fileName);
				
				if(!filePath.getParentFile().exists()) {
					filePath.getParentFile().mkdirs();
				}
				
				file.transferTo(new File(fileDb+File.separator+fileName));
				
				int num = studentService.uploadGaiyao(studentId, filePath.toString());
				System.out.println("?????????"+num+"?????????");
				
				model.addAttribute("message", "??????????????????????????????");
				return "student/main.jsp";
			}else {
				model.addAttribute("message", "??????????????????????????????");
				return "student/main.jsp";
			}
		}
	}
	
	@RequestMapping(value="/uploadShujuku")
	public String studentUploadShujuku(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "?????????????????????????????????");
			return "student/main.jsp";
		}else {
			if(!file.isEmpty()) {
				
				File fileRoot = new File("E:\\BSM\\student");
				File fileDb = new File(fileRoot, studentIdToString);
				String fileName = file.getOriginalFilename();
				
				File filePath = new File(fileDb, fileName);
				
				if(!filePath.getParentFile().exists()) {
					filePath.getParentFile().mkdirs();
				}
				
				file.transferTo(new File(fileDb+File.separator+fileName));
				
				int num = studentService.uploadShujuku(studentId, filePath.toString());
				System.out.println("?????????"+num+"?????????");
				
				model.addAttribute("message", "?????????????????????????????????");
				return "student/main.jsp";
			}else {
				model.addAttribute("message", "?????????????????????????????????");
				return "student/main.jsp";
			}
		}
	}
	
	@RequestMapping(value="/fileDelete")
	public String fileDelete(HttpServletRequest request,HttpServletResponse response, @RequestParam("filePath") String filePath,@RequestParam("fileName") String fileName, Model model) throws Exception {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		fileName = new String(fileName.getBytes("iso-8859-1"),"utf-8");
		filePath = new String(filePath.getBytes("iso-8859-1"),"utf-8");
		File deleteFile = new File(filePath);
		String message = "";
		boolean flag = false;
		if(deleteFile.exists()) {
			flag = deleteFile.delete();
			if(flag ) {
				message = "????????????";
			}else {
				message = "????????????";
			}
			
		}else {
			message = "???????????????";
		}
		
		ThesisInformation thesis = studentService.getInfoByFilePath(filePath);
		if(thesis== null || "".equals(thesis)) {
			
		}else {
			int num1 = studentService.deleteThesisInformation(studentId);
			System.out.println("????????????????????????");
		}
		
		StudentTaskBookOpening STBO = studentService.getInfoByTaskBookPath(filePath);
		if(STBO == null || "".equals(STBO)) {
			
		}else {
			int num = studentService.resetTaskBook(studentId);
			System.out.println("?????????????????????");
		}
		
		StudentTaskBookOpening STBO2 = studentService.getInfoByOpeningPath(filePath);
		if(STBO2==null || "".equals(STBO2) ) {
			
		}else {
			int num = studentService.resetOpening(studentId);
			System.out.println("????????????????????????");
		}
		
		model.addAttribute("message", "????????????????????????");
	
		return "student/main.jsp";
	}
	
	@RequestMapping(value="/openingResult")
	public String studentOpeningResult(HttpServletRequest request,Model model) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		StudentTaskBookOpening STBO = studentService.getSTBOInfoById(studentId);
		if(STBO == null || "".equals(STBO)) {
			model.addAttribute("message", "????????????????????????");
			return "student/studentOpeningResult.jsp";
		}else {
			int completion = STBO.getCompletion();
			if(completion == 0) {
				model.addAttribute("message", "????????????????????????????????????????????????");
			}else if(completion == 1) {
				model.addAttribute("message", "??????????????????????????????????????????????????????");
			}else {
				model.addAttribute("message", "?????????????????????");
			}
		}
		return "student/studentOpeningResult.jsp";
	}
	
	@RequestMapping(value="/wendangResult")
	public String studentwendangResult(HttpServletRequest request,Model model) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		StudentTaskBookOpening STBO = studentService.getSTBOInfoById(studentId);
		Map<String, String> wendangList = new HashMap<String, String>();
		wendangList.put("open", STBO.getOpenscore());
		wendangList.put("kexing", STBO.getKexingscore());
		wendangList.put("xuqiu", STBO.getXuqiuscore());
		wendangList.put("gaiyao", STBO.getGaiyaoscore());
		wendangList.put("shujuku", STBO.getShujukuscore());
/*		if(STBO == null || "".equals(STBO)) {
			model.addAttribute("message", "????????????????????????");
			return "student/studentOpeningResult.jsp";
		}else {
			int completion = STBO.getCompletion();
			if(completion == 0) {
				model.addAttribute("message", "????????????????????????????????????????????????");
			}else if(completion == 1) {
				model.addAttribute("message", "??????????????????????????????????????????????????????");
			}else {
				model.addAttribute("message", "?????????????????????");
			}
		}*/
		System.out.println(wendangList);
		if(wendangList==null) {
			model.addAttribute("message", "????????????????????????");
		}
		model.addAttribute("wendangList", wendangList);
		return "student/studentWendangResult.jsp";
	}
	
	@RequestMapping(value="/getRealTimeTopic")
	public void getRealTimeTopic(HttpServletResponse response,HttpServletRequest request) throws Exception {
		String message = (String)request.getSession().getServletContext().getAttribute("realTimeTopicMessage");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter write = response.getWriter();
		write.write(JSONObject.toJSONString(message));
		write.close();
	}
	
	@RequestMapping(value="/uploadThesisInformation")
	public String studentUploadThesisInformation(HttpServletRequest request, Model model,@RequestParam("file") MultipartFile file) throws Exception {
		
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		String studentIdToString = String.valueOf(studentId);
		
		Topic topic = studentService.chosenThesisTitle(studentId);
		if(topic == null || "".equals(topic)) {
			model.addAttribute("message", "????????????????????????");
			return "student/main.jsp";
		}else {
			
			StudentTaskBookOpening stbo  = studentService.getSTBOInfoById(studentId);
			if(stbo == null || "".equals(stbo)) {
				model.addAttribute("message", "??????????????????????????????????????????????????????");
				return "student/main.jsp";
			}else {
				int completion = stbo.getCompletion();
				if(completion == 2) {
					if(!file.isEmpty()) {
						
						File fileRoot = new File("E:\\BSM\\student");
						File fileDb = new File(fileRoot, studentIdToString);
						String fileName = file.getOriginalFilename();
						
						File filePath = new File(fileDb, fileName);
						
						if(!filePath.getParentFile().exists()) {
							filePath.getParentFile().mkdirs();
						}
						
						file.transferTo(new File(fileDb+File.separator+fileName));
						
						int num = studentService.uploadThesisInformation(studentId, filePath.toString());
						System.out.println("?????????"+num+"?????????");
						
						model.addAttribute("message", "??????????????????");
						return "student/main.jsp";
					}else {
						model.addAttribute("message", "??????????????????");
						return "student/main.jsp";
					}
					
				}else {
					
					model.addAttribute("message", "??????????????????????????????????????????????????????");
					return "student/main.jsp";
					
				}
				
				
				
			}
			}
			
			
	}
	
	
	@RequestMapping(value="/qualification")
	public String studentQualification(HttpServletRequest request,Model model) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		
		ThesisInformation thesis = studentService.getThesisInforInfoByStudentId(studentId);
		
		if(thesis == null || "".equals(thesis)) {
			model.addAttribute("message", "?????????????????????");
		}else {
			int status = thesis.getStatus();
			if(status == 0) {
				model.addAttribute("message", "?????????????????????");
			}else if(status == 1) {
				model.addAttribute("message", "?????????????????????");
			}else {
				model.addAttribute("message", "????????????????????????");
			}
			
		}
		return "student/studentQualifications.jsp";
	}
	
	
	@RequestMapping(value="/studentDoubt")
	public String studentDoubtForm() {
		return "student/studentDoubt.jsp";
	}
	
	@RequestMapping(value="/studentDoubtList")
	public String studentDoubtListForm(Model model,HttpServletRequest request) {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		List<Doubt> doubtList = studentService.getAllDoubtByStudent(studentId);
		for(int i= 0;i<doubtList.size();i++) {
			String answer = doubtList.get(i).getAnswer();
			if(answer == null || "".equals(answer)) {
				doubtList.get(i).setAnswer("????????????????????????");
			}
		}
		model.addAttribute("doubtList", doubtList);
		return "student/studentDoubtList.jsp";
	}
	
	@RequestMapping(value="/studentDoubtToDb")
	public String studentDoubtToDb(Model model,String doubt,HttpServletRequest request) throws Exception {
		Student currentUser = (Student)request.getSession().getAttribute("student");
		int studentId = currentUser.getId();
		doubt = new String(doubt.getBytes("iso-8859-1"),"utf-8");
		Doubt d = new Doubt();
		d.setStudentId(studentId);
		d.setStudentDoubt(doubt);
		studentService.addDoubt(d);
		model.addAttribute("message", "??????????????????");
		return "student/main.jsp";
	}
	
	@RequestMapping(value="/getThesisDescById")
	public void getThesisDescById(Model model,int topicId,HttpServletResponse response) throws Exception {
		String description = studentService.getThesisDesc(topicId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("desc", description);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter write = response.getWriter();
		write.write(JSONObject.toJSONString(map));
		write.close();
	}
}
