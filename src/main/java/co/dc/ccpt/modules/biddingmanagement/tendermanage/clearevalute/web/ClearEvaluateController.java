package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Company;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.ClearEvaluate;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.EvaluateWorker;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.service.ClearEvaluateService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.utils.WordUtils;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.service.SubBidCompanyService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service.SubpackageProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.service.TenderService;
import co.dc.ccpt.modules.sys.entity.Role;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;

import com.google.common.collect.Lists;

/**
 * 清评标管理Controller
 * @author lxh
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/tendermanage/clearevaluate")
public class ClearEvaluateController extends BaseController {

	@Autowired
	private ClearEvaluateService clearEvaluateService;
	
	@Autowired
	private SubBidCompanyService subBidCompanyService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private SubpackageProgramService subpackageProgramService;
	
	@Autowired
	private TenderService tenderService;
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public ClearEvaluate get(@RequestParam(required=false) String id) {
		ClearEvaluate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = clearEvaluateService.get(id);
		}
		if (entity == null){
			entity = new ClearEvaluate();
		}
		return entity;
	}
	
	/**
	 * 清评标管理列表页面
	 */
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/biddingmanagement/tendermanage/clearevaluate/clearEvaluateList";
	}
	
	/**
	 * 清评标管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ClearEvaluate clearEvaluate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ClearEvaluate> page = clearEvaluateService.findPage(new Page<ClearEvaluate>(request, response), clearEvaluate); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑清评标管理表单页面
	 */
