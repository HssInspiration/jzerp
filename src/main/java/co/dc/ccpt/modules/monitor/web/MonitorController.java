package co.dc.ccpt.modules.monitor.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import co.dc.ccpt.core.web.BaseController;
import co.dc.ccpt.modules.monitor.utils.SystemInfo;


/**
 * 系统监控Controller
 * @author liugf
 * @version 2016-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/monitor")
public class MonitorController extends BaseController {
	@RequestMapping("info")
	public String info(Model model) throws Exception {
		model.addAttribute("systemInfo", SystemInfo.SystemProperty());
		return  "modules/monitor/jvm/systemInfo";
	}
	
}