package co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.CompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.ClearEvaluate;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.service.ClearEvaluateService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.service.SubBidCompanyService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service.SubpackageProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.service.TenderService;

/**
 * 子项目参投单位管理Controller
 * @author lxh
 * @version 2018-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/tendermanage/subBidCompany")
public class SubBidCompanyController extends BaseController {

	@Autowired
	private SubpackageProgramService subpackageProgramService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private SubBidCompanyService subBidCompanyService;
	
	@Autowired
	private TenderService tenderService;
	
	@Autowired
	private ClearEvaluateService clearEvaluateService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private ProgramService programService;
	
	@ModelAttribute
	public SubBidCompany get(@RequestParam(required=false) String id) {
		SubBidCompany entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = subBidCompanyService.get(id);
		}
		if (entity == null){
			entity = new SubBidCompany();
		}
		return entity;
	}
	
	/**
	 * 子项目参投单位管理列表页面
	 */
//	@RequiresPermissions("subbidcompany:subBidCompany:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/biddingmanagement/tendermanage/subbidcompany/subBidCompanyList";
	}
	
		/**
	 * 子项目参投单位管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("subbidcompany:subBidCompany:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SubBidCompany subBidCompany, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SubBidCompany> page = subBidCompanyService.findPage(new Page<SubBidCompany>(request, response), subBidCompany); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑子项目参投单位管理表单页面
	 */
//	@RequiresPermissions(value={"subbidcompany:subBidCompany:view","subbidcompany:subBidCompany:add","subbidcompany:subBidCompany:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SubBidCompany subBidCompany, Model model) {
		model.addAttribute("subBidCompany", subBidCompany);
		if(StringUtils.isBlank(subBidCompany.getId())){
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/tendermanage/subbidcompany/subBidCompanyForm";
	}

	/**
	 * 保存子项目参投单位管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"subbidcompany:subBidCompany:add","subbidcompany:subBidCompany:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SubBidCompany subBidCompany, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subBidCompany)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		
		//新增或编辑表单保存 ：保存之前获取投标id和投标时间（投标时间不能晚于项目计划开标时间）
		//保存思路：
		//1.一个单位只能在一个项目上投标一次
		//(1)获取当前选取的项目id
		SubpackageProgram subpackageProgram = new SubpackageProgram();
		String subpackageProgramId = "";
		String tenderId = subBidCompany.getTender().getId();//招标id
		Tender tender = tenderService.get(tenderId);//一条招标信息
		subpackageProgram = tender.getSubpackageProgram();
		if(subpackageProgram!=null){
			subpackageProgramId = subpackageProgram.getId();
		}
		//(2)查询其已参投的项目id集合
		List<String> subpackageProgramIdList = subBidCompanyService.listAllSubBidProIdByCompId(subBidCompany);
		//(3)判断与当前是否重复
		if(StringUtils.isBlank(subBidCompany.getId())){//新增时判断是否已投过
			for(String subProId:subpackageProgramIdList){
				if(subProId.equals(subpackageProgramId)){
					j.setSuccess(false);
					j.setMsg("当前单位已投过该项目，不允许重复投标!");
					return j;
				}
			}
		}
		
		Date deadDate = new Date();
		if(subpackageProgram!=null){//排除空指针异常
			subpackageProgram = subpackageProgramService.get(subpackageProgramId);
			deadDate = tender.getOpenBidDate();//投标截止时间
			tender.setSubpackageProgram(subpackageProgram);//同步保存子项目id
		}
		Date date = subBidCompany.getSubBidDate();//投标记录中的投标日期
		if(date.after(deadDate)){
			j.setSuccess(false);
			j.setMsg("投标时间不能晚于项目投标截止时间!");
			return j;
		}
		subBidCompany.setTender(tender);
		subBidCompanyService.save(subBidCompany);//保存
		j.setSuccess(true);
		String companyName = subBidCompany.getCompany().getCompanyName();
		if(companyName!=null && !companyName.equals("")){
			j.setMsg("保存投标单位'" + subBidCompany.getCompany().getCompanyName()+ "'记录成功！");
		}else{
			j.setMsg("保存投标单位记录成功！");
		}
		return j;
	}
	
	//通过名称查询出所有的单位
	@ResponseBody
	@RequestMapping(value = "getAllCompanyList")
	public List<Company> getListAllCompanyByName(@RequestParam  String companyName,String tenderId) {
		List<Company> companyList = new ArrayList<Company>();
		companyList = companyService.listAllCompanyByName(companyName);
		Tender tender = tenderService.get(tenderId);
		String compName = "";
		SubpackageProgram subpackageProgram = new SubpackageProgram();
		Program program = new Program();
		Company comp = new Company();
		if(tender!=null){
			tender = tenderService.getCompanyNameByTenderId(tender);
			if(tender != null){
				subpackageProgram = tender.getSubpackageProgram();
				if(subpackageProgram != null){
					program = subpackageProgram.getProgram();
					if(program != null){
						program = programService.get(program);
						if(program!=null){
							comp = program.getCompany();
							if(comp != null){
								compName = comp.getCompanyName();
							}
						}
					}
				}
			}
		}
		//将金卓从集合中去掉
		Iterator<Company> iterator = companyList.iterator();
		while (iterator.hasNext()) {
			Company company = iterator.next();
			if (company.getCompanyNum().equals("Comp001")|company.getCompanyName().equals(compName))
				iterator.remove();
		}
		return companyList;
	}
	
	/**
	  通过子项目名称查询出所有的子项目
	  */
	@ResponseBody
	@RequestMapping(value = "getTenderList")
	public List<Tender> getsubpackageProgramList(@RequestParam  String subpackageProgramName) {
		SubpackageProgram subpackageProgram = new SubpackageProgram();
		subpackageProgram.setSubpackageProgramName(subpackageProgramName);
		Tender tender = new Tender();
		tender.setSubpackageProgram(subpackageProgram);
		List<Tender> tenderList = new ArrayList<Tender>();
		tenderList = tenderService.ListTenderBySubproName(tender);
		return tenderList;
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
	 * 通过招标信息查询出对应的主项目发包公司
	 */
//	@ResponseBody
//	@RequestMapping(value = "getCompanyNameByTenderId",method = RequestMethod.POST)
//	public Integer getCompanyNameByTenderId(@RequestBody Program program) {
//		String foreginId = program.getId();
//		Integer i = enclosuretabService.countEnclosure(foreginId);
//		System.out.println("i为："+i);
//		return i;
//	}
	
	/**
	 * 删除子项目参投单位管理
	 */
	@ResponseBody
//	@RequiresPermissions("subbidcompany:subBidCompany:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SubBidCompany subBidCompany, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		subBidCompanyService.delete(subBidCompany);
		j.setMsg("删除子项目参投单位管理成功");
		return j;
	}
	
	/**
	 * 批量删除子项目参投单位管理
	 */
	@ResponseBody
