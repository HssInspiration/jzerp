package co.dc.ccpt.modules.contractmanagement.contractStatistics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.contractmanagement.contractStatistics.entity.ContractStatistics;
import co.dc.ccpt.modules.contractmanagement.contractStatistics.mapper.ContractStatisticsMapper;

/**
 * 合同统计Service
 * @author lxh
 * @version 2018-05-03
 */
@Service
@Transactional(readOnly = true)
public class ContractStatisticsService extends CrudService<ContractStatisticsMapper, ContractStatistics> {
	
	@Autowired
	public ContractStatisticsMapper ContractStatisticsMapper;
	
	public ContractStatistics get(String id) {
		return super.get(id);
	}
	
	public List<ContractStatistics> findList(ContractStatistics contractStatistics) {
		return super.findList(contractStatistics);
	}
	
	public Page<ContractStatistics> findPage(Page<ContractStatistics> page, ContractStatistics contractStatistics) {
		return super.findPage(page, contractStatistics);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractStatistics contractStatistics) {
		super.save(contractStatistics);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractStatistics contractStatistics) {
		super.delete(contractStatistics);
	}
	
}
