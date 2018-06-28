/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.oa.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.oa.entity.AttachContract;
import co.dc.ccpt.modules.oa.service.AttachContractService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.utils.UserUtils;

/**
 * 审批Controller
 * @author dckj 
 * @version 2017-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/attachContract")
public class AttachContractController extends BaseController {

	@Autowired
	private AttachContractService attachContractService;
	
	@Autowired 
	private ProContractService proContractService;
	
	@Autowired 
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public AttachContract get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		AttachContract AttachContract = null;
		if (StringUtils.isNotBlank(id)){
			AttachContract = attachContractService.get(id);
//		}else if (StringUtils.isNotBlank(procInsId)){
//			testAudit = testAuditService.getByProcInsId(procInsId);
		}
		if (AttachContract == null){
			AttachContract = new AttachContract();
		}
		return AttachContract;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(AttachContract AttachContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			AttachContract.setCreateBy(user);
		}
        Page<AttachContract> page = attachContractService.findPage(new Page<AttachContract>(request, response), AttachContract); 
        model.addAttribute("page", page);
		return "modules/oa/attachContract/attachContractList";
	}
	
	/**
	 * 申请单填写
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(AttachContract attachContract, Model model) {
		
		String view = "attachContractForm";
		
		// 查看审批申请单
		if (StringUtils.isNotBlank(attachContract.getId())){//.getAct().getProcInsId())){

			// 环节编号
			String taskDefKey = attachContract.getAct().getTaskDefKey();
			
			// 查看工单
			if(attachContract.getAct().isFinishTask()){
				view = "attachContractView";
			}
			// 修改环节
			else if ("modify".equals(taskDefKey)){
				view = "attachContractForm";
			}
			// 审核环节
			else if ("subLead".equals(taskDefKey)){
				view = "attachContractAudit";
//				String formKey = "/oa/testAudit";
//				return "redirect:" + ActUtils.getFormUrl(formKey, testAudit.getAct());
			}
			else if ("lead".equals(taskDefKey)){
				view = "attachContractAudit";
//				String formKey = "/oa/testAudit";
//				return "redirect:" + ActUtils.getFormUrl(formKey, testAudit.getAct());
			}
			// 审核环节2
			else if ("mainLead".equals(taskDefKey)){
				view = "attachContractAudit";
			}
			// 兑现环节
//			else if ("apply_end".equals(taskDefKey)){
//				view = "AttachContractAudit";
//			}
		}

		model.addAttribute("attachContract", attachContract);
		return "modules/oa/attachContract/" + view;
	}
	
	/**
	 * 申请单保存/修改
	 * @param testAudit
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(AttachContract attachContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, attachContract)){
			return form(attachContract, model);
		}
		attachContractService.save(attachContract);
		addMessage(redirectAttributes, "提交审批成功");
		if(attachContract.getId()==null || attachContract.getId().equals("")){
			return "redirect:" + adminPath + "/act/task/process/";//发起流程
		}else{
			//审批通过更改合同审批状态-->审批通过(1-->2)
			Act act = attachContract.getAct();
			if(act != null){
				String taskKey = act.getTaskDefKey();
				if(taskKey==null || taskKey.equals("")){
					proContractService.updateProContractStatus(attachContract, 1);
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
	public String saveAudit(AttachContract attachContract, Model model) {
		if (StringUtils.isBlank(attachContract.getAct().getFlag())
				|| StringUtils.isBlank(attachContract.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(attachContract, model);
		}else{
			Act act = attachContract.getAct();
			if(act!=null){
				String taskKey = act.getTaskDefKey();
				if(taskKey.equals("mainLead")){
					proContractService.updateProContractStatus(attachContract, 2);
				}else if(taskKey.equals("contract_modify")){//若为合同修改
					if(act.getFlag().equals("no")){//销毁
						proContractService.updateProContractStatus(attachContract, 3);
					}
				}
			}
		}
		attachContractService.auditSave(attachContract);
		return "redirect:" + adminPath + "/act/task";
	}
	
	/**
	 * 删除工单
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(AttachContract attachContract, RedirectAttributes redirectAttributes) {
		attachContractService.delete(attachContract);
		addMessage(redirectAttributes, "删除审批成功");
		return "redirect:" + adminPath + "/oa/attachContract/?repage";
	}
	
	
	/**
	 * 通过合同名称查询出所有未审批的市场投标合同
	 * @param contractName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAppointProContractByName",method = RequestMethod.POST)
	public List<ProContract> getAppointProContractByName(@RequestParam String contractName) {
		List<ProContract> proContractList = new ArrayList<ProContract>();
		proContractList = proContractService.getAppointProContractByName(contractName);
		return proContractList;
	}
	
	/**
	 * 通过总包合同id查出对应附件集合
	 * @param proContract
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getEnclosureContByForeginId",method = RequestMethod.POST)
	public List<Enclosuretab> getEnclosureContByForeginId(@RequestBody ProContract proContract){
		List<Enclosuretab> enclosuretabList = new ArrayList<Enclosuretab>();
		String foreginId = proContract.getId();
		enclosuretabList = enclosuretabService.getEnclosureContByForeginId(foreginId);
		return enclosuretabList;
	}

}
