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
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.oa.entity.ActSubContract;
import co.dc.ccpt.modules.oa.service.ActSubContractService;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.entity.SubpackageProgram;
import co.dc.ccpt.modules.programmanage.service.CompanyService;
import co.dc.ccpt.modules.programmanage.service.SubpackageProgramService;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;
import co.dc.ccpt.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/subprocontract")
public class SubProContractController extends BaseController{
	@Autowired
	public SubProContractService subProContractService;
	
	@Autowired
	public SystemService userService;
	
	@Autowired
	public ActSubContractService actSubContractService;
	
	@Autowired
	public ProContractService proContractService;

	@Autowired
	public EnclosuretabService enclosuretabService;
	
	@Autowired
	public SubpackageProgramService subpackageProgramService;
	
	@Autowired
	public CompanyService companyService;
	
	@Autowired
	private ActProcessService actProcessService;
	
	@ModelAttribute
	public SubProContract get(@RequestParam(required=false) String id) {
		SubProContract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = subProContractService.get(id);
		}
		if (entity == null){
			entity = new SubProContract();
		}
		return entity;
	}
	
	/**
	 * 分包合同列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/contractmanagement/procontract/subprocontractList";
	}
	
	/**
	 * 分包合同列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(SubProContract subProContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SubProContract> page = subProContractService.findPage(new Page<SubProContract>(request, response), subProContract); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑分包合同表单页面
	 */
	@RequestMapping(value = "form")
	public String form(SubProContract subProContract, Model model) {
		User user = new User();
		String id = "";
		String name = "";
		if(StringUtils.isBlank(subProContract.getId())){//如果ID是空为添加
			user = UserUtils.getUser();//获取当前登录的人员信息
			System.out.println(UserUtils.getUser());
			if(user != null){
				id = user.toString();//转换user.id
				user = userService.getOnlyOneUser(id);//通过ID获取user信息
			}
			subProContract.setUser(user);
			String num = subProContractService.setSubProContractNum();
			subProContract.setSubProContractNum(num);
			
			model.addAttribute("isAdd", true);
		}else{
			name = subProContract.getUser().getName();
			if(StringUtils.isNotBlank(name)){
				user = UserUtils.getByUserName(name);
				if(user != null){
					id = user.toString();//转换user.id
					user = userService.getOnlyOneUser(id);//通过ID获取user信息
				}
			}
			model.addAttribute("edit",true);
		}
		model.addAttribute("subProContract", subProContract);
		return "modules/contractmanagement/procontract/subprocontractForm";
	}

	/**
	 * 通过分包项目名称查询出所有的单位
	 */
	@ResponseBody
	@RequestMapping(value = "getSubpackageProgramListByName",method = RequestMethod.POST)
	public List<SubpackageProgram> getSubpackageProgramListByName(@RequestParam String subpackageProgramName) {
		List<SubpackageProgram> subpackageProgramList = new ArrayList<SubpackageProgram>();
		subpackageProgramList = subpackageProgramService.getSubpackageProgramListByName(subpackageProgramName);
		return subpackageProgramList;
	}
	
	/**
	 * 通过总包项目名称查询出所有符合条件的总包项目（已生效或者执行中）
	 */
	@ResponseBody
	@RequestMapping(value = "getProContractList",method = RequestMethod.POST)
	public List<ProContract> getProContractList(@RequestParam String proContractName) {
		List<ProContract> proContractList = new ArrayList<ProContract>();
		proContractList = proContractService.getProContractList(proContractName);
		return proContractList;
	}
	
	/**
	 * 更改生效状态
	 * @param proContract
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "confirmValid")
	public String confirmValid(SubProContract subProContract, Model model) {
		model.addAttribute("subProContract", subProContract);
		return "modules/contractmanagement/procontract/confirmSubProValidForm";
	}
	
	/**
	 * 保存分包合同信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(SubProContract subProContract, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subProContract)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		
		Date beforeDate = subProContract.getStartDate();
		Date afterDate = subProContract.getCompleteDate();
//		Date subProContractDate = subProContract.getSubProContractDate();
		if(beforeDate.after(afterDate)){//保存前处理1：开工日期不得晚于竣工日期
			j.setSuccess(false);
			j.setMsg("开工日期不得晚于竣工日期!");
			return j;
//		}else if(subProContractDate.after(beforeDate)){//保存前处理2：签订日期不得晚于开工日期
//			j.setSuccess(false);
//			j.setMsg("签订日期不得晚于开工日期!");
//			return j;
		}else{//保存前处理3：设置工期为开工日期与竣工日期间隔天数
			Double buildDate= DateUtils.getDistanceOfTwoDate(beforeDate, afterDate);//调用日期工具类方法实现工期计算
			Integer i = (int) Math.round(buildDate);
			subProContract.setBuildDate(i.toString());
		}
		//新增或编辑表单保存
		//新增保存之前联动保存对应的proContractId到表中
//		if(StringUtils.isBlank(subProContract.getId())){
		SubpackageProgram subPro = subProContract.getSubpackageProgram();
		ProContract proContract = new ProContract();
		Integer contractStatus = subProContract.getContractStatus();
		Date contractDate = subProContract.getSubProContractDate();
		if(subPro != null){//分包项目非空，获取整个分包项目信息
			subPro = subpackageProgramService.get(subPro);
			if(subPro != null){
				Program pro = subPro.getProgram();//获取主项目
				if(pro != null){
					proContract.setProgram(pro);
					proContract = proContractService.getProContractByProgramId(proContract);
					if(proContract != null){
						subProContract.setProContract(proContract);
					}
				}
			}
		}
		//若为生效且生效时间为空，则设置生效时间为当前时间
		if(contractStatus!=null){
			if(contractDate==null && contractStatus == 1){
				subProContract.setSubProContractDate(new Date());
			}
		}
//		}
		subProContractService.save(subProContract);//保存
		j.setSuccess(true);
		j.setMsg("保存分包合同信息成功！");
		return j;
	}
	
	/**
	 * 通过子项目id获取分包合同对象
	 */
	@ResponseBody
	@RequestMapping(value="getSubProContractBySubCompId",method=RequestMethod.POST)
	public String getSubProContractBySubCompId(@RequestBody SubpackageProgram subPro){
		SubProContract subProContract = new SubProContract();
		subProContract.setSubpackageProgram(subPro);
		subProContract = subProContractService.getSubProContractBySubProId(subProContract);
		
		if(subProContract!=null){
			return "true";
		}else{
			return "false";
		}
	}
	/**
	 * 通过分包项目id获取分包中的已中标参投单位
	 * @param subPro
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCompBySubIsBid", method = RequestMethod.POST)
	public List<Company> getCompBySubIsBid(@RequestBody SubpackageProgram subPro){
		String subProId = subPro.getId();
		return companyService.getCompListBySubIsBid(subProId);
	}
	/**
	 * 终止
	 * @param subProContract
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "shutdown")
	public AjaxJson shutdown(SubProContract subProContract, String reason, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subProContract)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = subProContract.getApprovalStatus();
		String procInsId="";
		if(status != null && !status.equals("")){
			if(status==1){
				subProContract.setContractStatus(3);
				subProContract.setApprovalStatus(4);//审批终止
				//将当前合同审批流程也终止
				subProContractService.save(subProContract);
				//将当前合同审批流程也终止
				//1.利用合同id查询到部署的流程实例id
				ActSubContract actSubContract = actSubContractService.getBySubContract(subProContract);//通过proId获取部署的流程实例id
				if(actSubContract != null){
					procInsId = actSubContract.getProcInsId();
				}
				//2.再利用流程实例id和原因进行终止操作
				actProcessService.deleteProcIns(procInsId, reason);
				j.setSuccess(true);
				j.setMsg("已终止合同!");
			}else{
				j.setSuccess(false);
				j.setMsg("当前合同非审批中，不允许终止！");
			}
			return j;
		}
		return j;
	}
	
	/**
	 * 结案
	 * @param subProContract
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "closeCase")
	public AjaxJson closeCase(SubProContract subProContract, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, subProContract)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer status = subProContract.getApprovalStatus();
		if(status != null && !status.equals("")){
			if(status==2){
				subProContract.setContractStatus(2);
				subProContractService.save(subProContract);
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
	 * 删除分包合同
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(SubProContract subProContract, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		subProContractService.delete(subProContract);
		j.setMsg("删除分包合同成功!");
		return j;
	}
	
	/**
	 * 批量删除分包合同
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			SubProContract subProContract = subProContractService.get(id);
			if(subProContract!=null){
				//删除前进行状态判断:
				if(subProContract.getApprovalStatus() == 1){//审批中不可删除
					j.setSuccess(false);
					j.setMsg("分包合同审批中，不可删除!");
					return j;
				}else if(subProContract.getApprovalStatus() == 2){//审批通过不可删除
					j.setSuccess(false);
					j.setMsg("分包合同审批通过，不可删除!");
					return j;
				}
				
				if(subProContract.getContractStatus() == 1){//合同生效
					j.setSuccess(false);
					j.setMsg("分包合同已生效，不可删除!");
					return j;
				}
				subProContractService.delete(subProContract);
				enclosuretabService.deleteEnclosureByForeginId(subProContract.getId());//同步删除对应附件
			}
		}
		j.setMsg("删除分包合同信息成功!");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SubProContract subProContract, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "分包合同"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SubProContract> page = subProContractService.findPage(new Page<SubProContract>(request, response, -1), subProContract);
    		new ExportExcel("分包合同", SubProContract.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出分包合同记录失败！失败信息："+e.getMessage());
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
			List<SubProContract> list = ei.getDataList(SubProContract.class);
			for (SubProContract subProContract : list){
				try{
					subProContractService.save(subProContract);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分包合同记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条分包合同记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入分包合同失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subprocontract/?repage";
    }
	
	/**
	 * 下载导入分包合同数据模板
	 */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分包合同数据导入模板.xlsx";
    		List<SubProContract> list = Lists.newArrayList(); 
    		new ExportExcel("分包合同数据", SubProContract.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/subprocontract/?repage";
    }
}
