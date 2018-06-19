/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.deposit.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.service.BidtableService;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.depositmanagement.deposit.entity.Deposit;
import co.dc.ccpt.modules.depositmanagement.deposit.service.DepositService;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;
import co.dc.ccpt.modules.depositmanagement.depositapproval.service.DepositApprovalService;

/**
 * 保证金信息管理Controller
 * @author lxh
 * @version 2018-04-11
 */
@Controller
@RequestMapping(value = "${adminPath}/deposit")
public class DepositController extends BaseController {

	@Autowired
	private DepositService depositService;
	
	@Autowired
	private DepositApprovalService depositApprovalService;
	
	@Autowired
	private BidtableService bidtableService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	/**
	 * 项目业务处理层对象
	 */
	@Autowired
	private ProgramService programService;
	
	@ModelAttribute
	public Deposit get(@RequestParam(required=false) String id) {
		Deposit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = depositService.get(id);
		}
		if (entity == null){
			entity = new Deposit();
		}
		return entity;
	}
	
	/**
	 * 保证金信息管理列表页面
	 */
//	@RequiresPermissions("deposit:deposit:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/depositmanagement/deposit/depositList";
	}
	
		/**
	 * 保证金信息管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("deposit:deposit:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Deposit deposit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Deposit> page = depositService.findPage(new Page<Deposit>(request, response), deposit); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保证金信息管理表单页面
	 */
//	@RequiresPermissions(value={"deposit:deposit:view","deposit:deposit:add","deposit:deposit:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Deposit deposit, Model model) {
		
		if(StringUtils.isBlank(deposit.getId())){
			String depositNum = "BZJ-"+DateUtils.getDate("yyyyMM")+depositService.setNewNum();
			deposit.setDepositNum(depositNum);
			model.addAttribute("deposit", deposit);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("deposit", deposit);
			model.addAttribute("edit", true);
		}
		return "modules/depositmanagement/deposit/depositForm";
	}

	/**
	 * 保存保证金信息管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"deposit:deposit:add","deposit:deposit:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Deposit deposit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, deposit)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		
		List<String> depositTypeList = new ArrayList<String>();
		String depositType = deposit.getDepositType();
		depositTypeList = depositService.getDepositTypeListById(deposit);
		if(StringUtils.isBlank(deposit.getId())){
			if(depositTypeList.contains(depositType)){
				j.setSuccess(false);
				j.setMsg("当前项目保证金类型重复！");
				return j;
			}
		}else{
			//若修改保证金金额要更改对应的审批中的等级
			Double depositPrice = deposit.getPayCount();
			String str = depositPrice.toString();
			String checkClass = "";
			DepositApproval depositApproval = new DepositApproval();
			if(StringUtils.isNotBlank(str)){
				depositApproval.setDeposit(deposit);
				depositApproval = depositApprovalService.getDepositApprovalByDepositId(depositApproval);
				if(depositPrice<10){
					checkClass = "2";
				}else if(10<=depositPrice && depositPrice<20){
					checkClass = "3";
				}else if(20<=depositPrice && depositPrice<50){
					checkClass = "4";
				}else if(depositPrice>=50){
					checkClass = "5";
				}
				if(depositApproval!=null){
					depositApproval.setCheckClass(checkClass);
				}
				depositApprovalService.save(depositApproval);
			}
		}
		
		depositService.save(deposit);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存保证金信息管理成功");
		return j;
	}
	
	/**
	 * 通过项目名称查询出所有的项目
	 */
	@ResponseBody
	@RequestMapping(value = "getProgramList",method = RequestMethod.POST)
	public List<Program> getProgramList(@RequestParam String programName) {
		List<Program> programList = new ArrayList<Program>();
		programList = programService.listAllProgramByName(programName);
		return programList;
	}
	
	/**
	 * 通过项目id查询当前所选项目状态
	 * 若为自由、招标--不申请保证金，
	 * 若为施工、竣工、停工、结案--可申请所有类型保证金，
	 * 若为未中标--只申请投标保证金
	 */
	@ResponseBody
	@RequestMapping(value = "getProgramById",method = RequestMethod.POST)
	public Program getProgramById(@RequestBody Program program) {
		String programId = "";
		if(program != null){//排除空指针异常
			programId = program.getId();
		}
		program = programService.get(programId);
		return program;
	}
	
	/**
	 * 通过项目id在保证金表中查询对应的用途（类型）集合
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositTypeListById",method = RequestMethod.POST)
	public List<String> getDepositTypeListById(@RequestBody Program program){
		Deposit deposit = new Deposit();
		List<String> depositTypeList = new ArrayList<String>();
		if(StringUtils.isNotBlank(program.getId())){
			deposit.setProgram(program);
			depositTypeList = depositService.getDepositTypeListById(deposit);
		}
		return depositTypeList;
	}
	
	/**
	 * 通过项目id获取投标对象--保证金金额自动设置
	 * @param program
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositByProId",method = RequestMethod.POST)
	public Bidtable getDepositByProId(@RequestBody Program program) {
		Bidtable bidtable = new Bidtable();
		if(program != null){//排除空指针异常
			bidtable.setProgram(program);
		}
		bidtable = bidtableService.getBidtableByProId(bidtable);
		return bidtable;
	}
	
	/**
	 * 删除保证金信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("deposit:deposit:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Deposit deposit, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		depositService.delete(deposit);
		j.setMsg("删除保证金信息管理成功");
		return j;
	}
	
	/**
	 * 批量删除保证金信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("deposit:deposit:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Deposit deposit = depositService.get(id);//先获取一条数据
			DepositApproval depositApproval = new DepositApproval();//创建对象
			if(deposit!=null){
				depositApproval.setDeposit(deposit);//非空,存入对象中	
				depositApproval = depositApprovalService.getDepositApprovalByDepositId(depositApproval);//获取一条信息
				if(depositApproval!=null){
					if(depositApproval.getId() != null && !depositApproval.getId().equals("")){
						j.setSuccess(false);
						j.setMsg("删除失败！审批中存在关联信息");
					}
				}else if(deposit != null){
					depositService.delete(deposit);
					enclosuretabService.deleteEnclosureByForeginId(deposit.getId());//同步删除对应附件
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
//	@RequiresPermissions("deposit:deposit:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Deposit deposit, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保证金信息管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Deposit> page = depositService.findPage(new Page<Deposit>(request, response, -1), deposit);
    		new ExportExcel("保证金信息管理", Deposit.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保证金信息管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("deposit:deposit:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Deposit> list = ei.getDataList(Deposit.class);
			for (Deposit deposit : list){
				try{
					depositService.save(deposit);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保证金信息管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保证金信息管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保证金信息管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/deposit/?repage";
    }
	
	/**
	 * 下载导入保证金信息管理数据模板
	 */
//	@RequiresPermissions("deposit:deposit:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保证金信息管理数据导入模板.xlsx";
    		List<Deposit> list = Lists.newArrayList(); 
    		new ExportExcel("保证金信息管理数据", Deposit.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"deposit/?repage";
    }

}