package co.dc.ccpt.modules.oa.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;

public class StartContractListener implements TaskListener {
	private static final long serialVersionUID = 1L;
	@Autowired
	private SystemService systemService; // 添加所需service的私有成员
	/** 日志对象 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void notify(DelegateTask delegateTask) {
		// 设置代理人
		// delegateTask.setAssignee("tzy001");

		/** 通过上下文加载器获得应用程序上下文的实例，而后获取spring中的注入类，实现监听器类可调用service层的方法 */
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		systemService = (SystemService) context.getBean("systemService");
		List<User> userList = new ArrayList<User>();
		List<String> userNameList = new ArrayList<String>();
		String roleId = "7092829b903f404f8357956612f1aeef";//角色id--金卓经营部负责人
		userList = systemService.getUserListByRoleId(roleId);
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				userNameList.add(user.getLoginName());
			}
			delegateTask.addCandidateUsers(userNameList);
			logger.info("################代理人设置成功###########");
		} else {
			delegateTask.addCandidateUsers(userNameList);
			logger.info("################代理人设置失败###########");
		}
		for (String str : userNameList) {
			System.out.println("################" + str + "################");
		}
	}
}
