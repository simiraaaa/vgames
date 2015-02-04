package myclass.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myclass.wrap.MyHashMap;

/**
 * JSONが扱える
 *
 */
public class JSON {

    private static final char//
            COLON = ':',
            MINUS = '-',//
            DOT = '.',//
            COMMA = ',',//
            SPACE = ' ',//
            DB_QUOT = '"',//
            BACK = '\\',//
            TRUE = 't',//
            FALSE = 'f',//
            NULL = 'n',//
            OBJECT_START = '{',//
            OBJECT_END = '}',//
            ARRAY_START = '[',//
            ARRAY_END = ']';

    private static final String//
            LONG_ARRAY = getClassNameLast(long[].class),//
            INT_ARRAY = getClassNameLast(int[].class),//
            BYTE_ARRAY = getClassNameLast(byte[].class),//
            CHAR_ARRAY = getClassNameLast(char[].class),//
            SHORT_ARRAY = getClassNameLast(short[].class),//
            BOOL_ARRAY = getClassNameLast(boolean[].class),//
            DOUBLE_ARRAY = getClassNameLast(double[].class),//
            FLOAT_ARRAY = getClassNameLast(float[].class),//
            OBJECT_ARRAY = getClassNameLast(Object[].class),//
            ARRAY_TWO = "[[";

    private static final HashMap<Character, Character> ESCAPE_SEQ_LIST = MyHashMap.puts(new HashMap<Character, Character>(), //
            't', EscapeSequence.TAB,//
            'b', EscapeSequence.BS,//
            'n', EscapeSequence.LF,//
            'r', EscapeSequence.CR,//
            'f', EscapeSequence.F//
    );

    private static <T> String getClassNameLast(Class<T> c) {
        String name = c.getName();
        return name.substring(name.length() - 1);
    }

    /**
     * JSON文字列のエンコード
     * 
     * @param s
     *            {"x":0,"text":"text"}のようなもの
     * @return
     */
    public static String toUnicode(final String s) {
        byte status = 0;
        int start = 0;
        final int//
        OUT = 0, //
        IN = 1, //
        ESC = 2;
        StringBuffer sb = new StringBuffer(s);
        int len = sb.length();
        for (int i = 0; i < len; ++i) {
            final char c = sb.charAt(i);
            switch (status) {
            case OUT:
                if (c == DB_QUOT) {
                    status = IN;
                    start = i + 1;
                }
                break;
            case IN:
                switch (c) {
                case DB_QUOT:
                    final String before = sb.substring(start, i);
                    final String after = Convert.toUnicode(before, true);
                    sb.replace(start, i, after);
                    status = OUT;
                    int add = after.length() - before.length();
                    i += add;
                    len += add;
                    break;

                case BACK:
                    status = ESC;
                    break;
                }
                break;
            case ESC:
                if (c != BACK) {
                    status = IN;
                }
                break;
            }
        }
        return sb.toString();
    }

    /**
     * JSON文字列からJAVAオブジェクトを生成
     * 
     * @param s
     *            JSON
     * @param i
     *            検索を開始したいindex
     * @param len
     *            JSON文字列のlength
     * @return
     */
    private static Object parse(final String s, int[] i, final int len) {
        // if(i==null){i =new int[]{0};}

        for (; i[0] < len; ++i[0]) {
            final char c = s.charAt(i[0]);

            if (Base62.isNumber(c) || c == MINUS) {
                return parseNumber(s, i, len);
            }

            switch (c) {

            case DB_QUOT:
                return parseString(s, i, len);
            case TRUE:
                i[0] += 4;
                return true;
            case FALSE:
                i[0] += 5;
                return false;
            case NULL:
                i[0] += 4;
                return null;
            case ARRAY_START:
                return parseArray(s, i, len);
            case OBJECT_START:
                return parseObject(s, i, len);

            default:
                continue;
            }
        }

        return s;
    }

