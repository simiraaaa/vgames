package vgames.util;

public class CreateSessionKey {
    private static final int LONG_MAXLENGTH = 19;
    private static final String PADDING0 = "0000000000000000000";
    private static final char[] RANDOM_LIST = {
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'q',
            'w',
            'e',
            'r',
            't',
            'y',
            'u',
            'i',
            'o',
            'p',
            'a',
            's',
            'd',
            'f',
            'g',
            'h',
            'j',
            'k',
            'l',
            'z',
            'x',
            'c',
            'v',
            'b',
            'n',
            'm',
            'Q',
            'W',
            'E',
            'R',
            'T',
            'Y',
            'U',
            'I',
            'O',
            'P',
            'A',
            'S',
            'D',
            'F',
            'G',
            'H',
            'J',
            'K',
            'L',
            'Z',
            'X',
            'C',
            'V',
            'B',
            'N',
            'M'
    };

    public static String create(long id) {
        String leftKey = PADDING0 + id;
        StringBuffer sb = new StringBuffer(69);
        sb.append(leftKey.substring(leftKey.length() - LONG_MAXLENGTH));
        final int len = RANDOM_LIST.length;
        for (int i = 0; i < 50; ++i) {
            sb.append(RANDOM_LIST[(int) (Math.random() * len)]);
        }
        return sb.toString();
    }

}
