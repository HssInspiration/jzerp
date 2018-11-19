package co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity.BidCompanyManage;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.entity.BidtableQuery;
import co.dc.ccpt.modules.biddingmanagement.bid.bidquerymanage.service.BidtableQueryService;
import co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.utils.WordUtils;
import co.dc.ccpt.modules.programmanage.entity.Company;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.service.CompanyService;
import co.dc.ccpt.modules.programmanage.service.ProgramService;


/**
 * 投标综合查询Controller
 * @author lxh
 * @version 2018-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/bidquerymanage/bidtablequery")
public class BidtableQueryController extends BaseController {

	@Autowired
	private BidtableQueryService bidtableService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProgramService programService;
	
	@ModelAttribute
	public BidtableQuery get(@RequestParam(required=false) String id) {
		BidtableQuery entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bidtableService.get(id);
		}
		if (entity == null){
			entity = new BidtableQuery();
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
	 * 投标管理列表页面
	 */
//	@RequiresPermissions("bidquerymanage:bidtable:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "modules/biddingmanagement/bid/bidquerymanage/bidtableQueryList";
	}
	
	/**
	 * 投标管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BidtableQuery bidtableQuery,HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("----bidtableQuery是:----"+bidtableQuery);
		String programStatus= bidtableQuery.getProgramStatus();//复选框查询参数自前台传入
		System.out.println("programStatus:"+programStatus);
		//将字符串转成集合存入到指定集合中
		List<String> statusList = java.util.Arrays.asList(programStatus.split(",")); 
		bidtableQuery.setProgramStatusList(statusList);
		Page<BidtableQuery> page = bidtableService.findPage(new Page<BidtableQuery>(request, response), bidtableQuery); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑投标管理表单页面
	 */
//	@RequiresPermissions(value={"bidquerymanage:bidtable:view","bidquerymanage:bidtable:add","bidquerymanage:bidtable:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BidtableQuery bidtableQuery, Model model) {
		model.addAttribute("bidtableQuery", bidtableQuery);
		model.addAttribute("companyList",companyService.listAllCompany());
		model.addAttribute("programList",programService.listAllProgram());
		return "modules/biddingmanagement/bid/bidquerymanage/bidtableQueryForm";
	}

	/**
	 * 保存投标管理
	 */
	@ResponseBody
