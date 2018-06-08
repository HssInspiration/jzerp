/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.iim.mapper;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.iim.entity.MailCompose;

/**
 * 发件箱MAPPER接口
 * @author dckj
 * @version 2015-11-15
 */
@MyBatisMapper
public interface MailComposeMapper extends BaseMapper<MailCompose> {
	public int getCount(MailCompose entity);
}