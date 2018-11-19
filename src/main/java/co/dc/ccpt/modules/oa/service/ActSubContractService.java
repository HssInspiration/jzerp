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
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.oa.entity.ActSubContract;
import co.dc.ccpt.modules.oa.mapper.ActSubContractMapper;
import co.dc.ccpt.modules.oa.utils.Jacob;
import co.dc.ccpt.modules.oa.utils.UrlEx;
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
	
	@Autowired
	private  ActSubContractMapper actSubContractMapper;
	
	@Autowired
	private SubProContractService subProContractService;
	
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
			List<String> subLeadList = new ArrayList<String>();
			/**
			 * 定义流程变量集合：【会签人员*3：分公司的负责人（技术负责人，财务负责人，经理）】
			 * 获取流程启动人员：
			 * 1.所在分公司的负责人（技术负责人，财务负责人，经理）
			 * #######################将人员分拆为分公司和总公司部门，当前代码保留，防止客户要求七个人同时审批########################
			 * 【总公司（经营部负责人+工程部负责人+财务部负责人+结算部负责人）】
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
							subLeadList.add(primaryUserLoginName);
						}
						User tecPerson = systemService.getTecPersonById(officeId);//获取分公司技术负责人
						if(tecPerson != null){
							tecUserLoginName = tecPerson.getLoginName();
							subLeadList.add(tecUserLoginName);
						}
						User accPerson = systemService.getAccPersonById(officeId);//获取分公司财务负责人
						if(accPerson != null){
							accUserLoginName = accPerson.getLoginName();
							subLeadList.add(accUserLoginName);
						}
					}
				}
			}
//			2.获取总公司四个审批人员（经营部负责人+工程部负责人+财务部负责人+结算部负责人）
			List<String> leadList = new ArrayList<String>();
			List<String> list = systemService.getPrimaryPersonByRoleId();
			if(list!=null && list.size()>0){
				for(String lead:list){
					leadList.add(lead);
				}
			}
			/**
			 * method2   可在初始时设置对应的审批人员
			 */
