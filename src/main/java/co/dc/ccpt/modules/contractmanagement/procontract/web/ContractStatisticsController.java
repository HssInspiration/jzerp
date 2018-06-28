package co.dc.ccpt.modules.contractmanagement.procontract.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContractStatistics;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubContractStatistics;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.service.ContractStatisticsService;

/**
 * 合同查询统计Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/contractstatistics")
public class ContractStatisticsController extends BaseController {
	@Autowired
	private ContractStatisticsService contractStatisticsService;
	
	@ModelAttribute
	public ProContract get(@RequestParam(required=false) String id) {
		ProContract entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = contractStatisticsService.get(id);
		}
		if (entity == null){
			entity = new ProContract();
		}
		return entity;
	}
	
	/**
	 * 列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		return "modules/contractmanagement/procontract/contractstatisticsList";
	}
	
	@ResponseBody
	@RequestMapping(value = "getSubProContract")
	public Map<String, Object> getSubProContract(String proContractId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(proContractId == null || "".equals(proContractId)){
			map.put("rows","[]");
			map.put("total",0);
		}else{
			List<SubProContract> list = contractStatisticsService.get(proContractId).getSubProContractList();
			map.put("rows",list);
			map.put("total", list.size());
		}
		return map;
	}
	
	/**
	 * 列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProContract proContract, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProContract> page = contractStatisticsService.findPage(new Page<ProContract>(request, response), proContract); 
		return getBootstrapData(page);
	}

	@ResponseBody
	@RequestMapping(value = "detail")
	public ProContract detail(String id) {
		return contractStatisticsService.get(id);
	}
	
	/**
	 * 总包数据统计
	 * @return
	 */
	@RequestMapping(value = "proStatisticsForm")
	public String proStatisticsForm(ProContract proContract, Model model) {
		ProContractStatistics proContractStatistics = contractStatisticsService.proStatistics(proContract);
		model.addAttribute("proContractStatistics", proContractStatistics);
		return "modules/contractmanagement/procontract/contractstatisticsForm";
	}
	
	/**
	 * 分包数据统计
	 * @return
	 */
	@RequestMapping(value = "subStatisticsForm")
	public String subStatisticsForm(ProContract proContract, Model model) {
		SubProContract subProContract = new SubProContract();
		subProContract.setProContract(proContract);
		SubContractStatistics subContractStatistics = contractStatisticsService.subStatistics(subProContract);
		model.addAttribute("subContractStatistics", subContractStatistics);
		return "modules/contractmanagement/procontract/subcontractstatisticsForm";
	}
	
	/**
	 * 通过总包合同id查询出对应的分包合同
	 * @param proContract
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSubProContractById",method = RequestMethod.POST)
	public List<SubProContract> getSubProContractById(@RequestBody ProContract proContract){
		List<SubProContract> subProContractList = new ArrayList<SubProContract>();
		subProContractList = contractStatisticsService.getSubProContractById(proContract);
		return subProContractList;
	}
	
}
