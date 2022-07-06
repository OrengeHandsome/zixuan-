# 毕业设计管理系统

### 介绍
毕业设计是本科教育的最后一个环节，整个过程包括课题拟定与审核、师生双选、开题报告、文档审核、中期检查、毕业答辩等多个环节，是一项系统而复杂的工作。开发一个适合本校的毕业设计管理系统，不仅能够极大地减少教务管理人员的工作量，还能较大地提高学生和教师的效率。

本系统采用B/S模式，主要开发工具为Eclipse（Oxygen.1a Release），用到的编程语言有Java、JavaScript和JSP，数据库采用MySQL 5.7，服务器则用的Apache Tomcat 9.0。前端页面的编写基于bootstrap，整个项目基于SSM框架。

### 软件架构
系统的组织架构如图所示，三种用户根据自己不同的权限对课题、文档、成绩、公告、任务五种数据信息进行处理，不同用户角色再根据这些数据信息进行交互通过课题进行交互，并且围绕着文档进行数据的传递和处理等。

 ![img](https://img-blog.csdnimg.cn/20190925144750623.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70) 

系统的功能模块如图所示，每个模块各有几个子功能模块。

 ![img](https://img-blog.csdnimg.cn/2019092514482270.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70) 

本系统包含三种用户：学生、教师和管理员。通过本系统，学生可以获取指导教师和管理员发布的相关教学信息和教学资源；教师可以进行毕设进度的日常管理，发布任务、上传资料、审核文档等；管理员可以进行系统的管理和维护，并对教师、学生、课题和文档进行管理，确保系统的正常运行。系统总共分为3个模块：

1.学生模块

学生登录系统后进入该模块，在该模块中，学生能够修改个人资料和登录密码，选择感兴趣的课题及导师，下载导师上传的相关参考资料，上传自己已经完成的文档，查看发布的公告、任务和自己的阶段成绩等。

2.教师模块

教师登录系统后进入该模块，在该模块中，教师能够修改个人资料和登录密码，选择学生，上传拟好的课题题目给管理员审核，发布阶段任务，上传相关资料给学生参考，下载学生上传的阶段文档并审核和打分，查看公告及发布新公告等。

3.管理员模块

管理员登录系统后进入该模块，在该模块中，管理员可以增删查改导师和学生的信息，审核教师上传的课题，发布公告，查看各种信息，查看各种数据分析的结果和图表。

### 数据库设计

数据分析：

本系统的主要数据表为存储学生、教师、文档、课题数据集的四个表。其余表大多作为外键关联用于完善四张表的信息。

概念设计：

数据库主要围绕学生、教师、 课题、文档4个实体进行数据处理。即系统只要处理好这4个实体的数据，就可以完成系统的绝大部分功能并让毕业设计管理的整个流程顺利进行下去。其余的数据表则是围绕这4个实体相应展开，进行一些扩展和补充，以完善整个数据库的结构并加快数据库对请求的响应速度。

重要E-R图：

数据库文档处理的E-R如图所示

![img](https://img-blog.csdnimg.cn/20190925145330555.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

数据库文档处理的E-R如图所示。

![img](https://img-blog.csdnimg.cn/20190925145357350.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

表列表：

数据库中一共设计了15张表，和学生相关的表有6张（包括学生信息、开题报告、文档成绩、毕设进度、学生疑问等），和教师相关的表有4张（包括教师信息、任务书、阶段任务、课题申报等），文档有1张表，课题涉及2张表，还有2张表属于公共信息表，用于进行一些补充以完善数据库。数据库的表清单如下表。

| **序号** | **物理表名**                       | **中文名称**       |
| -------- | ---------------------------------- | ------------------ |
| **1**    | t_announcement                     | 公告表             |
| **2**    | t_department                       | 学院表             |
| **3**    | t_doubt                            | 疑问表             |
| **4**    | t_major                            | 专业表             |
| **5**    | t_student                          | 学生表             |
| **6**    | t_student_opening_report_task_book | 学生开题报告表     |
| **7**    | t_student_progress                 | 学生进度通知填写表 |
| **8**    | t_student_score                    | 学生成绩表         |
| **9**    | t_teacher                          | 教师表             |
| **10**   | t_teacher_opening_report_task_book | 教师开题报告表     |
| **11**   | t_teacher_progress_notification    | 教师阶段任务表     |
| **12**   | t_thesis_information               | 文档提交表         |
| **13**   | t_thesis_title                     | 教师提交课题表     |
| **14**   | t_topic                            | 选题信息表         |
| **15**   | t_user                             | 用户表             |

### 功能模块的设计与实现

本系统依据学生、导师、管理员三种不同用户角色的需求，分为了三个大的功能模块，三种用户角色在各自的模块进行操作，可以通过课题进行与其他两种用户角色之间的交互，并且围绕着文档进行各种数据（用户信息、文档信息、课题信息）的传递和处理等，这就形成了系统的体系结构，管理员发布课题给学生，学生选择课题呈现给导师，导师自拟课题向管理员申报，如图所示。

![img](https://img-blog.csdnimg.cn/20190925152358253.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

同时三种用户可对五种数据信息进行不同的处理（增删查改的权限不同），表现在系统上就是拥有不同的功能模块，所以分为三个大的模块，每个模块有相应的小的功能模块，系统的功能结构如图。

![img](https://img-blog.csdnimg.cn/20190925152556896.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

### 项目截图

**学生模块部分页面**

![img](https://img-blog.csdnimg.cn/20190925154205713.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)



![img](https://img-blog.csdnimg.cn/2019092515442220.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)



**教师模块部分页面**

![img](https://img-blog.csdnimg.cn/2019092515444438.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)



![img](https://img-blog.csdnimg.cn/20190925154455179.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)



管理员模块部分页面

![img](https://img-blog.csdnimg.cn/20190925154631772.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)



![img](https://img-blog.csdnimg.cn/2019092515465119.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FjZV8y,size_16,color_FFFFFF,t_70)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)