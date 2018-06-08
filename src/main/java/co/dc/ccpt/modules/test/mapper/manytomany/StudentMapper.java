/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.manytomany;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.manytomany.Student;

/**
 * 学生MAPPER接口
 * @author lgf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface StudentMapper extends BaseMapper<Student> {
	
}