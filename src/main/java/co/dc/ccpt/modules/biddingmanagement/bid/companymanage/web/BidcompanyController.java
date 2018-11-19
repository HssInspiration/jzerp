/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.web;

import java.text.ParseException;
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
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.BidStatistics;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service.BidcompanyService;
import co.dc.ccpt.modules.biddingmanagement.bid.enclosuremanage.service.EnclosuretabService;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.PersonCertificate;
import co.dc.ccpt.modules.coreperson.basicinfo.service.CorePersonService;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.entity.SubpackageProgram;
import co.dc.ccpt.modules.programmanage.service.CompanyService;
import co.dc.ccpt.modules.programmanage.service.ProgramService;
import co.dc.ccpt.modules.programmanage.service.SubpackageProgramService;
import co.dc.ccpt.modules.sys.entity.User;

/**
 * 参投单位管理Controller
 * @author lxh
 * @version 2018-02-08
 */
@Controller
@RequestMapping(value = "${adminPath}/companymanage/bidcompany")
public class BidcompanyController extends BaseController {

	@Autowired
	private BidcompanyService bidcompanyService;
	
	@Autowired
	private BidtableService bidtableService;
	
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private EnclosuretabService enclosuretabService;
	
	@Autowired
	private CorePersonService corePersonService;
	
	@Autowired
	private SubpackageProgramService subProgramService;
	
