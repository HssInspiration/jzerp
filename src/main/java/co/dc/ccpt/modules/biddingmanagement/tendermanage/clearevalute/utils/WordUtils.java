package co.dc.ccpt.modules.biddingmanagement.tendermanage.clearevalute.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.dc.ccpt.common.utils.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

//import com.google.common.collect.Lists;

 
@SuppressWarnings("deprecation")
public class WordUtils {
     //配置信息,代码本身写的还是很可读的,就不过多注解了  
    private static Configuration configuration = null;  
    //这里注意的是利用WordUtils的类加载器动态获得模板文件的位置  
    private static final String templateFolder = WordUtils.class.getClassLoader().getResource("../../").getPath() + "WEB-INF/temp/";  
    private static final String templateFolder1 =WordUtils.class.getClassLoader().getResource("../../").toString();
    private static final String templateFolder2 = Thread.currentThread().getContextClassLoader().getResource("").toString();
    private static final String templateFolder3 = new File("").getAbsolutePath();
    private static final String templateFolder4 = System.getProperty("user.dir");
//    private static final String templateFolder = "D:/NewLoad/BackUpJzerp/src/main/webapp/WEB-INF/temp";  
    		
    static {  
        configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");  
        try {  
            configuration.setDirectoryForTemplateLoading(new File(templateFolder)); 
            System.out.println("--------------------"+templateFolder);
            System.out.println("--------------------1"+templateFolder1);
            System.out.println("--------------------1"+templateFolder2);
            System.out.println("--------------------1"+templateFolder3);
            System.out.println("--------------------1"+templateFolder4);
        } catch (IOException e) { 
        	System.out.println("--------------------"+templateFolder);
        	System.out.println("--------------------2"+templateFolder1);
        	System.out.println("--------------------2"+templateFolder2);
            System.out.println("--------------------2"+templateFolder3);
            System.out.println("--------------------2"+templateFolder4);
            e.printStackTrace();  
        }  
   }  
    private WordUtils() {  
        throw new AssertionError();  
    }  
  
    public static void exportMillCertificateWord(HttpServletRequest request, HttpServletResponse response, Map<?, ?> map,String title,String ftlFile,int status) throws IOException {  
        Template freemarkerTemplate = configuration.getTemplate(ftlFile);  
        File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 调用工具类的createDoc方法生成Word文档  
            file = createDoc(map,freemarkerTemplate);  
            fin = new FileInputStream(file);  
            response.setCharacterEncoding("utf-8");  
            response.setContentType("application/msword");  
            // 设置浏览器以下载的方式处理该文件名  
            String fileName = title+DateUtils.getDate("yyyyMMddHHmmss")+ (status==0?".doc":".xls");  
            response.setHeader("Content-Disposition", "attachment;filename="  
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));  
  
            out = response.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } finally {  
            if(fin != null) fin.close();  
            if(out != null) out.close();  
            if(file != null) file.delete(); // 删除临时文件  
        }  
    }  
    
    private static File createDoc(Map<?, ?> dataMap, Template template) {  
        String name =  "tempReport.doc";  
        File f = new File(name);  
        Template t = template;  
        try {  
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");  
            t.process(dataMap, w);  
            w.close();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            throw new RuntimeException(ex);  
        }  
        return f;  
    }  
    
    public static void main(String[] args) {
    	WordUtils test = new WordUtils();    
        test.createWord(); 
	}
    
    public void createWord(){    
        Map<String,Object> dataMap=new HashMap<String,Object>();    
        getData(dataMap);    
        try {//FTL文件所存在的位置  
			configuration.setDirectoryForTemplateLoading(new File(templateFolder));
		} catch (IOException e2) {
			e2.printStackTrace();
		}     
        Template t=null;    
        try {    
            t = configuration.getTemplate("tempBidtab.ftl"); //文件名    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        File outFile = new File("D:/test"+Math.random()*10000+".xls");  //生成文件的路径  
        Writer out = null;    
        try {    
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));    
        } catch (FileNotFoundException e1) {    
            e1.printStackTrace();    
        }    
             
        try {    
            t.process(dataMap, out);    
        } catch (TemplateException e) {    
            e.printStackTrace();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
    }    
    
    //这里赋值的时候需要注意,xml中需要的数据你必须提供给它,不然会报找不到某元素错的.  
    private void getData(Map<String, Object> dataMap) {    
        dataMap.put("programName", "testTemp");    
        dataMap.put("programCont", "管道穿越");    
        dataMap.put("openDate", "2018-08-15");    
        dataMap.put("openAddr", "江苏南京");    
        dataMap.put("openMeterials", "标准标书一份");    
        dataMap.put("evaluateMethod", "择优录取");    
        dataMap.put("ctrlPrice", "150");    
        dataMap.put("floorPrice", "200");    
        dataMap.put("provisionPrice", "250");    
        	
        List<Map<String,Object>> bidContList = new ArrayList<Map<String,Object>>();    
        for (int i = 0; i < 10; i++) {    
            Map<String,Object> map = new HashMap<String,Object>();    
            map.put("num", i);    
            map.put("bidCompanyName", "金卓"+i);    
            map.put("bidPrice", 150+i);    
            map.put("buildDate",10+i);    
            map.put("quality", "合格");    
            map.put("isBid", "是");    
            bidContList.add(map);    
        }    
        dataMap.put("bidContList", bidContList);    
    }  
}
