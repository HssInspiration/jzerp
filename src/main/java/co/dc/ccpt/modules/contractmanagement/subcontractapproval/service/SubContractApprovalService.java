package co.dc.ccpt.modules.contractmanagement.subcontractapproval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.subcontractapproval.entity.SubContractApproval;
import co.dc.ccpt.modules.contractmanagement.subcontractapproval.mapper.SubContractApprovalMapper;

/**
 * 分包合同审核管理Service
 * @author lxh
 * @version 2018-05-17
 */
@Service
@Transactional(readOnly = true)
public class SubContractApprovalService extends CrudService<SubContractApprovalMapper, SubContractApproval> {
	
	@Autowired
	public SubContractApprovalMapper subContractApprovalMapper;
	
	public SubContractApproval get(String id) {
		return super.get(id);
	}
	
	public List<SubContractApproval> findList(SubContractApproval subContractApproval) {
		return super.findList(subContractApproval);
	}
	
	public Page<SubContractApproval> findPage(Page<SubContractApproval> page, SubContractApproval subContractApproval) {
		return super.findPage(page, subContractApproval);
	}
	
	@Transactional(readOnly = false)
	public void save(SubContractApproval subContractApproval) {
		super.save(subContractApproval);
	}
	
	@Transactional(readOnly = false)
	public void delete(SubContractApproval subContractApproval) {
		super.delete(subContractApproval);
	}
	
}
