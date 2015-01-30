package myclass.html;

import java.util.HashMap;

/**
 * Tagのプロパティを作る
 *
 * @author yuki
 *
 */
public class Attribute extends StyleSheet {
    private static final char EQ = '=', DBQT = '"', SPACE = ' ';

    public Attribute() {
        map = new HashMap<>();
    }

    @Override
    public String create() {
        // TODO 自動生成されたメソッド・スタブ
        return create(map);
    }

    /**
     * k="v"にする
     *
     * @param map
     * @return
     */
    public static String create(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();

        map.forEach((k, v) -> {
            sb.append(SPACE).append(k).append(EQ).append(DBQT).append(v).append(DBQT);
        });

        return sb.toString();
    }

}
