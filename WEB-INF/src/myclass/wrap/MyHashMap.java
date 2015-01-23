package myclass.wrap;

import java.util.HashMap;

/**
 * putとか楽にやる
 * @author yuki
 *
 */
public class MyHashMap {

    /**
     * mapにいろいろ をputsして返す。代入の必要はない
     *
     * @param map
     * @param os
     * @return
     */
    public static <K extends Object, V extends Object> HashMap<K, V> puts(HashMap<K, V> map,
            Object... os) {
        for (int i = 0, l = os.length; i < l;) {
            map.put((K) os[i++], (V) os[i++]);
        }
        return map;
    }

    /**
     * HashMap作って返す
     *
     * @param os
     * @return
     */
    public static <K extends Object, V extends Object> HashMap<K, V> create(Object... os) {
        return puts(new HashMap<K, V>(), os);
    }


}
