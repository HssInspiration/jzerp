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
import co.dc.ccpt.modules.act.entity.Act;
import co.dc.ccpt.modules.act.service.ActTaskService;
import co.dc.ccpt.modules.act.utils.ActUtils;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.oa.entity.ActContract;
import co.dc.ccpt.modules.oa.mapper.ActContractMapper;

/**
 * 审批Service
 * 
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

	@Autowired
	private ProContractService proContractService;

	@Autowired
	private ActContractMapper actContractMapper;

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
	 * 
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void save(ActContract actContract) {
		// 申请发起
		if (StringUtils.isBlank(actContract.getId())) {
			// 如果执行前该对象id是空的话，先执行新增
			actContract.preInsert();
			mapper.insert(actContract);
			System.out.println("********************actContract*******************:" + actContract);
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(actContract.getCurrentUser().getLoginName());
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_ACT_CONTRACT[0], ActUtils.PD_ACT_CONTRACT[1], actContract.getId(),
					actContract.getRemarks());
		} else {// 重新编辑申请
				// 如果执行前该对象非空的话，先执行更新
			actContract.preUpdate();
			mapper.update(actContract);
			System.out.println("**************************actContract:" + actContract);
			actContract.getAct().setComment(("yes".equals(actContract.getAct().getFlag()) ? "[重申] " : "[销毁] ")
					+ actContract.getAct().getComment());
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(actContract.getAct().getFlag()) ? "1" : "0");
			actTaskService.complete(actContract.getAct().getTaskId(), actContract.getAct().getProcInsId(),
					actContract.getAct().getComment(), actContract.getRemarks(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * 
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(ActContract actContract) {
		// 设置意见
		actContract.getAct().setComment(
				("yes".equals(actContract.getAct().getFlag()) ? "[同意] " : "[驳回] ") + actContract.getAct().getComment());
		actContract.preUpdate();
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = actContract.getAct().getTaskDefKey();
		// 审核环节
		if ("lead".equals(taskDefKey)) {// 经营部负责人审批
			actContract.setLeadText(actContract.getAct().getComment());
			mapper.updateLeadText(actContract);
		} else if ("mainLead".equals(taskDefKey)) {// 总经理审批
			actContract.setMainLeadText(actContract.getAct().getComment());
			mapper.updateMainLeadText(actContract);
		} else {// 未知环节，直接返回
			return;
		}
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(actContract.getAct().getFlag()) ? "1" : "0");
		actTaskService.complete(actContract.getAct().getTaskId(), actContract.getAct().getProcInsId(),
				actContract.getAct().getComment(), vars);
	}

	/**
	 * 依据审核表单更改合同状态
	 * 
	 * @param actContract
	 */
	@Transactional(readOnly = false)
	public void changeStatusForAudit(ActContract actContract) {
		Act act = actContract.getAct();
		if (act != null) {
			String taskKey = act.getTaskDefKey();
			String flag = act.getFlag();
			System.out.println("flag:" + flag);
			if (taskKey.equals("lead")) {
				if (flag.equals("no")) {// 不同意
					proContractService.updateProContractStatus(actContract, 3);
				}
			} else if (taskKey.equals("mainLead")) {// 总经理审批
				if (flag.equals("no")) {// 不同意
					proContractService.updateProContractStatus(actContract, 3);
				} else if (flag.equals("yes")) {// 同意
					proContractService.updateProContractStatus(actContract, 2);
				}
			} 
//			else if (taskKey.equals("contract_modify")) {// 若为合同修改
//				if (flag.equals("no")) {// 销毁--审批不通过
//					proContractService.updateProContractStatus(actContract, 0);
//				} else if (flag.equals("yes")) {// 同意
//					proContractService.updateProContractStatus(actContract, 1);
//				}
//			}
		}
	}

	/**
	 * 依据提交申请&驳回修改表单更改状态
	 * 
	 * @param actContract
	 */
	@Transactional(readOnly = false)
	public void changeStatusForForm(ActContract actContract) {
		// 审批通过更改合同审批状态-->审批通过(1-->2)
		Act act = actContract.getAct();
		if (act != null) {
			String taskKey = act.getTaskDefKey();
			if (taskKey == null || taskKey.equals("")) {// 审批中
				proContractService.updateProContractStatus(actContract, 1);
			} else if (taskKey.equals("contract_modify")) {// 若为合同修改
				if (act.getFlag().equals("no")) {// 销毁--审批不通过
					proContractService.updateProContractStatus(actContract, 0);
				} else if (act.getFlag().equals("yes")) {
					proContractService.updateProContractStatus(actContract, 1);
				}
			}
		}
	}

	/**
	 * 通过合同id获取审批对象
	 * @param proContract
	 * @return
	 */
	public ActContract getByProContract(ProContract proContract) {
		String proContractId = proContract.getId();
		ActContract actContract = new ActContract();
		if(StringUtils.isNotBlank(proContractId)){
			actContract = actContractMapper.getByContractId(proContractId);
		}
		if (actContract != null) {
			return actContract;
		} else{
			return new ActContract();
		}
	}
}
