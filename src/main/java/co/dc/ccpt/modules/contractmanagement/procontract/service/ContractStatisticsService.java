package co.dc.ccpt.modules.contractmanagement.procontract.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.ProContractStatistics;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubContractStatistics;
import co.dc.ccpt.modules.contractmanagement.procontract.entity.SubProContract;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.ProContractMapper;
import co.dc.ccpt.modules.contractmanagement.procontract.mapper.SubProContractMapper;
import co.dc.ccpt.modules.programmanage.entity.Program;
import co.dc.ccpt.modules.programmanage.mapper.ProgramMapper;

/**
 * 投标综合查询Service
 * @author lxh
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class ContractStatisticsService extends CrudService<ProContractMapper, ProContract>{

	@Autowired
	public SubProContractMapper subProContractMapper;
	
	@Autowired
	public ProContractMapper proContractMapper;
	
	@Autowired
	public ProgramMapper programMapper;
	
	public ProContract get(String id) {
		ProContract proContract = super.get(id);
		proContract.setSubProContractList(subProContractMapper.getList(new SubProContract(proContract)));
		return proContract;
	}
	
	public SubProContract getSubProContract(String id) {
		return subProContractMapper.get(id);
	}
	
	public List<ProContract> findList(ProContract proContract) {
		return super.findList(proContract);
	}
	
	public Page<ProContract> findPage(Page<ProContract> page, ProContract proContract) {
		return super.findPage(page, proContract);
	}
	
	//对前台传入的总包合同数据进行处理，分解整合出需要的数据
	public ProContractStatistics proStatistics(ProContract proContract){
		ProContractStatistics proContractStatistics = new ProContractStatistics();
		List<ProContract> proContractList1 = new ArrayList<ProContract>();//proContract中的参数可有可无
		List<ProContract> proContractList2 = new ArrayList<ProContract>();//查询出业主指定集合
		List<ProContract> proContractList3 = new ArrayList<ProContract>();//查询出市场投标集合
		List<ProContract> proContractList4 = new ArrayList<ProContract>();//查询出合同状态是已生效的集合
		List<ProContract> proContractList5 = new ArrayList<ProContract>();//根据合同状态获取集合
		Integer contractStatus = proContract.getContractStatus();//合同状态
		//判断生效与否、是业主指定还是市场投标
		if(proContract!=null){
			proContractList1 = super.findList(proContract);
			List<Double> totalProContractPriceList = new ArrayList<Double>();
			if(proContractList1 !=null && proContractList1.size() > 0){
				for(ProContract p:proContractList1){
					totalProContractPriceList.add(p.getContractTotalPrice());
					Program program = p.getProgram();
					if(program!=null){
						Integer getMethod = program.getGetMethod();
						if(getMethod!=null){
							if(getMethod == 0){//业主指定
								proContractList2.add(p);
								List<Double> totalAppointProContractPriceList = new ArrayList<Double>();
								if(proContractList2!=null && proContractList2.size()>0){
									for(ProContract p1:proContractList2){
										totalAppointProContractPriceList.add(p1.getContractTotalPrice());
									}
									if(totalAppointProContractPriceList!=null && totalAppointProContractPriceList.size()>0){
										proContractStatistics.setAppointProContractPrice(super.doubleListToArray(totalAppointProContractPriceList));
									}
								}
							}else if(getMethod == 1){//市场招标
								proContractList3.add(p);
								List<Double> totalMarketProContractPriceList = new ArrayList<Double>();
								if(proContractList3!=null && proContractList3.size()>0){
									for(ProContract p2:proContractList3){
										totalMarketProContractPriceList.add(p2.getContractTotalPrice());
									}
									if(totalMarketProContractPriceList!=null && totalMarketProContractPriceList.size()>0){
										proContractStatistics.setMarketProContractPrice(super.doubleListToArray(totalMarketProContractPriceList));
									}
									
								}
							}
						}
					}
					if(contractStatus == null){//获取初始查询时合同状态，若为空
						Integer status = p.getContractStatus();//通过当前已取到的集合获取合同状态
						if(status == 1){//若生效
							proContractList4.add(p);
							if(proContractList4!=null && proContractList4.size()>0){
								List<Double> totalEffectProContractPriceList = new ArrayList<Double>();
								proContractStatistics.setEffectProContractCount(proContractList4.size());//生效总包合同数量
								for(ProContract p4:proContractList4){
									totalEffectProContractPriceList.add(p4.getContractTotalPrice());
								}
								//生效总包合同总价
								if(totalEffectProContractPriceList != null && totalEffectProContractPriceList.size()>0){
									proContractStatistics.setEffectProContractPrice(super.doubleListToArray(totalEffectProContractPriceList));
								}
							}
						}
					}
				}
				if(totalProContractPriceList != null && totalProContractPriceList.size()>0){
					proContractStatistics.setTotalProContractPrice(super.doubleListToArray(totalProContractPriceList));//总包合同总价
				}
				proContractStatistics.setProContractCount(proContractList1.size());//总包合同数量
//				proContractStatistics.setAppointAndEffectedtPrice(appointAndEffectedtPrice);//业主指定且生效总包合同总价
//				proContractStatistics.setMarketAndEffectedPrice(marketAndEffectedPrice);//市场投标且生效总包合同总价
			}
			
			if(contractStatus != null){
				if(contractStatus == 0){//未生效
					proContractList5 = proContractMapper.getEffectList(proContract);
					if(proContractList5!=null && proContractList5.size()>0){
						List<Double> totalEffectProContractPriceList = new ArrayList<Double>();
						proContractStatistics.setNotEffectProContractCount(proContractList5.size());//未生效总包合同数量
						for(ProContract p:proContractList5){
							totalEffectProContractPriceList.add(p.getContractTotalPrice());
						}
						//未生效总包合同总价
						if(totalEffectProContractPriceList != null && totalEffectProContractPriceList.size()>0){
							proContractStatistics.setNotEffectProContractPrice(super.doubleListToArray(totalEffectProContractPriceList));
						}
					}
				}else if(contractStatus == 1){//已生效
					proContractList5 = proContractMapper.getEffectList(proContract);
					if(proContractList5!=null && proContractList5.size()>0){
						List<Double> totalEffectProContractPriceList = new ArrayList<Double>();
						proContractStatistics.setEffectProContractCount(proContractList5.size());//生效总包合同数量
						for(ProContract p:proContractList5){
							totalEffectProContractPriceList.add(p.getContractTotalPrice());
						}
						//生效总包合同总价
						if(totalEffectProContractPriceList !=null && totalEffectProContractPriceList.size()>0){
							proContractStatistics.setEffectProContractPrice(super.doubleListToArray(totalEffectProContractPriceList));
						}
					}
				}else if(contractStatus == 2){//结案(暂未处理)
					proContractList5 = proContractMapper.getEffectList(proContract);
				}else if(contractStatus == 3){//终止
					proContractList5 = proContractMapper.getEffectList(proContract);
				} 
			}
		}
		return proContractStatistics;
	}

	// 通过总包合同id查询出对应的分包合同
	public List<SubProContract> getSubProContractById(ProContract proContract) {
		List<SubProContract> subproContractList = new ArrayList<SubProContract>();
		if(proContract!=null){
			SubProContract subProContract = new SubProContract();
			subProContract.setProContract(proContract);
			subproContractList = subProContractMapper.getSubProContractListById(subProContract);
		}
		return subproContractList;
	}
	
	//对前台传入的分包合同数据进行处理，分解整合出需要的数据
	public SubContractStatistics subStatistics(SubProContract subProContract) {
		SubContractStatistics subProContractStatistics = new SubContractStatistics();
		List<SubProContract> subProContractList = new ArrayList<SubProContract>();
		subProContractList = subProContractMapper.getSubProContractListById(subProContract);
		if(subProContractList != null && subProContractList.size()>0){
			subProContractStatistics.setSubContractCount(subProContractList.size());//当前总包合同下对应的分包合同总数
			List<Double> totalSubContractPriceList = new ArrayList<Double>();
			List<Double> subContractPriceList = new ArrayList<Double>();
			for(SubProContract sub : subProContractList){
				totalSubContractPriceList.add(sub.getSubProTotalPrice());
					Integer status = sub.getContractStatus();//通过当前已取到的集合中对象获取合同状态
					if(status == 1){//若生效
						subContractPriceList.add(sub.getSubProTotalPrice());
					}
			}
			if(subContractPriceList != null && subContractPriceList.size()>0){
				subProContractStatistics.setEffectSubContractCount(subContractPriceList.size());
				subProContractStatistics.setEffectSubContractPrice(super.doubleListToArray(subContractPriceList));
			}
			//当前总包合同下对应的分包合同总价
			subProContractStatistics.setTotalSubContractPrice(super.doubleListToArray(totalSubContractPriceList));
		}
		
		return subProContractStatistics;
	}
	
}
