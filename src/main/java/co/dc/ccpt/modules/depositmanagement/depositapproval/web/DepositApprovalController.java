/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.depositmanagement.depositapproval.web;

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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.depositmanagement.deposit.entity.Deposit;
import co.dc.ccpt.modules.depositmanagement.deposit.service.DepositService;
import co.dc.ccpt.modules.depositmanagement.depositapproval.entity.DepositApproval;
import co.dc.ccpt.modules.depositmanagement.depositapproval.service.DepositApprovalService;
import co.dc.ccpt.modules.depositmanagement.depositstatement.entity.DepositStatement;
import co.dc.ccpt.modules.depositmanagement.depositstatement.service.DepositStatementService;
import co.dc.ccpt.modules.sys.entity.Role;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;
import co.dc.ccpt.modules.sys.utils.UserUtils;


/**
 * 保证金审批Controller
 * @author lxh
 * @version 2018-04-20
 */
@Controller
@RequestMapping(value = "${adminPath}/depositApproval")
public class DepositApprovalController extends BaseController {

	@Autowired
	private DepositApprovalService depositApprovalService;
	
	@Autowired
	private DepositService depositService; 
	
	@Autowired
	private DepositStatementService depositStatementService; 
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private SystemService userService; 
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@ModelAttribute
	public DepositApproval get(@RequestParam(required=false) String id) {
		DepositApproval entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = depositApprovalService.get(id);
		}
		if (entity == null){
			entity = new DepositApproval();
		}
		return entity;
	}
	
	/**
	 * 保证金审批列表页面
	 */
//	@RequiresPermissions("depositapproval:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/depositmanagement/depositapproval/depositApprovalList";
	}
	
		/**
	 * 保证金审批列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("depositapproval:depositApproval:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DepositApproval depositApproval, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DepositApproval> page = depositApprovalService.findPage(new Page<DepositApproval>(request, response), depositApproval); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑保证金审批表单页面
	 */
//	@RequiresPermissions(value={"depositapproval:depositApproval:view","depositapproval:depositApproval:add","depositapproval:depositApproval:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DepositApproval depositApproval, Model model) {
		if(StringUtils.isBlank(depositApproval.getId())){
			String approvalNum = "BZJSP"+DateUtils.getDate("yyyyMM")+depositApprovalService.getLastInsertNum();
			depositApproval.setApprovalNum(approvalNum);
			model.addAttribute("isAdd", true);
			model.addAttribute("depositApproval", depositApproval);
		}else{
			model.addAttribute("edit", true);
			model.addAttribute("depositApproval", depositApproval);
		}
		
		return "modules/depositmanagement/depositapproval/depositApprovalForm";
	}
	
	@RequestMapping(value = "check")
	public String check(DepositApproval depositApproval, Model model) {
		User user = UserUtils.getUser();
		String userId = user.toString();
		User newUser = userService.getUser(userId);
		if(newUser != null){
//			Role role = newUser.getRole();
			//获取人当前登录人员的角色集合，给予对应的审批权限：
			List<Role> roleList = new ArrayList<Role>();
			roleList = newUser.getRoleList();
			List<String> roleIdList = new ArrayList<String>();
			if(roleList!=null && roleList.size()>0){
				for(Role r : roleList){
					String roleId = r.getId();
					roleIdList.add(roleId);
				}
				if(roleIdList.contains("6e67ffecc9434305812becb3f5a879c0")){
					model.addAttribute("num1", 1);//经营部技术人员
				}else if(roleIdList.contains("7092829b903f404f8357956612f1aeef")){
					model.addAttribute("num2", 2);//经营部负责人
				}else if(roleIdList.contains("4768d8801d5d45329275d50909412f6f")){
					model.addAttribute("num3", 3);//总经理
				}else if(roleIdList.contains("07e097b4e34c4e949f4701ef757f5d18")){
					model.addAttribute("num4", 4);//董事长
				}else if(roleIdList.contains("6ee0b6c5a4834910a2e286d26eac7e51")){
					model.addAttribute("num5", 5);//集团董事长
				}
			}
//			if(role!=null){
//				model.addAttribute("role", role);
//			}
		}
		model.addAttribute("depositApproval", depositApproval);
		return "modules/depositmanagement/depositapproval/check";
	}
	
	/**
	 * 通过项目id从保证金申请表中查询出对应的保证金类型集合
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositTypeListById",method = RequestMethod.POST)
	public List<String> getDepositTypeListById(@RequestBody Deposit deposit) {
		// 获取前台传入的id
		deposit = depositService.get(deposit);
		List<String> depositTypeList = new ArrayList<String>();
		if(deposit!=null){
			logger.info(deposit.getId());
			depositTypeList = depositService.getDepositTypeListById(deposit);
		}
		return depositTypeList;
	}
	
	/**
	 * 通过项目id查询出对应工程类别集合
	 */
	@ResponseBody
	@RequestMapping(value = "getTypeByDepositId",method = RequestMethod.POST)
	public List<String> getTypeByDepositId(@RequestBody Deposit deposit) {
		List<String> depositTypeList = new ArrayList<String>();
		//当前与子项目中取类型有区别，应用项目名称在保证金表中具体匹配
		deposit = depositService.get(deposit);
		Program program = deposit.getProgram();
		if(program!=null){
			program = programService.get(program);
			String programName = program.getProgramName();
			if(programName!=null && !programName.equals("")){
				//调用方法查询出保证金中当前项目名的类型集合
				depositTypeList = depositApprovalService.getDepositTypeListByProName(programName);
			}
		}
		return depositTypeList;
	}
	
	/**
	 * 变更出账时间
	 */
	@RequestMapping(value = "changeDate")
	public String changeDate(DepositApproval depositApproval, Model model) {
		model.addAttribute("depositApproval", depositApproval);
		return "modules/depositmanagement/depositapproval/check";
	}
	
	/**
	 * 保存保证金审批
	 */
