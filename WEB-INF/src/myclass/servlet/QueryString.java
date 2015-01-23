package myclass.servlet;

import myclass.util.Convert;

public class QueryString {

    private static final char//
            EQUAL = '=',
            AMP = '&', //
            HATENA = '?';

    public static String stringify(boolean noHatena, String... ss) {
        StringBuffer sb = new StringBuffer();
        if (!noHatena) {
            sb.append(HATENA);
        }
        for (int i = 0, len = ss.length; i < len; ++i) {
            sb.append(Convert.encodeURL(ss[i])).append(EQUAL).append(Convert.encodeURL(ss[++i]));
            if (i < len - 1) {
                sb.append(AMP);
            }
        }
        return sb.toString();
    }


    /**
     * ?key=value&key2=value2<br>
     * という感じにする
     * 
     * @param ss
     * @return
     */
    public static String stringify(String... ss) {
        return stringify(false, ss);
    }
}
