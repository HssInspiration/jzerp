package co.dc.ccpt.modules.contractmanagement.procontractapproval.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontractapproval.entity.ProContractApproval;
import co.dc.ccpt.modules.contractmanagement.procontractapproval.service.ProContractApprovalService;
import co.dc.ccpt.modules.sys.service.SystemService;

@Controller
@RequestMapping(value = "${adminPath}/procontractapproval")
public class ProContractApprovalController extends BaseController{
	@Autowired
	public ProContractApprovalService proContractApprovalService;
	
	@Autowired
	public ProgramService programService;
	
	@Autowired
	public ProContractService proContractService;
	
	@Autowired
	public SystemService userService;
	
	@ModelAttribute
	public ProContractApproval get(@RequestParam(required=false) String id) {
		ProContractApproval entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = proContractApprovalService.get(id);
		}
		if (entity == null){
			entity = new ProContractApproval();
		}
		return entity;
	}
	
	/**
	 * 总包合同列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/contractmanagement/procontractapproval/procontractapprovalList";
	}
	
	/**
	 * 总包合同列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProContractApproval proContractApproval, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProContractApproval> page = proContractApprovalService.findPage(new Page<ProContractApproval>(request, response), proContractApproval); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑总包合同表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ProContractApproval proContractApproval, Model model) {
		model.addAttribute("proContractApproval", proContractApproval);
		if(StringUtils.isBlank(proContractApproval.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/contractmanagement/procontractapproval/procontractapprovalForm";
	}

	/**
	 * 保存总包合同信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ProContractApproval proContractApproval, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, proContractApproval)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		
		//新增或编辑表单保存
		proContractApprovalService.save(proContractApproval);//保存
		j.setSuccess(true);
		j.setMsg("保存总包合同信息成功！");
		return j;
	}
	
	/**
	 * 通过总包项目名称查询出所有符合条件的总包项目（已生效或者执行中）
	 */
	@ResponseBody
	@RequestMapping(value = "getProContractList",method = RequestMethod.POST)
	public List<ProContract> getProContractList(@RequestParam String contractName) {
		List<ProContract> proContractList = new ArrayList<ProContract>();
		proContractList = proContractService.getProContractList(contractName);
		return proContractList;
	}
	
	/**
	 * 删除总包合同
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProContractApproval proContractApproval, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		proContractApprovalService.delete(proContractApproval);
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
			proContractApprovalService.delete(proContractApprovalService.get(id));
		}
		j.setMsg("删除总包合同信息成功!");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProContractApproval proContractApproval, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "总包合同"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProContractApproval> page = proContractApprovalService.findPage(new Page<ProContractApproval>(request, response, -1), proContractApproval);
    		new ExportExcel("总包合同", ProContractApproval.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出总包合同记录失败！失败信息："+e.getMessage());
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
			List<ProContractApproval> list = ei.getDataList(ProContractApproval.class);
			for (ProContractApproval ProContractApproval : list){
				try{
					proContractApprovalService.save(ProContractApproval);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条总包合同记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条总包合同记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入总包合同失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/procontractapproval/?repage";
    }
	
	/**
	 * 下载导入总包合同数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "总包合同数据导入模板.xlsx";
    		List<ProContractApproval> list = Lists.newArrayList(); 
    		new ExportExcel("总包合同数据", ProContractApproval.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/procontractapproval/?repage";
    }
}
