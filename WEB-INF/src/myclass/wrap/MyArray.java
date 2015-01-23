package myclass.wrap;

import java.util.ArrayList;

import myclass.util.Compare;
/**
 * ArrayList、配列操作を楽にする
 * @author yuki
 *
 */
public class MyArray {

    public final static <E> ArrayList<E> adds(final ArrayList<E> arr, final E... os) {
        for (int i = 0, l = os.length; i < l; ++i) {
            arr.add(os[i]);
        }
        return arr;
    }

    public final static <E> ArrayList<E> create(final E... os) {
        return adds(new ArrayList<E>(), os);
    }


        /**
     * ArrayListを結合
     * 
     * @param arr
     * @param s
     * @return
     */
    public static <E> String join(ArrayList<E> arr, final String s) {
        StringBuffer joined = new StringBuffer();
        for (int i = 0, l = arr.size(); i < l;) {
            joined.append(arr.get(i++));
            if (i < l) {
                joined.append(s);
            }
        }
        return joined.toString();
    }

    /**
     * 配列と配列をインデックス順に文字列で結合した <br>
     * 新しいString配列を返す <br>
     * sizeが少ない方の配列長で返す
     * 
     * @param arr
     * @param ar2
     * @param s
     * @return
     */
    public static <E, T> String[] join(ArrayList<E> arr, ArrayList<T> ar2,
            final String s) {
        final int l = Compare.small(arr.size(), ar2.size());
        String[] ars = new String[l];
        for (int i = 0; i < l; ++i) {
            ars[i] = arr.get(i) + s + ar2.get(i);
        }
        return ars;
    }

    /**
     * 配列と配列をインデックス順に文字列で結合した <br>
     * 新しいString配列を返す <br>
     * sizeが少ない方の配列長で返す
     * 
     * @param arr
     * @param ar2
     * @param s
     * @return
     */
    public static <E, T> String[] join(E[] arr, ArrayList<T> ar2, final String s) {
        final int l = Compare.small(arr.length, ar2.size());
        String[] ars = new String[l];
        for (int i = 0; i < l; ++i) {
            ars[i] = arr[i] + s + ar2.get(i);
        }
        return ars;
    }

    /**
     * 配列と配列をインデックス順に文字列で結合した <br>
     * 新しいString配列を返す <br>
     * sizeが少ない方の配列長で返す
     * 
     * @param arr
     * @param ar2
     * @param s
     * @return
     */
    public static <E, T> String[] join(ArrayList<E> arr, T[] ar2, final String s) {
        final int l = Compare.small(arr.size(), ar2.length);
        String[] ars = new String[l];
        for (int i = 0; i < l; ++i) {
            ars[i] = arr.get(i) + s + ar2[i];
        }
        return ars;
    }

    /**
     * 配列と配列をインデックス順に文字列で結合した <br>
     * 新しいString配列を返す <br>
     * sizeが少ない方の配列長で返す
     * 
     * @param arr
     * @param ar2
     * @param s
     * @return
     */
    public static <E, T> String[] join(E[] arr, T[] ar2, final String s) {
        final int l = Compare.small(arr.length, ar2.length);
        String[] ars = new String[l];
        for (int i = 0; i < l; ++i) {
            ars[i] = arr[i] + s + ar2[i];
        }
        return ars;
    }





    /**
     * 配列をじょいん
     * 
     * @param arr
     * @param s
     * @return
     */
    public static <E> String join(E[] arr, final String s) {
        StringBuffer joined = new StringBuffer();
        for (int i = 0, l = arr.length; i < l;) {
            joined.append(arr[i++]);
            if (i < l) {
                joined.append(s);
            }
        }
        return joined.toString();
    }

    /**
     * ArrayListをただの配列に変換
     * 
     * @param arr
     * @return
     */
    public static <E> E[] convert(ArrayList<E> arr) {
        int size = arr.size();
        Object[] es = new Object[size];
        for (int i = size - 1; i >= 0; --i) {
            es[i] = arr.get(i);
        }
        return (E[]) es;
    }


    /**
     * 配列をArrayListに
     * 
     * @param es
     * @return
     */
    public static <E> ArrayList<E> convert(E[] es) {
        return create(es);
    }

    /**
     * 第一引数に第二引数を付け加えた新しい配列を返す
     * 
     * @param es1
     * @param es2
     * @return
     */
    public static <E> E[] add(E[] es1, E[] es2) {
        final int i1 = es1.length;
        final int i2 = es2.length + i1;

        Object[] es3 = new Object[i2];
        int i = 0;
        for (; i < i1; ++i) {
            es3[i] = es1[i];
        }
        for (int j = 0; i < i2; ++j, ++i) {
            es3[i] = es2[j];
        }
        return (E[]) es3;
    }

    public static <E> int indexOf(final E[] arr, final E e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (e == null ? arr[i] == null : e.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <E> int lastIndexOf(final E[] arr, final E e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (e == null ? arr[i] == null : e.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final int[] arr, final int e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final int[] arr, final int e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final char[] arr, final char e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final char[] arr, final char e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final float[] arr, final float e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final float[] arr, final float e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final double[] arr, final double e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final double[] arr, final double e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final long[] arr, final long e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final long[] arr, final long e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final short[] arr, final short e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final short[] arr, final short e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final byte[] arr, final byte e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final byte[] arr, final byte e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(final boolean[] arr, final boolean e) {
        for (int i = 0, l = arr.length; i < l; ++i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final boolean[] arr, final boolean e) {
        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] == e) {
                return i;
            }
        }
        return -1;
    }


}
