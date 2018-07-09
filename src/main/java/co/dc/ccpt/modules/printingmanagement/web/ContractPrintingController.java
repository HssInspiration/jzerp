/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.printingmanagement.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.printingmanagement.entity.ContractPrinting;
import co.dc.ccpt.modules.printingmanagement.service.ContractPrintingService;

/**
 * 总包合同用章管理Controller
 * @author lxh
 */
@Controller
@RequestMapping(value = "${adminPath}/contractprint/proprinting")
public class ContractPrintingController extends BaseController {

	@Autowired
	private ContractPrintingService contractPrintingService;
	
	@Autowired
	private ProContractService proContractService;
	
	@Autowired
	private SubProContractService subProContractService;
	
	@ModelAttribute
	public ContractPrinting get(@RequestParam(required=false) String id) {
		ContractPrinting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractPrintingService.get(id);
		}	
		if (entity == null){
			entity = new ContractPrinting();
		}
		return entity;
	}
	
	@ModelAttribute
	public ProContract getProContract(@RequestParam(required=false) String proContractId) {
		ProContract entity = null;
		if (StringUtils.isNotBlank(proContractId)){
			entity = proContractService.get(proContractId);
		}
		if (entity == null){
			entity = new ProContract();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubProContract getSubProContract(@RequestParam(required=false) String SubContractId) {
		SubProContract entity = null;
		if (StringUtils.isNotBlank(SubContractId)){
			entity = subProContractService.get(SubContractId);
		}
		if (entity == null){
			entity = new SubProContract();
		}
		return entity;
	}
	
	/**
	 * 保证金审批列表页面
	 */
//	@RequiresPermissions("ContractPrinting:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/printingmanagement/contractprint/proprinting/contractPrintingList";
	}
	
	@ResponseBody
//	@RequiresPermissions("ContractPrinting:ContractPrinting:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractPrinting contractPrinting, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractPrinting> page = contractPrintingService.findPage(new Page<ContractPrinting>(request, response), contractPrinting); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑表单页面
	 */
//	@RequiresPermissions(value={"ContractPrinting:ContractPrinting:view","ContractPrinting:ContractPrinting:add","ContractPrinting:ContractPrinting:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ContractPrinting contractPrinting, ProContract proContract, Model model) {
		String proContractId = proContract.getId();
		if(StringUtils.isNotBlank(proContractId)){//总包合同id非空
			proContract = proContractService.get(proContract);
			if(proContract != null){
				contractPrinting.setProContract(proContract);//将总包对象放入用章管理对象中
				//设置用章编号
				String printNum = contractPrintingService.getProContractNum();
				contractPrinting.setPrintNum(printNum);
				//设置对应的用章类型--0（总包合同用章）
				contractPrinting.setPrintType(0);
			}
		}
		model.addAttribute("contractPrinting", contractPrinting);
		return "modules/printingmanagement/contractprint/proprinting/contractPrintingForm";
	}
	
	/**
	 * 用章表单
	 * @param contractPrinting
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "printForm")
	public String printForm(ContractPrinting contractPrinting, Model model) {
		model.addAttribute("contractPrinting", contractPrinting);
		return "modules/printingmanagement/contractprint/proprinting/proPrintingForm";
	}
	
	
	
	
	/**
	 * 更新状态
	 */
	@ResponseBody
	@RequestMapping(value = "updateStampStatus")
	public AjaxJson updateStampStatus(ContractPrinting contractPrinting, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractPrinting)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		contractPrintingService.updateStampStatus(contractPrinting);
		j.setSuccess(true);
		j.setMsg("保存成功");
		return j;
	}
	
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ContractPrinting contractPrinting, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractPrinting)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增之前用总包合同id查询是否存在对应集合（设置用章次数）
		List<ContractPrinting> contractPrintList = contractPrintingService.getContractPrintingByProId(contractPrinting);
		if(contractPrintList!=null && contractPrintList.size()>0){
			contractPrinting.setTimes("第"+(contractPrintList.size()+1)+"次申请");
		}else{
			contractPrinting.setTimes("第"+1+"次申请");
		}
		contractPrintingService.save(contractPrinting);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getProContractByName",method = RequestMethod.POST)
	public List<ProContract> getProContractByName(@RequestParam String contractName) {
		List<ProContract> proContractList = new ArrayList<ProContract>();
		proContractList = proContractService.getProContractListByName(contractName);
		return proContractList;
	}
	
	/**
	 * 通过总包合同id获取总包用章实例
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"getContractPrinting", "getPrintingTimes"},method = RequestMethod.POST)
	public List<ContractPrinting> getContractPrinting(@RequestBody ProContract proContract) {
		ContractPrinting contractPrint = new ContractPrinting();
		if(StringUtils.isNotBlank(proContract.getId())){
			contractPrint.setProContract(proContract);
		}
		List<ContractPrinting> contractPrintList = new ArrayList<ContractPrinting>();
		contractPrintList = contractPrintingService.getContractPrintingByProId(contractPrint);
		return contractPrintList;
	}
	
	/**
	 * 删除
	 */	
	@ResponseBody
//	@RequiresPermissions("ContractPrinting:ContractPrinting:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractPrinting contractPrinting, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractPrintingService.delete(contractPrinting);
		j.setMsg("删除成功");
		return j;
	}
	
	
	
}