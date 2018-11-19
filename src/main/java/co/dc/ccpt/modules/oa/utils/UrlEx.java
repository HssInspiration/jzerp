package co.dc.ccpt.modules.oa.utils;

import java.io.UnsupportedEncodingException;

public class UrlEx {
	private final static String ENCODE = "utf-8"; 
    /**
     * URL 解码
     * @return String
     */
    public String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
