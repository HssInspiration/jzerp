/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.service.one;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.test.entity.one.LeaveForm;
import co.dc.ccpt.modules.test.mapper.one.LeaveFormMapper;

/**
 * 请假表单Service
 * @author lgf
 * @version 2017-06-11
 */
@Service
@Transactional(readOnly = true)
public class LeaveFormService extends CrudService<LeaveFormMapper, LeaveForm> {

	public LeaveForm get(String id) {
		return super.get(id);
	}
	
	public List<LeaveForm> findList(LeaveForm leaveForm) {
		return super.findList(leaveForm);
	}
	
	public Page<LeaveForm> findPage(Page<LeaveForm> page, LeaveForm leaveForm) {
		return super.findPage(page, leaveForm);
	}
	
	@Transactional(readOnly = false)
	public void save(LeaveForm leaveForm) {
		super.save(leaveForm);
	}
	
	@Transactional(readOnly = false)
	public void delete(LeaveForm leaveForm) {
		super.delete(leaveForm);
	}
	
}