//	@RequiresPermissions("depositapproval:check")
	@ResponseBody
//	@RequiresPermissions(value={"depositapproval:depositApproval:add","depositapproval:depositApproval:edit","depositapproval:depositApproval:check"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DepositApproval depositApproval, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, depositApproval)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//先获取审批级别--再获取当前已审批情况
		//审批级别
		String checkClass = "";
		Double depositCount = null;
		Deposit deposit = depositApproval.getDeposit();
		//级别依据保证金金额（<10万二级审批，<=10<20三级审批，<=20<50四级审批，50<=五级审批）
		if(deposit!=null){
			deposit = depositService.get(deposit);//获取一条
			if(deposit!=null){
				depositCount = deposit.getPayCount();
				if(depositCount<=10){
					checkClass = "2";
				}else if(10<depositCount && depositCount<=20){
					checkClass = "3";
				}else if(20<depositCount && depositCount<=50){
					checkClass = "4";
				}else if(depositCount>50){
					checkClass = "5";
				}
				depositApproval.setCheckClass(checkClass);
			}
		}
//		checkClass = depositApproval.getCheckClass();//1--5个级别
		//当前审批状况（人员是否已审批）
		String operator = depositApproval.getOperator();//经办
		String managingDirector = depositApproval.getManagingDirector();//分管负责人
		String topManager = depositApproval.getTopManager();//总经理
		String chairMan = depositApproval.getChairman();//董事长
		String groupChairMan = depositApproval.getGroupChairman();//集团董事长
		if(deposit!=null){
			deposit = depositService.get(deposit);
			if(deposit!=null){
				if(checkClass!=null){
					switch(checkClass){//判断级别为几级（在第五级审批中有详细代码注释）
						case "1":
							logger.info("这是级别一");
							//若只有一个级别审批，那直接将待审核转成审核通过或审核不通过
							if(!StringUtils.isBlank(operator)){
								if(operator.equals("1")){
									deposit.setCheckStatus(2);
									depositService.save(deposit);
								}else if(operator.equals("0")){
									deposit.setCheckStatus(3);
									depositService.save(deposit);
//									depositApproval.setCheckStatus(3);
								}
							}
							break;
						case "2":
							logger.info("这是级别二");
							//若为二级审批，当一级审批完之后将状态变更为审核中，二级审核完变为审核通过状态或者未通过
							if(!StringUtils.isBlank(operator) && operator.equals("1")){
//								depositApproval.setCheckStatus(1);
								deposit.setCheckStatus(1);
								depositService.save(deposit);
								if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("1")){
//									depositApproval.setCheckStatus(2);
									deposit.setCheckStatus(2);
									depositService.save(deposit);
								}else if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("0")){
//									depositApproval.setCheckStatus(3);
									deposit.setCheckStatus(3);
									depositService.save(deposit);
								}
							}else if(!StringUtils.isBlank(operator) && operator.equals("0")){
//								depositApproval.setCheckStatus(3);
								deposit.setCheckStatus(3);
								depositService.save(deposit);
							}
							break;
						case "3":
							logger.info("这是级别三");
							if(!StringUtils.isBlank(operator) && operator.equals("1")){
//								depositApproval.setCheckStatus(1);
								deposit.setCheckStatus(1);
								depositService.save(deposit);
								if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("1")){
									if(!StringUtils.isBlank(topManager) && topManager.equals("1")){
//										depositApproval.setCheckStatus(2);
										deposit.setCheckStatus(2);
										depositService.save(deposit);
									}else if(!StringUtils.isBlank(topManager) && topManager.equals("0")){
//										depositApproval.setCheckStatus(3);
										deposit.setCheckStatus(3);
										depositService.save(deposit);
									}
								}else if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("0")){
//									depositApproval.setCheckStatus(3);
									deposit.setCheckStatus(3);
									depositService.save(deposit);
								}
							}else if(!StringUtils.isBlank(operator) && operator.equals("0")){
//								depositApproval.setCheckStatus(3);
								deposit.setCheckStatus(3);
								depositService.save(deposit);
							}
							break;
						case "4":
							logger.info("这是级别四");
							if(!StringUtils.isBlank(operator) && operator.equals("1")){
//								depositApproval.setCheckStatus(1);
								deposit.setCheckStatus(1);
								depositService.save(deposit);
								if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("1")){
									if(!StringUtils.isBlank(topManager) && topManager.equals("1")){
										if(!StringUtils.isBlank(chairMan) && chairMan.equals("1")){
//											depositApproval.setCheckStatus(2);
											deposit.setCheckStatus(2);
											depositService.save(deposit);
										}else if(!StringUtils.isBlank(chairMan) && chairMan.equals("0")){
//											depositApproval.setCheckStatus(3);
											deposit.setCheckStatus(3);
											depositService.save(deposit);
										}
									}else if(!StringUtils.isBlank(topManager) && topManager.equals("0")){
//										depositApproval.setCheckStatus(3);
										deposit.setCheckStatus(3);
										depositService.save(deposit);
									}
								}else if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("0")){
//									depositApproval.setCheckStatus(3);
									deposit.setCheckStatus(3);
									depositService.save(deposit);
								}
							}else if(!StringUtils.isBlank(operator) && operator.equals("0")){
//								depositApproval.setCheckStatus(3);
								deposit.setCheckStatus(3);
								depositService.save(deposit);
							}
							break;
						case "5":
							logger.info("这是级别五");
							if(!StringUtils.isBlank(operator) && operator.equals("1")){//当前为五级审批，经办非空且审核通过-->设置状态为审核中
//								depositApproval.setCheckStatus(1);
								deposit.setCheckStatus(1);
								depositService.save(deposit);
								if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("1")){//分管领导非空且审核通过
									if(!StringUtils.isBlank(topManager) && topManager.equals("1")){//总经理非空且审核通过
										if(!StringUtils.isBlank(chairMan) && chairMan.equals("1")){//董事长非空且审核通过
											if(!StringUtils.isBlank(groupChairMan) && groupChairMan.equals("1")){//集团董事长非空且审核通过-->设置状态为审核通过
//												depositApproval.setCheckStatus(2);
												deposit.setCheckStatus(2);
												depositService.save(deposit);
											}else if(!StringUtils.isBlank(groupChairMan) && groupChairMan.equals("0")){//集团董事长非空且审核不通过
//												depositApproval.setCheckStatus(3);
												deposit.setCheckStatus(3);
												depositService.save(deposit);
											}
										}else if(!StringUtils.isBlank(chairMan) && chairMan.equals("0")){//董事长非空且审核不通过
//											depositApproval.setCheckStatus(3);
											deposit.setCheckStatus(3);
											depositService.save(deposit);
										}
									}else if(!StringUtils.isBlank(topManager) && topManager.equals("0")){//总经理非空且审核不通过
//										depositApproval.setCheckStatus(3);
										deposit.setCheckStatus(3);
										depositService.save(deposit);
									}
								}else if(!StringUtils.isBlank(managingDirector) && managingDirector.equals("0")){//分管领导非空且审核不通过
//									depositApproval.setCheckStatus(3);
									deposit.setCheckStatus(3);
									depositService.save(deposit);
								}
							}else if(!StringUtils.isBlank(operator) && operator.equals("0")){//经办非空且审核不通过
//								depositApproval.setCheckStatus(3);
								deposit.setCheckStatus(3);
								depositService.save(deposit);
							}
							
							break;
						default:
							logger.info("当前审批级别有误！请检查");
							break;
			 		}
				}
			}
		}
		if(StringUtils.isBlank(depositApproval.getId())){
			//新增保存前验证不能重复登记
			if(deposit!=null){
				if(!"false".equals(getDepositById(deposit))){
					j.setSuccess(false);
					j.setMsg( "当前保证金名称已存在！");
					return j;
				}
			}
			
		}
		depositApprovalService.save(depositApproval);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存保证金审批成功");
		return j;
	}
	
	/**
	 * 通过项目名称查询出所有的项目
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositList")
	public List<Deposit> getDepositList(@RequestParam String depositName) {
		Deposit deposit = new Deposit();
		deposit.setDepositName(depositName);
		List<Deposit> depositList = new ArrayList<Deposit>();
		depositList = depositService.getProNameInDepositByProName(deposit);
		return depositList;
	}
	
	/**
	 * 通过保证金id在审批表中查询是否已登记
	 */
	@ResponseBody
	@RequestMapping(value = "getDepositById",method=RequestMethod.POST)
	public String getDepositById(@RequestBody Deposit deposit) {
		DepositApproval depositApproval = new DepositApproval();
		depositApproval.setDeposit(deposit);
		depositApproval = depositApprovalService.getDepositApprovalByDepositId(depositApproval);
		if(depositApproval!=null){
			return "true";
		}else{
			return "false";
		}
	}
	
	/**
	 * 删除保证金审批
	 */
	@ResponseBody
