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
import co.dc.ccpt.modules.oa.entity.ActContract;
import co.dc.ccpt.modules.oa.mapper.ActContractMapper;

/**
 * 审批Service
 * @author dckj
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class ActContractService extends CrudService<ActContractMapper, ActContract> {

	@Autowired
	private ActTaskService actTaskService;
	
	@Autowired
	private IdentityService identityService;
	
	public ActContract getByProcInsId(String procInsId) {
		return mapper.getByProcInsId(procInsId);
	}
	
	public Page<ActContract> findPage(Page<ActContract> page, ActContract actContract) {
		actContract.setPage(page);
		page.setList(mapper.findList(actContract));
		return page;
	}
	
	/**
	 * 审核新增或编辑
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void save(ActContract actContract) {
		
		// 申请发起
		if (StringUtils.isBlank(actContract.getId())){
			//如果执行前该对象id是空的话，先执行新增
			actContract.preInsert();
			mapper.insert(actContract);
			System.out.println("********************actContract*******************:"+actContract);
			
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(actContract.getCurrentUser().getLoginName());
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_ACT_CONTRACT[0], ActUtils.PD_ACT_CONTRACT[1], actContract.getId(), actContract.getRemarks());
		}
		
		// 重新编辑申请		
		else{
			//如果执行前该对象id是空的话，先执行新增
			actContract.preUpdate();
			mapper.update(actContract);

			actContract.getAct().setComment(("yes".equals(actContract.getAct().getFlag())?"[重申] ":"[销毁] ")+actContract.getAct().getComment());
			
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(actContract.getAct().getFlag())? "1" : "0");
			actTaskService.complete(actContract.getAct().getTaskId(), actContract.getAct().getProcInsId(), actContract.getAct().getComment(), actContract.getRemarks(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(ActContract actContract) {
		
		// 设置意见
		actContract.getAct().setComment(("yes".equals(actContract.getAct().getFlag())?"[同意] ":"[驳回] ")+actContract.getAct().getComment());
		
		actContract.preUpdate();
		
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = actContract.getAct().getTaskDefKey();

		// 审核环节
		if ("audit".equals(taskDefKey)){
			
		}
		else if ("lead".equals(taskDefKey)){
			actContract.setLeadText(actContract.getAct().getComment());
			mapper.updateLeadText(actContract);
		}
		else if ("mainLead".equals(taskDefKey)){
			actContract.setMainLeadText(actContract.getAct().getComment());
			mapper.updateMainLeadText(actContract);
		}
		else if ("apply_end".equals(taskDefKey)){
			
		}
		
		// 未知环节，直接返回
		else{
			return;
		}
		
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(actContract.getAct().getFlag())? "1" : "0");
		actTaskService.complete(actContract.getAct().getTaskId(), actContract.getAct().getProcInsId(), actContract.getAct().getComment(), vars);
		
	}
	
}
