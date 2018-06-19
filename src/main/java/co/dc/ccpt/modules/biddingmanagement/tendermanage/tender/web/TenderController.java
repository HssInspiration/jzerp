package co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.service.SubBidCompanyService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service.SubpackageProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.service.TenderService;

import com.google.common.collect.Lists;


/**
 * 招标信息管理Controller
 * @author lxh
 * @version 2018-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/tendermanage/tender")
public class TenderController extends BaseController {

	
	@Autowired
	private SubpackageProgramService subpackageProgramService;
	
	@Autowired
	private SubBidCompanyService subBidCompanyService;
	
	@Autowired
	private TenderService tenderService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public Tender get(@RequestParam(required=false) String id) {
		Tender entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tenderService.get(id);
		}
		if (entity == null){
			entity = new Tender();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubpackageProgram getSubpackageProgram(@RequestParam(required=false) String subproid) {
		SubpackageProgram entity = null;
		if (StringUtils.isNotBlank(subproid)){
			entity = subpackageProgramService.get(subproid);
		}
		if (entity == null){
			entity = new SubpackageProgram();
		}
		return entity;
	}
	
	/**
	 * 招标信息管理列表页面
	 */
//	@RequiresPermissions("tender:tender:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/biddingmanagement/tendermanage/tender/tenderList";
	}
	
		/**
	 * 招标信息管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("tender:tender:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Tender tender, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tender> page = tenderService.findPage(new Page<Tender>(request, response), tender); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑招标信息管理表单页面
	 */
//	@RequiresPermissions(value={"tender:tender:view","tender:tender:add","tender:tender:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Tender tender, Model model) {
		model.addAttribute("tender", tender);
		if(StringUtils.isBlank(tender.getId())){
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/tendermanage/tender/tenderForm";
	}

	@RequestMapping(value = "newform")
	public String newform(Tender tender, SubpackageProgram subpackageProgram, Model model) {
		tender.setSubpackageProgram(subpackageProgram);//将子项目id存入招标对象中
		tender = tenderService.getTenderBySubProId(tender);//通过招标信息中的子项目id获取对应的一条招标信息
		if(tender == null){//如果上述查询为空，创建一个招标信息的实例
			tender = new Tender();
			tender.setSubpackageProgram(subpackageProgram);//将子项目id存入招标信息，此时招标编辑表单中的子项目信息会自动显示
		}
		if(StringUtils.isBlank(tender.getId())){//如果ID是空为添加
			String tenderNum = subpackageProgramService.getLastInsertNum();
			subpackageProgram.setSubpackageProgramNum(tenderNum);
			tender.setSubpackageProgram(subpackageProgram);
			model.addAttribute("tender", tender);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("tender", tender);
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/tendermanage/tender/tenderForm";
	}
	
	/**
	 * 通过分包项目名称查询出所有的单位
	 */
	@ResponseBody
	@RequestMapping(value = "getSubpackageProgramList",method = RequestMethod.POST)
	public List<SubpackageProgram> getSubpackageProgramList(@RequestParam String subpackageProgramName) {
		List<SubpackageProgram> subpackageProgramList = new ArrayList<SubpackageProgram>();
		subpackageProgramList = subpackageProgramService.getSubpackageProgramList(subpackageProgramName);
		return subpackageProgramList;
	}
	
	/**
	 * 保存招标信息管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"tender:tender:add","tender:tender:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Tender tender, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, tender)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		tenderService.save(tender);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存招标信息管理成功");
		return j;
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
	 * 删除招标信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("tender:tender:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Tender tender, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		tenderService.delete(tender);
		j.setMsg("删除招标信息管理成功");
		return j;
	}
	
	/**
	 * 批量删除招标信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("tender:tender:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		Tender tender = new Tender();//定义招标对象
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tender = tenderService.get(id);//每次进入循环先获取一条信息
			SubBidCompany subBidCompany = new SubBidCompany();//定义投标记录对象
			if(tender!=null){
				subBidCompany.setTender(tender);;//存入招标对象中
				List<SubBidCompany> subBidCompList = subBidCompanyService.getSubBidCompanyByTenderId(subBidCompany);//利用招标id查询投标记录信息
				if(subBidCompList!=null && !subBidCompList.isEmpty()){
					j.setSuccess(false);
					j.setMsg("删除失败！投标记录中存在关联信息");
				}else if(tender!=null){
					tenderService.delete(tender);
					enclosuretabService.deleteEnclosureByForeginId(tender.getId());//同步删除对应附件
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
//	@RequiresPermissions("tender:tender:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Tender tender, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "招标信息管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Tender> page = tenderService.findPage(new Page<Tender>(request, response, -1), tender);
    		new ExportExcel("招标信息管理", Tender.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出招标信息管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("tender:tender:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Tender> list = ei.getDataList(Tender.class);
			for (Tender tender : list){
				try{
					tenderService.save(tender);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条招标信息管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条招标信息管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入招标信息管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
    }
	
	/**
	 * 下载导入招标信息管理数据模板
	 */
//	@RequiresPermissions("tender:tender:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "招标信息管理数据导入模板.xlsx";
    		List<Tender> list = Lists.newArrayList(); 
    		new ExportExcel("招标信息管理数据", Tender.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
    }

}