//	@RequiresPermissions("depositapproval:depositApproval:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DepositApproval depositApproval, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		depositApprovalService.delete(depositApproval);
		j.setMsg("删除保证金审批成功");
		return j;
	}
	
	/**
	 * 批量删除保证金审批
	 */
	@ResponseBody
//	@RequiresPermissions("depositapproval:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		//删除前判断：状态为待审核且出账中无记录
		for(String id : idArray){
			DepositApproval depositApproval = depositApprovalService.get(id);//先获取一条审批数据
			DepositStatement depositStatement = new DepositStatement();//创建出账对象
			if(depositApproval!=null){
				depositStatement.setDepositApproval(depositApproval);//非空,存入出账对象中	
				depositStatement = depositStatementService.getDepositStatementByApprovalId(depositStatement);//获取一条出账信息
				if(depositStatement!=null){
					if(depositStatement.getId() != null && !depositStatement.getId().equals("")){
						j.setSuccess(false);
						j.setMsg("删除失败！出账记录中存在关联信息");
						return j;
					}
				}else if(depositApproval != null){
					Integer status = null;
//					status = depositApproval.getCheckStatus();
//					if(status == 1){
//						j.setSuccess(false);
//						j.setMsg("删除失败！当前保证金正在审核中");
//						return j;
//					}else if(status == 1){
//						
//					}
					Deposit deposit = depositApproval.getDeposit();
					if(deposit!=null){
						deposit = depositService.get(deposit);
						if(deposit!=null){
							status = deposit.getCheckStatus();
						}
					}
					if(status!=null){
						if(status == 0 || status == 3){//待审核或未通过
							depositApprovalService.delete(depositApproval);
							enclosuretabService.deleteEnclosureByForeginId(depositApproval.getId());//同步删除对应附件
							//删除之后将状态更改为待审核
							deposit = depositApproval.getDeposit();
							String depositId = "";
							if(deposit!=null){
								depositId = deposit.getId();
								deposit = depositService.get(depositId);
								if(deposit!=null){//deposit在堆内存中指向最新的对象
									deposit.setCheckStatus(0);//设置状态为待审核
									depositService.save(deposit);
								}
							}
							j.setSuccess(true);
							j.setMsg("删除成功!");
							return j;
						}else if(status == 1){
							j.setSuccess(false);
							j.setMsg("删除失败!当前保证金正在审核中，不可删除!");
							return j;
						}else if(status == 2){
							j.setSuccess(false);
							j.setMsg("删除失败!当前审批状态为通过，不可删除!");
							return j;
						}
					}
				}
			}
		}
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("depositapproval:depositApproval:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(DepositApproval depositApproval, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "保证金审批"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DepositApproval> page = depositApprovalService.findPage(new Page<DepositApproval>(request, response, -1), depositApproval);
    		new ExportExcel("保证金审批", DepositApproval.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出保证金审批记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("depositapproval:depositApproval:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DepositApproval> list = ei.getDataList(DepositApproval.class);
			for (DepositApproval depositApproval : list){
				try{
					depositApprovalService.save(depositApproval);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条保证金审批记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条保证金审批记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入保证金审批失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/depositApproval/?repage";
    }
	
	/**
	 * 下载导入保证金审批数据模板
	 */
//	@RequiresPermissions("depositapproval:depositApproval:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "保证金审批数据导入模板.xlsx";
    		List<DepositApproval> list = Lists.newArrayList(); 
    		new ExportExcel("保证金审批数据", DepositApproval.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/depositApproval/?repage";
    }

}