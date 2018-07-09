/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.oa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.activiti.bpmn.model.Task;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
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
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.SubProContractMapper;
import co.dc.ccpt.modules.oa.entity.ActSubContract;
import co.dc.ccpt.modules.oa.mapper.ActSubContractMapper;
import co.dc.ccpt.modules.sys.entity.Office;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;

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
	private SystemService systemService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
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
			String userLoginName = actSubContract.getCurrentUser().getLoginName();
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(userLoginName);
			List<String> assigneeList = new ArrayList<String>();
			/**
			 * 定义流程变量集合：【会签人员*7：分公司的负责人（技术负责人，财务负责人，经理）+总公司（经营部负责人+工程部负责人+财务部负责人+结算部负责人）】
			 * 获取流程启动人员：
			 * 1.所在分公司的负责人（技术负责人，财务负责人，经理）
			 * 2.总公司人员基本固定--用id获取指定人员：总公司（经营部负责人+工程部负责人+财务部负责人+结算部负责人）
			*/
			//1.获取分公司三个审批人员（技术负责人，财务负责人，负责人）
			User user = systemService.getUserByLoginName(userLoginName);
			String primaryUserLoginName = "";
			String tecUserLoginName = "";
			String accUserLoginName = "";
			if(user != null){
				Office office = user.getOffice();
				if(office != null){
					String officeId = office.getId();
					if(officeId != null && !officeId.equals("")){
						User primaryPerson = systemService.getPrimaryPersonById(officeId);//获取分公司负责人
						if(primaryPerson != null){
							primaryUserLoginName = primaryPerson.getLoginName();
							assigneeList.add(primaryUserLoginName);
						}
						User tecPerson = systemService.getTecPersonById(officeId);//获取分公司技术负责人
						if(tecPerson != null){
							tecUserLoginName = tecPerson.getLoginName();
							assigneeList.add(tecUserLoginName);
						}
						User accPerson = systemService.getAccPersonById(officeId);//获取分公司财务负责人
						if(accPerson != null){
							accUserLoginName = accPerson.getLoginName();
							assigneeList.add(accUserLoginName);
						}
					}
				}
			}
			//2.获取总公司四个审批人员（经营部负责人+工程部负责人+财务部负责人+结算部负责人）
			List<String> list = systemService.getPrimaryPersonByRoleId();
			if(list!=null && list.size()>0){
				for(String str:list){
					assigneeList.add(str);
				}
			}
			Map<String,Object> variables = new HashMap<String,Object>();
			//审批人员集合
			variables.put("assigneeList", assigneeList);
			//会签节点初始状态
			variables.put("status", 0);
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_SUB_ACT_CONTRACT[0], ActUtils.PD_SUB_ACT_CONTRACT[1], actSubContract.getId(), actSubContract.getRemarks(),variables);
			
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
		Act act = actSubContract.getAct();
		String taskDefKey = actSubContract.getAct().getTaskDefKey();
		Integer i = null;
		String str = "";
		String taskId = "";
		// 审核环节
		if ("parallel".equals(taskDefKey)){
			taskId = act.getTaskId();
			actSubContract.setSubLeadText(actSubContract.getAct().getComment());
			mapper.updateSubLeadText(actSubContract);
			if(act.getFlag().equals("no")){//有一个人不同意就转到合同修改(先结束此节点任务，再转到合同修改)
				str	= act.getProcInsId();
				i = (Integer)runtimeService.getVariable(str, "status");//此处可以用流程实例id代替执行对象id因为在任务执行表中二者一样
				System.out.println("流程变量值为："+i);
			}
		}else if ("manage_approval".equals(taskDefKey)){
			actSubContract.setLeadText(actSubContract.getAct().getComment());
			mapper.updateLeadText(actSubContract);
		}else if ("chairman_approval".equals(taskDefKey)){
			actSubContract.setMainLeadText(actSubContract.getAct().getComment());
			mapper.updateMainLeadText(actSubContract);
		}
		// 未知环节，直接返回
		else{
			return;
		}
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		if(i != null && i == 0){//status==1---驳回的时候，结束会签节点任务到排他网关
			vars.put("status", 1);
		}
		if(taskId != null && !taskId.equals("")){
			Integer nrOfInstances =(Integer)taskService.getVariable(taskId, "nrOfInstances");//runtimeService.getVariable(str, "nrOfInstances");//会签所有人员
			Integer nrOfCompletedInstances =(Integer)taskService.getVariable(taskId, "nrOfCompletedInstances");//runtimeService.getVariable(str, "nrOfCompletedInstances");//会签已完成人员
			if((nrOfCompletedInstances/nrOfInstances)>=1){//所有人员全部完成
				vars.put("status", 1);
			}
		}
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