//	@RequiresPermissions(value={"bidquerymanage:bidtable:add","bidquerymanage:bidtable:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(BidtableQuery bidtableQuery, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, bidtableQuery)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		bidtableService.save(bidtableQuery);//保存
		j.setSuccess(true);
		j.setMsg("保存投标管理成功");
		return j;
	}
	
	/**
	 * 删除投标管理
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BidtableQuery bidtableQuery, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		bidtableService.delete(bidtableQuery);
		j.setMsg("删除投标管理成功");
		return j;
	}
	
	/**
	 * 批量删除投标管理
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bidtableService.delete(bidtableService.get(id));
		}
		j.setMsg("删除投标管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
//	@RequiresPermissions("bidquerymanage:bidtable:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(BidtableQuery bidtableQuery, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "投标管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BidtableQuery> page = bidtableService.findPage(new Page<BidtableQuery>(request, response, -1), bidtableQuery);
    		new ExportExcel("投标管理", BidtableQuery.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出投标管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
	
	
	/**
	 * 导出投标项目开标情况表
	 */
	@ResponseBody
	@RequestMapping("/exportBidTab")
    public void exportBidTab(@RequestParam String bidId, HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat smf = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
		List<BidCompanyManage> list = new ArrayList<BidCompanyManage>();
		List<Map<String,Object>> bidContList = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotBlank(bidId)){
			BidtableQuery bidtabQuery = bidtableService.get(bidId);
			list = bidtabQuery.getBidCompanyManageList();
			Program program = null;
			if(bidtabQuery != null){
				program = bidtabQuery.getProgram();
				if(program != null){
					map.put("programName", StringUtils.isNotBlank(program.getProgramName())?program.getProgramName():"");// 工程名称  
					map.put("programCont", StringUtils.isNotBlank(program.getProDescription())?program.getProDescription():"");// 工程概况
					map.put("openDate", program.getPlanToStart() != null?smf.format(program.getPlanToStart()):"");// 计划开标时间
				}
				map.put("openAddr", StringUtils.isNotBlank(bidtabQuery.getOpenBidAddr())?bidtabQuery.getOpenBidAddr():"");// 开标地点
				map.put("openMeterials", StringUtils.isNotBlank(bidtabQuery.getProvideMeterial())?bidtabQuery.getProvideMeterial():"");// 现场所需材料  
				map.put("evaluateMethod", StringUtils.isNotBlank(bidtabQuery.getEvaluateMethod())?bidtabQuery.getEvaluateMethod():"");// 评标办法
				map.put("ctrlPrice", bidtabQuery.getCtrlPrice()!=null?bidtabQuery.getCtrlPrice():"");// 控制价
				map.put("floorPrice", bidtabQuery.getFloorPrice()!=null?bidtabQuery.getFloorPrice():"");// 标底价
				map.put("provisionPrice", bidtabQuery.getProvisionPrice()!=null?bidtabQuery.getProvisionPrice():"");// 暂列金额
				map.put("recorder", bidtabQuery.getRecordWorker()!=null?bidtabQuery.getRecordWorker():"");// 评标记录人员
			}
			if(list != null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("num",i+1); // 序号
					BidCompanyManage bidCompanyManage = list.get(i);
					if(bidCompanyManage != null){
						Company company = bidCompanyManage.getCompany();
						if(company != null){
							// 参投单位  
							dataMap.put("bidCompanyName", StringUtils.isNotBlank(company.getCompanyName())?company.getCompanyName():"");
						}
						System.out.println("bidCompanyManage.getBidPrice():"+bidCompanyManage.getBidPrice());
						dataMap.put("bidPrice", bidCompanyManage.getBidPrice() != null?bidCompanyManage.getBidPrice():"");// 投标价
						dataMap.put("buildDate", bidCompanyManage.getBuildDate() != null?bidCompanyManage.getBuildDate():"");// 工期
						logger.debug(bidCompanyManage.getBuildDate().toString());
						dataMap.put("quality", bidCompanyManage.getQuality() != null?bidCompanyManage.getQuality():"");// 质量
					 	Integer isBid = bidCompanyManage.getIsBid();
					 	if(isBid != null){
					 		if(isBid == 0){
					 			dataMap.put("isBid", "未中标");// 是否中标
					 		}else if(isBid == 1){
					 			dataMap.put("isBid", "中标");// 是否中标
					 		}
					 	}
						bidContList.add(dataMap);
					}
				}
			}
			map.put("bidContList", bidContList);
		}
        try {
//            WordUtils.exportMillCertificateWord(req,resp,map,"投标项目开标情况表","tempBidTab.ftl",1);
            WordUtils.exportMillCertificateWord(req,resp,map,"投标项目开标情况表","temp.ftl",1);
        } catch (IOException e) {
        	e.printStackTrace();
        } 
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public BidtableQuery detail(String id) {
		return bidtableService.get(id);
	}
	

	/**
	 * 导入Excel数据
	 */
//	@RequiresPermissions("bidquerymanage:bidtable:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BidtableQuery> list = ei.getDataList(BidtableQuery.class);
			for (BidtableQuery bidtableQuery : list){
				try{
					bidtableService.save(bidtableQuery);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条投标管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条投标管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入投标管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bidquerymanage/bidtable/?repage";
    }
	
	/**
	 * 下载导入投标管理数据模板
	 */
//	@RequiresPermissions("bidquerymanage:bidtable:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标管理数据导入模板.xlsx";
    		List<BidtableQuery> list = Lists.newArrayList(); 
    		new ExportExcel("投标管理数据", BidtableQuery.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bidquerymanage/bidtable/?repage";
    }
	

}