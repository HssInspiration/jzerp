package co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.web;

import java.text.SimpleDateFormat;
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
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service.SubpackageProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.service.TenderService;

import com.google.common.collect.Lists;


/**
 * 子项目工程管理Controller
 * @author lxh
 * @version 2018-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/tendermanage/subpackageProgram")
public class SubpackageProgramController extends BaseController {

	@Autowired
	private SubpackageProgramService subpackageProgramService;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private TenderService tenderService;
	
	@ModelAttribute
	public SubpackageProgram get(@RequestParam(required=false) String id) {
		SubpackageProgram entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = subpackageProgramService.get(id);
		}
		if (entity == null){
			entity = new SubpackageProgram();
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
	 * 子项目工程管理列表页面
	 */
//	@RequiresPermissions("subpackage:subpackageProgram:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/biddingmanagement/tendermanage/subprogram/subpackageProgramList";
	}
	
		/**
	 * 子项目工程管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("subpackage:subpackageProgram:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SubpackageProgram subpackageProgram, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SubpackageProgram> page = subpackageProgramService.findPage(new Page<SubpackageProgram>(request, response), subpackageProgram); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑子项目工程管理表单页面
	 */
//	@RequiresPermissions(value={"subpackage:subpackageProgram:view","subpackage:subpackageProgram:add","subpackage:subpackageProgram:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SubpackageProgram subpackageProgram, Model model) throws Exception{
		model.addAttribute("subpackageProgram", subpackageProgram);
		if(StringUtils.isBlank(subpackageProgram.getId())){
			//生成编号:
//			1.设置编号自增数
//			Integer num = subpackageProgramService.getNumCount()+1;
			String oldNum = subpackageProgramService.getLastInsertNum();
			String numberStr = "";
			Integer num = 0;
			if(oldNum!=null && !oldNum.equals("")){
				String subStringNum = oldNum.substring(oldNum.length()-6, oldNum.length()-3);
				if(subStringNum.substring(0, 1).equals("0")){//若后三位中的前一位是0
					subStringNum = subStringNum.substring(1, 3);
					if(subStringNum.substring(0, 1).equals("0")){//若后两位中的前一位是0
						subStringNum = subStringNum.substring(1, 2);
					}
				}
				num = Integer.parseInt(subStringNum)+1; //将截取到的编号设成integer类型并加1
				numberStr = num.toString();//转成String类型用于拼接 
			}else{
				numberStr = "1";
			}
			if(num<10){
				numberStr = "00"+numberStr;
			}else if(num>=10 & num<100){
				numberStr = "0"+numberStr;
			}
			//2.设置字符串拼接
			String numStr = "JSJZZB";//固定字符串
			Date date=new Date();
			String dateStr  = new SimpleDateFormat("yyyy").format(date);//日期字符串--后期要考虑实际使用中一年后的编号排序要从01开始
			/**
			 * 1.确认dateStr最后一位是否变化，若是进行第二步，若否，继续沿用前面的公式；
			 * 2.最后一位变化，说明年份增加，（不考虑录入2018之前的数据）取出当前表中所有的数据条数；
			 * 3.dateStr继续使用，但自增编号要用当前自增编号减去数据库中已有的数据条数，重新自01开始自增
			 */
			//3.赋值给编号
			String finalNum = numStr+dateStr+numberStr+"-";
			System.out.println(finalNum);
			model.addAttribute("subpackageProgramNum", finalNum);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		
		return "modules/biddingmanagement/tendermanage/subprogram/subpackageProgramForm";
	}

	
	/**
	 * 通过项目id查询出对应工程类别集合
	 */
	@ResponseBody
	@RequestMapping(value = "getTypeByParentId",method = RequestMethod.POST)
	public List<Integer> getTypeByParentId(@RequestBody Program program) {
		List<Integer> programTypeList = new ArrayList<Integer>();
		SubpackageProgram subpackageProgram = new SubpackageProgram();
		System.out.println("id:"+program.getId());
		subpackageProgram.setProgram(program);
		programTypeList = subpackageProgramService.getTypeByParentId(subpackageProgram);
		return programTypeList;
	}
	
	/**
	 * 保存子项目工程管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"subpackage:subpackageProgram:add","subpackage:subpackageProgram:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SubpackageProgram subpackageProgram, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subpackageProgram)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//验证计划开始时间小于计划结束时间
		Date startDate = subpackageProgram.getPlanToStart();
		Date endDate = subpackageProgram.getPlanToEnd();
		if(startDate!=null){
			if(endDate!=null){
				if(startDate.after(endDate)){
					j.setSuccess(false);
					j.setMsg("计划开始时间不能大于计划结束时间！");
					return j;
				}
			}else{
				j.setSuccess(false);
				j.setMsg("计划结束时间不允许为空！");
				return j;
			}
		}else{
			j.setSuccess(false);
			j.setMsg("计划开始时间不允许为空！");
			return j;
		}
		//1.获取父项目对象
		Program program = subpackageProgram.getProgram();
		//2.用id查询出对应的类型集合
		List<String> programTypeList = new ArrayList<String>();
		if(program!=null){
			programTypeList = programService.getProgramTypeById(program);
		}
		//3.获取类别
		Integer programType = subpackageProgram.getSubProgramType();
		//保存前验证当前类别不存在已记录的情况
		List<Integer> typeList = subpackageProgramService.getTypeByParentId(subpackageProgram);
		//修改保存之前做验证:如果子项目类别中父项目中没有，提示增加失败！
		if(StringUtils.isNotBlank(subpackageProgram.getId())){
			if(programType!=null){
				String programNewType = programType.toString();
				//4.遍历集合与当下的做匹配，如果匹配不上则不更新
				for(String str:programTypeList){
					if(programNewType==str||programNewType.equals(str)){
						subpackageProgramService.save(subpackageProgram);
						j.setSuccess(true);
						j.setMsg("保存子项目工程管理成功");
						break;
					}
				}
				return j;
			}else{
				j.setSuccess(false);
				j.setMsg("招标工程类别不允许为空！");
				return j;
			}
		}else{
			for(int i:typeList){
				if(i==programType){
					j.setSuccess(false);
					j.setMsg("招标工程类别已存在，不允许重复添加！");
					return j;
				}
			}
				subpackageProgramService.save(subpackageProgram);//新建
				j.setSuccess(true);
				j.setMsg("保存子项目工程管理成功");
				return j;
		}
	}
	
	/**
	 * 通过项目名称查询出施工中的项目
	 */
	@ResponseBody
	@RequestMapping(value = "getProgramList",method = RequestMethod.POST)
	public List<Program> getProgramList(@RequestParam String programName) {
		List<Program> programList = new ArrayList<Program>();
		programList = programService.listProgramByisBid(programName);
		return programList;
	}
	
	/**
	 * 通过项目id查询出对应集合类型
	 */
	@ResponseBody
	@RequestMapping(value = "getProgramTypeById",method = RequestMethod.POST)
	public List<String> getProgramTypeById(@RequestBody Program program) {
		System.out.println(program.getId());
		List<String> programTypeList = programService.getProgramTypeById(program);
		for(String str:programTypeList){
			System.out.println(str);
		}
		return programTypeList;
	}
	
	/**
	 * 删除子项目工程管理
	 */
	@ResponseBody
