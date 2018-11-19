package co.dc.ccpt.modules.contractmanagement.contracttext.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.common.utils.AddWaterMarkUtil;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.contractmanagement.contracttext.entity.ContractText;
import co.dc.ccpt.modules.contractmanagement.contracttext.service.ContractTextService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;

@Controller
@RequestMapping(value = "${adminPath}/contractTextManage")
public class ContractTextController extends BaseController{
	@Autowired
	public ContractTextService contractTextService;
	
	@Autowired
	public ProContractService proContractService;
	
	@Autowired
	public SubProContractService subContractService;
	
	@ModelAttribute
	public ContractText get(@RequestParam(required = false) String id){
		ContractText entity = null;
		if(StringUtils.isNotBlank(id)){
			entity = contractTextService.get(id);
		}else{
			entity = new ContractText();
		}
		return entity;
	}
	
	@ModelAttribute
	public ProContract getProContract(@RequestParam(required = false) String contractId){
		ProContract entity = null;
		if(StringUtils.isNotBlank(contractId)){
			entity = proContractService.get(contractId);
		}else{
			entity = new ProContract();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubProContract getSubContract(@RequestParam(required = false) String subContractId){
		SubProContract entity = null;
		if(StringUtils.isNotBlank(subContractId)){
			entity = subContractService.get(subContractId);
		}else{
			entity = new SubProContract();
		}
		return entity;
	}
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value = {"list",""})
	public String list(ProContract proContract, SubProContract subProContract, Model model){
		String contractId = null;
		
		if(StringUtils.isNotBlank(proContract.getId())){
			contractId = proContract.getId();
		}
		
		if(StringUtils.isNotBlank(subProContract.getId())){
			contractId = subProContract.getId();
		}
		model.addAttribute("contractId", contractId);
		return "modules/contractmanagement/contracttext/contractTextList";
	}
	
	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(String contractId, ContractText contractText, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isBlank(contractId)){
			contractId = UUID.randomUUID().toString().replaceAll("-", "");
		}
		contractText.setContractId(contractId);
		Page<ContractText> page = contractTextService.findPage(new Page<ContractText>(request, response), contractText); 
		return getBootstrapData(page);
	}
	
	/**
	 * 列只显示合同正文页面
	 * @return
	 */
	@RequestMapping(value = {"show"})
	public String show(ContractText contractText, ProContract proContract, SubProContract subProContract, Model model){
		Integer approvalStatus = null;
		Integer contractStatus = null;
		String contractTextCont = "";
		contractText = contractTextService.getByContractId(contractText);
		if(contractText != null){
			model.addAttribute("contractText", contractText);
			contractTextCont = contractText.getContractTextCont();
		}
		
		if(StringUtils.isNotBlank(proContract.getId())){
			contractText.setContractId(proContract.getId());
			proContract = proContractService.get(proContract);
			if(proContract != null){
				approvalStatus = proContract.getApprovalStatus();
				contractStatus = proContract.getContractStatus();
				model.addAttribute("approvalStatus", approvalStatus);	
				model.addAttribute("contractStatus", contractStatus);	
			}
		}
		
		if(StringUtils.isNotBlank(subProContract.getId())){
			contractText.setContractId(subProContract.getId());
			subProContract = subContractService.get(subProContract);
			if(subProContract != null){
				approvalStatus = proContract.getApprovalStatus();
				contractStatus = proContract.getContractStatus();
				model.addAttribute("approvalStatus", approvalStatus);	
				model.addAttribute("contractStatus", contractStatus);
			}
		}
		
		if(approvalStatus != null && approvalStatus == 2){// 审批通过
			if(contractStatus != null && contractStatus == 1){// 合同生效
				// 为文档添加水印：
				if(StringUtils.isNotBlank(contractTextCont)){//正文内容非空
					// 1.将'/ccpt'更改为'D:/jzerp_files'
					contractTextCont = contractTextCont.replace("/ccpt","D:/jzerp_files");
					AddWaterMarkUtil awmu = new AddWaterMarkUtil();
					boolean b = awmu.addWaterMark(contractTextCont,"D:/test/test3.png",-10,100,450,450);
					if(b){
						System.out.println("转换成功!");
						model.addAttribute("contractTextCont", contractTextCont);
					}else{
						System.out.println("转换失败!");
					}
				}
			}
		}
		return "modules/contractmanagement/contracttext/contractTextShow";
	}

	/**
	 * 查看，增加，编辑表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ContractText contractText, ProContract proContract, SubProContract subProContract, Model model) {
		if(StringUtils.isBlank(contractText.getId())){
			model.addAttribute("isAdd", true);
			if(StringUtils.isNotBlank(proContract.getId())){
				contractText.setContractId(proContract.getId());
				model.addAttribute("proContract", proContract);
			}
			
			if(StringUtils.isNotBlank(subProContract.getId())){
				contractText.setContractId(subProContract.getId());
				model.addAttribute("subProContract", subProContract);
			}
			contractText.setContractTextNum(contractTextService.setContractTextNum());
		}else{
			model.addAttribute("edit", true);
		}
		
		model.addAttribute("contractText", contractText);
		return "modules/contractmanagement/contracttext/contractTextForm";
	}
	
	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ContractText contractText, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractText)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		contractTextService.save(contractText);//保存
		j.setSuccess(true);
		j.setMsg("保存合同正文成功！");
		return j;
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractText contractText, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractTextService.delete(contractText);
		j.setMsg("删除总包合同成功!");
		return j;
	}
	
	/**
	 * 批量删除
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		Collection<ContractText> strList = new ArrayList<ContractText>();
		for(String id : idArray){
			ContractText contractText = contractTextService.get(id);
//			contractTextService.delete(contractText);
			contractText.setDelFlag("1");
			strList.add(contractText);
		}
		contractTextService.deleteAllByLogic(strList);
		j.setMsg("删除合同正文成功!");
		return j;
	}
}
