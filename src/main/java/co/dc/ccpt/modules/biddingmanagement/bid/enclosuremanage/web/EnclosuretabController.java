package co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.web;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.service.BidtableService;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.BidcompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.entity.Enclosuretab;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.service.ProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.entity.ClearEvaluate;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.service.ClearEvaluateService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.entity.SubBidCompany;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subbidcompany.service.SubBidCompanyService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.entity.SubpackageProgram;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.subprogram.service.SubpackageProgramService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.entity.Tender;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.tender.service.TenderService;
import co.dc.ccpt.modules.contractmanagement.contracttemp.entity.ContractTemp;
import co.dc.ccpt.modules.contractmanagement.contracttemp.service.ContractTempService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ProContractService;
import co.dc.ccpt.modules.contractmanagement.procontract.service.SubProContractService;
import co.dc.ccpt.modules.contractmanagement.procontractapproval.entity.ProContractApproval;
import co.dc.ccpt.modules.contractmanagement.procontractapproval.service.ProContractApprovalService;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.PersonCertificate;
import co.dc.ccpt.modules.coreperson.basicinfo.service.CorePersonService;

/**
 * 附件信息管理Controller
 * @author lxh
 * @version 2018-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/enclosuremanage/enclosuretab")
public class EnclosuretabController extends BaseController {

	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private BidtableService bidtableService;
	
	@Autowired
	private BidcompanyService bidcompanyService;
	
	@Autowired
	private SubpackageProgramService subpackageProgramService;
	
	@Autowired
	private TenderService tenderService;
	
	@Autowired
	private SubBidCompanyService subBidCompanyService;
	
	@Autowired
	private ClearEvaluateService clearEvaluateService;
	
	@Autowired
	private ProContractService proContractService;
	
	@Autowired
	private SubProContractService subProContractService;
	
	@Autowired
	private ProContractApprovalService proContractApprovalService;
	
	@Autowired
	private CorePersonService personCertificateService;
	
	@Autowired
	private ContractTempService contractTempService; 
	
	@ModelAttribute
	public ContractTemp getContractTemp(@RequestParam(required=false) String contractTempId) {
		ContractTemp entity = null;
		if (StringUtils.isNotBlank(contractTempId)){
			entity = contractTempService.get(contractTempId);
		}
		if (entity == null){
			entity = new ContractTemp();
		}
		return entity;
	}
	
	@ModelAttribute
	public PersonCertificate getPersonCertificate(@RequestParam(required=false) String personCertificateId) {
		PersonCertificate entity = null;
		if (StringUtils.isNotBlank(personCertificateId)){
			entity = personCertificateService.getPersonCertificate(personCertificateId);
		}
		if (entity == null){
			entity = new PersonCertificate();
		}
		return entity;
	}
	
	@ModelAttribute
	public ProContractApproval getProContractApproval(@RequestParam(required=false) String proContractId) {
		ProContractApproval entity = null;
		if (StringUtils.isNotBlank(proContractId)){
			entity = proContractApprovalService.get(proContractId);
		}
		if (entity == null){
			entity = new ProContractApproval();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubProContract getSubProContract(@RequestParam(required=false) String subContractId) {
		SubProContract entity = null;
		if (StringUtils.isNotBlank(subContractId)){
			entity = subProContractService.get(subContractId);
		}
		if (entity == null){
			entity = new SubProContract();
		}
		return entity;
	}
	
	@ModelAttribute
	public ProContract getProContract(@RequestParam(required=false) String contractId) {
		ProContract entity = null;
		if (StringUtils.isNotBlank(contractId)){
			entity = proContractService.get(contractId);
		}
		if (entity == null){
			entity = new ProContract();
		}
		return entity;
	}
	
	@ModelAttribute
	public ClearEvaluate getClearEvaluate(@RequestParam(required=false) String clearid) {
		ClearEvaluate entity = null;
		if (StringUtils.isNotBlank(clearid)){
			entity = clearEvaluateService.get(clearid);
		}
		if (entity == null){
			entity = new ClearEvaluate();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubBidCompany getSubBidCompany(@RequestParam(required=false) String subbidid) {
		SubBidCompany entity = null;
		if (StringUtils.isNotBlank(subbidid)){
			entity = subBidCompanyService.get(subbidid);
		}
		if (entity == null){
			entity = new SubBidCompany();
		}
		return entity;
	}	
	
	@ModelAttribute
	public Tender getTender(@RequestParam(required=false) String tenderid) {
		Tender entity = null;
		if (StringUtils.isNotBlank(tenderid)){
			entity = tenderService.get(tenderid);
		}
		if (entity == null){
			entity = new Tender();
		}
		return entity;
	}
	
	
	@ModelAttribute
	public Enclosuretab get(@RequestParam(required=false) String id) {
		Enclosuretab entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = enclosuretabService.get(id);
		}
		if (entity == null){
			entity = new Enclosuretab();
		}
		return entity;
	}
	
	@ModelAttribute
	public Program query(@RequestParam(required=false) String proid) {
		Program entity = null;
		if (StringUtils.isNotBlank(proid)){
			entity = programService.get(proid);
		}
		if (entity == null){
			entity = new Program();
		}
		return entity;
	}
	
	@ModelAttribute
	public Bidcompany getBidcompany(@RequestParam(required=false) String bidCompId) {
		Bidcompany entity = null;
		if (StringUtils.isNotBlank(bidCompId)){
			entity = bidcompanyService.get(bidCompId);
		}
		if (entity == null){
			entity = new Bidcompany();
		}
		return entity;
	}
	
	@ModelAttribute
	public Bidtable getbid(@RequestParam(required=false) String bidId) {
		Bidtable entity = null;
		if (StringUtils.isNotBlank(bidId)){
			entity = bidtableService.get(bidId);
		}
		if (entity == null){
			entity = new Bidtable();
		}
		return entity;
	}
	
	@ModelAttribute
	public SubpackageProgram getSubProId(@RequestParam(required=false) String subproid) {
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
	 * 附件信息管理列表页面
	 */
