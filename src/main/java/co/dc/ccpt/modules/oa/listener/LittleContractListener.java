package co.dc.ccpt.modules.oa.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import co.dc.ccpt.modules.sys.entity.Office;
import co.dc.ccpt.modules.sys.entity.User;
import co.dc.ccpt.modules.sys.service.SystemService;

public class LittleContractListener implements TaskListener {
	private static final long serialVersionUID = 1L;
	/** 日志对象 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SystemService systemService; // 添加所需service
//	@Autowired
//	private OfficeService officeService; // 添加所需service

	@Override
	public void notify(DelegateTask delegateTask) {
		/**通过上下文加载器获得应用程序上下文的实例，而后获取spring中的注入类，实现监听器类可调用service层的方法*/
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		systemService = (SystemService) context.getBean("systemService");
//		officeService = (OfficeService) context.getBean("officeService");
		// 获取启动人的标识
		String loginName = (String)delegateTask.getVariable("applyUserId");
		User user = systemService.getUserByLoginName(loginName);
		if(user != null){// 获取用户的所属机构id
//			String userId = user.toString();
//			user = systemService.getOnlyOneUser(userId);
			Office office = user.getOffice();
			if(office != null){
				String officeId = office.getId();
				if(officeId != null && !officeId.equals("")){
					User primaryPerson = systemService.getPrimaryPersonById(officeId);//获取分公司负责人
					if(primaryPerson != null){
						delegateTask.addCandidateUser(primaryPerson.getLoginName());
					}
				}
			}
		}
	}
}
