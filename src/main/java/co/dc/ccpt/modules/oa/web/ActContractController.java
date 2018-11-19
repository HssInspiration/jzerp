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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.entity.Enclosuretab;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.contractmanagement.contracttext.entity.ContractText;
import co.dc.ccpt.modules.contractmanagement.contracttext.service.ContractTextService;
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
	
	@Autowired
	private ContractTextService contractTextService;
	
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
	
	@ModelAttribute
	public ProContract getProContract(@RequestParam(required=false) String proContractId){
		ProContract proContract = null;
		if (StringUtils.isNotBlank(proContractId)){
			proContract = proContractService.get(proContractId);
		}
		if (proContract == null){
			proContract = new ProContract();
		}
		return proContract;
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
			else if ("contract_modify".equals(taskDefKey)){
				view = "actContractForm";
			}
			// 总经理审核环节&完成环节
			else if ("mainLead".equals(taskDefKey)){
				view = "actContractAudit";
			}
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
			actContractService.changeStatusForForm(actContract);//执行状态变更操作
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
		if (StringUtils.isBlank(actContract.getAct().getFlag())){
//				|| StringUtils.isBlank(actContract.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(actContract, model);
		}else{
			actContractService.changeStatusForAudit(actContract);//执行状态变更操作
		}
		actContractService.auditSave(actContract);
		model.addAttribute("actContract", actContract);
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
	 * 通过合同名称查询出所有未审批的市场投标合同
	 */
	@ResponseBody
	@RequestMapping(value = "getProContractByName",method = RequestMethod.POST)
	public List<ProContract> getProContractByName(@RequestParam String contractName) {
		List<ProContract> proContractList = new ArrayList<ProContract>();
		proContractList = proContractService.getProContractByName(contractName);
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
	
	/**
	 * 通过合同id查出对应合同正文
	 * @param proContract
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getContractTextByContractId",method = RequestMethod.POST)
	public ContractText getContractTextByContractId(@RequestBody ProContract proContract){
		String proContractId = proContract.getId();
		ContractText contractText = new ContractText();
		contractText.setContractId(proContractId);
		contractText = contractTextService.getByContractId(contractText);
		if(contractText != null){
			return contractText;
		}else{
			return new ContractText();
		}
	}
	
	/**
	 * 将word转换成PDF方便在线预览
	 * @param proContract
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="exchangeWordToPdf",method = RequestMethod.POST)
	public ActContract exchangeWordToPdf(@RequestBody ActContract actContract){
		actContract = proContractService.exchangeWordToPdf(actContract);
		if(actContract != null){
			return actContract;
		}else{
			return new ActContract();
		}
	}
	
	@RequestMapping(value = "showActProcess")
	public String showProcess(ProContract proContract, Model model){
		ActContract actContract = new ActContract();
		if(StringUtils.isNotBlank(proContract.getId())){
			actContract = actContractService.getByProContract(proContract);
		}
		model.addAttribute("actContract", actContract);
		return "modules/oa/actContract/actContractShow";
	}
	
}