//	@RequiresPermissions("subbidcompany:subBidCompany:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		SubBidCompany subBidCompany = new SubBidCompany();//定义投标记录对象
		String idArray[] =ids.split(",");
		for(String id : idArray){
			subBidCompany = subBidCompanyService.get(id);//每次进入循环先获取一条信息
			ClearEvaluate clearEvaluate = new ClearEvaluate();//定义清评对象
			if(subBidCompany!=null){
				clearEvaluate.setSubBidCompany(subBidCompany);//存入招标对象中
				clearEvaluate = clearEvaluateService.getClearEvaluateBySubCompId(clearEvaluate);
				if(clearEvaluate!=null){
					if(clearEvaluate.getId() != null && !clearEvaluate.getId().equals("")){
						j.setSuccess(false);
						j.setMsg("删除失败！开评标报告中存在关联信息");
					}
				}else if(subBidCompany!=null){
					subBidCompanyService.delete(subBidCompany);
					enclosuretabService.deleteEnclosureByForeginId(subBidCompany.getId());//同步删除对应附件
					j.setSuccess(true);
					j.setMsg("删除成功!");
				}
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("subbidcompany:subBidCompany:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SubBidCompany subBidCompany, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "子项目参投单位管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SubBidCompany> page = subBidCompanyService.findPage(new Page<SubBidCompany>(request, response, -1), subBidCompany);
    		new ExportExcel("子项目参投单位管理", SubBidCompany.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出子项目参投单位管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("subbidcompany:subBidCompany:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SubBidCompany> list = ei.getDataList(SubBidCompany.class);
			for (SubBidCompany subBidCompany : list){
				try{
					subBidCompanyService.save(subBidCompany);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条子项目参投单位管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条子项目参投单位管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入子项目参投单位管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subbidcompany/subBidCompany/?repage";
    }
	
	/**
	 * 下载导入子项目参投单位管理数据模板
	 */
//	@RequiresPermissions("subbidcompany:subBidCompany:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "子项目参投单位管理数据导入模板.xlsx";
    		List<SubBidCompany> list = Lists.newArrayList(); 
    		new ExportExcel("子项目参投单位管理数据", SubBidCompany.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subbidcompany/subBidCompany/?repage";
    }

}