//			String userId = actSubContract.getUserId();
//			String[] strArr = userId.split(","); 
//			for(int i=0;i<strArr.length;i++){
//				String loginName = systemService.getUser(strArr[i]).getLoginName();
//				assigneeList.add(loginName);
//			}
//			System.out.println(userId);
			
			Map<String,Object> variables = new HashMap<String,Object>();
			//审批人员集合
			variables.put("subLeadList", subLeadList);
			
			variables.put("leadList", leadList);
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
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		// 审核环节
		if ("parallel1".equals(taskDefKey)){
			taskId = act.getTaskId();
			actSubContract.setSubLeadText(actSubContract.getAct().getComment());
			mapper.updateSubLeadText(actSubContract);
			if(act.getFlag().equals("no")){//有一个人不同意就转到合同修改(先结束此节点任务，再转到合同修改)
				str	= act.getProcInsId();
				i = (Integer)runtimeService.getVariable(str, "status");//此处可以用流程实例id代替执行对象id因为在任务执行表中二者一样
				System.out.println("流程变量值为："+i);
			}
		}else if ("parallel2".equals(taskDefKey)){
			taskId = act.getTaskId();
			actSubContract.setSubLeadText(actSubContract.getAct().getComment());
			mapper.updateSubLeadText(actSubContract);
			//重新设置status的值
			Integer nrOfCompletedInstances =(Integer)taskService.getVariable(taskId, "nrOfCompletedInstances");
			Integer nrOfInstances =(Integer)taskService.getVariable(taskId, "nrOfInstances");
			if(nrOfInstances-nrOfCompletedInstances > 1){//还剩最后一个
				vars.put("status", 0);
			}
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
		
		if(i != null && i == 0){//status==1---驳回的时候，结束会签节点任务到排他网关
			vars.put("status", 1);
		}
		if(taskId != null && !taskId.equals("")){
			Integer nrOfInstances =(Integer)taskService.getVariable(taskId, "nrOfInstances");//runtimeService.getVariable(str, "nrOfInstances");//会签所有人员
			Integer nrOfCompletedInstances =(Integer)taskService.getVariable(taskId, "nrOfCompletedInstances");//runtimeService.getVariable(str, "nrOfCompletedInstances");//会签已完成人员
			if((nrOfInstances-nrOfCompletedInstances)==1){//最后一个会签人员
				if("yes".equals(actSubContract.getAct().getFlag())){//同意审批
//					获取总公司四个审批人员（经营部负责人+工程部负责人+财务部负责人+结算部负责人）
//					List<String> leadList = new ArrayList<String>();
//					List<String> list = systemService.getPrimaryPersonByRoleId();
//					if(list!=null && list.size()>0){
//						for(String lead:list){
//							leadList.add(lead);
//						}
//					}
//					vars.put("leadList", leadList);
					vars.put("status", 1);//更改状态
				}
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
	}
	
	/**
	 * 依据审核表单更改合同状态
	 * 
	 * @param actContract
	 */
	@Transactional(readOnly = false)
	public void changeStatusForAudit(ActSubContract actSubContract) {
		Act act = actSubContract.getAct();
		if(act!=null){
			String taskKey = act.getTaskDefKey();
			String flag = act.getFlag();
			System.out.println("flag:"+flag);
			if(taskKey.equals("parallel1")){
				System.out.println("会签节点1！");
				if(flag.equals("no")){//不同意
					subProContractService.updateSubProContractStatus(actSubContract, 3);
				}
			}else if(taskKey.equals("parallel2")){
				System.out.println("会签节点2！");
				if(flag.equals("no")){//不同意
					subProContractService.updateSubProContractStatus(actSubContract, 3);
				}
			}else if(taskKey.equals("manage_approval")){//总经理审批
				if(flag.equals("no")){//不同意
					subProContractService.updateSubProContractStatus(actSubContract, 3);
				}
			}else if(taskKey.equals("chairman_approval")){//董事长审批
				if(flag.equals("no")){//不同意
					subProContractService.updateSubProContractStatus(actSubContract, 3);
				}else if(flag.equals("yes")){//同意
					subProContractService.updateSubProContractStatus(actSubContract, 2);
				}
			}
//			else if(taskKey.equals("modify")){//若为合同修改
//				if(flag.equals("no")){//销毁--审批不通过
//					subProContractService.updateSubProContractStatus(actSubContract, 0);
//				}else if(flag.equals("yes")){//同意
//					subProContractService.updateSubProContractStatus(actSubContract, 1);
//				}
//			}
		}
	}


	public ActSubContract exchangeWordToPdf(ActSubContract actSubContract) {
		UrlEx url = new UrlEx();//url转码
		Jacob jacob = new Jacob();//word转pdf
		String wordFile = "";
		//先获取路径
		actSubContract = actSubContractMapper.get(actSubContract);
		if(actSubContract != null){
			String contractTextCont = actSubContract.getContractTextCont();//表中存放的合同路径
			System.out.println("contractTextCont:"+contractTextCont);
			if(StringUtils.isNotBlank(contractTextCont)){
				contractTextCont = contractTextCont.replace("/ccpt","D:/jzerp_files");
				wordFile = url.getURLDecoderString(contractTextCont);
				wordFile = wordFile.replaceAll("/", "\\\\");
				System.out.println("wordFile:"+wordFile);
			}else{
				System.out.println("contractTextCont is Null");
			}
			String pdfFile = wordFile.substring(0,wordFile.lastIndexOf("."))+".pdf";//转换后的路径
			System.out.println("pdfFile:"+pdfFile);
			String contractContToPdf = jacob.exchangeWordToPdf(wordFile, pdfFile);
			actSubContract.setContractContToPdf(contractContToPdf);
		}
		return actSubContract;
	}

	public ActSubContract getBySubContract(SubProContract subProContract) {
		String subContractId = subProContract.getId();
		ActSubContract actSubContract = new ActSubContract();
		if(StringUtils.isNotBlank(subContractId)){
			actSubContract = actSubContractMapper.getByContractId(subContractId);
		}
		if (actSubContract != null) {
			return actSubContract;
		} else {
			return new ActSubContract();
		}
	}

	@Transactional(readOnly = false)
	public void changeStatusForForm(ActSubContract actSubContract) {
		Act act = actSubContract.getAct();
		if(act != null){
			String taskKey = act.getTaskDefKey();
			if(taskKey==null || taskKey.equals("")){
				subProContractService.updateSubProContractStatus(actSubContract, 1);
			}else if(taskKey.equals("modify")){//若为合同修改
				if(act.getFlag().equals("no")){//销毁--未审批
					subProContractService.updateSubProContractStatus(actSubContract, 0);
				}else if(act.getFlag().equals("yes")){
					subProContractService.updateSubProContractStatus(actSubContract, 1);
				}
			}
		}
	}
}
