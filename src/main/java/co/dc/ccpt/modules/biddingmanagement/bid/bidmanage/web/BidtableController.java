/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.service.BidtableService;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.BidcompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.service.CompanyService;
import co.dc.ccpt.modules.programmanage.service.ProgramService;

/**
 * 投标管理Controller
 * @author lxh
 * @version 2018-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/bidmanage/bidtable")
public class BidtableController extends BaseController {
	/**
	 * 投标业务处理层对象
	 */
	@Autowired
	private BidtableService bidtableService;
	/**
	 * 单位业务处理层对象
	 */
	@Autowired
	private CompanyService companyService;
	/**
	 * 项目业务处理层对象
	 */
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private BidcompanyService bidcompanyService;
	
	@ModelAttribute
	public Bidtable get(@RequestParam(required=false) String id) {
		Bidtable entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bidtableService.get(id);
		}
		if (entity == null){
			entity = new Bidtable();
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
	//@RequiresPermissions("bidmanage:bidtable:list")
	@RequestMapping(value = {"list", ""})
	public String list(Bidtable bidtable,Program program, Model model) {
		return "modules/biddingmanagement/bid/bidmanage/bidtableList";
	}
	
	/**
	 * 投标管理列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("bidmanage:bidtable:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Bidtable bidtable, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Bidtable> page = bidtableService.findPage(new Page<Bidtable>(request, response), bidtable); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑投标管理表单页面
	 */
	//@RequiresPermissions(value={"bidmanage:bidtable:view","bidmanage:bidtable:add","bidmanage:bidtable:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Bidtable bidtable, Program program, Model model) {
		model.addAttribute("bidtable", bidtable);
		model.addAttribute("companyList",companyService.listAllCompany());
		model.addAttribute("programList",programService.listAllProgram());
		if(StringUtils.isBlank(bidtable.getId())){//如果ID是空为添加
			bidtable.setProgram(program);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/bid/bidmanage/bidtableForm";
	}

	@RequestMapping(value = "newform")
	public String newform(Bidtable bidtable, Program program, Model model) {
		bidtable.setProgram(program);//将项目id存入投标对象中
		bidtable = bidtableService.getBidtableByProId(bidtable);//通过投标信息中的项目id获取对应的一条投标信息
		if(bidtable == null){//如果上述查询为空，创建一个投标信息的实例
			bidtable = new Bidtable();
			bidtable.setProgram(program);//将项目id存入投标信息，此时投标编辑表单中的项目信息会自动显示
		}
		if(StringUtils.isBlank(bidtable.getId())){//如果ID是空为添加
			String bidtableNum = bidtableService.getBidtableNum();
			bidtable.setBidNum(bidtableNum);
			model.addAttribute("bidtable", bidtable);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("bidtable", bidtable);
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/bid/bidmanage/bidtableForm";
	}
	
	/**
	 * 保存投标管理
	 */
	@ResponseBody
	//@RequiresPermissions(value={"sys:dict:add","sys:dict:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Bidtable bidtable, Model model) {
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		if (!beanValidator(model, bidtable)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		if (!"true".equals(checkBidNum(bidtable.getBidOldNum(), bidtable.getBidNum()))){
			j.setSuccess(false);
			j.setMsg( "保存投标信息'" + bidtable.getBidNum() + "'失败, 投标编号已存在");
			return j;
		}
		Program program = new Program();
		// 判断投标时间要在计划开标时间之前
		
		// 将前台传入的项目id获取到
		String programId = bidtable.getProgram().getId();
		// 获取完id，根据id获取一条program
		program = programService.get(programId);
		if(program!=null){
			// 获取项目计划开标时间
			Date openDate = program.getPlanToStart();
			// 获取当前准备存入的投标时间
			Date bidDate = bidtable.getOpenBidDate();
			if(openDate!=null & bidDate!=null){
				if(bidDate.before(openDate) || bidDate.equals(openDate)){
					bidtableService.save(bidtable);
					// 转换项目状态，投标后自由-->招标
					//更新当前获取的项目状态
					program.setStatus(1);
					programService.save(program);
					j.setSuccess(true);
					j.setMsg("保存投标信息'" + bidtable.getBidNum() + "'成功！");
					return j;
				}else{
					j.setSuccess(false);
					j.setMsg("保存失败！投标日期不能晚于计划开标日期！");
					return j;
				}
			}
		}
		return j;
	}
	
	/**
	 * 检验投标编号是否重复
	 */
	@ResponseBody
	@RequestMapping(value = "checkBidNum")
	public String checkBidNum(String bidOldNum, String bidNum) {
		if (bidNum!=null && bidNum.equals(bidOldNum)) {
			return "true";
		} else if (bidNum!=null && bidtableService.getByBidtableNum(bidNum) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 查询出对应id的附件数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getEnclosureCount",method = RequestMethod.POST)
	public Integer getEnclosureCount(@RequestBody Program program) {
		String foreginId = program.getId();
		Integer i = enclosuretabService.countEnclosure(foreginId);
		System.out.println("i为："+i);
		return i;
	}
	
	/**
	 * 通过项目名称查询出所有的单位
	 */
	@ResponseBody
	@RequestMapping(value = "getProgramList",method = RequestMethod.POST)
	public List<Program> getList(@RequestParam String programName) {
		List<Program> programList = new ArrayList<Program>();
		programList = programService.listAllProgramByName(programName);
		return programList;
	}
	
	/**
	 * 删除投标管理
	 */
	@ResponseBody
	//@RequiresPermissions("bidmanage:bidtable:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Bidtable bidtable, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		bidtableService.delete(bidtable);
		j.setMsg("删除投标管理成功");
		return j;
	}
	
	/**
	 * 批量删除投标管理
	 */
	@ResponseBody
	//@RequiresPermissions("bidmanage:bidtable:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Bidtable bidtable = bidtableService.get(id);
			List<Bidcompany> bidcompanyList = bidcompanyService.getBidcompanyIdByBidtableId(id);	
			if(bidcompanyList!=null && !bidcompanyList.isEmpty()){
				j.setSuccess(false);
				j.setMsg("删除失败！当前所删项目在参投信息中有记录！");
			}else{
				bidtableService.delete(bidtable);
				enclosuretabService.deleteEnclosureByForeginId(bidtable.getId());//同步删除对应附件
				//删除时将对应的项目状态做更改-->自由
				Program program = bidtable.getProgram();
				if(program!=null){
					String programId = program.getId();
					if(programId != null && !programId.equals("")){
						program = programService.get(programId);
						if(program!=null){
							program.setStatus(0);
							programService.save(program);
						}
					}
				}
				
				j.setSuccess(true);
				j.setMsg("删除成功!");
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	//@RequiresPermissions("bidmanage:bidtable:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Bidtable bidtable, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "投标管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Bidtable> page = bidtableService.findPage(new Page<Bidtable>(request, response, -1), bidtable);
    		new ExportExcel("投标管理", Bidtable.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出投标管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	//@RequiresPermissions("bidmanage:bidtable:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Bidtable> list = ei.getDataList(Bidtable.class);
			for (Bidtable bidtable : list){
				try{
					bidtableService.save(bidtable);
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
		return "redirect:"+Global.getAdminPath()+"/bidmanage/bidtable/?repage";
    }
	/**
	 * 下载导入投标管理数据模板
	 */
    //@RequiresPermissions("bidmanage:bidtable:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标管理数据导入模板.xlsx";
    		List<Bidtable> list = Lists.newArrayList(); 
    		new ExportExcel("投标管理数据", Bidtable.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bidmanage/bidtable/?repage";
    }
}