    private static Object parseNumber(final String s, int[] i, final int len) {
        // if(i==null){i =new int[]{0};}
        final int start = i[0]++;
        boolean isFloat = false;
        for (; i[0] < len; ++i[0]) {
            final char c = s.charAt(i[0]);
            if (!isFloat && c == DOT) {
                isFloat = true;
                continue;
            }
            if (c == SPACE || c == COMMA || c == ARRAY_END || c == OBJECT_END) {
                if (isFloat) {
                    return Float.parseFloat(s.substring(start, i[0]));
                } else {
                    return Integer.parseInt(s.substring(start, i[0]));
                }
                // return (isFloat) ? Float.parseFloat(s.substring(start, i[0]))
                // : Integer.parseInt(s.substring(start, i[0])) ;
            }
        }
        if (isFloat) {
            return Float.parseFloat(s.substring(start, i[0]));
        } else {
            return Integer.parseInt(s.substring(start, i[0]));
        }
        // return (isFloat ? Float.parseFloat(s.substring(start, i[0])) :
        // Integer.parseInt(s.substring(start, i[0])) );
    }

    private static String parseString(final String s, int[] i, final int len) {
        // if(i==null){i =new int[]{0};}
        StringBuffer sb = new StringBuffer();
        ++i[0];
        for (; i[0] < len; ++i[0]) {
            final char c = s.charAt(i[0]);
            switch (c) {
            case BACK:
                char c2 = s.charAt(++i[0]);
                Character seq = ESCAPE_SEQ_LIST.get(c2);
                if (seq == null) {
                    if (c2 == 'u') {
                        char hex = (char) Integer.parseInt(s.substring(++i[0], i[0] + 4), 16);
                        sb.append(hex);
                        i[0] += 3;
                    } else {
                        sb.append(c2);
                    }
                } else {
                    sb.append(seq);
                }
                continue;
            case DB_QUOT:
                ++i[0];
                return sb.toString();
            default:
                sb.append(c);
                continue;
            }
        }
        throw new RuntimeException("JSON:StringFormatException");
    }

    /**
     * JSON文字列からJAVAオブジェクトを生成
     * 
     * @param s
     *            JSON文字列
     * @return parseされたJAVAオブジェクト
     */
    public static Object parse(final String s) {
        return parse(s, new int[] { 0 }, s.length());
    }

    private static HashMap<String, Object> parseObject(final String s, int[] i, final int len) {
        // if(i==null){i =new int[]{0};}
        ++i[0];
        HashMap<String, Object> o = new HashMap<String, Object>();
        if (isEmptyObject(s, i, len)) {
            return o;
        }

        do {
            o.put(parseString(s, i, len), parse(s, addIndex(i), len));
        } while (hasNextKey(s, i, len));

        return o;
    }

    private static ArrayList<Object> parseArray(final String s, int[] i, final int len) {
        // if(i==null){i =new int[]{0};}
        ++i[0];
        ArrayList<Object> array = new ArrayList<Object>();
        if (isEmptyArray(s, i, len)) {
            return array;
        }

        do {
            array.add(parse(s, i, len));
        } while (hasNextElement(s, i, len));

        return array;

    }

    private static boolean isEmptyObject(final String s, int[] i, final int len) {
        int copy = i[0];
        while (copy < len) {
            switch (s.charAt(copy++)) {

            case DB_QUOT:
                return false;
            case OBJECT_END:
                i[0] = copy;
                return true;

            default:
                continue;
            }
        }

        throw new RuntimeException("JSON:ObjectFormatException");
    }

    private static boolean isEmptyArray(final String s, int[] i, final int len) {
        int copy = i[0];

        while (copy < len) {
            final char c = s.charAt(copy++);
            if (Base62.isNumber(c) || c == MINUS || c == DB_QUOT || c == ARRAY_START || c == OBJECT_START || c == TRUE || c == FALSE || c == NULL) {
                return false;
            } else if (c == ARRAY_END) {
                i[0] = copy;
                return true;
            }
        }

        throw new RuntimeException("JSON:ArrayFormatException");
    }

    private static boolean hasNextKey(final String s, int[] i, final int len) {
        while (i[0] < len) {
            switch (s.charAt(i[0]++)) {
            case COMMA:
                return true;
            case OBJECT_END:
                return false;
            default:
                continue;
            }
        }
        throw new RuntimeException("JSON:ObjectFormatException");
    }

