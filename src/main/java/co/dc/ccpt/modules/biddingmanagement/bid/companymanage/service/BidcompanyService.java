/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.common.utils.StringUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.bidmanage.entity.Bidtable;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Bidcompany;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.mapper.BidcompanyMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.programmanage.entity.Program;
import co.dc.ccpt.modules.coreperson.basicinfo.entity.CorePerson;
import co.dc.ccpt.modules.coreperson.basicinfo.service.CorePersonService;

/**
 * 参投单位管理Service
 * 
 * @author lxh
 * @version 2018-02-08
 */
@Service
@Transactional(readOnly = true)
public class BidcompanyService extends CrudService<BidcompanyMapper, Bidcompany> {

	@Autowired
	private BidcompanyMapper bidCompanyMapper;

	@Autowired
	private CorePersonService CorePersonService;

	public Bidcompany get(String id) {
		return super.get(id);
	}

	public List<Bidcompany> validateWorkerByCorePersonId(String corePersonId) {
		return bidCompanyMapper.validateWorkerByCorePersonId(corePersonId);
	}

	public List<Bidcompany> findList(Bidcompany bidcompany) {
		return super.findList(bidcompany);
	}

	// 查询所有已中标数量
	public Integer getTotalIsBidCount() {
		return bidCompanyMapper.getTotalIsBidCount();
	}

	public Double getTotalLaborCost() {
		return bidCompanyMapper.getTotalLaborCost();
	}

	// 查询所有已中标投标价
	public Double getTotalBidPrice() {
		return bidCompanyMapper.getTotalBidPrice();
	}

	public List<Bidcompany> findJzList(Bidcompany bidcompany) {
		return bidCompanyMapper.findJzList(bidcompany);
	}

	public Page<Bidcompany> findPage(Page<Bidcompany> page, Bidcompany bidcompany) {
		return super.findPage(page, bidcompany);
	}

