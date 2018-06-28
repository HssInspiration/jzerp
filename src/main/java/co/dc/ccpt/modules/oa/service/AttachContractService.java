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
import co.dc.ccpt.modules.oa.entity.AttachContract;
import co.dc.ccpt.modules.oa.mapper.AttachContractMapper;

/**
 * 审批Service
 * @author dckj
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class AttachContractService extends CrudService<AttachContractMapper, AttachContract> {

	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private IdentityService identityService;
	
	public AttachContract getByProcInsId(String procInsId) {
		return mapper.getByProcInsId(procInsId);
	}
	
	public Page<AttachContract> findPage(Page<AttachContract> page, AttachContract attachContract) {
		attachContract.setPage(page);
		page.setList(mapper.findList(attachContract));
		return page;
	}
	
	/**
	 * 审核新增或编辑
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void save(AttachContract attachContract) {
		
		// 申请发起
		if (StringUtils.isBlank(attachContract.getId())){
			attachContract.preInsert();
			mapper.insert(attachContract);
			
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(attachContract.getCurrentUser().getLoginName());
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_ATTACH_CONTRACT[0], ActUtils.PD_ATTACH_CONTRACT[1], attachContract.getId(), attachContract.getRemarks());
			
		}
		// 重新编辑申请		
		else{
			attachContract.preUpdate();
			mapper.update(attachContract);

			attachContract.getAct().setComment(("yes".equals(attachContract.getAct().getFlag())?"[重申] ":"[销毁] ")+attachContract.getAct().getComment());
			
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(attachContract.getAct().getFlag())? "1" : "0");
			actTaskService.complete(attachContract.getAct().getTaskId(), attachContract.getAct().getProcInsId(), attachContract.getAct().getComment(), attachContract.getRemarks(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(AttachContract attachContract) {
		
		// 设置意见
		attachContract.getAct().setComment(("yes".equals(attachContract.getAct().getFlag())?"[同意] ":"[驳回] ")+attachContract.getAct().getComment());
		
		attachContract.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = attachContract.getAct().getTaskDefKey();

		// 审核环节
		if ("subLead".equals(taskDefKey)){
			attachContract.setSubLeadText(attachContract.getAct().getComment());
			mapper.updateSubLeadText(attachContract);
		}
		else if ("lead".equals(taskDefKey)){
			attachContract.setLeadText(attachContract.getAct().getComment());
			mapper.updateLeadText(attachContract);
		}
		else if ("mainLead".equals(taskDefKey)){
			attachContract.setMainLeadText(attachContract.getAct().getComment());
			mapper.updateMainLeadText(attachContract);
			//
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
		vars.put("pass", "yes".equals(attachContract.getAct().getFlag())? "1" : "0");
		actTaskService.complete(attachContract.getAct().getTaskId(), attachContract.getAct().getProcInsId(), attachContract.getAct().getComment(), vars);
		
	}
	
}
