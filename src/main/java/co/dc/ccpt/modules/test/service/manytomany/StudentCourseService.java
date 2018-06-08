/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.service.manytomany;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.test.entity.manytomany.StudentCourse;
import co.dc.ccpt.modules.test.mapper.manytomany.StudentCourseMapper;

/**
 * 学生课程记录Service
 * @author lgf
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class StudentCourseService extends CrudService<StudentCourseMapper, StudentCourse> {

	public StudentCourse get(String id) {
		return super.get(id);
	}
	
	public List<StudentCourse> findList(StudentCourse studentCourse) {
		return super.findList(studentCourse);
	}
	
	public Page<StudentCourse> findPage(Page<StudentCourse> page, StudentCourse studentCourse) {
		return super.findPage(page, studentCourse);
	}
	
	@Transactional(readOnly = false)
	public void save(StudentCourse studentCourse) {
		super.save(studentCourse);
	}
	
	@Transactional(readOnly = false)
	public void delete(StudentCourse studentCourse) {
		super.delete(studentCourse);
	}
	
}