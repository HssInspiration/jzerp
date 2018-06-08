/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.oa.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonFormat;
import co.dc.ccpt.common.utils.DateUtils;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.service.CrudService;
import co.dc.ccpt.modules.oa.entity.OaNotify;
import co.dc.ccpt.modules.oa.entity.OaNotifyRecord;
import co.dc.ccpt.modules.oa.mapper.OaNotifyMapper;
import co.dc.ccpt.modules.oa.mapper.OaNotifyRecordMapper;

/**
 * 通知通告Service
 * @author dckj
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class OaNotifyService extends CrudService<OaNotifyMapper, OaNotify> {

	@Autowired
	private OaNotifyRecordMapper oaNotifyRecordMapper;

	public OaNotify get(String id) {
		OaNotify entity = mapper.get(id);
		return entity;
	}
	
	/**
	 * 获取通知发送记录
	 * @param oaNotify
	 * @return
	 */
	public OaNotify getRecordList(OaNotify oaNotify) {
		oaNotify.setOaNotifyRecordList(oaNotifyRecordMapper.findList(new OaNotifyRecord(oaNotify)));
		return oaNotify;
	}
	
	public Page<OaNotify> find(Page<OaNotify> page, OaNotify oaNotify) {
		oaNotify.setPage(page);
		page.setList(mapper.findList(oaNotify));
		return page;
	}
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify) {
		return mapper.findCount(oaNotify);
	}
	
	@Transactional(readOnly = false)
	public void save(OaNotify oaNotify) {
		super.save(oaNotify);
		
		// 更新发送接受人记录
		oaNotifyRecordMapper.deleteByOaNotifyId(oaNotify.getId());
		if (oaNotify.getOaNotifyRecordList().size() > 0){
			oaNotifyRecordMapper.insertAll(oaNotify.getOaNotifyRecordList());
		}
	}
	
	/**
	 * 更新阅读状态
	 */
	@Transactional(readOnly = false)
	public void updateReadFlag(OaNotify oaNotify) {
		OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
		oaNotifyRecord.setUser(oaNotifyRecord.getCurrentUser());
		oaNotifyRecord.setReadDate(new Date());
		oaNotifyRecord.setReadFlag("1");
		oaNotifyRecordMapper.update(oaNotifyRecord);
	}
}