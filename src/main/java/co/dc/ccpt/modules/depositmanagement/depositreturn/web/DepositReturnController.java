/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositreturn.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.depositmanagement.deposit.entity.Deposit;
import co.dc.ccpt.modules.depositmanagement.deposit.service.DepositService;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;
import co.dc.ccpt.modules.depositmanagement.depositapproval.service.DepositApprovalService;
import co.dc.ccpt.modules.depositmanagement.depositreturn.entity.DepositReturn;
import co.dc.ccpt.modules.depositmanagement.depositreturn.service.DepositReturnService;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;
import co.dc.ccpt.modules.depositmanagement.depositstatement.service.DepositStatementService;

/**
 * 保证金催退Controller
 * @author lxh
 * @version 2018-04-20
 */
@Controller
@RequestMapping(value = "${adminPath}/depositReturn")
public class DepositReturnController extends BaseController {

	@Autowired
	private DepositReturnService depositReturnService;
	
	@Autowired 
	private DepositStatementService depositStatementService;
	
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private DepositApprovalService depositApprovalService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public DepositReturn get(@RequestParam(required=false) String id) {
		DepositReturn entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = depositReturnService.get(id);
		}
		if (entity == null){
			entity = new DepositReturn();
		}
		return entity;
	}
	
	/**
	 * 保证金催退列表页面
	 */
//	@RequiresPermissions("depositreturn:depositReturn:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/depositmanagement/depositreturn/depositReturnList";
	}
	
	/**
	 * 保证金催退列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("depositreturn:depositReturn:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DepositReturn depositReturn, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DepositReturn> page = depositReturnService.findPage(new Page<DepositReturn>(request, response), depositReturn); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保证金催退表单页面
	 */
//	@RequiresPermissions(value={"depositreturn:depositReturn:view","depositreturn:depositReturn:add","depositreturn:depositReturn:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DepositReturn depositReturn, Model model) {
		if(StringUtils.isBlank(depositReturn.getId())){
			//设置编号：
			String num = depositReturnService.getLastInsertNum();
			String depositReturnNum = "";
			if(num!=null && !num.equals("")){
				depositReturnNum = "CT"+DateUtils.getDate("yyyyMM")+num;
				depositReturn.setReturnNum(depositReturnNum);
			}
			model.addAttribute("depositReturn", depositReturn);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit", true);
			model.addAttribute("depositReturn", depositReturn);
		}
		
		return "modules/depositmanagement/depositreturn/depositReturnForm";
	}

	/**
	 * 保存保证金催退
	 */
	@ResponseBody
