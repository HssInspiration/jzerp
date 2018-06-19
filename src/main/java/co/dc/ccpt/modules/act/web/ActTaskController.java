/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.act.web;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.act.entity.Act;
import co.dc.ccpt.modules.act.service.ActTaskService;
import co.dc.ccpt.modules.act.utils.ActUtils;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.sys.utils.UserUtils;

/**
 * 流程个人任务相关Controller
 * @author dckj
 * @version 2016-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ActTaskController extends BaseController {

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private ProContractService proContractService;
	
	@Autowired
	private SubProContractService subProContractService;
	
	/**
	 * 获取待办列表
	 * @return
	 */
	@RequestMapping(value = {"todo", ""})
	public String todoList(Act act, HttpServletResponse response, Model model) throws Exception {
		return "modules/act/actTaskTodoList";
	}

	@ResponseBody
	@RequestMapping(value = "todo/data")
	public  Map<String, Object> todoListData(Act act, HttpServletResponse response, Model model) throws Exception {
		List<HashMap<String,String>> list = actTaskService.todoList(act);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows",list);
		map.put("total",list.size());
		return map;
	}
	
	/**
	 * 获取已办任务
	 * @return
	 */
	@RequestMapping(value = "historic")
	public String historicList(Act act, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		return "modules/act/actTaskHistoricList";
	}

	@ResponseBody
	@RequestMapping(value = "historic/data")
	public Map<String, Object>  historicListData(Act act, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<HashMap<String,String>> page = actTaskService.historicList(new  Page<HashMap<String,String>>(request, response), act);
		return getBootstrapData(page);
	}


	/**
	 * 获取流转历史列表
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	@RequestMapping(value = "histoicFlow")
	public String histoicFlow(Act act, String startAct, String endAct, Model model){
		if (StringUtils.isNotBlank(act.getProcInsId())){
			List<Act> histoicFlowList = actTaskService.histoicFlowList(act.getProcInsId(), startAct, endAct);
			model.addAttribute("histoicFlowList", histoicFlowList);
		}
		return "modules/act/actTaskHistoricFlow";
	}
	
	/**
	 * 获取流程流向图
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	@RequestMapping(value = "flowChart")
	public String flowChart(Act act, String startAct, String endAct, Model model){
		if (StringUtils.isNotBlank(act.getProcInsId())){
			List<Act> histoicFlowList = actTaskService.histoicFlowList(act.getProcInsId(), startAct, endAct);
			model.addAttribute("histoicFlowList", histoicFlowList);
		}
		return "modules/act/actTaskFlowChart";
	}
	
	/**
	 * 获取流程列表
	 * @param category 流程分类
	 */
	@RequestMapping(value = "process")
	public String processList(String category, String id, HttpServletRequest request, HttpServletResponse response, Model model) {
		logger.info("###########################:"+id);
		//如果id不为空，利用id找到对应的对象（ProContract）和对应的类别（总包中的市场投标，）
		if(id!=null){
			ProContract proContract = proContractService.get(id);
			SubProContract subProContract = subProContractService.get(id);
			if(proContract != null){
				Program pro = proContract.getProgram();
				if(pro != null){
					Integer getMethod = pro.getGetMethod();
					if(getMethod == 0){//"业主指定"
						logger.info("###########################:"+"业主指定");
					}else if(getMethod == 1){//市场投标
						logger.info("###########################:"+"市场投标");
					}
				}
			}else if(subProContract != null){
				logger.info("###########################:"+"分包合同");
			}else{
				logger.info("###########################:"+"another");
			}
		}
		Page<Object[]> page = new Page<Object[]>(request, response);
	    page = actTaskService.processList(page, category);
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		return "modules/act/actTaskProcessList";
	}
	
	/**
	 * 获取流程表单
	 */
	@RequestMapping(value = "form")
