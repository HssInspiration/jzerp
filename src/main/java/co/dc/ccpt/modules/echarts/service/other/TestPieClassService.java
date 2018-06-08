/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.echarts.service.other;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.echarts.entity.other.TestPieClass;
import co.dc.ccpt.modules.echarts.mapper.other.TestPieClassMapper;

/**
 * 学生Service
 * @author lgf
 * @version 2017-06-04
 */
@Service
@Transactional(readOnly = true)
public class TestPieClassService extends CrudService<TestPieClassMapper, TestPieClass> {

	public TestPieClass get(String id) {
		return super.get(id);
	}
	
	public List<TestPieClass> findList(TestPieClass testPieClass) {
		return super.findList(testPieClass);
	}
	
	public Page<TestPieClass> findPage(Page<TestPieClass> page, TestPieClass testPieClass) {
		return super.findPage(page, testPieClass);
	}
	
	@Transactional(readOnly = false)
	public void save(TestPieClass testPieClass) {
		super.save(testPieClass);
	}
	
	@Transactional(readOnly = false)
	public void delete(TestPieClass testPieClass) {
		super.delete(testPieClass);
	}
	
}