package co.dc.ccpt.modules.constructormanagement.basicinfo.web;

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
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.CoreStaff;
import co.dc.ccpt.modules.constructormanagement.basicinfo.entity.StaffCertificate;
import co.dc.ccpt.modules.constructormanagement.basicinfo.service.CoreStaffService;
import co.dc.ccpt.modules.constructormanagement.buildingpro.entity.Building;
import co.dc.ccpt.modules.constructormanagement.buildingpro.service.BuildingService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;

@Controller
@RequestMapping(value = "${adminPath}/basicinfo")
public class CoreStaffController extends BaseController {
	@Autowired
	private CoreStaffService coreStaffService;
	
	@Autowired
	private SystemService userService;
	
	@Autowired
	private BuildingService buildingService;
	@ModelAttribute
	public CoreStaff get(@RequestParam(required=false) String id) {
		CoreStaff entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coreStaffService.get(id);
		}
		if (entity == null){
			entity = new CoreStaff();
		}
		return entity;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "modules/constructormanagement/basicinfo/coreStaffList";
	}
	
	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoreStaff coreStaff, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoreStaff> page = coreStaffService.findPage(new Page<CoreStaff>(request, response), coreStaff); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑管理表单页面
	 */
	@RequestMapping(value = "form")
	public String form(CoreStaff coreStaff, Model model) {
		if(StringUtils.isBlank(coreStaff.getId())){//新增
			model.addAttribute("coreStaff", coreStaff);
		}else{//修改
			model.addAttribute("coreStaff", coreStaff);
		}
		return "modules/constructormanagement/basicinfo/coreStaffForm";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CoreStaff coreStaff, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coreStaff)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		//保存前判断对应证书不可为空
		List<StaffCertificate> staffCertificateList = new ArrayList<StaffCertificate>();
		staffCertificateList = coreStaff.getStaffCertificateList();
		if(StringUtils.isBlank(coreStaff.getId())){//新增
//			保存前判断人员不可重复
			if(coreStaffService.getCoreStaffByUserId(coreStaff)!=null){//如果人员重复不可新增
				j.setSuccess(false);
				j.setMsg("保存失败，人员重复！");
				return j;
			}
		}
		if(staffCertificateList!=null && staffCertificateList.size()>0){
			coreStaffService.save(coreStaff);//保存
			j.setSuccess(true);
			j.setMsg("保存成功");
			return j;
		}else{
			j.setSuccess(false);
			j.setMsg("保存失败，请至少维护一个证书信息！");
			return j;
		}
	}
	
	/**
	 * 查询所有用户名称（模糊匹配）
	 */
	@ResponseBody
	@RequestMapping(value="getAllUserList")
	public List<User> getAllUserList (@RequestParam String name){
		User user = new User();
		user.setName(name);
		List<User> userList = new ArrayList<User>();
		userList = userService.getAllUserList(user);
		return userList;
	}
	
	/**
	 * 通过用户id查询核心人员信息
	 */
	@ResponseBody
	@RequestMapping(value="getCoreStaffByUserId")
	public String getCoreStaffByUserId (@RequestBody User user){
		CoreStaff coreStaff =new CoreStaff();
		coreStaff.setUser(user);
		coreStaff = coreStaffService.getCoreStaffByUserId(coreStaff);
		if(coreStaff!=null){
			return "true";
		}else{
			return "false";
		}
		
	}
	
	/**
	 * 删除投标管理
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoreStaff coreStaff, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coreStaffService.delete(coreStaff);
		j.setMsg("删除成功!");
		return j;
	}
	
	/**
	 * 批量删除投标管理
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//删除前判断：若已在人员在建项目信息中登记不可删
			CoreStaff coreStaff = coreStaffService.get(id);
			List<Building> buildingList = buildingService.getBuildingByCoreStaffId(id);
			if(buildingList!=null && !buildingList.isEmpty()){
				j.setSuccess(false);
				j.setMsg("删除失败！当前所删人员在人员在建信息中有记录！");
				return j;
			}else{
				coreStaffService.delete(coreStaff);
				j.setSuccess(true);
				j.setMsg("删除成功!");
				return j;
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoreStaff coreStaff, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "建造师管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoreStaff> page = coreStaffService.findPage(new Page<CoreStaff>(request, response, -1), coreStaff);
    		new ExportExcel("建造师管理", CoreStaff.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public CoreStaff detail(String id) {
		return coreStaffService.get(id);
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
			List<CoreStaff> list = ei.getDataList(CoreStaff.class);
			for (CoreStaff coreStaff : list){
				try{
					coreStaffService.save(coreStaff);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条建造师管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条建造师管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入建造师管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/basicinfo/coreStaff/?repage";
    }
	
	/**
	 * 下载导入投标管理数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "建造师管理数据导入模板.xlsx";
    		List<CoreStaff> list = Lists.newArrayList(); 
    		new ExportExcel("建造师管理数据", CoreStaff.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/basicinfo/coreStaff/?repage";
    }
}
