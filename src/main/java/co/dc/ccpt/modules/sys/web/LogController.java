/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import co.dc.ccpt.common.json.AjaxJson;
import co.dc.ccpt.core.persistence.Page;
import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.sys.entity.Log;
import co.dc.ccpt.modules.sys.service.LogService;

/**
 * 日志Controller
 * @author dckj
 * @version 2016-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@RequiresPermissions("sys:log:list")
	@RequestMapping(value = {"list", ""})
	public String list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/log/logList";
	}

	@ResponseBody
	@RequiresPermissions("sys:log:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log); 
		return super.getBootstrapData(page);
	}

	
	/**
	 * 批量删除
	 */
	@ResponseBody
	@RequiresPermissions("sys:log:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			logService.delete(logService.get(id));
		}
		j.setSuccess(true);
		j.setMsg("删除日志成功！");
		return j;
	}
	
	/**
	 * 批量删除
	 */
	@ResponseBody
	@RequiresPermissions("sys:log:del")
	@RequestMapping(value = "empty")
	public AjaxJson empty() {
		AjaxJson j = new AjaxJson();
		logService.empty();
		j.setSuccess(true);
		j.setMsg("清空日志成功!");
		return j;
	}
}
