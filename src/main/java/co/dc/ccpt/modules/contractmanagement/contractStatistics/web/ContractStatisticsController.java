package co.dc.ccpt.modules.contractmanagement.contractStatistics.web;

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
import co.dc.ccpt.modules.contractmanagement.contractStatistics.entity.ContractStatistics;
import co.dc.ccpt.modules.contractmanagement.contractStatistics.service.ContractStatisticsService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.subprocontract.entity.SubProContract;
import co.dc.ccpt.modules.sys.service.SystemService;

@Controller
@RequestMapping(value = "${adminPath}/contractstatistics")
public class ContractStatisticsController extends BaseController{
	
	@Autowired
	public ContractStatisticsService contractStatisticsService;
	
	@Autowired
	public SystemService userService;
	
	@Autowired
	public ProContractService proContractService;
	
	@Autowired
	public SubpackageProgramService subpackageProgramService;
	
	@ModelAttribute
	public ContractStatistics get(@RequestParam(required=false) String id) {
		ContractStatistics entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractStatisticsService.get(id);
		}
		if (entity == null){
			entity = new ContractStatistics();
		}
		return entity;
	}
	
	/**
	 * 分包合同列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/contractmanagement/contractstatistics/contractstatisticsList";
	}
	
	/**
	 * 分包合同列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(ContractStatistics contractStatistics, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractStatistics> page = contractStatisticsService.findPage(new Page<ContractStatistics>(request, response), contractStatistics); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分包合同表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ContractStatistics contractStatistics, Model model) {
		model.addAttribute("contractStatistics", contractStatistics);
		if(StringUtils.isBlank(contractStatistics.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/contractmanagement/contractstatistics/contractstatisticsForm";
	}

	/**
	 * 保存分包合同信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ContractStatistics contractStatistics, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, contractStatistics)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		contractStatisticsService.save(contractStatistics);//保存
		j.setSuccess(true);
		j.setMsg("保存分包合同信息成功！");
		return j;
	}
	
	/**
	 * 删除分包合同
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ContractStatistics contractStatistics, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		contractStatisticsService.delete(contractStatistics);
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
			contractStatisticsService.delete(contractStatisticsService.get(id));
		}
		j.setMsg("删除分包合同信息成功!");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ContractStatistics contractStatistics, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分包合同"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractStatistics> page = contractStatisticsService.findPage(new Page<ContractStatistics>(request, response, -1), contractStatistics);
    		new ExportExcel("分包合同", SubProContract.class).setDataList(page.getList()).write(response, fileName).dispose();
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
			List<ContractStatistics> list = ei.getDataList(ContractStatistics.class);
			for (ContractStatistics contractStatistics : list){
				try{
					contractStatisticsService.save(contractStatistics);
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
		return "redirect:"+Global.getAdminPath()+"/contractstatistics/?repage";
    }
	
	/**
	 * 下载导入分包合同数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分包合同数据导入模板.xlsx";
    		List<ContractStatistics> list = Lists.newArrayList(); 
    		new ExportExcel("分包合同数据", ContractStatistics.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/contractstatistics/?repage";
    }
}