//	@ResponseBody
	public String form(Act act, HttpServletRequest request, Model model){
//		public String form(Act act, HttpServletRequest request, Model model, String proContractId){
		// 获取流程XML上的表单KEY
		String formKey = actTaskService.getFormKey(act.getProcDefId(), act.getTaskDefKey());   
		// 获取流程实例对象
		if (act.getProcInsId() != null){
			if(actTaskService.getProcIns(act.getProcInsId())!=null){
				act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
			}else{
				act.setFinishedProcIns(actTaskService.getFinishedProcIns(act.getProcInsId()));
			}
		}
		String url = ActUtils.getFormUrl(formKey, act);
		System.out.println("###############URL###############"+url);
//		ActContract actContract = new ActContract();
//		if(proContractId!=null){
//			ProContract proContract = proContractService.get(proContractId);
//			if(proContract!=null){
//				actContract.setProContract(proContract);
//				model.addAttribute("proContract", proContract);
//			}
//		}
//		model.addAttribute("actContract", actContract);
		return "redirect:" + url;
		// 传递参数到视图
//		String formUrl = ActUtils.getFormUrl(formKey, act);
//		model.addAttribute("act", act);
//		model.addAttribute("formUrl", formUrl);
//		return "modules/act/actTaskForm";
	}
	
	/**
	 * 启动流程
	 */
	@RequestMapping(value = "start")
	@ResponseBody
	public String start(Act act, String table, String id, Model model) throws Exception {
		actTaskService.startProcess(act.getProcDefKey(), act.getBusinessId(), act.getBusinessTable(), act.getTitle());
		return "true";//adminPath + "/act/task";
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "claim")
	@ResponseBody
	public String claim(Act act) {
		String userId = UserUtils.getUser().getLoginName();//ObjectUtils.toString(UserUtils.getUser().getId());
		actTaskService.claim(act.getTaskId(), userId);
		return "true";//adminPath + "/act/task";
	}
	
	/**
	 * 完成任务
	 * 		vars.keys=flag,pass
	 * 		vars.values=1,true
	 * 		vars.types=S,B  @see co.dc.ccpt.dckj.modules.act.utils.PropertyType
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	public String complete(Act act) {
		actTaskService.complete(act.getTaskId(), act.getProcInsId(), act.getComment(), act.getVars().getVariableMap());
		return "true";//adminPath + "/act/task";
	}
	
	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "trace/photo/{procDefId}/{execId}")
	public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId, HttpServletResponse response) throws Exception {
		InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);
		
		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}
	
	/**
	 * 输出跟踪流程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "trace/info/{proInsId}")
	public List<Map<String, Object>> traceInfo(@PathVariable("proInsId") String proInsId) throws Exception {
		List<Map<String, Object>> activityInfos = actTaskService.traceProcess(proInsId);
		return activityInfos;
	}

	/**
	 * 显示流程图
	 
	@RequestMapping(value = "processPic")
	public void processPic(String procDefId, HttpServletResponse response) throws Exception {
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		String diagramResourceName = procDef.getDiagramResourceName();
		InputStream imageStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}*/
	
	/**
	 * 获取跟踪信息
	 
	@RequestMapping(value = "processMap")
	public String processMap(String procDefId, String proInstId, Model model)
			throws Exception {
		List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(procDefId)
				.singleResult();
		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
		String processDefinitionId = pdImpl.getId();// 流程标识
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(proInstId);
		for (String activeId : activeActivityIds) {
			for (ActivityImpl activityImpl : activitiList) {
				String id = activityImpl.getId();
				if (activityImpl.isScope()) {
					if (activityImpl.getActivities().size() > 1) {
						List<ActivityImpl> subAcList = activityImpl
								.getActivities();
						for (ActivityImpl subActImpl : subAcList) {
							String subid = subActImpl.getId();
							System.out.println("subImpl:" + subid);
							if (activeId.equals(subid)) {// 获得执行到那个节点
								actImpls.add(subActImpl);
								break;
							}
						}
					}
				}
				if (activeId.equals(id)) {// 获得执行到那个节点
					actImpls.add(activityImpl);
					System.out.println(id);
				}
			}
		}
		model.addAttribute("procDefId", procDefId);
		model.addAttribute("proInstId", proInstId);
		model.addAttribute("actImpls", actImpls);
		return "modules/act/actTaskMap";
	}*/
	
	/**
	 * 删除任务
	 * @param taskId 流程实例ID
	 * @param reason 删除原因
	 */
	@ResponseBody
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteTask")
	public AjaxJson deleteTask(String taskId, String reason, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		if (StringUtils.isBlank(reason)){
			j.setSuccess(false);
			j.setMsg("请填写删除原因");
		}else{
			actTaskService.deleteTask(taskId, reason);
			j.setSuccess(true);
			j.setMsg("删除任务成功，任务ID=" + taskId);
		}
		return j;
	}
}
