package co.dc.ccpt.modules.contractmanagement.procontract.web;

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
import co.dc.ccpt.modules.act.service.ActProcessService;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.BidcompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.oa.entity.ActContract;
import co.dc.ccpt.modules.oa.entity.AttachContract;
import co.dc.ccpt.modules.oa.service.ActContractService;
import co.dc.ccpt.modules.oa.service.AttachContractService;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.service.ProgramService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;
import co.dc.ccpt.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/procontract")
public class ProContractController extends BaseController{
	@Autowired
	public ProContractService proContractService;
	
	@Autowired
	public ActContractService actContractService;

	@Autowired
	public AttachContractService attachContractService;
	
	@Autowired
	public ProgramService programService;

	@Autowired
	public BidcompanyService bidcompanyService;
	
	@Autowired
	public EnclosuretabService enclosuretabService;
	
	@Autowired
	public SubProContractService subContractService;
	
	@Autowired
	private ActProcessService actProcessService;
	
	@Autowired
	public SystemService userService;
	
	@ModelAttribute
	public ProContract get(@RequestParam(required=false) String id) {
		ProContract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = proContractService.get(id);
		}
		if (entity == null){
			entity = new ProContract();
		}
		return entity;
	}
	
	/**
	 * 总包合同列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/contractmanagement/procontract/procontractList";
	}
	
	/**
	 * 总包合同列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProContract proContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProContract> page = proContractService.findPage(new Page<ProContract>(request, response), proContract); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑总包合同表单页面
	 */
	@RequestMapping(value = "form")
	public String form(ProContract proContract, Model model) {
		User user = new User();
		String id = "";
		String name = "";
		if(StringUtils.isBlank(proContract.getId())){//如果ID是空为添加
			user = UserUtils.getUser();//获取当前登录的人员信息
			System.out.println(UserUtils.getUser());
			if(user != null){
				id = user.toString();//转换user.id
				user = userService.getOnlyOneUser(id);//通过ID获取user信息
			}
			proContract.setUser(user);
			String num = proContractService.setProContractNum();
			proContract.setContractNum(num);
			model.addAttribute("proContract", proContract);
			model.addAttribute("isAdd", true);
		}else{
			name = proContract.getUser().getName();
			if(StringUtils.isNotBlank(name)){
				user = UserUtils.getByUserName(name);
				if(user != null){
					id = user.toString();//转换user.id
					user = userService.getOnlyOneUser(id);//通过ID获取user信息
				}
			}
			model.addAttribute("proContract", proContract);
			model.addAttribute("edit",true);
		}
		return "modules/contractmanagement/procontract/procontractForm";
	}
	
	/**
	 * 更改生效状态
	 * @param proContract
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "confirmValid")
	public String confirmValid(ProContract proContract, Model model) {
		model.addAttribute("proContract", proContract);
		return "modules/contractmanagement/procontract/confirmValidForm";
	}
	
	/**
	 * 查看，增加，编辑总包合同表单页面
	 */
	@RequestMapping(value = {"checkForm"})
	public String checkForm(ProContract proContract, Model model) {
		String view = "actContractForm";
		// 查看审批申请单
		if (StringUtils.isNotBlank(proContract.getId())){//.getAct().getProcInsId())){

			// 环节编号
			String taskDefKey = proContract.getAct().getTaskDefKey();
			
			// 查看工单
			if(proContract.getAct().isFinishTask()){
				view = "actContractView";
			}
			// 审核环节
			else if ("deptLeaderAudit".equals(taskDefKey)){
				view = "actContractAudit";
			}
			// 审核环节2
			else if ("hrAudit".equals(taskDefKey)){
				view = "actContractAudit";
			}
			// 审核环节3
			else if ("reportBack".equals(taskDefKey)){
				view = "actContractAudit";
			}
			// 修改环节
			else if ("modifyApply".equals(taskDefKey)){
				view = "actContractAudit";
			}
		}

		model.addAttribute("proContract", proContract);
		return "modules/contractmanagement/procontract/"+view;
	}

	/**
	 * 通过项目名称查询出所有的单位
	 */
	@ResponseBody
	@RequestMapping(value = "getProgramByName",method = RequestMethod.POST)
	public List<Program> getProgramByName(@RequestParam String programName) {
		List<Program> programList = new ArrayList<Program>();
		programList = programService.getProgramByName(programName);
		return programList;
	}
	
	/**
	 * 通过项目id查询项目
	 */
	@ResponseBody
	@RequestMapping(value = "getProContractByProgramId",method = RequestMethod.POST)
	public String getProContractByProgramId(@RequestBody Program program) {
		ProContract proContract = new ProContract();
		proContract.setProgram(program);
		proContract = proContractService.getProContractByProgramId(proContract);
		if(proContract!=null){
			return "true";
		}else{
			return "false";
		}
	}
	
	/**
	 * 通过项目id查询项目
	 */
	@ResponseBody
	@RequestMapping(value = "getBidPriceByProId",method = RequestMethod.POST)
	public Bidcompany getBidPriceByProId(@RequestBody Program program) {
		Bidcompany bidcomp = new Bidcompany();
		program = programService.get(program);
		if(program != null){
			Integer getMethod = program.getGetMethod(); 
			if(getMethod != null && getMethod == 1){//市场投标
				bidcomp.setProgram(program);
				bidcomp = bidcompanyService.getBidPriceByProId(bidcomp);
			}
		}
		if(bidcomp != null){
			return bidcomp;
		}else{
			return new Bidcompany();
		}
	}
	
	/**
	 * 保存总包合同信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ProContract proContract, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, proContract)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		
		Date beforeDate = proContract.getStartDate();
		Date afterDate = proContract.getCompleteDate();
		Date contractDate = proContract.getContractDate();
		Integer contractStatus = proContract.getContractStatus();
		if(beforeDate.after(afterDate)){//保存前处理1：开工日期不得晚于竣工日期
			j.setSuccess(false);
			j.setMsg("开工日期不得晚于竣工日期!");
			return j;
		}else{//保存前处理3：设置工期为开工日期与竣工日期间隔天数
			Double buildDate= DateUtils.getDistanceOfTwoDate(beforeDate, afterDate);//调用日期工具类方法实现工期计算
			Integer i = (int) Math.round(buildDate);
			proContract.setBuildDate(i.toString());
		}
		//若为生效且生效时间为空，则设置生效时间为当前时间
		if(contractStatus!=null){
			if(contractDate==null && contractStatus == 1){
				proContract.setContractDate(new Date());
			}
		}
		//新增或编辑表单保存
		proContractService.save(proContract);//保存
		j.setSuccess(true);
		j.setMsg("保存总包合同信息成功！");
		return j;
	}
	
	/**
	 * 删除总包合同
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProContract proContract, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		proContractService.delete(proContract);
		j.setMsg("删除总包合同成功!");
		return j;
	}
	
	/**
	 * 批量删除总包合同
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//删除前判断：分包有关联不可删；状态为审批中不可删，审批通过不可删，合同已生效不可删
			ProContract proContract = proContractService.get(id);
			if(proContract != null){
				// 分包有关联不可删：
				SubProContract subProContract = new SubProContract();
				subProContract.setProContract(proContract);
				List<SubProContract> subContractList = subContractService.getSubProContractListById(subProContract);
				if(subContractList != null && subContractList.size()>0){
					j.setSuccess(false);
					j.setMsg("分包中存在关联信息，不允许删除!");
					return j;
				}
				// 状态为审批中、审批通过不可删：
				Integer approvalStatus = proContract.getApprovalStatus();
				if(approvalStatus == 1){//审批中
					j.setSuccess(false);
					j.setMsg("当前信息审批中，不允许删除!");
					return j;
				}else if(approvalStatus == 2){
					j.setSuccess(false);
					j.setMsg("当前信息审批通过，不允许删除!");
					return j;
				}
				// 合同已生效不可删：
				Integer contractStatus = proContract.getContractStatus();
				if(contractStatus==1){//已生效
					j.setSuccess(false);
					j.setMsg("当前信息已生效，不允许删除!");
					return j;
				}
				proContractService.delete(proContract);
				enclosuretabService.deleteEnclosureByForeginId(proContract.getId());//同步删除对应附件
			}
		}
		j.setMsg("删除总包合同信息成功!");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProContract proContract, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "总包合同"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProContract> page = proContractService.findPage(new Page<ProContract>(request, response, -1), proContract);
    		new ExportExcel("总包合同", ProContract.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出总包合同记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
	
	@ResponseBody
	@RequestMapping(value = "shutdown")
	public AjaxJson shutdown(ProContract proContract, Integer getMethod, String reason, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, proContract)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = proContract.getApprovalStatus();
		String procInsId="";
		if(status != null && !status.equals("")){
			if(status==1){
				if (StringUtils.isBlank(reason)){
					j.setSuccess(false);
					j.setMsg("请填写终止原因");
					return j;
				}else{
					//将当前合同审批流程终止
					//1.利用合同id查询到部署的流程实例id
					if(getMethod != null && getMethod == 0){//区分投标和业主指定
						AttachContract attachContract = attachContractService.getByProContract(proContract);
						if(attachContract != null){
							procInsId = attachContract.getProcInsId();
						}
					}else{
						ActContract actContract = actContractService.getByProContract(proContract);//通过proId获取部署的流程实例id
						if(actContract != null){
							procInsId = actContract.getProcInsId();
						}
					}
					//更新合同中的状态
					proContract.setContractStatus(3);//合同终止
					proContract.setApprovalStatus(4);//审批终止
					proContractService.save(proContract);
					//2.再利用流程实例id和原因进行终止操作
					actProcessService.deleteProcIns(procInsId, reason);
					j.setSuccess(true);
					j.setMsg("已终止合同!");
				}
			}else{
				j.setSuccess(false);
				j.setMsg("当前合同非审批中，不允许终止！");
			}
			return j;
		}
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "closeCase")
	public AjaxJson closeCase(ProContract proContract, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, proContract)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = proContract.getApprovalStatus();
		if(status != null && !status.equals("")){
			if(status==2){
				proContract.setContractStatus(2);
				proContractService.save(proContract);
				j.setSuccess(true);
				j.setMsg("保存成功！");
			}else{
				j.setSuccess(false);
				j.setMsg("当前合同非审批通过，不允许结案！");
			}
			return j;
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
			List<ProContract> list = ei.getDataList(ProContract.class);
			for (ProContract proContract : list){
				try{
					proContractService.save(proContract);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条总包合同记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条总包合同记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入总包合同失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ProContractpro/staffCertificate/?repage";
    }
	
	/**
	 * 下载导入总包合同数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "总包合同数据导入模板.xlsx";
    		List<ProContract> list = Lists.newArrayList(); 
    		new ExportExcel("总包合同数据", ProContract.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ProContractpro/staffCertificate/?repage";
    }
}