//	@RequiresPermissions(value={"tendermanage:clearevaluate:clearEvaluateBid:view","tendermanage:clearevaluate:clearEvaluateBid:add","tendermanage:clearevaluate:clearEvaluateBid:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ClearEvaluate clearEvaluate, Model model) {
		List<User> userList = systemService.findAllUser();
		model.addAttribute("userList", userList);
		model.addAttribute("clearEvaluate", clearEvaluate);
		if(StringUtils.isBlank(clearEvaluate.getId())){
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit", true);
		}
		return "modules/biddingmanagement/tendermanage/clearevaluate/clearEvaluateForm";
	}

	@RequestMapping(value = "bidStatus")
	public String bidStatus(ClearEvaluate clearEvaluate, Model model) {
		model.addAttribute("clearEvaluate", clearEvaluate);
		return "modules/biddingmanagement/tendermanage/clearevaluate/bidStatus";
	}
	
	/**
	 * 保存清评标管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"tendermanage:clearevaluate:clearEvaluateBid:add","tendermanage:clearevaluate:clearEvaluateBid:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ClearEvaluate clearEvaluate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, clearEvaluate)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//判断评标人员不为空
		List<EvaluateWorker> evaluateUserList = clearEvaluate.getEvaluateUserList();
		if(evaluateUserList!=null && evaluateUserList.size()>0){
			//保存之前确保唯一性：
			//1.获取当前清评对象中的参数:投标记录id(每一个投标记录只能对应一个清评记录)
			//2.通过投标记录id在清评列表中查询对应的信息
			//3.匹配结果并判断是否重复
			if(StringUtils.isBlank(clearEvaluate.getId())){
				
				if(clearEvaluateService.getClearEvaluateBySubCompId(clearEvaluate)!=null){
					j.setSuccess(false);
					j.setMsg("保存失败，当前信息已在开评标列表中登记!");
					return j;
				}else{
					clearEvaluateService.save(clearEvaluate);//新建或者编辑保存
					j.setSuccess(true);
					j.setMsg("保存清评标管理成功!");
					return j;
				}
			}else{
				clearEvaluateService.save(clearEvaluate);//新建或者编辑保存
				j.setSuccess(true);
				j.setMsg("保存清评标管理成功!");
				return j;
			}
		}else{
			j.setSuccess(false);
			j.setMsg("保存失败，评标人员不可为空!");
			return j;
		}
		
	}
	
	//通过名称查询出所有的单位
	@ResponseBody
	@RequestMapping(value = "getAlreadyBidCompanyList")
	public List<SubBidCompany> getAlreadyBidCompanyList(@RequestParam  String companyName) {
		List<SubBidCompany> subBidCompanyList = new ArrayList<SubBidCompany>();
		Company company = new Company();
		company.setCompanyName(companyName);
		SubBidCompany subBidCompany = new SubBidCompany();
		subBidCompany.setCompany(company);
		subBidCompanyList = subBidCompanyService.getAlreadyBidCompanyList(subBidCompany);
		return subBidCompanyList;
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
	  通过子项目名称查询出投标记录中的对应当前子项目的投标公司集合
	  */
	@ResponseBody
	@RequestMapping(value = "getBidCompanyList")
	public List<SubBidCompany> getBidCompanyList(@RequestBody SubpackageProgram subpackageProgram) {
		List<SubBidCompany> subBidCompList= new ArrayList<SubBidCompany>();
		Tender tender = new Tender();
		tender.setSubpackageProgram(subpackageProgram);
		SubBidCompany subBidCompany = new SubBidCompany();
		subBidCompany.setTender(tender);
		subBidCompList = subBidCompanyService.getBidCompanyList(subBidCompany);
		return subBidCompList;
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
		
	//变更状态
	@ResponseBody
	@RequestMapping(value = "change")
	public AjaxJson change(ClearEvaluate clearEvaluate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, clearEvaluate)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Integer isBid = clearEvaluate.getIsBid();
		if(isBid != null && !isBid.equals("")){
			if(isBid == 0){
				clearEvaluate.setIsBid(1);
			}else if(isBid == 1){
				clearEvaluate.setIsBid(0);
			}
			clearEvaluateService.save(clearEvaluate);
			j.setSuccess(true);
			j.setMsg("保存清评标管理成功");
		}
		return j;
	}
	
	/**
	 * 删除清评标管理
	 */
	@ResponseBody
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ClearEvaluate clearEvaluate, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		clearEvaluateService.delete(clearEvaluate);
		j.setMsg("删除清评标管理成功");
		return j;
	}
	
	/**
	 * 批量删除清评标管理
	 */
	@ResponseBody
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			clearEvaluateService.delete(clearEvaluateService.get(id));
		}
		j.setMsg("删除清评标管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ClearEvaluate clearEvaluate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "清评标管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ClearEvaluate> page = clearEvaluateService.findPage(new Page<ClearEvaluate>(request, response, -1), clearEvaluate);
    		new ExportExcel("清评标管理", ClearEvaluate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出清评标管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    /**
     * 
     * @param proid
     * @param req
     * @param resp
     * 利用freemarker生成动态word文档
     */
	@ResponseBody
	@RequestMapping("/exportFbmb")
    public void exportFbmb(@RequestParam String proid, HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat smf = new SimpleDateFormat("yyyy年MM月dd日");
        if(proid!=null){
        	//id为项目id
        	//四大对象：
        	ClearEvaluate clearEvaluate = new ClearEvaluate();
        	SubBidCompany subBidCompany = new SubBidCompany();
        	Tender tender = new Tender();
        	SubpackageProgram subpackageProgram = new SubpackageProgram();
        	//依次组装在对象中
        	subpackageProgram = subpackageProgramService.get(proid);//获得一个项目信息
        	String subpackageProgramName ="";
        	String subpackageProgramNum ="";
            Date tenderDate = null;
        	if(subpackageProgram!=null){
        		subpackageProgramName = subpackageProgram.getSubpackageProgramName();
        		subpackageProgramNum = subpackageProgram.getSubpackageProgramNum();
        		tenderDate = subpackageProgram.getPlanToStart();
        	}
        	tender.setSubpackageProgram(subpackageProgram);
        	//通过一个项目id查询一个招标信息
        	tender = tenderService.getTenderBySubProId(tender);
        	String tenderArrange = "";
            String programCont = "";
            String tenderCtrlPrice = "";
            String quality = "";
            String buildDate = "";
            Date openDate = null;
            String openAddr = "";
            String evaluateMethod = "";
        	if(tender!=null){
        		tenderArrange = tender.getArrange();
        		programCont = tender.getProjectProfile();
        		tenderCtrlPrice = tender.getTenderCtrlPrice();
        		quality = tender.getQuality();
        		buildDate = tender.getBuildDate();
        		openDate = tender.getOpenBidDate();
        		openAddr = tender.getOpenBidAddr();
        		evaluateMethod = tender.getEvaluateMethod();
        	}
        	subBidCompany.setTender(tender);
        	//通过项目id获取对应投标记录信息集合（便于投标人数据记录和递交人信息统计）
        	String compName = "";
        	Company company = new Company();
        	List<String> bidCompany = new ArrayList<String>();
        	List<SubBidCompany> subCompList = subBidCompanyService.getBidCompanyListByProId(subBidCompany);
        	List<Map<String, Object>> subBidList=new ArrayList<Map<String,Object>>();
            
        	if(subCompList!=null && subCompList.size()!=0){//集合不为空且有元素时
        		 // 序号、单位名称、递交人、联系电话、递交时间---word表格组装遍历
	    		for(int i=0;i<subCompList.size();i++){
	    			Map<String, Object> submitMap = new HashMap<String, Object>();
	    			submitMap.put("num",i+1);
	    			company = subCompList.get(i).getCompany();
	    			if(company!=null){
	    				compName = company.getCompanyName();
	    				if(compName!=null && !compName.equals("")){
	    					bidCompany.add(compName);//投标人记录
	    				}
	    			}
	    			submitMap.put("companyName",compName);
	    			submitMap.put("submiter", subCompList.get(i).getSubmiter()!=null?subCompList.get(i).getSubmiter():"");
	    			submitMap.put("submitTel", subCompList.get(i).getSubmitTel()!=null?subCompList.get(i).getSubmitTel():"");
	            	submitMap.put("subBidDate", subCompList.get(i).getSubBidDate()!=null?smf.format(subCompList.get(i).getSubBidDate()):"");
	            	subBidList.add(submitMap);
	    		}
	    	}
        	map.put("subBidList", subBidList);
        	
        	clearEvaluate.setSubBidCompany(subBidCompany);
        	//通过项目id获取对应的清评标数据集合
        	List<ClearEvaluate> ceList = clearEvaluateService.getClearEvaluateByProId(clearEvaluate);
            
            List<Map<String, Object>> clearEvaluateList=new ArrayList<Map<String,Object>>();
            
            String companyName = "";
        	String subBidPrice = "";
        	Date accessDate = null;
        	if(ceList!=null && ceList.size()!=0){
        		//投标单位、投标价、工期、业绩、施工企业资质证书、投标书填写签署情况、标书密封情况、标书送达时间、施工组织设计--word表格组装遍历
        		for(int i=0;i<ceList.size();i++){
        			Map<String, Object> clearEvaluateMap = new HashMap<String, Object>();
                	SubBidCompany subComp = ceList.get(i).getSubBidCompany();
                	if(subComp!=null){
                		Company comp = subComp.getCompany();
                		subBidPrice = subComp.getSubBidPrice();
                		accessDate = subComp.getSubBidDate();
                		if(comp!=null){
                			companyName = comp.getCompanyName();
                		}
                	}
                	clearEvaluateMap.put("companyName", companyName);
                	clearEvaluateMap.put("subBidPrice", subBidPrice);
                	clearEvaluateMap.put("buildDate", ceList.get(i).getBuildDate()!=null?ceList.get(i).getBuildDate():"");
                	clearEvaluateMap.put("performance", ceList.get(i).getPerformance()!=null?ceList.get(i).getPerformance():"");
                	clearEvaluateMap.put("certificate", ceList.get(i).getCertificate()!=null?ceList.get(i).getCertificate():"");
                	clearEvaluateMap.put("writeCircumstances", ceList.get(i).getWriteCircumstances()!=null?ceList.get(i).getWriteCircumstances():"");
                	clearEvaluateMap.put("secretCircumstances", ceList.get(i).getSecretCircumstances()!=null?ceList.get(i).getSecretCircumstances():"");
                	clearEvaluateMap.put("accessDate", accessDate!=null?smf.format(accessDate):"");
                	clearEvaluateMap.put("design", ceList.get(i).getDesign()!=null?ceList.get(i).getDesign():"");
                	clearEvaluateList.add(clearEvaluateMap);
                }
                map.put("clearEvaluateList", clearEvaluateList);
        	}
        	
            // 项目名称
            map.put("subpackageProgramName", subpackageProgramName!=null?subpackageProgramName:"");
            // 招标编号
            map.put("subpackageProgramNum", subpackageProgramNum!=null?subpackageProgramNum:"");
            // 招标日期(子项目--计划开始时间)
            map.put("tenderDate", tenderDate!=null?smf.format(tenderDate):"");
            // 招标范围
            map.put("tenderArrange", tenderArrange!=null?tenderArrange:"");
            // 工程概况
            map.put("programCont", programCont!=null?programCont:"");
            // 招标控制价
            map.put("tenderCtrlPrice", tenderCtrlPrice!=null?tenderCtrlPrice:"");
            // 质量要求
            map.put("quality", quality!=null?quality:"");
            // 工期要求
            map.put("buildDate", buildDate!=null?buildDate:"");
            // 开标时间(招标中--投标截止时间)
            map.put("openDate",  openDate!=null?smf.format(openDate):"");
            // 开标地点
            map.put("openAddr", openAddr!=null?openAddr:"");
            // 评标办法
            map.put("evaluateMethod", evaluateMethod!=null?evaluateMethod:"");
            // 投标人
            map.put("bidCompany", bidCompany.toString());
            
            List<EvaluateWorker> evaluateWorkerList = clearEvaluateService.getUserList(proid);//获取评标人员信息
            //序号、姓名、职务、电话（备注）--word表格组装遍历
            List<Map<String, Object>> userList=new ArrayList<Map<String,Object>>();
            
            String userName = "";
        	String roleName = "";
            if(evaluateWorkerList!=null && evaluateWorkerList.size()!=0){
            	for(int i=0;i<evaluateWorkerList.size();i++){
            		Map<String, Object> userMap = new HashMap<String, Object>();
                	userMap.put("num", i+1);
                	User user = evaluateWorkerList.get(i).getUser();
                	if(user!=null){
                		userName = user.getName();
                		Role role = user.getRole();
                		if(role!=null){
                			roleName = role.getName();
                		}
                	}
                	userMap.put("name", userName);
                	userMap.put("position", roleName);
                	userMap.put("remarks", evaluateWorkerList.get(i).getRemarks()!=null?evaluateWorkerList.get(i).getRemarks():"");
                	userList.add(userMap);
                }
                map.put("userList", userList);
        	}
            try {
                WordUtils.exportMillCertificateWord(req,resp,map,"开评标报告","tempReport.ftl");
            } catch (IOException e) {
            	e.printStackTrace();
            } 
        }
    }

	@ResponseBody
    @RequestMapping(value = "detail")
	public ClearEvaluate detail(String id) {
		return clearEvaluateService.get(id);
	}
	
	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ClearEvaluate> list = ei.getDataList(ClearEvaluate.class);
			for (ClearEvaluate clearEvaluate : list){
				try{
					clearEvaluateService.save(clearEvaluate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条清评标管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条清评标管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入清评标管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/clearevaluate/?repage";
    }
	
	/**
	 * 下载导入清评标管理数据模板
	 */
//	@RequiresPermissions("tendermanage:clearevaluate:clearEvaluateBid:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "清评标管理数据导入模板.xlsx";
    		List<ClearEvaluate> list = Lists.newArrayList(); 
    		new ExportExcel("清评标管理数据", ClearEvaluate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/clearevaluate/?repage";
    }
	

}