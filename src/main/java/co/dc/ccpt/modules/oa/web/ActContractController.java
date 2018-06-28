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
import co.dc.ccpt.modules.oa.entity.ActContract;
import co.dc.ccpt.modules.oa.service.ActContractService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.utils.UserUtils;

/**
 * 审批Controller
 * @author dckj
 * @version 2017-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/actContract")
public class ActContractController extends BaseController {

	@Autowired
	private ActContractService actContractService;
	
	@Autowired 
	private ProContractService proContractService;

	@Autowired 
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public ActContract get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		ActContract actContract = null;
		if (StringUtils.isNotBlank(id)){
			actContract = actContractService.get(id);
//		}else if (StringUtils.isNotBlank(procInsId)){
//			testAudit = testAuditService.getByProcInsId(procInsId);
		}
		if (actContract == null){
			actContract = new ActContract();
		}
		return actContract;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(ActContract actContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			actContract.setCreateBy(user);
		}
        Page<ActContract> page = actContractService.findPage(new Page<ActContract>(request, response), actContract); 
        model.addAttribute("page", page);
		return "modules/oa/actContract/actContractList";
	}
	
	/**
	 * 申请单填写
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(ActContract actContract, Model model) {
		
		String view = "actContractForm";
		
		// 查看审批申请单
		if (StringUtils.isNotBlank(actContract.getId())){//.getAct().getProcInsId())){

			// 环节编号
			String taskDefKey = actContract.getAct().getTaskDefKey();
			
			// 查看工单
			if(actContract.getAct().isFinishTask()){
				view = "actContractView";
			}
			// 审核环节
			else if ("lead".equals(taskDefKey)){
				view = "actContractAudit";
//				String formKey = "/oa/testAudit";
//				return "redirect:" + ActUtils.getFormUrl(formKey, testAudit.getAct());
			}
			// 修改环节
			else if ("modify".equals(taskDefKey)){
				view = "actContractForm";
			}
			// 审核环节2
			else if ("mainLead".equals(taskDefKey)){
				view = "actContractAudit";
			}
			// 兑现环节
//			else if ("apply_end".equals(taskDefKey)){
//				view = "actContractAudit";
//			}
		}

		model.addAttribute("actContract", actContract);
		return "modules/oa/actContract/" + view;
	}
	
	/**
	 * 申请单保存/修改
	 * @param testAudit
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(ActContract actContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actContract)){
			return form(actContract, model);
		}
		actContractService.save(actContract);
		addMessage(redirectAttributes, "提交审批成功");
		if(actContract.getId()==null || actContract.getId().equals("")){
			return "redirect:" + adminPath + "/act/task/process/";//发起流程
		}else{
			//审批通过更改合同审批状态-->审批通过(1-->2)
			Act act = actContract.getAct();
			if(act != null){
				String taskKey = act.getTaskDefKey();
				if(taskKey==null || taskKey.equals("")){//审批中
					proContractService.updateProContractStatus(actContract, 1);
				}else if(taskKey.equals("mainLead")){//审批通过
					proContractService.updateProContractStatus(actContract, 2);
				}else if(taskKey.equals("contract_modify")){//若为合同修改
					if(act.getFlag().equals("no")){//销毁--审批不通过
						proContractService.updateProContractStatus(actContract, 3);
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
	public String saveAudit(ActContract actContract, Model model) {
		if (StringUtils.isBlank(actContract.getAct().getFlag())
				|| StringUtils.isBlank(actContract.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(actContract, model);
		}else{
			Act act = actContract.getAct();
			if(act!=null){
				String taskKey = act.getTaskDefKey();
				if(taskKey.equals("mainLead")){//审批通过
					proContractService.updateProContractStatus(actContract, 2);
				}else if(taskKey.equals("contract_modify")){//若为合同修改
					if(act.getFlag().equals("no")){//销毁--审批不通过
						proContractService.updateProContractStatus(actContract, 3);
					}
				}
			}
		}
		actContractService.auditSave(actContract);
		return "redirect:" + adminPath + "/act/task";
	}
	
	/**
	 * 删除工单
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(ActContract actContract, RedirectAttributes redirectAttributes) {
		actContractService.delete(actContract);
		addMessage(redirectAttributes, "删除审批成功");
		return "redirect:" + adminPath + "/oa/actContract/?repage";
	}
	
	
	/**
	 * 通过合同名称查询出所有未审批的市场投标合同
	 */
	@ResponseBody
	@RequestMapping(value = "getMarketProContractByName",method = RequestMethod.POST)
	public List<ProContract> getMarketProContractByName(@RequestParam String contractName) {
		List<ProContract> proContractList = new ArrayList<ProContract>();
		proContractList = proContractService.getMarketProContractByName(contractName);
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