//	@RequiresPermissions("enclosuretab:enclosuretab:list")
	@RequestMapping(value = {"list", ""})
	public String list(Program program, Bidtable bidtable, 
			ClearEvaluate clearEvaluate, SubBidCompany subBidCompany, 
			Bidcompany bidCompany, SubpackageProgram subpackageProgram, ContractTemp contractTemp,
			Tender tender, ProContract proContract, SubProContract subProContract, 
			ProContractApproval proContractApproval, PersonCertificate personCertificate, Model model) {
		System.out.println(program);
		String foreginId = null;
		if(StringUtils.isNotBlank(program.getId())){
			foreginId = program.getId();
		}else if(StringUtils.isNotBlank(bidtable.getId())){
			foreginId = bidtable.getId();
		}else if(StringUtils.isNotBlank(bidCompany.getId())){
			foreginId = bidCompany.getId();
		}else if(StringUtils.isNotBlank(subpackageProgram.getId())){
			foreginId = subpackageProgram.getId();
		}else if(StringUtils.isNotBlank(tender.getId())){
			foreginId = tender.getId();
		}else if(StringUtils.isNotBlank(subBidCompany.getId())){
			foreginId = subBidCompany.getId();
		}else if(StringUtils.isNotBlank(clearEvaluate.getId())){
			foreginId = clearEvaluate.getId();
		}else if(StringUtils.isNotBlank(proContract.getId())){
			foreginId = proContract.getId();
		}else if(StringUtils.isNotBlank(subProContract.getId())){
			foreginId = subProContract.getId();
		}else if(StringUtils.isNotBlank(proContractApproval.getId())){
			foreginId = proContractApproval.getId();
		}else if(StringUtils.isNotBlank(personCertificate.getId())){
			foreginId = personCertificate.getId();
		}else if(StringUtils.isNotBlank(contractTemp.getId())){
			foreginId = contractTemp.getId();
		}
		model.addAttribute("foreginId",foreginId);
		return "modules/biddingmanagement/bid/enclosuremanage/enclosuretabList";
	}
	
	/**
	 * 附件信息管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("enclosuretab:enclosuretab:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(String foreginId, Enclosuretab enclosuretab, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(foreginId != null && !foreginId.equals("")){//初始判断所传id是否为空，若为空赋值为任意uuid
//			foreginId = foreginId.substring(0, foreginId.length()-1);
			enclosuretab.setForeginId(foreginId);
		}else{
			String uuid = UUID.randomUUID().toString();
			System.out.println("uuid"+uuid);
			foreginId = uuid.replaceAll("-", "");
			enclosuretab.setForeginId(foreginId);
		}
		System.out.println(enclosuretab);
		Page<Enclosuretab> page = enclosuretabService.findPage(new Page<Enclosuretab>(request, response), enclosuretab); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑附件信息管理表单页面
	 */
