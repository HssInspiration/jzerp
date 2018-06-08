/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.service.pic;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.test.entity.pic.TestPic;
import co.dc.ccpt.modules.test.mapper.pic.TestPicMapper;

/**
 * 图片管理Service
 * @author lgf
 * @version 2017-06-19
 */
@Service
@Transactional(readOnly = true)
public class TestPicService extends CrudService<TestPicMapper, TestPic> {

	public TestPic get(String id) {
		return super.get(id);
	}
	
	public List<TestPic> findList(TestPic testPic) {
		return super.findList(testPic);
	}
	
	public Page<TestPic> findPage(Page<TestPic> page, TestPic testPic) {
		return super.findPage(page, testPic);
	}
	
	@Transactional(readOnly = false)
	public void save(TestPic testPic) {
		super.save(testPic);
	}
	
	@Transactional(readOnly = false)
	public void delete(TestPic testPic) {
		super.delete(testPic);
	}
	
}