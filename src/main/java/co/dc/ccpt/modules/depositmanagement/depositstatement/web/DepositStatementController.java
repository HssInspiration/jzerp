package co.dc.ccpt.modules.depositmanagement.depositstatement.web;

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
import co.dc.ccpt.modules.depositmanagement.deposit.entity.Deposit;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;
import co.dc.ccpt.modules.depositmanagement.depositapproval.service.DepositApprovalService;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;
import co.dc.ccpt.modules.depositmanagement.depositstatement.service.DepositStatementService;

/**
 * 保证金出账记录Controller
 * @author lxh
 * @version 2018-04-20
 */
@Controller
@RequestMapping(value = "${adminPath}/depositStatement")
public class DepositStatementController extends BaseController {

	@Autowired
	private DepositStatementService depositStatementService;
	
	@Autowired
	private DepositApprovalService depositApprovalService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public DepositStatement get(@RequestParam(required=false) String id) {
		DepositStatement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = depositStatementService.get(id);
		}
		if (entity == null){
			entity = new DepositStatement();
		}
		return entity;
	}
	
	@ModelAttribute
	public DepositApproval getApproId(@RequestParam(required=false) String approId) {
		DepositApproval entity = null;
		if (StringUtils.isNotBlank(approId)){
			entity = depositApprovalService.get(approId);
		}
		if (entity == null){
			entity = new DepositApproval();
		}
		return entity;
	}
	
	/**
	 * 保证金出账记录列表页面
	 */
//	@RequiresPermissions("depositstatement:depositStatement:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/depositmanagement/depositstatement/depositStatementList";
	}
	
		/**
	 * 保证金出账记录列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("depositstatement:depositStatement:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DepositStatement depositStatement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DepositStatement> page = depositStatementService.findPage(new Page<DepositStatement>(request, response), depositStatement); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保证金出账记录表单页面
	 */
//	@RequiresPermissions(value={"depositstatement:depositStatement:view","depositstatement:depositStatement:add","depositstatement:depositStatement:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DepositStatement depositStatement, DepositApproval depositApproval, Model model) {
		depositStatement.setDepositApproval(depositApproval);//将id存入出账对象中
		depositStatement = depositStatementService.getDepositStatementByApprovalId(depositStatement);//通过出账中的审批id获取对应的一条出账信息
		if(depositStatement == null){//如果上述查询为空，创建一个出账信息的实例
			depositStatement = new DepositStatement();
			depositStatement.setDepositApproval(depositApproval);//将id存入出账信息，此时出账编辑表单中的审批信息会自动显示
		}
		if(StringUtils.isBlank(depositStatement.getId())){//增加
			//设置出账编号：
			String statementNum = "CZ"+DateUtils.getDate("yyyyMM")+depositStatementService.getLastInsertNum();
			depositStatement.setStatementNum(statementNum);
			depositStatement.setDepositApproval(depositApproval);
			model.addAttribute("depositStatement", depositStatement);
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("depositStatement", depositStatement);
			model.addAttribute("edit", true);
		}
		return "modules/depositmanagement/depositstatement/depositStatementForm";
	}

	/**
	 * 保存保证金出账记录
	 */
	@ResponseBody
//	@RequiresPermissions(value={"depositstatement:depositStatement:add","depositstatement:depositStatement:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DepositStatement depositStatement, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, depositStatement)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//获取审批的id
		DepositApproval depositApproval = depositStatement.getDepositApproval();
		String depositApprovalId = "";
		if(depositApproval!=null){
			depositApprovalId = depositApproval.getId();//获取id
		}
		depositApproval = depositApprovalService.get(depositApprovalId);//通过id获取一条对应的审批信息
		Date date= depositApproval.getStatementDate();
		if(date == null){//若日期为空设置当前日期为出账日期
			depositApproval.setStatementDate(new Date());
			depositApprovalService.save(depositApproval);
		}
		depositStatementService.save(depositStatement);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存保证金出账记录成功");
		return j;
	}
	
	
	/**
	 * 删除保证金出账记录
	 */
	@ResponseBody
//	@RequiresPermissions("depositstatement:depositStatement:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DepositStatement depositStatement, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		depositStatementService.delete(depositStatement);
		j.setMsg("删除保证金出账记录成功");
		return j;
	}
	
	/**
	 * 批量删除保证金出账记录
	 */
	@ResponseBody
//	@RequiresPermissions("depositstatement:depositStatement:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			DepositStatement depositStatement = depositStatementService.get(id);
			if(depositStatement != null){
				DepositApproval depositApproval = depositStatement.getDepositApproval();
				depositApproval = depositApprovalService.get(depositApproval);
				if(depositApproval!=null){
					Date date = null;
					depositApproval.setStatementDate(date);
					depositApprovalService.save(depositApproval);
				}
			}
			depositStatementService.delete(depositStatement);
			enclosuretabService.deleteEnclosureByForeginId(depositStatement.getId());//同步删除对应附件
			//出账记录删除后将原先的审批中的出账记录时间还原（清空）
			
		}
		j.setMsg("删除保证金出账记录成功");
		return j;
	}
	
	/**
	 * 通过项目名称查询出所有的项目
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositApprovalList",method = RequestMethod.POST)
	public List<DepositApproval> getDepositApprovalList(@RequestParam String depositName) {
		Deposit deposit = new Deposit();
		deposit.setDepositName(depositName);
		DepositApproval depositApproval = new DepositApproval();
		depositApproval.setDeposit(deposit);
		List<DepositApproval> depositApprovalList = new ArrayList<DepositApproval>();
		depositApprovalList = depositApprovalService.getDepositApprovalList(depositApproval);
		return depositApprovalList;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("depositstatement:depositStatement:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(DepositStatement depositStatement, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保证金出账记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DepositStatement> page = depositStatementService.findPage(new Page<DepositStatement>(request, response, -1), depositStatement);
    		new ExportExcel("保证金出账记录", DepositStatement.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保证金出账记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("depositstatement:depositStatement:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DepositStatement> list = ei.getDataList(DepositStatement.class);
			for (DepositStatement depositStatement : list){
				try{
					depositStatementService.save(depositStatement);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保证金出账记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保证金出账记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保证金出账记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/depositstatement/depositStatement/?repage";
    }
	
	/**
	 * 下载导入保证金出账记录数据模板
	 */
//	@RequiresPermissions("depositstatement:depositStatement:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保证金出账记录数据导入模板.xlsx";
    		List<DepositStatement> list = Lists.newArrayList(); 
    		new ExportExcel("保证金出账记录数据", DepositStatement.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/depositstatement/depositStatement/?repage";
    }

}