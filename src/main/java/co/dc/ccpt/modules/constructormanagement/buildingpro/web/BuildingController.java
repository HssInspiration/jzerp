package co.dc.ccpt.modules.constructormanagement.buildingpro.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.BidcompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.StaffCertificate;
import co.dc.ccpt.modules.constructormanagement.basicinfo.service.CoreStaffService;
import co.dc.ccpt.modules.constructormanagement.buildingpro.entity.Building;
import co.dc.ccpt.modules.constructormanagement.buildingpro.service.BuildingService;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 建造师在建项目Controller
 * @author lxh
 * @version 2018-05-03
 */
@Controller
@RequestMapping(value = "${adminPath}/buildingpro")
public class BuildingController extends BaseController{
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private CoreStaffService coreStaffService;
	
	@Autowired
	private BidcompanyService bidcompanyService;
	
	@ModelAttribute
	public Building get(@RequestParam(required=false) String id) {
		Building entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = buildingService.get(id);
		}
		if (entity == null){
			entity = new Building();
		}
		return entity;
	}
	
	/**
	 * 建造师在建项目列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/constructormanagement/buildingpro/buildingList";
	}
	
	/**
	 * 建造师在建项目列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(Building building, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Building> page = buildingService.findPage(new Page<Building>(request, response), building); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑建造师在建项目表单页面
	 */
	@RequestMapping(value = "form")
	public String form(Building building, Model model) {
		model.addAttribute("building", building);
		if(StringUtils.isBlank(building.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/constructormanagement/buildingpro/buildingForm";
	}

	/**
	 * 保存人员在建项目信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(Building building, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, building)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		buildingService.save(building);//保存
		//保存后要实现人员基础信息中的是否有在建项目的状态变更
		building = buildingService.get(building);
		CoreStaff coreStaff = building.getCoreStaff();
		if(coreStaff!=null){
			coreStaff = coreStaffService.get(coreStaff);
			if(coreStaff!=null){
				coreStaff.setIsBuild("1");
				coreStaffService.save(coreStaff);
			}
		}
		j.setSuccess(true);
		j.setMsg("保存建造师在建项目信息成功！");
		return j;
	}
	
	
	/**
	 * 通过项目名称获取参投集合（模糊匹配）
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getBidcompanyByProName")
	public List<Bidcompany> getBidcompanyByProName (@RequestParam String programName){
		Program program = new Program();
		program.setProgramName(programName);
		Bidcompany bidcompany = new Bidcompany();
		bidcompany.setProgram(program);
		return bidcompanyService.getBidcompanyByProName(bidcompany);
	}
	
	@ResponseBody
	@RequestMapping(value="getCoreStaffByName")
	public List<CoreStaff> getCoreStaffByName (@RequestParam String name){
		CoreStaff coreStaff = new CoreStaff();
		User user = new User();
		user.setName(name);
		coreStaff.setUser(user);
		List<CoreStaff> coreStaffList = coreStaffService.getAllCoreStaffList(coreStaff);
		return coreStaffList;
	}
	
	@ResponseBody
	@RequestMapping(value="getCertificateListById",method=RequestMethod.POST)
	public List<StaffCertificate> getCertificateListById (@RequestBody CoreStaff coreStaff){
		String id = coreStaff.getId();
		if(StringUtils.isNotBlank(id)){
			coreStaff = coreStaffService.get(id);
		}
		List<StaffCertificate> certificateList = new ArrayList<StaffCertificate>();
		if(coreStaff!=null){
			certificateList = coreStaff.getStaffCertificateList();
		}
		return certificateList;
	}
	
	/**
	 * 通过一个参投id获取其项下的人员信息集合
	 * @param bidcompany
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getUserListById",method=RequestMethod.POST)
	public List<CorePerson> getUserListById (@RequestBody Bidcompany bidcompany){
		List<CorePerson> corePersonList = new ArrayList<CorePerson>();
		corePersonList = bidcompanyService.getUserListById(bidcompany);
		return corePersonList;
	}
	
	/**
	 * 删除建造师在建项目
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(Building building, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		buildingService.delete(building);
		j.setMsg("删除建造师在建项目信息成功!");
		return j;
	}
	
	/**
	 * 批量删除建造师在建项目
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			buildingService.delete(buildingService.get(id));
		}
		j.setMsg("删除在建项目信息成功!");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Building building, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "建造师在建项目信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Building> page = buildingService.findPage(new Page<Building>(request, response, -1), building);
    		new ExportExcel("建造师在建项目信息", Building.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出建造师在建项目记录失败！失败信息："+e.getMessage());
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
			List<Building> list = ei.getDataList(Building.class);
			for (Building building : list){
				try{
					buildingService.save(building);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条建造师在建项目记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条建造师在建项目记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入建造师在建项目失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/buildingpro/staffCertificate/?repage";
    }
	
	/**
	 * 下载导入建造师在建项目信息数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "建造师在建项目数据导入模板.xlsx";
    		List<Building> list = Lists.newArrayList(); 
    		new ExportExcel("建造师在建项目数据", Building.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/buildingpro/staffCertificate/?repage";
    }

}
