/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Worker;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.mapper.WorkerMapper;

/**
 * 人员信息Service
 * @author lxh
 * @version 2018-02-08
 */
@Service
@Transactional(readOnly = true)
public class WorkerService extends CrudService<WorkerMapper, Worker> {
	@Autowired
	private WorkerMapper workerMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly = false)
	public List<Worker> listAllWorkers(){
		return workerMapper.listAllWorkers();
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly = false)
	public List<Worker> getAllWorkerList(Worker worker){
		return workerMapper.getAllWorkerList(worker);
	}
	
	public Worker get(String id) {
		return super.get(id);
	}
	
	public List<Worker> findList(Worker worker) {
		return super.findList(worker);
	}
	
	public Page<Worker> findPage(Page<Worker> page, Worker worker) {
		return super.findPage(page, worker);
	}
	
	@Transactional(readOnly = false)
	public void save(Worker worker) {
		super.save(worker);
	}
	
	@Transactional(readOnly = false)
	public void delete(Worker worker) {
		super.delete(worker);
	}
	
}