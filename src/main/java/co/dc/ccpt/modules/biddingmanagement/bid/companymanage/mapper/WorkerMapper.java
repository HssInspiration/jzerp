/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.biddingmanagement.bid.companymanage.mapper;

import java.util.List;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.biddingmanagement.bid.companymanage.entity.Worker;

/**
 * 人员信息MAPPER接口
 * 
 * @author lxh
 * @version 2018-02-08
 */
@MyBatisMapper
public interface WorkerMapper extends BaseMapper<Worker> {
	List<Worker> listAllWorkers();
	List<Worker> getAllWorkerList(Worker worker);
}