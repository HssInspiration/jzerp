package co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity.BidtableQuery;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.service.BidtableQueryService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.CompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;


/**
 * 投标综合查询Controller
 * @author lxh
 * @version 2018-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/bidquerymanage/bidtablequery")
public class BidtableQueryController extends BaseController {

	@Autowired
	private BidtableQueryService bidtableService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProgramService programService;
	
	@ModelAttribute
	public BidtableQuery get(@RequestParam(required=false) String id) {
		BidtableQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bidtableService.get(id);
		}
		if (entity == null){
			entity = new BidtableQuery();
		}
		return entity;
	}
	
	@ModelAttribute
	public Program getProgram(@RequestParam(required=false) String proid) {
		Program entity = null;
		if (StringUtils.isNotBlank(proid)){
			entity = programService.get(proid);
		}
		if (entity == null){
			entity = new Program();
		}
		return entity;
	}
	/**
	 * 投标管理列表页面
	 */
//	@RequiresPermissions("bidquerymanage:bidtable:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "modules/biddingmanagement/bid/bidquerymanage/bidtableQueryList";
	}
	
	/**
	 * 投标管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BidtableQuery bidtableQuery,HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("----bidtableQuery是:----"+bidtableQuery);
		String programStatus= bidtableQuery.getProgramStatus();//复选框查询参数自前台传入
		System.out.println("programStatus:"+programStatus);
		//将字符串转成集合存入到指定集合中
		List<String> statusList = java.util.Arrays.asList(programStatus.split(",")); 
		bidtableQuery.setProgramStatusList(statusList);
		Page<BidtableQuery> page = bidtableService.findPage(new Page<BidtableQuery>(request, response), bidtableQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑投标管理表单页面
	 */
//	@RequiresPermissions(value={"bidquerymanage:bidtable:view","bidquerymanage:bidtable:add","bidquerymanage:bidtable:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BidtableQuery bidtableQuery, Model model) {
		model.addAttribute("bidtableQuery", bidtableQuery);
		model.addAttribute("companyList",companyService.listAllCompany());
		model.addAttribute("programList",programService.listAllProgram());
		return "modules/biddingmanagement/bid/bidquerymanage/bidtableQueryForm";
	}

	/**
	 * 保存投标管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"bidquerymanage:bidtable:add","bidquerymanage:bidtable:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(BidtableQuery bidtableQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, bidtableQuery)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		bidtableService.save(bidtableQuery);//保存
		j.setSuccess(true);
		j.setMsg("保存投标管理成功");
		return j;
	}
	
	/**
	 * 删除投标管理
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BidtableQuery bidtableQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		bidtableService.delete(bidtableQuery);
		j.setMsg("删除投标管理成功");
		return j;
	}
	
	/**
	 * 批量删除投标管理
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bidtableService.delete(bidtableService.get(id));
		}
		j.setMsg("删除投标管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BidtableQuery bidtableQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "投标管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BidtableQuery> page = bidtableService.findPage(new Page<BidtableQuery>(request, response, -1), bidtableQuery);
    		new ExportExcel("投标管理", BidtableQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出投标管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public BidtableQuery detail(String id) {
		return bidtableService.get(id);
	}
	

	/**
	 * 导入Excel数据
	 */
//	@RequiresPermissions("bidquerymanage:bidtable:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BidtableQuery> list = ei.getDataList(BidtableQuery.class);
			for (BidtableQuery bidtableQuery : list){
				try{
					bidtableService.save(bidtableQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条投标管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条投标管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入投标管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bidquerymanage/bidtable/?repage";
    }
	
	/**
	 * 下载导入投标管理数据模板
	 */
//	@RequiresPermissions("bidquerymanage:bidtable:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标管理数据导入模板.xlsx";
    		List<BidtableQuery> list = Lists.newArrayList(); 
    		new ExportExcel("投标管理数据", BidtableQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bidquerymanage/bidtable/?repage";
    }
	

}