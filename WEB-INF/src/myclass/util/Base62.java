package myclass.util;

public class Base62 {
    public static final char //
            q = 'q',//
            w = 'w',//
            e = 'e',//
            r = 'r',//
            t = 't',//
            y = 'y',//
            u = 'u',//
            i = 'i',//
            o = 'o',//
            p = 'p',//
            a = 'a',//
            s = 's',//
            d = 'd',//
            f = 'f',//
            g = 'g',//
            h = 'h',//
            j = 'j',//
            k = 'k',//
            l = 'l',//
            z = 'z',//
            x = 'x',//
            c = 'c',//
            v = 'v',//
            b = 'b',//
            n = 'n',//
            m = 'm',//
            Q = 'Q',//
            W = 'W',//
            E = 'E',//
            R = 'R',//
            T = 'T',//
            Y = 'Y',//
            U = 'U',//
            I = 'I',//
            O = 'O',//
            P = 'P',//
            A = 'A',//
            S = 'S',//
            D = 'D',//
            F = 'F',//
            G = 'G',//
            H = 'H',//
            J = 'J',//
            K = 'K',//
            L = 'L',//
            Z = 'Z',//
            X = 'X',//
            C = 'C',//
            V = 'V',//
            B = 'B',//
            N = 'N',//
            M = 'M',//
            C1 = '1', //
            C2 = '2',//
            C3 = '3',//
            C4 = '4',//
            C5 = '5',//
            C6 = '6', //
            C7 = '7',//
            C8 = '8',//
            C9 = '9', //
            C0 = '0';

    /**
     * char型の[0-9]ならtrue
     * 
     * @param c
     * @return
     */
    public static boolean isNumber(char c) {
        return c == C1 || c == C2 || c == C3 || c == C4 || c == C5 || c == C6 || c == C7 || c == C8 || c == C9 || c == C0;
    }

    /**
     * char型の[a-z]ならtrue
     * 
     * @param c
     * @return
     */
    public static boolean isLowercase(char c) {
        return c == q || c == w || c == e || c == r || c == t || c == y || c == u || c == i || c == o || c == p || c == a || c == s || c == d || c == f || c == g || c == h || c == j || c == k || c == l || c == z || c == x || c == Base62.c || c == v || c == b || c == n || c == m;
    }

    /**
     * char型の[A-Z]ならtrue
     * 
     * @param c
     * @return
     */
    public static boolean isUppercase(char c) {
        return c == Q || c == W || c == E || c == R || c == T || c == Y || c == U || c == I || c == O || c == P || c == A || c == S || c == D || c == F || c == G || c == H || c == J || c == K || c == L || c == Z || c == X || c == C || c == V || c == B || c == N || c == M;
    }

    /**
     * char型の[a-zA-Z]ならtrue
     * 
     * @param c
     * @return
     */
    public static boolean isAlphabet(char c) {
        return isLowercase(c) || isUppercase(c);
    }

    /**
     * char型の[0-9a-zA-Z]ならtrue
     * 
     * @param c
     * @return
     */
    public static boolean isBase62(char c) {
        return isLowercase(c) || isUppercase(c) || isNumber(c);
    }

}