//	@RequiresPermissions(value={"enclosuretab:enclosuretab:view","enclosuretab:enclosuretab:add","enclosuretab:enclosuretab:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Enclosuretab enclosuretab, ClearEvaluate clearEvaluate, 
			SubpackageProgram subpackageProgram, SubBidCompany subBidCompany, 
			Tender tender, Program program, Bidtable bidtable, SubProContract subProContract,
			Bidcompany bidCompany, ProContract proContract,ContractTemp contractTemp,
			ProContractApproval proContractApproval, PersonCertificate personCertificate, Model model) {
		
		if(StringUtils.isBlank(enclosuretab.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
			Integer enclosureType = 0;
			String enclosureNum = "";
			if(StringUtils.isNotBlank(program.getId())){
				model.addAttribute("program", program);
				enclosureType = 1;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(bidtable.getId())){
				model.addAttribute("bidtable", bidtable);
				enclosureType = 2;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(bidCompany.getId())){
				model.addAttribute("bidCompany", bidCompany);
				enclosureType = 3;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(subpackageProgram.getId())){
				model.addAttribute("subpackageProgram", subpackageProgram);
				enclosureType = 4;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(tender.getId())){
				model.addAttribute("tender", tender);
				enclosureType = 5;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(subBidCompany.getId())){
				model.addAttribute("subBidCompany", subBidCompany);
				enclosureType = 6;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(clearEvaluate.getId())){
				model.addAttribute("clearEvaluate", clearEvaluate);
				enclosureType = 7;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(proContract.getId())){
				model.addAttribute("proContract", proContract);
				enclosureType = 8;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(subProContract.getId())){
				model.addAttribute("subProContract", subProContract);
				enclosureType = 9;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(proContractApproval.getId())){
				model.addAttribute("proContractApproval", proContractApproval);
				enclosureType = 10;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(personCertificate.getId())){
			model.addAttribute("personCertificate", personCertificate);
			enclosureType = 11;
			enclosuretab.setEnclosureType(enclosureType);
			enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
			enclosuretab.setEnclosureNum(enclosureNum);
			}else if(StringUtils.isNotBlank(contractTemp.getId())){
				model.addAttribute("contractTemp", contractTemp);
				enclosureType = 12;
				enclosuretab.setEnclosureType(enclosureType);
				enclosureNum = enclosuretabService.countEnclosureByType(enclosuretab);
				enclosuretab.setEnclosureNum(enclosureNum);
			}
			model.addAttribute("enclosuretab", enclosuretab);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/bid/enclosuremanage/enclosuretabForm";
	}
	
	/**
	 * 验证项目编号是否有效
	 */
	@ResponseBody
	@RequestMapping(value = "checkEnclosureNum")
	public String checkEnclosureNum(String enclosureOldNum, String enclosureNum) {
		if (enclosureNum!=null && enclosureNum.equals(enclosureOldNum)) {
			return "true";
		} else if (enclosureNum!=null && enclosuretabService.getByEnclosuretabNum(enclosureNum) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 保存附件信息管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"enclosuretab:enclosuretab:add","enclosuretab:enclosuretab:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Enclosuretab enclosuretab, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		if (!beanValidator(model, enclosuretab)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		if (!"true".equals(checkEnclosureNum(enclosuretab.getEnclosureOldNum(), enclosuretab.getEnclosureNum()))){
			j.setSuccess(false);
			j.setMsg( "保存附件'" + enclosuretab.getEnclosureNum() + "'失败, 附件编号已存在");
			return j;
		}
		enclosuretabService.save(enclosuretab);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存附件信息管理成功");
		return j;
	}
	
	/**
	 * 删除附件信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("enclosuretab:enclosuretab:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Enclosuretab enclosuretab, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		enclosuretabService.delete(enclosuretab);
		j.setMsg("删除附件信息管理成功");
		return j;
	}
	
	/**
	 * 批量删除附件信息管理
	 */
	@ResponseBody
//	@RequiresPermissions("enclosuretab:enclosuretab:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			enclosuretabService.delete(enclosuretabService.get(id));
		}
		j.setMsg("删除附件信息管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("enclosuretab:enclosuretab:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Enclosuretab enclosuretab, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "附件信息管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Enclosuretab> page = enclosuretabService.findPage(new Page<Enclosuretab>(request, response, -1), enclosuretab);
    		new ExportExcel("附件信息管理", Enclosuretab.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出附件信息管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
//	@RequiresPermissions("enclosuretab:enclosuretab:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Enclosuretab> list = ei.getDataList(Enclosuretab.class);
			for (Enclosuretab enclosuretab : list){
				try{
					enclosuretabService.save(enclosuretab);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条附件信息管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条附件信息管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入附件信息管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/enclosuretab/enclosuretab/?repage";
    }
	
	/**
	 * 下载导入附件信息管理数据模板
	 */
//	@RequiresPermissions("enclosuretab:enclosuretab:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "附件信息管理数据导入模板.xlsx";
    		List<Enclosuretab> list = Lists.newArrayList(); 
    		new ExportExcel("附件信息管理数据", Enclosuretab.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/enclosuretab/enclosuretab/?repage";
    }
    
}