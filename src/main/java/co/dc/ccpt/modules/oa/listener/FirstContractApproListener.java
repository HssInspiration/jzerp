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
/**
 * 启动审批节点监听器--启动成功设置下一节点的处理人FirstContractApproListener
 * @author Administrator
 *
 */
//@Component   
public class FirstContractApproListener implements TaskListener{
	private static final long serialVersionUID = 1L;
	/** 日志对象*/
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SystemService systemService; // 添加所需service
	@Override
	public void notify(DelegateTask delegateTask) {
		//设置代理候选人：
		/** 通过上下文加载器获得应用程序上下文的实例，而后获取spring中的注入类，实现监听器类可调用service层的方法 */
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		systemService = (SystemService) context.getBean("systemService");
		List<User> userList = new ArrayList<User>();
		List<String> userNameList = new ArrayList<String>();
		String roleId = "4768d8801d5d45329275d50909412f6f";//角色id--金卓总经理
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
//		logger.info("################进入代理人设置监听器###########");
//		String[] userName = {"sm001","wlm001"};
//		delegateTask.addCandidateUsers(Arrays.asList(userName));
	}
}
