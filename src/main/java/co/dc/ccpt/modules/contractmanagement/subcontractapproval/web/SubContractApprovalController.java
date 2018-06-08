package co.dc.ccpt.modules.contractmanagement.subcontractapproval.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

import co.dc.ccpt.common.config.Global;
import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.common.utils.DateUtils;
import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.common.utils.excel.ExportExcel;
import co.dc.ccpt.common.utils.excel.ImportExcel;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service.SubpackageProgramService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.subcontractapproval.entity.SubContractApproval;
import co.dc.ccpt.modules.contractmanagement.subcontractapproval.service.SubContractApprovalService;
import co.dc.ccpt.modules.contractmanagement.subprocontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.subprocontract.service.SubProContractService;
import co.dc.ccpt.modules.sys.service.SystemService;

@Controller
@RequestMapping(value = "${adminPath}/subcontractapproval")
public class SubContractApprovalController extends BaseController{
	@Autowired
	public SubContractApprovalService subContractApprovalService;
	
	@Autowired
	public SystemService userService;
	
	@Autowired
	public ProContractService proContractService;
	
	@Autowired
	public SubpackageProgramService subpackageProgramService;
	
	@Autowired
	public SubProContractService subProContractService;
	
	@ModelAttribute
	public SubContractApproval get(@RequestParam(required=false) String id) {
		SubContractApproval entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = subContractApprovalService.get(id);
		}
		if (entity == null){
			entity = new SubContractApproval();
		}
		return entity;
	}
	
	/**
	 * 分包合同列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/contractmanagement/subcontractapproval/subcontractapprovalList";
	}
	
	/**
	 * 分包合同列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(SubContractApproval subContractApproval, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SubContractApproval> page = subContractApprovalService.findPage(new Page<SubContractApproval>(request, response), subContractApproval); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分包合同表单页面
	 */
	@RequestMapping(value = "form")
	public String form(SubContractApproval subContractApproval, Model model) {
		model.addAttribute("subContractApproval", subContractApproval);
		if(StringUtils.isBlank(subContractApproval.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/contractmanagement/subcontractapproval/subcontractapprovalForm";
	}

	/**
	 * 通过分包项目名称模糊匹配出对应的集合
	 */
	@ResponseBody
	@RequestMapping(value = "getSubProContractList",method = RequestMethod.POST)
	public List<SubProContract> getSubProContractList(@RequestParam String subProContractName) {
		List<SubProContract> subProContractList = new ArrayList<SubProContract>();
		subProContractList = subProContractService.getSubProContractList(subProContractName);
		return subProContractList;
	}
	
	/**
	 * 保存分包合同信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(SubContractApproval subContractApproval, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subContractApproval)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		subContractApprovalService.save(subContractApproval);//保存
		j.setSuccess(true);
		j.setMsg("保存分包合同信息成功！");
		return j;
	}
	
	
	/**
	 * 删除分包合同
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(SubContractApproval subContractApproval, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		subContractApprovalService.delete(subContractApproval);
		j.setMsg("删除分包合同成功!");
		return j;
	}
	
	/**
	 * 批量删除分包合同
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			subContractApprovalService.delete(subContractApprovalService.get(id));
		}
		j.setMsg("删除分包合同信息成功!");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SubContractApproval subContractApproval, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分包合同"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SubContractApproval> page = subContractApprovalService.findPage(new Page<SubContractApproval>(request, response, -1), subContractApproval);
    		new ExportExcel("分包合同", SubContractApproval.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出分包合同记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据
	 */
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SubContractApproval> list = ei.getDataList(SubContractApproval.class);
			for (SubContractApproval subContractApproval : list){
				try{
					subContractApprovalService.save(subContractApproval);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分包合同记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条分包合同记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入分包合同失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subprocontract/?repage";
    }
	
	/**
	 * 下载导入分包合同数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分包合同数据导入模板.xlsx";
    		List<SubContractApproval> list = Lists.newArrayList(); 
    		new ExportExcel("分包合同数据", SubContractApproval.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/SubContractApproval/?repage";
    }
}
