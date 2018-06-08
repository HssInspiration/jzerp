/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.entity.manytomany;


import co.dc.ccpt.core.persistence.DataEntity;
import co.dc.ccpt.common.utils.excel.annotation.ExcelField;

/**
 * 课程Entity
 * @author lgf
 * @version 2017-06-12
 */
public class Course extends DataEntity<Course> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 课程名
	
	public Course() {
		super();
	}

	public Course(String id){
		super(id);
	}

	@ExcelField(title="课程名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}