//	@RequiresPermissions(value={"depositreturn:depositReturn:add","depositreturn:depositReturn:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DepositReturn depositReturn, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, depositReturn)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		DepositStatement depositStatement = depositReturn.getDepositStatement();
		depositStatement = depositStatementService.get(depositStatement);
		//新增保存前验证不能重复
		if(StringUtils.isBlank(depositReturn.getId())){//判断是否为新增
			if(depositStatement!=null){
				if(!"false".equals(getDepositStatementById(depositStatement))){
					j.setSuccess(false);
					j.setMsg( "当前保证金名称已存在！");
					return j;
				}
				//设置催退时间：（投标模块中业主项目的计划开标时间+14天）
				//先获取计划开标时间
				Date planToStart = null;
				Date newDate = null;
				Deposit deposit = new Deposit();
				Program program = new Program();
				DepositApproval depositApproval = depositStatement.getDepositApproval();
				depositApproval = depositApprovalService.get(depositApproval);
				if(depositApproval!=null){
					deposit = depositApproval.getDeposit();
					deposit = depositService.get(deposit);
					if(deposit!=null){
						program = deposit.getProgram();
						program = programService.get(program);
						if(program!=null){
							planToStart = program.getPlanToStart();
							//调用方法实现从计划开标时间往后推14天并保存到催退对象中
							newDate = DateUtils.addInteger(planToStart, 14);
							logger.info(newDate.toString());
//							program.setPlanToStart(newDate);
//							deposit.setProgram(program);
						}
//						depositApproval.setDeposit(deposit);
					}
//					depositStatement.setDepositApproval(depositApproval);
				}
				depositReturn.setReturnDate(newDate);
			}
		}
		depositReturnService.save(depositReturn);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存保证金催退成功");
		return j;
	}
	
	/**
	 * 通过项目名称查询出所有的项目
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositStatementList",method = RequestMethod.POST)
	public List<DepositStatement> getDepositStatementList(@RequestParam String depositName) {
		Deposit deposit = new Deposit();
		deposit.setDepositName(depositName);
		DepositApproval depositApproval = new DepositApproval();
		depositApproval.setDeposit(deposit);
		DepositStatement depositStatement = new DepositStatement();
		depositStatement.setDepositApproval(depositApproval);
		List<DepositStatement> depositStatementList = new ArrayList<DepositStatement>();
		depositStatementList = depositStatementService.getDepositStatementList(depositStatement);
		return depositStatementList;
	}
	
	/**
	 * 通过保证金id在审批表中查询是否已登记
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositStatementById",method=RequestMethod.POST)
	public String getDepositStatementById(@RequestBody DepositStatement depositStatement) {
		DepositReturn depositReturn = new DepositReturn();
		depositReturn.setDepositStatement(depositStatement);
		depositReturn = depositReturnService.getDepositReturnById(depositReturn);
		if(depositReturn!=null){
			return "true";
		}else{
			return "false";
		}
	}
	
	//变更状态
	@ResponseBody
	@RequestMapping(value = "change")
	public AjaxJson change(DepositReturn depositReturn, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, depositReturn)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//将催退状态做变更0--》1 or 1-->0
		Integer isReturn = depositReturn.getIsReturn();
		if(isReturn != null && !isReturn.equals("")){
			if(isReturn == 0){
				depositReturn.setIsReturn(1);
				//执行审批中的退还时间的新增：
				DepositStatement depositStatement = depositReturn.getDepositStatement();
				depositStatement = depositStatementService.get(depositStatement);
				if(depositStatement!=null){
					DepositApproval depositApproval = depositStatement.getDepositApproval();
					depositApproval = depositApprovalService.get(depositApproval);
					if(depositApproval!=null){
						Date date = new Date();
						depositApproval.setRefundDate(date);
						depositApprovalService.save(depositApproval);
					}
				}
			}else if(isReturn == 1){
				depositReturn.setIsReturn(0);
				//执行审批中的退还时间的还原：
				DepositStatement depositStatement = depositReturn.getDepositStatement();
				depositStatement = depositStatementService.get(depositStatement);
				if(depositStatement!=null){
					DepositApproval depositApproval = depositStatement.getDepositApproval();
					depositApproval = depositApprovalService.get(depositApproval);
					if(depositApproval!=null){
						Date date = null;
						depositApproval.setRefundDate(date);
						depositApprovalService.save(depositApproval);
					}
				}
			}
			depositReturnService.save(depositReturn);
			j.setSuccess(true);
			j.setMsg("保存催退信息成功！");
		}
		return j;
	}
	
	/**
	 * 通过项目id从保证金申请表中查询出对应的保证金类型集合
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositTypeListById",method = RequestMethod.POST)
	public List<String> getDepositTypeListById(@RequestBody DepositStatement depositStatement) {
		// 获取前台传入的id
		depositStatement = depositStatementService.get(depositStatement);
		List<String> depositTypeList = new ArrayList<String>();
		if(depositStatement!=null){
			DepositApproval depositApproval = depositStatement.getDepositApproval();
			if(depositApproval!=null){
				Deposit deposit = depositApproval.getDeposit();
				if(deposit!=null){
					logger.info(deposit.getId());
					depositTypeList = depositService.getDepositTypeListById(deposit);
				}
			}
		}
		return depositTypeList;
	}
	
	/**
	 * 删除保证金催退
	 */
	@ResponseBody
//	@RequiresPermissions("depositreturn:depositReturn:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DepositReturn depositReturn, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		depositReturnService.delete(depositReturn);
		j.setMsg("删除保证金催退成功");
		return j;
	}
	
	/**
	 * 批量删除保证金催退
	 */
	@ResponseBody
//	@RequiresPermissions("depositreturn:depositReturn:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//删除前添加判断：如果当前为未退回状态不可删除，若为已退回则可以删除
			DepositReturn depositreturn = depositReturnService.get(id);
			Integer status = null;
			if(depositreturn !=null){
				status = depositreturn.getIsReturn();
				if(status==0){//未退回
					j.setSuccess(false);
					j.setMsg("删除失败！当前保证金未退回，不可删除！");
					return j;
				}else if(status==1){//已退回
					depositReturnService.delete(depositreturn);
					enclosuretabService.deleteEnclosureByForeginId(depositreturn.getId());//同步删除对应附件
					//删除后执行审批中的退还时间的还原：
					DepositStatement depositStatement = depositreturn.getDepositStatement();
					depositStatement = depositStatementService.get(depositStatement);
					if(depositStatement!=null){
						DepositApproval depositApproval = depositStatement.getDepositApproval();
						depositApproval = depositApprovalService.get(depositApproval);
						if(depositApproval!=null){
							Date date = null;
							depositApproval.setRefundDate(date);
							depositApprovalService.save(depositApproval);
						}
					}
					j.setSuccess(true);
					j.setMsg("删除成功！");
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
//	@RequiresPermissions("depositreturn:depositReturn:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(DepositReturn depositReturn, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保证金催退"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DepositReturn> page = depositReturnService.findPage(new Page<DepositReturn>(request, response, -1), depositReturn);
    		new ExportExcel("保证金催退", DepositReturn.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保证金催退记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("depositreturn:depositReturn:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DepositReturn> list = ei.getDataList(DepositReturn.class);
			for (DepositReturn depositReturn : list){
				try{
					depositReturnService.save(depositReturn);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保证金催退记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保证金催退记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保证金催退失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/depositreturn/?repage";
    }
	
	/**
	 * 下载导入保证金催退数据模板
	 */
//	@RequiresPermissions("depositreturn:depositReturn:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保证金催退数据导入模板.xlsx";
    		List<DepositReturn> list = Lists.newArrayList(); 
    		new ExportExcel("保证金催退数据", DepositReturn.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/depositReturn/?repage";
    }

}