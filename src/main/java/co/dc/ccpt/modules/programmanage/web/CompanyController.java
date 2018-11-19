package co.dc.ccpt.modules.programmanage.web;


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
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.BidcompanyService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.service.SubBidCompanyService;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.service.CompanyService;
import co.dc.ccpt.modules.programmanage.service.ProgramService;

/**
 * 单位管理Controller
 * @author lxh
 * @version 2018-02-02
 */
@Controller
@RequestMapping(value = "${adminPath}/programmanage/company")
public class CompanyController extends BaseController {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private BidcompanyService bidcompanyService;
	
	@Autowired
	private SubBidCompanyService subBidCompanyService;
	
	@ModelAttribute
	public Company get(@RequestParam(required=false) String id) {
		Company entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = companyService.get(id);
		}
		if (entity == null){
			entity = new Company();
		}
		return entity;
	}
	
	/**
	 * 单位管理列表页面
	 */
	//@RequiresPermissions("programmanage:company:list")
	@RequestMapping(value = {"list", ""})
	public String list(Company company, Model model) {
		return "modules/programmanage/program/companyList";
	}
	
	/**
	 * 单位管理列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("programmanage:company:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Company company, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Company> page = companyService.findPage(new Page<Company>(request, response), company); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑单位管理表单页面
	 */
	//@RequiresPermissions(value={"programmanage:company:view","programmanage:company:add","programmanage:company:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Company company, Model model) {
		
		if(StringUtils.isBlank(company.getId())){//如果ID是空为添加
			String  companyNum= companyService.getCompanyNum();//新增设置编号
			company.setCompanyNum(companyNum);
			model.addAttribute("company", company);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("company", company);
			model.addAttribute("edit",true);
		}
		return "modules/programmanage/program/companyForm";
	}

	/**
	 * 保存单位管理
	 */
	//@RequiresPermissions(value={"programmanage:company:add","programmanage:company:edit"},logical=Logical.OR)
	/*@RequestMapping(value = "save")
	public String save(Company company, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, company)){
			return form(company, model);
		}
		//新增或编辑表单保存
		companyService.save(company);//保存
		addMessage(redirectAttributes, "保存单位管理成功");
		return "redirect:"+Global.getAdminPath()+"/programmanage/company/?repage";
	}*/
	@ResponseBody
	//@RequiresPermissions(value={"sys:dict:add","sys:dict:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Company company, Model model) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		if (!beanValidator(model, company)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		
		companyService.save(company);
		j.setSuccess(true);
		j.setMsg("保存单位'" + company.getCompanyName() + "'成功！");
		return j;
	}
	
	/**
	 * 删除单位管理
	 */
	@ResponseBody
	//@RequiresPermissions("programmanage:company:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Company company, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		companyService.delete(company);
		j.setMsg("删除单位管理成功");
		return j;
	}
	
	/**
	 * 批量删除单位管理
	 */
	@ResponseBody
	//@RequiresPermissions("programmanage:company:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Company company = companyService.get(id); 
			if(id.equals("03ae459404284f17bbd25e78a13397a6")){
				j.setSuccess(false);
				j.setMsg("不允许删除本单位");
			}else{
				//删除前判断：若当前公司在项目管理、参投管理、子项目的投标记录中被引用不可删
				List<Program> programList = programService.listProgramByCompId(id);
				List<Bidcompany> bidcompList = bidcompanyService.listBidcompanyByCompId(id);
				List<SubBidCompany> subBidCompanyList = subBidCompanyService.listSubBidCompanyByCompId(id);
				if(programList!=null && programList.size()!=0){
					j.setSuccess(false);
					j.setMsg("删除失败！单位已在项目管理中登记！");
					return j;
				}else if(bidcompList!=null && bidcompList.size()!=0){
					j.setSuccess(false);
					j.setMsg("删除失败！单位已在参投信息中登记！");
					return j;
				}else if(subBidCompanyList!=null && subBidCompanyList.size()!=0){
					j.setSuccess(false);
					j.setMsg("删除失败！单位已在子项目投标记录中登记！");
					return j;
				}else{
					companyService.delete(company);
					j.setSuccess(true);
					j.setMsg("删除单位管理成功");
					return j;
				}
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	//@RequiresPermissions("programmanage:company:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Company company, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "单位管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Company> page = companyService.findPage(new Page<Company>(request, response, -1), company);
    		new ExportExcel("单位管理", Company.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出单位管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	//@RequiresPermissions("programmanage:company:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Company> list = ei.getDataList(Company.class);
			for (Company company : list){
				try{
					companyService.save(company);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条单位管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条单位管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入单位管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/programmanage/company/?repage";
    }
	
	/**
	 * 下载导入单位管理数据模板
	 */
    //@RequiresPermissions("programmanage:company:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "单位管理数据导入模板.xlsx";
    		List<Company> list = Lists.newArrayList(); 
    		new ExportExcel("单位管理数据", Company.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/programmanage/company/?repage";
    }

}