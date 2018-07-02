/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.programmanage.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.CompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.sys.entity.Office;
import co.dc.ccpt.modules.sys.service.OfficeService;

import com.google.common.collect.Lists;

/**
 * 项目工程管理Controller
 * @author lxh
 * @version 2018-02-01
 */
@Controller
@RequestMapping(value = "${adminPath}/programmanage/program")
public class ProgramController extends BaseController {

	@Autowired
	private ProgramService programService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private BidtableService bidtableService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public Program get(@RequestParam(required=false) String id) {
		Program entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = programService.get(id);
		}
		if (entity == null){
			entity = new Program();
		}
		return entity;
	}
	
	/**
	 * 项目工程管理列表页面
	 */
	
	//@RequiresPermissions("programmanage:program:list")
	@RequestMapping(value = {"list", ""})
	public String list(Program program, Model model) {
		model.addAttribute("companyList",companyService.listAllCompany());
		return "modules/biddingmanagement/bid/programmanage/programList";
	}
	
	/**
	 * 项目工程管理列表数据
	 */
	
	@ResponseBody
	//@RequiresPermissions("programmanage:program:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Program program, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Program> page = programService.findPage(new Page<Program>(request, response), program); 
		return getBootstrapData(page);
	}
	
	/**
	 * 查看，增加，编辑项目工程管理表单页面
	 */
	
	//@RequiresPermissions(value={"programmanage:program:view","programmanage:program:add","programmanage:program:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Program program, Model model) {
		System.out.println("form2:"+companyService.listAllCompany());
		model.addAttribute("program", program);
		model.addAttribute("companyList",companyService.listAllCompany());
		if(StringUtils.isBlank(program.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/bid/programmanage/programForm";
	}
	
	
	/**
	 * 查询出所有的单位的id和名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAllCompanyList")
	public List<Company> getList() {
		List<Company> companyList = new ArrayList<Company>();
		companyList = companyService.listAllCompanyIdAndName();
		return companyList;
	}
	
	/**
	 * 查询出对应id的附件数量
	 * 其他js中的都调用此接口，
	 * 此时的program,临时充当接收参数--id--的对象，
	 * 接收之后，获取，此时的foreginId即为前台所传的row.id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getEnclosureCount",method = RequestMethod.POST)
	public Integer getEnclosureCount(@RequestBody Program program) {
		String foreginId = program.getId();
		Integer i = enclosuretabService.countEnclosure(foreginId);
		return i;
	}
	
	/**
	 * 变更为竣工状态
	 * */
	@ResponseBody
	@RequestMapping(value = "completed")
	public AjaxJson completed(Program program, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, program)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = program.getStatus();
		if(status != null && !status.equals("")){
			if(status==2){
				program.setStatus(3);
				programService.save(program);
				j.setSuccess(true);
				j.setMsg("保存项目状态成功！");
			}else{
				j.setSuccess(false);
				j.setMsg("当前为非施工状态，不允许更改为竣工状态！");
			}
			return j;
		}
		return j;
	}
	
	/**
	 * 变更为停工状态
	 * */
	@ResponseBody
	@RequestMapping(value = "shutdown")
	public AjaxJson shutdown(Program program, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, program)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = program.getStatus();
		if(status != null && !status.equals("")){
			if(status==2){
				program.setStatus(4);
				programService.save(program);
				j.setSuccess(true);
				j.setMsg("保存项目状态成功！");
			}else{
				j.setSuccess(false);
				j.setMsg("当前为非施工状态，不允许更改为停工状态！");
			}
			return j;
		}
		return j;
	}
	
	/**
	 * 变更为结案状态
	 * */
	@ResponseBody
	@RequestMapping(value = "closecase")
	public AjaxJson closecase(Program program, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, program)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = program.getStatus();
		if(status != null && !status.equals("")){
			if(status==3){
				program.setStatus(5);
				programService.save(program);
				j.setSuccess(true);
				j.setMsg("保存项目状态成功！");
			}else{
				j.setSuccess(false);
				j.setMsg("当前为非竣工状态，不允许更改为结案状态！");
			}
			return j;
		}
		return j;
	}
		
		
	/**
	 * 通过单位名称查询出对应集合（除金卓外）
	 * @param companyName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCompanyList")
	public List<Company> getListAllCompanyByName(@RequestParam  String companyName) {
		List<Company> companyList = new ArrayList<Company>();
		companyList = companyService.listAllCompanyByNameExceptJz(companyName);
		return companyList;
	}
	
	/**
	 * 通过分公司名称查询出对应集合
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOfficeList")
	public List<Office> getListAllOfficeByName(@RequestParam String name) {
		List<Office> officeList = new ArrayList<Office>();
		officeList = officeService.listAllOfficeByName(name);
		return officeList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getCompanyIdByName",method=RequestMethod.POST)
	public Company getCompanyIdByName(@RequestParam(value = "companyname",required = false) String companyname) {
		Company company = new Company();
		company = companyService.getCompanyIdByName(companyname);
		return company;
	}
	/**
	 * 保存项目工程管理
	 */
	//@RequiresPermissions(value={"programmanage:program:add","programmanage:program:edit"},logical=Logical.OR)
	/*@RequestMapping(value = "save")
	public String save(Program program, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, program)){
			return form(program, model);
		}
		//新增或编辑表单保存
		programService.save(program);//保存
		addMessage(redirectAttributes, "保存项目工程管理成功");
		return "redirect:"+Global.getAdminPath()+"/programmanage/program/?repage";
	}*/
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(Program program, Model model){
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		if (!beanValidator(model, program)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		if (!"true".equals(checkProgramNum(program.getProgramOldNum(), program.getProgramNum()))){
			j.setSuccess(false);
			j.setMsg( "保存项目'" + program.getProgramNum() + "'失败, 项目编号已存在");
			return j;
		}
		//新增或编辑表单保存
		//保存之前验证当前所选发标单位不是金卓
		//1.获取发标单位id
		String companyId = program.getCompany().getId();
		//2.判断是否为金卓
		if(companyId.equals("03ae459404284f17bbd25e78a13397a6")){
			j.setSuccess(false);
			j.setMsg( "保存项目失败,发标单位不能为本公司！");
			return j;
		}else{
			Integer getMethod = program.getGetMethod();
			if(getMethod == 0){//业主指定时
				program.setStatus(2);//默认施工状态
			}
			programService.save(program);//保存
			j.setSuccess(true);
			j.setMsg("保存项目'" + program.getProgramName() + "'成功！");
			return j;
			
		}
	}
	
	/**
	 * 验证项目编号是否有效
	 */
	@ResponseBody
	@RequestMapping(value = "checkProgramNum")
	public String checkProgramNum(String programOldNum, String programNum) {
		if (programNum!=null && programNum.equals(programOldNum)) {
			return "true";
		} else if (programNum!=null && programService.getByProgramNum(programNum) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 删除项目工程管理
	 */
	@ResponseBody
	//@RequiresPermissions("programmanage:program:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Program program, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		//删除项目之前先在投标管理中用项目id进行查询是否存在已投标未删除的项目
		Bidtable bidtable = new Bidtable();
		bidtable.setProgram(program);
		bidtable = bidtableService.getBidtableByProId(bidtable);
		if(bidtable.getId() != null && !bidtable.getId().equals("")){
			j.setSuccess(false);
			j.setMsg("删除失败！投标管理中有当前项目关联信息");
		}else{
			programService.delete(program);
			j.setSuccess(true);
			j.setMsg("删除项目工程管理成功！");
		}
		return j;
	}
	
	/**
	 * 批量删除项目工程管理
	 */
	
	@ResponseBody
	//@RequiresPermissions("programmanage:program:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		Program program = new Program();//定义项目对象
		String idArray[] =ids.split(",");
		for(String id : idArray){
			program = programService.get(id);//每次进入循环先获取一条项目信息
			Bidtable bidtable = new Bidtable();//定义投标对象
			if(program!=null){
				bidtable.setProgram(program);//存入投标对象中
				bidtable = bidtableService.getBidtableByProId(bidtable);//利用项目id查询投标信息
				if(bidtable!=null){
					if(bidtable.getId() != null && !bidtable.getId().equals("")){
						j.setSuccess(false);
						j.setMsg("删除失败！投标管理中存在项目"+bidtable.getProgram().getProgramName()+"关联信息");
						return j;
					}
				}else if(program!=null){
//					if(program.getStatus() == 0){//只能删除自由状态下的项目
					programService.delete(program);
					enclosuretabService.deleteEnclosureByForeginId(program.getId());//同步删除对应附件
					j.setSuccess(true);
					j.setMsg("删除项目工程\""+program.getProgramName()+"\"成功!");
					return j;
//					}
//					else if(program.getStatus() == 1){//招标
//						j.setSuccess(false);
//						j.setMsg("删除失败！项目招标中不可删除！");
//						return j;
//					}else if(program.getStatus() == 2){//施工
//						j.setSuccess(false);
//						j.setMsg("删除失败！项目施工中不可删除！");
//						return j;
//					}else if(program.getStatus() == 3){//竣工
//						j.setSuccess(false);
//						j.setMsg("删除失败！项目已竣工不可删除！");
//						return j;
//					}else if(program.getStatus() == 4){//停工
//						j.setSuccess(false);
//						j.setMsg("删除失败！项目已停工不可删除！");
//						return j;
//					}else if(program.getStatus() == 5){//结案
//						j.setSuccess(false);
//						j.setMsg("删除失败！项目已结案不可删除！");
//						return j;
//					}else if(program.getStatus() == 6){//未中标
//						j.setSuccess(false);
//						j.setMsg("删除失败！项目未中标不可删除！");
//						return j;
//					}
				}
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	//@RequiresPermissions("programmanage:program:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Program program, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "项目工程管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Program> page = programService.findPage(new Page<Program>(request, response, -1), program);
    		new ExportExcel("项目工程管理", Program.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出项目工程管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	//@RequiresPermissions("programmanage:program:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Program> list = ei.getDataList(Program.class);
			for (Program program : list){
				try{
					programService.save(program);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条项目工程管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条项目工程管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入项目工程管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/programmanage/program/?repage";
    }
	
	/**
	 * 下载导入项目工程管理数据模板
	 */
    //@RequiresPermissions("programmanage:program:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "项目工程管理数据导入模板.xlsx";
    		List<Program> list = Lists.newArrayList(); 
    		new ExportExcel("项目工程管理数据", Program.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/programmanage/program/?repage";
    }

}