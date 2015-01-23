package myclass.util;


/**
 * 比較が便利になる
 * @author yuki
 *
 */
public class Compare {

    /**
     * 二つの数値を比較して 小さい方を返す
     *
     * @param x
     * @param y
     * @return
     */
    public final static int small(int x, int y) {
        return x < y ? x : y;
    }

    /**
     * 二つの数値を比較して 大きい方を返す
     *
     * @param x
     * @param y
     * @return
     */
    public final static int big(int x, int y) {
        return x > y ? x : y;
    }

    /**
     * 配列の中で一番小さい値を返す
     *
     * @param x
     * @return
     */
    public final static int small(int[] x) {
        int s = Integer.MAX_VALUE;
        for (int i = 0, l = x.length; i < l; ++i) {
            s = small(s, x[i]);
        }
        return s;
    }

    /**
     * 配列の中で一番大きい値を返す
     *
     * @param x
     * @return
     */
    public final static int big(int[] x) {
        int s = Integer.MIN_VALUE;
        for (int i = 0, l = x.length; i < l; ++i) {
            s = big(s, x[i]);
        }
        return s;
    }

    /**
     * 文字列がnullか空の場合true
     * 
     * @param s
     * @return
     */
    public final static boolean isEmpty(String s) {
        return (s == null) || s.isEmpty();
    }

    /**
     * すべての文字列がnullか空の場合true
     * 
     * @param ss
     * @return
     */
    public static boolean isAllEmpty(String... ss) {
        for (int i = 0, len = ss.length; i < len; ++i) {
            if (isEmpty(ss[i]))
                continue;
            return false;
        }
        return true;
    }

    /**
     * すべての文字列のひとつでも空かnullの場合true
     * 
     * @param ss
     * @return
     */
    public static boolean isAnyEmpty(String... ss) {
        for (int i = 0, len = ss.length; i < len; ++i) {
            if (isEmpty(ss[i]))
                return true;
        }
        return false;
    }

    /**
     * 文字列の長さがmaxより大きい場合true
     * 
     * @param s
     * @param max
     * @return
     */
    public static boolean isOverLength(int max, String s) {
        return s.length() > max;
    }

    /**
     * すべての文字列の長さがmaxより大きい場合true
     * 
     * @param max
     * @param ss
     * @return
     */
    public static boolean isAllOverLength(int max, String... ss) {
        for (int i = 0, len = ss.length; i < len; ++i) {
            if (ss[i].length() > max) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 文字列の長さが一つでもmaxより大きい場合true
     * 
     * @param max
     * @param ss
     * @return
     */
    public static boolean isAnyOverLength(int max, String... ss) {
        for (int i = 0, len = ss.length; i < len; ++i) {
            if (ss[i].length() > max)
                return true;
        }
        return false;
    }

    /**
     * 文字列がその正規表現と一致するか
     * 
     * @param str
     * @param regex
     * @return
     */
    public static boolean isMatches(String regex, String str) {
        return str.matches(regex);
    }

    /**
     * すべての文字列が正規表現とマッチするか
     * 
     * @param regex
     * @param ss
     * @return
     */
    public static boolean isAllMatches(String regex, String... ss) {
        for (int i = 0, len = ss.length; i < len; ++i) {
            if (ss[i].matches(regex)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * いずれかの文字列が正規表現とマッチするか
     * 
     * @param regex
     * @param ss
     * @return
     */
    public static boolean isAnyMatches(String regex, String... ss) {
        for (int i = 0, len = ss.length; i < len; ++i) {
            if (ss[i].matches(regex))
                return true;
        }
        return false;
    }

}
