package co.dc.ccpt.modules.contractmanagement.contracttemp.web;

import java.util.Map;

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
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.contractmanagement.contracttemp.entity.ContractTemp;
import co.dc.ccpt.modules.contractmanagement.contracttemp.service.ContractTempService;

@Controller
@RequestMapping(value = "${adminPath}/contracttemp")
public class ContractTempController extends BaseController{
	@Autowired
	public ContractTempService contractTempService;
	
	@ModelAttribute
	public ContractTemp get(@RequestParam(required=false) String id) {
		ContractTemp entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractTempService.get(id);
		}
		if (entity == null){
			entity = new ContractTemp();
		}
		return entity;
	}
	
	/**
	 * 总包合同列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/contractmanagement/contracttemp/contracttempList";
	}
	
	/**
	 * 总包合同列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractTemp contractTemp, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractTemp> page = contractTempService.findPage(new Page<ContractTemp>(request, response), contractTemp); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑总包合同表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ContractTemp contractTemp, Model model) {
		if(StringUtils.isBlank(contractTemp.getId())){//如果ID是空为添加
			//设置编号：
			String num = contractTempService.setTempNum();
			contractTemp.setTempNum(num);
			model.addAttribute("contractTemp", contractTemp);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("contractTemp", contractTemp);
			model.addAttribute("edit",true);
		}
		return "modules/contractmanagement/contracttemp/contracttempForm";
	}
	
	/**
	 * 保存总包合同信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ContractTemp contractTemp, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractTemp)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		contractTempService.save(contractTemp);//保存
		j.setSuccess(true);
		j.setMsg("保存总包合同信息成功！");
		return j;
	}
	
	/**
	 * 删除总包合同
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractTemp contractTemp, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractTempService.delete(contractTemp);
		j.setMsg("删除总包合同成功!");
		return j;
	}
	
	/**
	 * 批量删除总包合同
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//删除前判断：
			ContractTemp contractTemp = contractTempService.get(id);
			if(contractTemp != null){
				contractTempService.delete(contractTemp);
			}
		}
		j.setMsg("删除总包合同信息成功!");
		return j;
	}
	
}
