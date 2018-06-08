/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.test.mapper.note;

import co.dc.ccpt.core.persistence.BaseMapper;
import co.dc.ccpt.core.persistence.annotation.MyBatisMapper;
import co.dc.ccpt.modules.test.entity.note.TestNote;

/**
 * 富文本测试MAPPER接口
 * @author liugf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface TestNoteMapper extends BaseMapper<TestNote> {
	
}