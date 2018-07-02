/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.oa.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.act.entity.Act;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.entity.Enclosuretab;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.oa.entity.ActSubContract;
import co.dc.ccpt.modules.oa.service.ActSubContractService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.utils.UserUtils;

/**
 * 审批Controller
 * @author dckj 
 * @version 2017-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/actSubContract")
public class ActSubContractController extends BaseController {

	@Autowired
	private ActSubContractService actSubContractService;
	
	@Autowired 
	private SubProContractService subProContractService;
	
	@Autowired 
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@ModelAttribute
	public ActSubContract get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		ActSubContract actSubContract = null;
		if (StringUtils.isNotBlank(id)){
			actSubContract = actSubContractService.get(id);
//		}else if (StringUtils.isNotBlank(procInsId)){
//			testAudit = testAuditService.getByProcInsId(procInsId);
		}
		if (actSubContract == null){
			actSubContract = new ActSubContract();
		}
		return actSubContract;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(ActSubContract actSubContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			actSubContract.setCreateBy(user);
		}
        Page<ActSubContract> page = actSubContractService.findPage(new Page<ActSubContract>(request, response), actSubContract); 
        model.addAttribute("page", page);
		return "modules/oa/actSubContract/actSubContractList";
	}
	
	/**
	 * 申请单填写
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(ActSubContract actSubContract, Model model) {
		
		String view = "actSubContractForm";
		
		// 查看审批申请单
		if (StringUtils.isNotBlank(actSubContract.getId())){//.getAct().getProcInsId())){

			// 环节编号
			String taskDefKey = actSubContract.getAct().getTaskDefKey();
			
			// 查看工单
			if(actSubContract.getAct().isFinishTask()){
				view = "actSubContractView";
			}
			// 修改环节
			else if ("modify".equals(taskDefKey)){
				view = "actSubContractForm";
			}
			// 审核环节
			else if ("parallel".equals(taskDefKey)){
				view = "actSubContractAudit";
//				String formKey = "/oa/testAudit";
//				return "redirect:" + ActUtils.getFormUrl(formKey, testAudit.getAct());
			}
			// 审核环节2
			else if ("manage_approval".equals(taskDefKey)){
				view = "actSubContractAudit";
			}
			//审核环节3
			else if ("chairman_approval".equals(taskDefKey)){
				view = "actSubContractAudit";
			}
			// 兑现环节
//			else if ("apply_end".equals(taskDefKey)){
//				view = "AttachContractAudit";
//			}
		}

		model.addAttribute("actSubContract", actSubContract);
		return "modules/oa/actSubContract/" + view;
	}
	
	/**
	 * 申请单保存/修改
	 * @param testAudit
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(ActSubContract actSubContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actSubContract)){
			return form(actSubContract, model);
		}
		actSubContractService.save(actSubContract);
		addMessage(redirectAttributes, "提交审批成功");
		if(actSubContract.getId()==null || actSubContract.getId().equals("")){
			return "redirect:" + adminPath + "/act/task/process/";//发起流程
		}else{
			//审批通过更改合同审批状态-->审批通过(1-->2)
			Act act = actSubContract.getAct();
			if(act != null){
				String taskKey = act.getTaskDefKey();
				if(taskKey==null || taskKey.equals("")){
					subProContractService.updateSubProContractStatus(actSubContract, 1);
				}else if(taskKey.equals("chairman_approval")){
					subProContractService.updateSubProContractStatus(actSubContract, 2);
				}else if(taskKey.equals("modify")){//若为合同修改
					if(act.getFlag().equals("no")){//销毁
						subProContractService.updateSubProContractStatus(actSubContract, 3);
					}
				}
			}
			return "redirect:" + adminPath + "/act/task/todo/";//待办任务
		}
		
	}

	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAudit")
	public String saveAudit(ActSubContract actSubContract, Model model) {
		if (StringUtils.isBlank(actSubContract.getAct().getFlag())
				|| StringUtils.isBlank(actSubContract.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(actSubContract, model);
		}else{
			Act act = actSubContract.getAct();
			if(act!=null){
				String taskKey = act.getTaskDefKey();
				if(taskKey.equals("parallel")){//会签节点
					System.out.println("会签节点！");
				}else if(taskKey.equals("chairman_approval")){//董事长审批
					actSubContractService.updateSubProContractStatus(actSubContract, 2);
				}else if(taskKey.equals("modify")){//若为合同修改
					if(act.getFlag().equals("no")){//销毁
						actSubContractService.updateSubProContractStatus(actSubContract, 3);
					}
				}
			}
		}
		actSubContractService.auditSave(actSubContract);
		return "redirect:" + adminPath + "/act/task";
	}
	
	/**
	 * 删除工单
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(ActSubContract actSubContract, RedirectAttributes redirectAttributes) {
		actSubContractService.delete(actSubContract);
		addMessage(redirectAttributes, "删除审批成功");
		return "redirect:" + adminPath + "/oa/actSubContract/?repage";
	}
	
	/**
	 * 通过合同名称查询出所有未审批的分包合同
	 * @param contractName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAppointSubProContractByName",method = RequestMethod.POST)
	public List<SubProContract> getAppointSubProContractByName(@RequestParam String subProContractName) {
		List<SubProContract> subProContractList = new ArrayList<SubProContract>();
		subProContractList = subProContractService.getAppointSubProContractByName(subProContractName);
		return subProContractList;
	}
	
	/**
	 * 通过分包合同id查出对应附件集合
	 * @param subProContract
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getEnclosureContByForeginId",method = RequestMethod.POST)
	public List<Enclosuretab> getEnclosureContByForeginId(@RequestBody SubProContract subProContract){
		List<Enclosuretab> enclosuretabList = new ArrayList<Enclosuretab>();
		String foreginId = subProContract.getId();
		enclosuretabList = enclosuretabService.getEnclosureContByForeginId(foreginId);
		return enclosuretabList;
	}

}
