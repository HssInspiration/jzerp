/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.service.onetomanyform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.modules.test.entity.onetomanyform.TestDataMainForm;
import co.dc.ccpt.modules.test.mapper.onetomanyform.TestDataMainFormMapper;
import co.dc.ccpt.modules.test.entity.onetomanyform.TestDataChild;
import co.dc.ccpt.modules.test.mapper.onetomanyform.TestDataChildMapper;
import co.dc.ccpt.modules.test.entity.onetomanyform.TestDataChild2;
import co.dc.ccpt.modules.test.mapper.onetomanyform.TestDataChild2Mapper;
import co.dc.ccpt.modules.test.entity.onetomanyform.TestDataChild3;
import co.dc.ccpt.modules.test.mapper.onetomanyform.TestDataChild3Mapper;

/**
 * 票务代理Service
 * @author liugf
 * @version 2017-06-19
 */
@Service
@Transactional(readOnly = true)
public class TestDataMainFormService extends CrudService<TestDataMainFormMapper, TestDataMainForm> {

	@Autowired
	private TestDataChildMapper testDataChildMapper;
	@Autowired
	private TestDataChild2Mapper testDataChild2Mapper;
	@Autowired
	private TestDataChild3Mapper testDataChild3Mapper;
	
	public TestDataMainForm get(String id) {
		TestDataMainForm testDataMainForm = super.get(id);
		testDataMainForm.setTestDataChildList(testDataChildMapper.findList(new TestDataChild(testDataMainForm)));
		testDataMainForm.setTestDataChild2List(testDataChild2Mapper.findList(new TestDataChild2(testDataMainForm)));
		testDataMainForm.setTestDataChild3List(testDataChild3Mapper.findList(new TestDataChild3(testDataMainForm)));
		return testDataMainForm;
	}
	
	public List<TestDataMainForm> findList(TestDataMainForm testDataMainForm) {
		return super.findList(testDataMainForm);
	}
	
	public Page<TestDataMainForm> findPage(Page<TestDataMainForm> page, TestDataMainForm testDataMainForm) {
		return super.findPage(page, testDataMainForm);
	}
	
	@Transactional(readOnly = false)
	public void save(TestDataMainForm testDataMainForm) {
		super.save(testDataMainForm);
		for (TestDataChild testDataChild : testDataMainForm.getTestDataChildList()){
			if (testDataChild.getId() == null){
				continue;
			}
			if (TestDataChild.DEL_FLAG_NORMAL.equals(testDataChild.getDelFlag())){
				if (StringUtils.isBlank(testDataChild.getId())){
					testDataChild.setTestDataMain(testDataMainForm);
					testDataChild.preInsert();
					testDataChildMapper.insert(testDataChild);
				}else{
					testDataChild.preUpdate();
					testDataChildMapper.update(testDataChild);
				}
			}else{
				testDataChildMapper.delete(testDataChild);
			}
		}
		for (TestDataChild2 testDataChild2 : testDataMainForm.getTestDataChild2List()){
			if (testDataChild2.getId() == null){
				continue;
			}
			if (TestDataChild2.DEL_FLAG_NORMAL.equals(testDataChild2.getDelFlag())){
				if (StringUtils.isBlank(testDataChild2.getId())){
					testDataChild2.setTestDataMain(testDataMainForm);
					testDataChild2.preInsert();
					testDataChild2Mapper.insert(testDataChild2);
				}else{
					testDataChild2.preUpdate();
					testDataChild2Mapper.update(testDataChild2);
				}
			}else{
				testDataChild2Mapper.delete(testDataChild2);
			}
		}
		for (TestDataChild3 testDataChild3 : testDataMainForm.getTestDataChild3List()){
			if (testDataChild3.getId() == null){
				continue;
			}
			if (TestDataChild3.DEL_FLAG_NORMAL.equals(testDataChild3.getDelFlag())){
				if (StringUtils.isBlank(testDataChild3.getId())){
					testDataChild3.setTestDataMain(testDataMainForm);
					testDataChild3.preInsert();
					testDataChild3Mapper.insert(testDataChild3);
				}else{
					testDataChild3.preUpdate();
					testDataChild3Mapper.update(testDataChild3);
				}
			}else{
				testDataChild3Mapper.delete(testDataChild3);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TestDataMainForm testDataMainForm) {
		super.delete(testDataMainForm);
		testDataChildMapper.delete(new TestDataChild(testDataMainForm));
		testDataChild2Mapper.delete(new TestDataChild2(testDataMainForm));
		testDataChild3Mapper.delete(new TestDataChild3(testDataMainForm));
	}
	
}