//	@RequiresPermissions("subpackage:subpackageProgram:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SubpackageProgram subpackageProgram, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		subpackageProgramService.delete(subpackageProgram);
		j.setMsg("删除子项目工程管理成功");
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
	 * 批量删除子项目工程管理
	 */
	@ResponseBody
//	@RequiresPermissions("subpackage:subpackageProgram:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		SubpackageProgram subpackageProgram = new SubpackageProgram();
		String idArray[] =ids.split(",");
		//删除之前作出判断，招标中是否存在相关联的信息
		//删除前还应判断当前项目是否结案、竣工、停工---获取主项目名称是时也要考虑这种情况
		for(String id : idArray){
			subpackageProgram = subpackageProgramService.get(id);
			Tender tender = new Tender();
			if(subpackageProgram!=null){
				tender.setSubpackageProgram(subpackageProgram);
				tender = tenderService.getTenderBySubProId(tender);
				Program program = subpackageProgram.getProgram();
				if(tender!=null){
					if(tender.getId() != null && !tender.getId().equals("")){
						j.setSuccess(false);
						j.setMsg("删除失败！招标标管理中存在关联信息");
						return j;
					}
				}
				if(program != null){
					program = programService.get(program);
					if(program != null){
						Integer status = program.getStatus();
						if(status == 3){//竣工
							j.setSuccess(false);
							j.setMsg("项目已竣工，不可删除分包项目！");
							return j;
						}else if(status == 4){//停工
							j.setSuccess(false);
							j.setMsg("项目已停工，不可删除分包项目！");
							return j;
						}else if(status== 5){//结案
							j.setSuccess(false);
							j.setMsg("项目已结案，不可删除分包项目");
							return j;
						}
					}
				}
				subpackageProgramService.delete(subpackageProgram);
				j.setSuccess(true);
				j.setMsg("删除子项目工程管理成功!");
				return j;
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("subpackage:subpackageProgram:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SubpackageProgram subpackageProgram, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "子项目工程管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SubpackageProgram> page = subpackageProgramService.findPage(new Page<SubpackageProgram>(request, response, -1), subpackageProgram);
    		new ExportExcel("子项目工程管理", SubpackageProgram.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出子项目工程管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("subpackage:subpackageProgram:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SubpackageProgram> list = ei.getDataList(SubpackageProgram.class);
			for (SubpackageProgram subpackageProgram : list){
				try{
					subpackageProgramService.save(subpackageProgram);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条子项目工程管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条子项目工程管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入子项目工程管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subpackage/subpackageProgram/?repage";
    }
	
	/**
	 * 下载导入子项目工程管理数据模板
	 */
//	@RequiresPermissions("subpackage:subpackageProgram:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "子项目工程管理数据导入模板.xlsx";
    		List<SubpackageProgram> list = Lists.newArrayList(); 
    		new ExportExcel("子项目工程管理数据", SubpackageProgram.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subpackage/subpackageProgram/?repage";
    }

}