    private static boolean hasNextElement(final String s, int[] i, final int len) {
        while (i[0] < len) {
            switch (s.charAt(i[0]++)) {
            case COMMA:
                return true;
            case ARRAY_END:
                return false;
            default:
                continue;
            }
        }
        throw new RuntimeException("JSON:ArrayFormatException");
    }

    private static boolean isPrimitive(Object o) {
        return o instanceof Integer || o instanceof Boolean || o instanceof Long || o instanceof Byte || o instanceof Short || o instanceof Float || o instanceof Double;
    }

    private static StringBuffer stringify(String s, StringBuffer sb) {
        return sb.append(DB_QUOT + Convert.toUnicode(s, true) + DB_QUOT);
    }

    private static StringBuffer stringify(Object[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            stringify((Object) ar[i], sb);
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(List<Object> ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.size(); i < len; ++i) {
            stringify((Object) ar.get(i), sb);
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(Map<String, Object> map, StringBuffer sb) {
        sb.append(OBJECT_START);
        int size = map.size();
        for (String key : map.keySet()) {
            stringify(key, sb).append(COLON);
            stringify((Object) map.get(key), sb);
            if (--size > 0) {
                sb.append(COMMA);
            }
        }
        return sb.append(OBJECT_END);
    }

    private static StringBuffer stringify(int[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(long[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(byte[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(short[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(boolean[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(double[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(float[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            sb.append(String.valueOf(ar[i]));
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(char[] ar, StringBuffer sb) {
        sb.append(ARRAY_START);
        for (int i = 0, len = ar.length; i < len; ++i) {
            stringify(String.valueOf(ar[i]), sb);
            if (i < len - 1) {
                sb.append(COMMA);
            }
        }
        return sb.append(ARRAY_END);
    }

    private static StringBuffer stringify(Object o, StringBuffer sb) {
        if (o == null) {
            return sb.append("null");
        }
        if (isPrimitive(o)) {
            return sb.append(String.valueOf(o));
        }
        if (o instanceof String) {
            return stringify((String) o, sb);
        }
        if (o instanceof Map) {
            return stringify((Map<String, Object>) o, sb);
        }
        if (o instanceof List) {
            return stringify((List<Object>) o, sb);
        }
        if (o instanceof Character) {
            return stringify(String.valueOf(o), sb);
        }
        if (o.getClass().isArray()) {
            String s = o.getClass().getName();
            if (s.endsWith(OBJECT_ARRAY) || s.contains(ARRAY_TWO)) {
                return stringify((Object[]) o, sb);
            }
            if (s.endsWith(INT_ARRAY)) {
                return stringify((int[]) o, sb);
            }
            if (s.endsWith(BOOL_ARRAY)) {
                return stringify((boolean[]) o, sb);
            }
            if (s.endsWith(FLOAT_ARRAY)) {
                return stringify((float[]) o, sb);
            }
            if (s.endsWith(DOUBLE_ARRAY)) {
                return stringify((double[]) o, sb);
            }
            if (s.endsWith(BYTE_ARRAY)) {
                return stringify((byte[]) o, sb);
            }
            if (s.endsWith(LONG_ARRAY)) {
                return stringify((long[]) o, sb);
            }
            if (s.endsWith(SHORT_ARRAY)) {
                return stringify((short[]) o, sb);
            }
            if (s.endsWith(CHAR_ARRAY)) {
                return stringify((char[]) o, sb);
            }
        }
        return stringify(String.valueOf(o), sb);
    }

    /**
     * JAVAのオブジェクトからJSON文字列を生成<br>
     * 型の制限<br>
     * 以下の型以外は全てString.valueOf(o)され<br>
     * 文字列として扱われます。<br>
     * プリミティブ型<br>
     * プリミティブ型のラッパークラス<br>
     * String<br>
     * List<br>
     * Map<br>
     * 各種配列<br>
     * 
     * @param o
     * @return
     */
    public static String stringify(Object o) {
        return stringify(o, new StringBuffer()).toString();
    }

    private static int[] addIndex(int[] i) {
        ++i[0];
        return i;
    }

    // private static int[] addIndex(int[] i, int add) {
    // i[0] += add;
    // return i;
    // }

}
