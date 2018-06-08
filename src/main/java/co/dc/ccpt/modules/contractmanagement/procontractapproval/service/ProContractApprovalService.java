package co.dc.ccpt.modules.contractmanagement.procontractapproval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.procontractapproval.entity.ProContractApproval;
import co.dc.ccpt.modules.contractmanagement.procontractapproval.mapper.ProContractApprovalMapper;
/**
 * 总包项目合同Service
 * @author Administrator
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class ProContractApprovalService extends CrudService<ProContractApprovalMapper, ProContractApproval> {
	
	@Autowired
	public ProContractApprovalMapper proContractApprovalMapper;
	
	public ProContractApproval get(String id) {
		return super.get(id);
	}
	
	public List<ProContractApproval> findList(ProContractApproval proContractApproval) {
		return super.findList(proContractApproval);
	}
	
	public Page<ProContractApproval> findPage(Page<ProContractApproval> page, ProContractApproval proContractApproval) {
		return super.findPage(page, proContractApproval);
	}
	
	@Transactional(readOnly = false)
	public void save(ProContractApproval proContractApproval) {
		super.save(proContractApproval);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProContractApproval proContractApproval) {
		super.delete(proContractApproval);
	}
	
}