	@ModelAttribute
	public Bidcompany get(@RequestParam(required=false) String id) {
		Bidcompany entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bidcompanyService.get(id);
		}
		if (entity == null){
			entity = new Bidcompany();
		}
		return entity;
	}
	@ModelAttribute
	public Company getCompany(@RequestParam(required=false) String compid) {
		Company entity = null;
		if (StringUtils.isNotBlank(compid)){
			entity = companyService.get(compid);
		}
		if (entity == null){
			entity = new Company();
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
	 * 参投单位管理列表页面
	 * @param corePersonId
	 * @param isBid首页中已中标项目传入的参数
	 * @param companyName首页中已中标项目传入的参数
	 * @param model
	 * @return
	 */
	//@RequiresPermissions("companymanage:bidcompany:list")
	@RequestMapping(value = {"list", "","showList"})
	public String list(String corePersonId, Integer isBid, String companyName, Model model) {
		Company comp = new Company();
		Bidcompany bidcomp= new Bidcompany();
		if(StringUtils.isNotBlank(companyName) && isBid != null){
			comp.setCompanyName(companyName);
			bidcomp.setCompany(comp);
			bidcomp.setIsBid(isBid);
		}
		model.addAttribute("bidcompany", bidcomp);
		model.addAttribute("corePersonId", corePersonId);
		return "modules/biddingmanagement/bid/companymanage/bidcompanyList";
	}
	
	/**
	 * 参投单位管理列表数据
	 * @throws ParseException 
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:bidcompany:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(String corePersonId, Bidcompany bidcompany, Company company, Program program, HttpServletRequest request, HttpServletResponse response, Model model) throws ParseException {
		Page<Bidcompany> page = new Page<Bidcompany>();
		if(StringUtils.isNotBlank(corePersonId)){
			bidcompany.setCorePersonId(corePersonId);
			page = bidcompanyService.findNewPage(new Page<Bidcompany>(request, response), bidcompany); 
		}else{
			page = bidcompanyService.findPage(new Page<Bidcompany>(request, response), bidcompany); 
		}
		return getBootstrapData(page);
	}
	
	/**
	 * 参投单位管理列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:bidcompany:list")
	@RequestMapping(value = "showdata")
	public Map<String, Object> showdata(Bidcompany bidcompany, HttpServletRequest request, HttpServletResponse response, Model model) {
		Company company = new Company();
		company.setCompanyName("金卓");
		bidcompany.setCompany(company);
		bidcompany.setIsBid(1);
		Page<Bidcompany> page = bidcompanyService.findPage(new Page<Bidcompany>(request, response), bidcompany); 
		return getBootstrapData(page);
	}
	
	/**
	 * 查看，增加，编辑参投单位管理表单页面
	 */
	//@RequiresPermissions(value={"companymanage:bidcompany:view","companymanage:bidcompany:add","companymanage:bidcompany:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Bidcompany bidcompany, Model model) {
		//若为金卓，投标日期直接自投标管理中获取
		Company company = bidcompany.getCompany();
		if(company!=null){
			String companyId = company.getId();
			if(companyId.equals("03ae459404284f17bbd25e78a13397a6")){
				Bidtable bidtable = bidcompany.getBidtable();
				if(bidtable!=null){
					Date bidDate = bidtable.getOpenBidDate();//投标日期
					bidcompany.setBidDate(bidDate);//将参投为金卓的投标日期设置与投标一致
				}
			}
		}
		model.addAttribute("bidcompany", bidcompany);
		if(StringUtils.isBlank(bidcompany.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}else{
			model.addAttribute("edit",true);
		}
		return "modules/biddingmanagement/bid/companymanage/bidcompanyForm";
	}
	/**
	 * 数据统计
	 * @param bidcompany
	 * @param program
	 * @param company
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "statisticsForm")
	public String statisticsForm(Bidcompany bidcompany, Program program, Company company, Model model) {
		bidcompany.setProgram(program);
		bidcompany.setCompany(company);
		String proName = bidcompany.getProgram().getProgramName();
		String compName = bidcompany.getCompany().getCompanyName();
		Date beginDate = bidcompany.getBeginBidDate();
		Date endDate = bidcompany.getEndBidDate();
		Integer isBid = bidcompany.getIsBid();
		//在执行筛选的时候将查询出的集合取出存入作用域
		BidStatistics bidStatistics = new BidStatistics();
		List<Bidcompany> bidCompanyList = new ArrayList<Bidcompany>();
		if(StringUtils.isBlank(proName)&&
		   StringUtils.isBlank(compName)&&
		   beginDate == null &&endDate == null &&isBid == null){//如果以上参数为空则查询所有
			bidCompanyList = bidcompanyService.findJzList(bidcompany);
		}else{
			bidCompanyList = bidcompanyService.findList(bidcompany);
		}
		List<Double> bidPriceList = new ArrayList<Double>();//投标价
		List<Double> laborCostList = new ArrayList<Double>();//劳务费
		List<Double> meterialPriceList = new ArrayList<Double>();//材料费
		List<Double> depositList = new ArrayList<Double>();//保证金
		for(Bidcompany b:bidCompanyList){//将每一个对应的金额属性存入对应集和中
			bidPriceList.add(b.getBidPrice());
			laborCostList.add(b.getLaborCost());
			meterialPriceList.add(b.getMeterialExpense());
			depositList.add(b.getDeposit());
		}
		if(bidPriceList!=null){
			Double[] bidPriceArr = bidPriceList.toArray(new Double[0]);//将集合转成Double数组
			Double sumBidPrice = (double) 0;
			for(int i=0;i<=bidPriceArr.length-1;i++){
				sumBidPrice += bidPriceArr[i] ;
			}
			bidStatistics.setTotalBidPrice(sumBidPrice);
			model.addAttribute("sumBidPrice", sumBidPrice);
		}
		if(laborCostList!=null){
			Double[] laborCostArr = laborCostList.toArray(new Double[0]);//将集合转成Double数组
			Double sumLaborCost = (double) 0;
			for(int i=0;i<=laborCostArr.length-1;i++){
				sumLaborCost += laborCostArr[i] ;
			}
			bidStatistics.setTotalBidLaborCost(sumLaborCost);
		}
		if(meterialPriceList!=null){
			Double[] meterialPriceArr = meterialPriceList.toArray(new Double[0]);//将集合转成Double数组
			Double sumMeterialPrice = (double) 0;
			for(int i=0;i<=meterialPriceArr.length-1;i++){
				sumMeterialPrice += meterialPriceArr[i] ;
			}
			bidStatistics.setTotalBeterialPrice(sumMeterialPrice);
		}
		if(depositList!=null){
			Double[] depositArr = depositList.toArray(new Double[0]);//将集合转成Double数组
			Double sumDeposit = (double) 0;
			for(int i=0;i<=depositArr.length-1;i++){
				sumDeposit += depositArr[i] ;
			}
			bidStatistics.setTotalDeposit(sumDeposit);
		}
		model.addAttribute("bidStatistics", bidStatistics);
		return "modules/biddingmanagement/bid/companymanage/statisticsForm";
	}
	

	/**
	 * 保存参投单位管理
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(Bidcompany bidcompany, Model model){
		AjaxJson j = new AjaxJson();
		if(Global.isDemoMode()){
			j.setSuccess(false);
			j.setMsg("演示模式，不允许操作！");
			return j;
		}
		
		if (!beanValidator(model, bidcompany)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存 ：保存之前获取投标id和投标时间（投标时间不能晚于项目计划开标时间）
		//保存思路：
		//1.一个单位只能在一个项目上投标一次，且只能对应一种中标状态
		//(1)获取当前选取的项目id
		Program program = new Program();
		String programId = "";
		String bidtableId = bidcompany.getBidtable().getId();//投标id
		Bidtable bidtable = bidtableService.get(bidtableId);//一条投标信息
		program = bidtable.getProgram();
		Company comp = new Company();
		Company comp1 = new Company();
		String compName = "";
		String compName1 = "";
		Integer isBid = 0;
//		if(StringUtils.isBlank(bidcompany.getId())){//增加时
			if(program!=null){
				programId = program.getId();
			}
			//(2)查询其已参投的项目id集合
			List<String> programIdList = bidcompanyService.listAllProgramIdByCompanyId(bidcompany);
			//(3)判断与当前是否重复
			//获取中标状态
			isBid = bidcompany.getIsBid();
			if(StringUtils.isBlank(bidcompany.getId())){//当其为增加时判断是否重复
				for(String progrId:programIdList){
					if(progrId.equals(programId)){
						j.setSuccess(false);
						j.setMsg("当前单位已投过该项目，不允许重复投标!");
						return j;
					}
				}	
				//新增参投单位时不可选择所选项目发包单位：
				if(program!=null){
					program = programService.get(program);
					if(program!=null){
						comp = program.getCompany();
						if(comp!=null){
							compName = comp.getCompanyName();//发包单位名称
						}
					}
				}
				//获取参投单位：
				comp1 = bidcompany.getCompany();
				if(comp1!=null){
					String compId = comp1.getId();
					comp1 = companyService.get(compId);
					if(comp1!=null){
						compName1 = comp1.getCompanyName();
						if(compName1!=null && compName1.equals(compName)){
							j.setSuccess(false);
							j.setMsg("当前单位为本项目发包单位，不允许投标本项目!");
							return j;
						}
					}
				}
				//一个项目只能有一个参投单位中标
				//判断参投信息中的中标状态(通过项目id查询当前)
				if(isBid==1){
					bidcompany.setBidtable(bidtable);
					List<Integer> bidStatusList = bidcompanyService.listAllIsBidStatusByProId(bidcompany); 
					for(Integer i:bidStatusList){
						if(isBid==i){
							j.setSuccess(false);
							j.setMsg("当前项目已有单位中标!");
							return j;
						}
					}
				}
			}
			
			//人员不可重复：（建造师、技术负责人、施工员、安全员、质检员、材料员、造价员）
			Boolean b = bidcompanyService.checkWorker(bidcompany);
			if(b==false){
				j.setSuccess(false);
				j.setMsg("当前参投信息中人员不可重复!");
				return j;
			}
			//日期判断处理
			Date openDate = new Date();
			if(program!=null){//排除空指针异常
				program = programService.get(programId);
				openDate = program.getPlanToStart();//项目计划开标时间
				bidtable.setProgram(program);//同步保存项目id
			}
			Date date = bidcompany.getBidDate();//参投中投标日期
			if(date.after(openDate)){
				j.setSuccess(false);
				j.setMsg("投标时间不能晚于项目开标时间!");
				return j;
			}
			
			bidcompany.setBidtable(bidtable);
			bidcompanyService.save(bidcompany);//保存
			j.setSuccess(true);
			String companyName = bidcompany.getCompany().getCompanyName();
			if(companyName!=null && !companyName.equals("")){
				j.setMsg("保存参投单位'" + bidcompany.getCompany().getCompanyName()+ "'成功！");
			}else{
				j.setMsg("保存参投单位成功！");
			}
			//保存参投之后先获取参投的id，再获取是否中标的状态，若id为金卓继续判断状态是否中标，若状态为已中标，将项目的状态自由更改为施工，若未中标则显示未中标
			String bidCompanyId = bidcompany.getCompany().getId();
			if(isBid==1){//判断是否已中标
			//2.获取参投的id
			   if(bidCompanyId.equals("03ae459404284f17bbd25e78a13397a6")){//若为金卓将对应项目状态更改为施工
				   program = programService.get(program);
				   if(program!=null){
					   program.setStatus(2);
					   programService.save(program);
				   }
				   //将人员基础信息中的是否在建更改为是：
				  List<CorePerson> CorePersonList = bidcompanyService.getCorePersonByBidcompany(bidcompany);
				  for(CorePerson CorePerson:CorePersonList){
					  if(CorePerson!=null){
						  CorePerson.setIsBuild("1");
						  corePersonService.save(CorePerson);
					  }else{
						  logger.info("CorePerson is null!");
					  }
				  }
			   }else{
					if(program!=null){
					   program.setStatus(6);
					   programService.save(program);
					}
			   }
			}else if(isBid==0){
				if(bidCompanyId.equals("03ae459404284f17bbd25e78a13397a6")){
				   program = programService.get(program);
				   if(program!=null){
					   program.setStatus(6);
					   programService.save(program);
				   }
				}
			}
		return j;
	}
	
	/**
	 * 验证投标日期晚于开标日期（待完善）
	 * @param openBidDate
	 * @param bidDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkBidDate")
	public String checkBidDate(String openBidDate, String bidDate) {
		//根据传入的两个参数进行比较
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date opendate  = sdf.parse(openBidDate);
			Date biddate  = sdf.parse(bidDate);
			if(opendate.getTime()<biddate.getTime()){
				return "true";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 * 
	 * @param bidtable
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getBidtableByProId")
	public Bidtable getBidtabAndBidCompanyByProId(@RequestBody Bidtable bidtable) {
		String bidtableId = bidtable.getId(); 
		bidtable = bidtableService.get(bidtableId);//通过投标对象id获取对应的投标信息
		List<Bidcompany> bidcompanyList = bidcompanyService.getBidcompanyIdByBidtableId(bidtableId);
		for(Bidcompany bidcomp:bidcompanyList){
			if(bidcomp.getIsBid()==1){
				String str = "true";
				bidtable.setRemarks(str);
				break;
			}
		}
		return bidtable;
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
		return i;
	}
	
	/**
	 * 验证人员是否重复
	 * @param bidcompany
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "validateWorker")
	public List<Bidcompany> validateWorkerByworkerId(@RequestBody Bidcompany bidcompany) {
		List<Bidcompany> bidcompList = new ArrayList<Bidcompany>();
		if(bidcompany.getConstructorId() != null && !bidcompany.getConstructorId().equals("") ){
			
			bidcompList = bidcompanyService.validateWorkerByCorePersonId(bidcompany.getConstructorId());
			
		}else if(bidcompany.getDirectorId() != null && !bidcompany.getDirectorId().equals("")){
			
			bidcompList = bidcompanyService.validateWorkerByCorePersonId(bidcompany.getDirectorId());
			
		}else if(bidcompany.getSaverId() != null && !bidcompany.getSaverId().equals("")){
			
			bidcompList = bidcompanyService.validateWorkerByCorePersonId(bidcompany.getSaverId());
			
		}else if(bidcompany.getInspectorId() != null && !bidcompany.getInspectorId().equals("")){
			
			bidcompList = bidcompanyService.validateWorkerByCorePersonId(bidcompany.getInspectorId());
			
		}else if(bidcompany.getConstrworkerId() != null && !bidcompany.getConstrworkerId().equals("")){
			
			bidcompList = bidcompanyService.validateWorkerByCorePersonId(bidcompany.getConstrworkerId());
			
		}
		return bidcompList;
	}
	
	@ResponseBody
	@RequestMapping(value = "validateOneWorker")
	public Bidcompany validateOneWorkerByworkerId(@RequestBody Bidcompany bidcompany) {
		if(bidcompany.getConstructorId() != null && !bidcompany.getConstructorId().equals("") ){
			
			bidcompany = bidcompanyService.getBidcompanyByWorkerId(bidcompany.getConstructorId());
			
		}else if(bidcompany.getDirectorId() != null && !bidcompany.getDirectorId().equals("")){
			
			bidcompany = bidcompanyService.getBidcompanyByWorkerId(bidcompany.getDirectorId());
			
		}else if(bidcompany.getSaverId() != null && !bidcompany.getSaverId().equals("")){
			
			bidcompany = bidcompanyService.getBidcompanyByWorkerId(bidcompany.getSaverId());
			
		}else if(bidcompany.getInspectorId() != null && !bidcompany.getInspectorId().equals("")){
			
			bidcompany = bidcompanyService.getBidcompanyByWorkerId(bidcompany.getInspectorId());
			
		}else if(bidcompany.getConstrworkerId() != null && !bidcompany.getConstrworkerId().equals("")){
			
			bidcompany = bidcompanyService.getBidcompanyByWorkerId(bidcompany.getConstrworkerId());
			
		}
		if(bidcompany!=null){
			return bidcompany;
		}else{
			return new Bidcompany();
		}
		
		
	}
	
	//通过名称查询出所有的项目
	@ResponseBody
	@RequestMapping(value = "getAllBidtableList")
	public List<Bidtable> getAllProgramList(@RequestParam  String programName) {
		System.out.println(programName);
		Program program = new Program();
		program.setProgramName(programName);
		Bidtable bidtable = new Bidtable();
		bidtable.setProgram(program);
		
		List<Bidtable> bidtableList = new ArrayList<Bidtable>();
		bidtableList = bidtableService.listAllBidtableByProName(bidtable);
		return bidtableList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getAllCorePersonListByName")
	public List<CorePerson> getAllCorePersonListByName(@RequestParam  String name) {
		CorePerson corePerson = new CorePerson();
		User user = new User();
		user.setName(name);
		corePerson.setUser(user);
		List<CorePerson> corePersonList = new ArrayList<CorePerson>();
		corePersonList = corePersonService.getAllCorePersonList(corePerson);
		return corePersonList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getAppointCorePersonListByName")
	public List<CorePerson> getAppointCorePersonListByName(@RequestParam  String name,String certificateName) {
		CorePerson corePerson = new CorePerson();
		PersonCertificate personCertificate = new PersonCertificate();
		personCertificate.setCertificateName(certificateName);
		User user = new User();
		user.setName(name);
		corePerson.setUser(user);
		corePerson.setPersonCertificate(personCertificate);
		corePerson.setPersonCertificate(personCertificate);
		List<CorePerson> corePersonList = new ArrayList<CorePerson>();
		corePersonList = corePersonService.getAppointCorePersonListByName(corePerson);
		return corePersonList;
	}
	
	//通过名称查询出所有的单位
	@ResponseBody
	@RequestMapping(value = "getAllCompanyList")
	public List<Company> getListAllCompanyByName(@RequestParam  String companyName) {
		List<Company> companyList = new ArrayList<Company>();
		companyList = companyService.listAllCompanyByName(companyName);
		return companyList;
	}
	
	@ResponseBody
	@RequestMapping(value = "getDeposit", method=RequestMethod.POST)
	public Bidtable getDeposit(@RequestBody Bidtable bidtable) {
		bidtable = bidtableService.get(bidtable);
		return bidtable;
	}
	
	/**
	 * 删除参投单位管理
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:bidcompany:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Bidcompany bidcompany, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		bidcompanyService.delete(bidcompany);
		j.setMsg("删除参投单位管理成功");
		return j;
	}
	
	/**
	 * 批量删除参投单位管理
	 */
	@ResponseBody
	//@RequiresPermissions("companymanage:bidcompany:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Bidcompany bidcompany = bidcompanyService.get(id);
			Bidtable bidtable = bidcompany.getBidtable();
			if(bidtable!=null){
				Program program = bidtable.getProgram();
				if(program!=null){
					String programId = program.getId();
					program = programService.get(programId);
					if(program!=null){
						Integer status = program.getStatus();
						if(status!=null){
							if(status==3){//竣工状态
								j.setSuccess(false);
								j.setMsg("项目已竣工，不可删除!");
								return j;
							}else if(status==4){//停工状态
								j.setSuccess(false);
								j.setMsg("项目已停工，不可删除!");
								return j;
							}else if(status==5){//结案状态
								j.setSuccess(false);
								j.setMsg("项目已结案，不可删除!");
								return j;
							}else if(programId!=null && !programId.equals("")){
								//查询分包中是否有记录
								List<SubpackageProgram> subProList = subProgramService.getByParentId(programId);
								if(subProList.size()>0){
									j.setSuccess(false);
									j.setMsg("分包项目中已登记相关信息，不可删除!");
									return j;
								}else{
									bidcompanyService.delete(bidcompany);
									enclosuretabService.deleteEnclosureByForeginId(bidcompany.getId());//同步删除对应附件
									program = programService.get(programId);
									program.setStatus(1);//参投删除项目状态更改为投标
									programService.save(program);
									j.setMsg("删除参投单位信息成功!");
									return j;
								}
							}
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
	//@RequiresPermissions("companymanage:bidcompany:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Bidcompany bidcompany, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "参投单位管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Bidcompany> page = bidcompanyService.findPage(new Page<Bidcompany>(request, response, -1), bidcompany);
    		new ExportExcel("参投单位管理", Bidcompany.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出参投单位管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	//@RequiresPermissions("companymanage:bidcompany:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Bidcompany> list = ei.getDataList(Bidcompany.class);
			for (Bidcompany bidcompany : list){
				try{
					bidcompanyService.save(bidcompany);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条参投单位管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条参投单位管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入参投单位管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/companymanage/bidcompany/?repage";
    }
	
	/**
	 * 下载导入参投单位管理数据模板
	 */
    //@RequiresPermissions("companymanage:bidcompany:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "参投单位管理数据导入模板.xlsx";
    		List<Bidcompany> list = Lists.newArrayList(); 
    		new ExportExcel("参投单位管理数据", Bidcompany.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/companymanage/bidcompany/?repage";
    }

}