	public Bidcompany getByBidcompanyId(String companyId) {
		Bidcompany bidcompany = new Bidcompany();
		bidcompany.setCompanyId(companyId);
		return bidCompanyMapper.getByBidcompanyId(bidcompany);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Bidcompany> getBidcompanyIdByBidtableId(String bidtableId) {
		Bidtable bidtable = new Bidtable();
		bidtable.setId(bidtableId);
		System.out.println(bidtable.getId());
		Bidcompany bidcompany = new Bidcompany();
		List<Bidcompany> bidcompanyList = new ArrayList<Bidcompany>();
		if (bidtable != null) {
			bidcompany.setBidtable(bidtable);
			if (bidcompany != null) {
				bidcompanyList = bidCompanyMapper.getBidcompanyByBidtableId(bidcompany);
			}
		}
		return bidcompanyList;
	}

	public List<Integer> listAllIsBidStatusByProId(Bidcompany bidcompany) {
		return bidCompanyMapper.listAllIsBidStatusByProId(bidcompany);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<String> listAllProgramIdByCompanyId(Bidcompany bidcompany) {
		return bidCompanyMapper.listAllProgramIdByCompanyId(bidcompany);
	};

	public Bidcompany BidcompanyMapper(String companyId) {
		Bidcompany bidcompany = new Bidcompany();
		bidcompany.setCompanyId(companyId);
		return bidCompanyMapper.getByBidcompanyId(bidcompany);
	}

	@Transactional(readOnly = false)
	public void save(Bidcompany bidcompany) {
		super.save(bidcompany);
	}

	@Transactional(readOnly = false)
	public void delete(Bidcompany bidcompany) {
		super.delete(bidcompany);
	}

	public List<Bidcompany> getBidcompanyByProName(Bidcompany bidcompany) {
		return bidCompanyMapper.getBidcompanyByProName(bidcompany);
	};

	public List<Bidcompany> listBidcompanyByCompId(String companyId) {
		return bidCompanyMapper.listBidcompanyByCompId(companyId);
	};

	// 通过参投对象获取并组装人员集合
	public List<CorePerson> getUserListById(Bidcompany bidcompany) {
		List<CorePerson> corePersonList = new ArrayList<CorePerson>();
		bidcompany = bidCompanyMapper.get(bidcompany);
		String constructorId = ""; // 建造师
		String directorId = ""; // 技术负责人
		String saverId = ""; // 安全员
		String constrworkerId = ""; // 施工员
		String inspectorId = ""; // 质检员

		String tecBid = ""; // 技术标
		String comBid = ""; // 商务标
		String meterialerId = ""; // 材料员
		String costerId = ""; // 造价员

		if (bidcompany != null) {
			constructorId = bidcompany.getConstructoor().getId();
			if (constructorId != null) {
				corePersonList.add(CorePersonService.get(constructorId));
			}

			directorId = bidcompany.getDirector().getId();
			if (directorId != null) {
				corePersonList.add(CorePersonService.get(directorId));
			}

			saverId = bidcompany.getSaver().getId();
			if (saverId != null) {
				corePersonList.add(CorePersonService.get(saverId));
			}

			constrworkerId = bidcompany.getConstrworker().getId();
			if (constrworkerId != null) {
				corePersonList.add(CorePersonService.get(constrworkerId));
			}

			inspectorId = bidcompany.getInspector().getId();
			if (inspectorId != null) {
				corePersonList.add(CorePersonService.get(inspectorId));
			}

			tecBid = bidcompany.getTecBidName().getId();
			if (tecBid != null) {
				corePersonList.add(CorePersonService.get(tecBid));
			}

			comBid = bidcompany.getComBidName().getId();
			if (comBid != null) {
				corePersonList.add(CorePersonService.get(comBid));
			}

			meterialerId = bidcompany.getMeterialer().getId();
			if (meterialerId != null) {
				corePersonList.add(CorePersonService.get(meterialerId));
			}

			costerId = bidcompany.getCoster().getId();
			if (costerId != null) {
				corePersonList.add(CorePersonService.get(costerId));
			}

		}
		return corePersonList;
	}
	
	//新增或修改时判断当前信息中人员不可重复（除技术标与商务标除外）
	public Boolean checkWorker(Bidcompany bidcompany){
		//挨个获取人员 
		String constructorId = ""; // 建造师
		String directorId = ""; // 技术负责人
		String saverId = ""; // 安全员
		String constrworkerId = ""; // 施工员
		String inspectorId = ""; // 质检员

		String meterialerId = ""; // 材料员
		String costerId = ""; // 造价员
		List<String> idList = new ArrayList<String>();
			constructorId = bidcompany.getConstructoor().getId();
			directorId = bidcompany.getDirector().getId();
			saverId = bidcompany.getSaver().getId();
			constrworkerId = bidcompany.getConstrworker().getId();
			inspectorId = bidcompany.getInspector().getId();
			meterialerId = bidcompany.getMeterialer().getId();
			costerId = bidcompany.getCoster().getId();
			
			if (StringUtils.isNotBlank(constructorId)) {
				idList.add(constructorId);
			}

			if (StringUtils.isNotBlank(directorId)) {
				idList.add(directorId);
			}

			if (StringUtils.isNotBlank(saverId)) {
				idList.add(saverId);
			}

			if (StringUtils.isNotBlank(constrworkerId)) {
				idList.add(constrworkerId);
			}

			if (StringUtils.isNotBlank(inspectorId)) {
				idList.add(inspectorId);
			}

			if (StringUtils.isNotBlank(meterialerId)) {
				idList.add(meterialerId);
			}

			if (StringUtils.isNotBlank(costerId)) {
				idList.add(costerId);
			}
		Boolean b = idList.size() == new HashSet<Object>(idList).size();
		System.out.println("---------------"+idList.size()+b+new HashSet<Object>(idList).size()+"-----------");
		return b;
	}
	
	public Bidcompany getBidcompanyByWorkerId(String CorePersonId){
		return bidCompanyMapper.getBidcompanyByWorkerId(CorePersonId);
	};//通过人员id获取一条参投信息
	
	//根据bidcompany获取核心人员信息
	public List<CorePerson> getCorePersonByBidcompany(Bidcompany bidcompany){
		List<CorePerson> corePersonList = new ArrayList<CorePerson>();
		CorePerson constructoor = bidcompany.getConstructoor();    // 建造师名称
		if(constructoor!=null){
			constructoor = CorePersonService.get(constructoor);
			if(constructoor!=null){
				corePersonList.add(constructoor);
			}
		}
		
		
		CorePerson director = bidcompany.getDirector();        // 技术负责人
		if(director!=null){
			director = CorePersonService.get(director);
			if(director!=null){
				corePersonList.add(director);
			}
		}
		
		CorePerson saver = bidcompany.getSaver();           // 安全员名称
		if(saver!=null){
			saver = CorePersonService.get(saver);
			if(saver!=null){
				corePersonList.add(saver);
			}
		}
		
		CorePerson constrworker = bidcompany.getConstrworker();    // 施工员名称
		if(constrworker!=null){
			constrworker = CorePersonService.get(constrworker);
			if(constrworker!=null){
				corePersonList.add(constrworker);
			}
		}
		
		CorePerson inspector = bidcompany.getInspector();       // 质检员名称
		if(inspector!=null){
			inspector = CorePersonService.get(inspector);
			if(inspector!=null){
				corePersonList.add(inspector);
			}
		}
		
		CorePerson meterialer = bidcompany.getMeterialer();      // 材料员名称
		if(meterialer!=null){
			meterialer = CorePersonService.get(meterialer);
			if(meterialer!=null){
				corePersonList.add(meterialer);
			}
		}
		
		CorePerson coster = bidcompany.getCoster();          // 造价员名称
		if(coster!=null){
			coster = CorePersonService.get(coster);
			if(coster!=null){
				corePersonList.add(coster);
			}
		}
		
		return corePersonList;
	}
	
	//验证人员是否在有效项目中使用若是不可添加
//	public Integer validateCorePerson(Bidcompany bidcompany){
	public Map<Integer,String> validateCorePerson(Bidcompany bidcompany){
		
		List<Bidcompany> bidcompanyList = new ArrayList<Bidcompany>();
		String constructorId = ""; // 建造师
		String directorId = ""; // 技术负责人
		String saverId = ""; // 安全员
		String constrworkerId = ""; // 施工员
		String inspectorId = ""; // 质检员

		String meterialerId = ""; // 材料员
		String costerId = ""; // 造价员
		CorePerson constructoor = bidcompany.getConstructoor();    // 建造师名称
		Program program = new Program(); 
		Map<Integer,String> statusMap = new HashMap<Integer,String>();
		Integer status = -1;
		if(constructoor!=null){
			constructorId = constructoor.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(constructorId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(2, program.getProgramName());
						}else if(status == 3){
							statusMap.put(3, program.getProgramName());
						}else{
							statusMap.put(0, "");
						}
					}
				}
			}
			
		}
		
		
		CorePerson director = bidcompany.getDirector();        // 技术负责人
		if(director!=null){
			directorId = director.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(directorId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(12, program.getProgramName());
						}else if(status == 3){
							statusMap.put(13, program.getProgramName());
						}else{
							statusMap.put(-1, "");
						}
					}
				}
			}
		}
		
		CorePerson saver = bidcompany.getSaver();           // 安全员名称
		if(saver!=null){
			saverId = saver.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(saverId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(22, program.getProgramName());
						}else if(status == 3){
							statusMap.put(23, program.getProgramName());
						}else{
							statusMap.put(-2, "");
						}
					}
				}
			}
		}
		
		CorePerson constrworker = bidcompany.getConstrworker();    // 施工员名称
		if(constrworker!=null){
			constrworkerId = constrworker.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(constrworkerId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(32, program.getProgramName());
						}else if(status == 3){
							statusMap.put(33, program.getProgramName());
						}else{
							statusMap.put(-3, "");
						}
					}
				}
			}
		}
		
		CorePerson inspector = bidcompany.getInspector();       // 质检员名称
		if(inspector!=null){
			inspectorId = inspector.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(inspectorId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(42, program.getProgramName());
						}else if(status == 3){
							statusMap.put(43, program.getProgramName());
						}else{
							statusMap.put(-4, "");
						}
					}
				}
			}
		}
		
		CorePerson meterialer = bidcompany.getMeterialer();      // 材料员名称
		if(meterialer!=null){
			meterialerId = meterialer.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(meterialerId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(52, program.getProgramName());
						}else if(status == 3){
							statusMap.put(53, program.getProgramName());
						}else{
							statusMap.put(-5, "");
						}
					}
				}
			}
		}
		
		CorePerson coster = bidcompany.getCoster();          // 造价员名称
		if(coster!=null){
			costerId = coster.getId();
			bidcompanyList = bidCompanyMapper.validateWorkerByCorePersonId(costerId);
			if(bidcompanyList != null && bidcompanyList.size()>0){
				for(int i=0;i<bidcompanyList.size();i++){
					program = bidcompanyList.get(i).getProgram();
					if(program != null){
						status = program.getStatus();
						if(status == 2 ){//施工或停工返回状态
							statusMap.put(62, program.getProgramName());
						}else if(status == 3){
							statusMap.put(63, program.getProgramName());
						}else{
							statusMap.put(-6, "");
						}
					}
				}
			}
		}
		return statusMap;
	}
	
	//传入参投单位集合，验证集合中的项目状态
	
}