/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.service.tree;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.service.TreeService;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.modules.test.entity.tree.TestTreeDialog;
import co.dc.ccpt.modules.test.mapper.tree.TestTreeDialogMapper;

/**
 * 组织机构Service
 * @author liugf
 * @version 2017-06-19
 */
@Service
@Transactional(readOnly = true)
public class TestTreeDialogService extends TreeService<TestTreeDialogMapper, TestTreeDialog> {

	public TestTreeDialog get(String id) {
		return super.get(id);
	}
	
	public List<TestTreeDialog> findList(TestTreeDialog testTreeDialog) {
		if (StringUtils.isNotBlank(testTreeDialog.getParentIds())){
			testTreeDialog.setParentIds(","+testTreeDialog.getParentIds()+",");
		}
		return super.findList(testTreeDialog);
	}
	
	@Transactional(readOnly = false)
	public void save(TestTreeDialog testTreeDialog) {
		super.save(testTreeDialog);
	}
	
	@Transactional(readOnly = false)
	public void delete(TestTreeDialog testTreeDialog) {
		super.delete(testTreeDialog);
	}
	
}