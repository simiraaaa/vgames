package myclass;

import java.util.HashMap;
import java.util.Map;


public class MyCreateTag {

	/**
	 * タグを作る
	 * @param tagName
	 * @param prop nameとかvalue
	 * @param styles style="ここ"
	 * @param innerHTML <tagName>ここ</tagName>
	 * @return tag
	 */
	public static String createTag(final String tagName, Map<String,String> prop, Map<String,String> styles ,String innerHTML){
		StringBuffer tag=new StringBuffer().append("<"+tagName);
		if (prop!=null) {
			for(String k : prop.keySet()){
				tag.append(" "+k+"=\""+prop.get(k)+"\"");
			}
		}
		if(styles!=null){
			tag.append(" style=\"");
			for(String k : styles.keySet()){
				tag.append(" "+k+":"+styles.get(k)+";");
			}
			tag.append("\"");
		}
		tag.append(" >");
		if(innerHTML!=null){
			tag.append(innerHTML);
		}
		tag.append("</"+tagName+">");
		return tag.toString();
	}

	public static String createTag(final String tagName, Map<String,String> prop, Map<String,String> styles ) {
		return createTag(tagName, prop, styles ,null);
	}

	public static String createTag(final String tagName, Map<String,String> prop) {
		return createTag(tagName, prop, null ,null);
	}

	public static String createTag(final String tagName) {
		return createTag(tagName, null, null ,null);
	}

    /**
     * scriptタグを作る
     * 
     * @param src
     * @param prop
     * @return
     */
    public static String createScript(String src, Map<String, String> prop) {
        prop.put("src", src);
        prop.put("type", "text/javascript");
        return createTag("script", prop);
    }

    /**
     * スクリプトタグを作る
     */
    public static String createScript(String src) {
        return createScript(src, new HashMap<String, String>());
    }

    /**
     * スクリプトタグを複数作る
     * 
     * @param srcs
     * @return
     */
    public static String createScript(String... srcs) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = srcs.length; i < len; ++i) {
            sb.append(createScript(srcs[i]));
        }
        return sb.toString();
    }

}
