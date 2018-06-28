package co.dc.ccpt.modules.oa.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import co.dc.ccpt.modules.sys.service.SystemService;
/**
 * 分包合同会签节点创建时所用的监听器
 * @author Administrator
 *
 */
public class multiInstanceListener implements TaskListener{

	private static final long serialVersionUID = 1L;
//	private SystemService systemService;
	@Override
	public void notify(DelegateTask delegateTask) {
		//利用spring的ContextLoader获取对应的类，从而实现service层的可用性
//		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//		systemService = (SystemService)context.getBean("systemService");
		//获取启动节点的用户
//		String loginName = (String)delegateTask.getVariable("applyUserId");
		String pass = (String)delegateTask.getVariable("pass");
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("pass", Boolean.parseBoolean(pass));
		List<String> assigneeList = Arrays.asList("dcp001","syd001","qgc001","xsl001","lvwen001","sxm001","zj001");
		variables.put("assigneeList", assigneeList);
		delegateTask.setVariables(variables);//设置流程变量
	}

}
