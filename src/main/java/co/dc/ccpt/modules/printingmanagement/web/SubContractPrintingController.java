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
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.printingmanagement.entity.ContractPrinting;
import co.dc.ccpt.modules.printingmanagement.entity.SubContractPrinting;
import co.dc.ccpt.modules.printingmanagement.service.SubContractPrintingService;


/**
 * 分包合同用章管理Controller
 * @author lxh
 */
@Controller
@RequestMapping(value = "${adminPath}/contractprint/subprinting")
public class SubContractPrintingController extends BaseController {

	@Autowired
	private SubContractPrintingService subContractPrintingService;
	
	@Autowired
	private SubProContractService subProContractService;
	@ModelAttribute
	public SubContractPrinting get(@RequestParam(required=false) String id) {
		SubContractPrinting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = subContractPrintingService.get(id);
		}
		if (entity == null){
			entity = new SubContractPrinting();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubProContract getSubProContract(@RequestParam(required=false) String subContractId) {
		SubProContract entity = null;
		if (StringUtils.isNotBlank(subContractId)){
			entity = subProContractService.get(subContractId);
		}
		if (entity == null){
			entity = new SubProContract();
		}
		return entity;
	}
	/**
	 * 审批列表页面
	 */
//	@RequiresPermissions("SubContractPrinting:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/printingmanagement/contractprint/subprinting/subContractPrintingList";
	}
	
	@ResponseBody
//	@RequiresPermissions("SubContractPrinting:SubContractPrinting:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SubContractPrinting subContractPrinting, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SubContractPrinting> page = subContractPrintingService.findPage(new Page<SubContractPrinting>(request, response), subContractPrinting); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑表单页面
	 */
//	@RequiresPermissions(value={"SubContractPrinting:SubContractPrinting:view","SubContractPrinting:SubContractPrinting:add","SubContractPrinting:SubContractPrinting:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SubContractPrinting subContractPrinting, SubProContract subProContract, Model model) {
		String subProContractId = subProContract.getId();
		if(StringUtils.isNotBlank(subProContractId)){//总包合同id非空
			subProContract = subProContractService.get(subProContract);
			if(subProContract != null){
				subContractPrinting.setSubProContract(subProContract);//将总包对象放入用章管理对象中
				//设置用章编号
				String printNum = subContractPrintingService.getSubContractPrintingNum();
				subContractPrinting.setPrintNum(printNum);
				//设置对应的用章类型--1（分包合同用章）
				subContractPrinting.setPrintType(1);
				// 用章时间--默认当前时间
				subContractPrinting.setPrintDate(new Date());
			}
		}
		
		if(StringUtils.isBlank(subContractPrinting.getId())){
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit", true);
		}
		
		model.addAttribute("subContractPrinting", subContractPrinting);
		
		return "modules/printingmanagement/contractprint/subprinting/subContractPrintingForm";
	}
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(SubContractPrinting subContractPrinting, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subContractPrinting)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增之前用总包合同id查询是否存在对应集合（设置用章次数）
		List<SubContractPrinting> subContractPrintList = subContractPrintingService.getSubContractPrintingBySubId(subContractPrinting);
		if(subContractPrintList!=null && subContractPrintList.size()>0){
			subContractPrinting.setTimes("第"+(subContractPrintList.size()+1)+"次用章");
		}else{
			subContractPrinting.setTimes("第"+1+"次用章");
		}
		subContractPrintingService.save(subContractPrinting);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存保证金审批成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getSubProContractByName",method = RequestMethod.POST)
	public List<SubProContract> getSubProContractByName(@RequestParam String contractName) {
		List<SubProContract> subProContractList = new ArrayList<SubProContract>();
		subProContractList = subProContractService.getSubProContractListByName(contractName);
		return subProContractList;
	}
	
	/**
	 * 通过总包合同id获取总包用章实例
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSubContractPrinting",method = RequestMethod.POST)
	public List<SubContractPrinting> getSubContractPrinting(@RequestBody SubProContract subProContract) {
		SubContractPrinting subContractPrinting = new SubContractPrinting();
		if(StringUtils.isNotBlank(subProContract.getId())){
			subContractPrinting.setSubProContract(subProContract);
		}
		List<SubContractPrinting> contractPrintList = new ArrayList<SubContractPrinting>();
		contractPrintList = subContractPrintingService.getSubContractPrintingBySubId(subContractPrinting);
		return contractPrintList;
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
//	@RequiresPermissions("SubContractPrinting:SubContractPrinting:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SubContractPrinting subContractPrinting, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		subContractPrintingService.delete(subContractPrinting);
		j.setMsg("删除保证金审批成功");
		return j;
	}
}