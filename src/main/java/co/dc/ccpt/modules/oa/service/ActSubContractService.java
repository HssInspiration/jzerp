/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.oa.service;

import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.act.service.ActTaskService;
import co.dc.ccpt.modules.act.utils.ActUtils;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.SubProContractMapper;
import co.dc.ccpt.modules.oa.entity.ActSubContract;
import co.dc.ccpt.modules.oa.mapper.ActSubContractMapper;

/**
 * 审批Service
 * @author dckj
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class ActSubContractService extends CrudService<ActSubContractMapper, ActSubContract> {

	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private  SubProContractMapper subProContractMapper;
	
	public ActSubContract getByProcInsId(String procInsId) {
		return mapper.getByProcInsId(procInsId);
	}
	
	public Page<ActSubContract> findPage(Page<ActSubContract> page, ActSubContract actSubContract) {
		actSubContract.setPage(page);
		page.setList(mapper.findList(actSubContract));
		return page;
	}
	
	/**
	 * 审核新增或编辑
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void save(ActSubContract actSubContract) {
		
		// 申请发起
		if (StringUtils.isBlank(actSubContract.getId())){
			actSubContract.preInsert();
			mapper.insert(actSubContract);
			
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(actSubContract.getCurrentUser().getLoginName());
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_SUB_ACT_CONTRACT[0], ActUtils.PD_SUB_ACT_CONTRACT[1], actSubContract.getId(), actSubContract.getRemarks());
			
		}
		// 重新编辑申请		
		else{
			actSubContract.preUpdate();
			mapper.update(actSubContract);

			actSubContract.getAct().setComment(("yes".equals(actSubContract.getAct().getFlag())?"[重申] ":"[销毁] ")+actSubContract.getAct().getComment());
			
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(actSubContract.getAct().getFlag())? "1" : "0");
			actTaskService.complete(actSubContract.getAct().getTaskId(), actSubContract.getAct().getProcInsId(), actSubContract.getAct().getComment(), actSubContract.getRemarks(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(ActSubContract actSubContract) {
		
		// 设置意见
		actSubContract.getAct().setComment(("yes".equals(actSubContract.getAct().getFlag())?"[同意] ":"[驳回] ")+actSubContract.getAct().getComment());
		
		actSubContract.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = actSubContract.getAct().getTaskDefKey();

		// 审核环节
		if ("subLead".equals(taskDefKey)){
			actSubContract.setSubLeadText(actSubContract.getAct().getComment());
			mapper.updateSubLeadText(actSubContract);
		}
		else if ("lead".equals(taskDefKey)){
			actSubContract.setLeadText(actSubContract.getAct().getComment());
			mapper.updateLeadText(actSubContract);
		}
		else if ("mainLead".equals(taskDefKey)){
			actSubContract.setMainLeadText(actSubContract.getAct().getComment());
			mapper.updateMainLeadText(actSubContract);
		}
//		else if ("apply_end".equals(taskDefKey)){
//			
//		}
		// 未知环节，直接返回
		else{
			return;
		}
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(actSubContract.getAct().getFlag())? "1" : "0");
		actTaskService.complete(actSubContract.getAct().getTaskId(), actSubContract.getAct().getProcInsId(), actSubContract.getAct().getComment(), vars);
		
	}
	
	//更新分包合同审批状态
	@Transactional(readOnly = false)
	public int updateSubProContractStatus(ActSubContract actSubContract, Integer approvalStatus){
		SubProContract subProContract = actSubContract.getSubProContract();
		int i = 0;
		if(subProContract != null){
			subProContract = subProContractMapper.get(subProContract);
			if(subProContract != null){
				//设置对应的合同审批状态为审批中
				subProContract.setApprovalStatus(approvalStatus);
				i = subProContractMapper.updateSubProContractStatus(subProContract);
			}
		}
		return i;
	};
		
	
}
