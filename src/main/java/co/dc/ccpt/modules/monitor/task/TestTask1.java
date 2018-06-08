package co.dc.ccpt.modules.monitor.task;

import org.quartz.DisallowConcurrentExecution;

import co.dc.ccpt.modules.monitor.entity.Task;

@DisallowConcurrentExecution  
public class TestTask1 extends Task{

	@Override
	public void run() {
		System.out.println("这是另一个测试任务TestTask1。");
		
	}

}
