package co.dc.ccpt.modules.oa.utils;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Jacob {
	public static void main(String args[]) {
		Jacob j = new Jacob();
		String wordFile = "D:\\jzerp_files\\userfiles\\8f5459e5ed3b477785212de06a9c9add\\files\\contractTextManage\\2018\\07\\JZERP-RE-IN-01-023 评标报告台积电定向钻.doc";//原word文档路径
		String pdfFile = wordFile.substring(0, wordFile.lastIndexOf(".")) + ".pdf"; // 转换后的路径
		j.exchangeWordToPdf(wordFile,pdfFile);
	}
	/**
	 * 将word转换成pdf
	 */
	public String exchangeWordToPdf(String wordFile, String pdfFile){
		ActiveXComponent app = null;
		System.out.println("开始转换...");
		// 开始时间
		long start = System.currentTimeMillis();
		try {
			// 打开word
			app = new ActiveXComponent("Word.Application");
			// 设置word不可见,很多博客下面这里都写了这一句话，其实是没有必要的，因为默认就是不可见的，如果设置可见就是会打开一个word文档，对于转化为pdf明显是没有必要的
			// app.setProperty("Visible", false);
			// 获得word中所有打开的文档
			Dispatch documents = app.getProperty("Documents").toDispatch();
			System.out.println("打开文件: " + wordFile);
			// 打开文档
			Dispatch document = Dispatch.call(documents, "Open", wordFile, false, true).toDispatch();
			// 如果文件存在的话，不会覆盖，会直接报错，所以我们需要判断文件是否存在
			File target = new File(pdfFile);
			if (target.exists()) {
				target.delete();
			}
			System.out.println("另存为: " + pdfFile);
			// 另存为，将文档报错为pdf，其中word保存为pdf的格式宏的值是17
			Dispatch.call(document, "SaveAs", pdfFile, 17);
			// 关闭文档
			Dispatch.call(document, "Close", false);
			// 结束时间
			long end = System.currentTimeMillis();
			System.out.println("转换成功，用时：" + (end - start) + "ms");
		} catch (Exception e) {
			System.out.println("转换失败" + e.getMessage());
		} finally {
			// 关闭office
			app.invoke("Quit", 0);
		}
		return pdfFile;
	}
}
