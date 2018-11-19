package co.dc.ccpt.modules.oa.listener;

import java.util.ArrayList;
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
	private SystemService systemService;
	@Override
	public void notify(DelegateTask delegateTask) {
		//利用spring的ContextLoader获取对应的类，从而实现service层的可用性
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		systemService = (SystemService)context.getBean("systemService");
//		String pass = (String)delegateTask.getVariable("pass");
		Map<String,Object> variables = new HashMap<String,Object>();
//		variables.put("pass", Boolean.parseBoolean(pass));
//		获取总公司四个审批人员（经营部负责人+工程部负责人+财务部负责人+结算部负责人）
		List<String> leadList = new ArrayList<String>();
		List<String> list = systemService.getPrimaryPersonByRoleId();
		if(list!=null && list.size()>0){
			for(String lead:list){
				leadList.add(lead);
			}
		}
		variables.put("leadList", leadList);
		delegateTask.setVariables(variables);//设置流程变量
	}
}
