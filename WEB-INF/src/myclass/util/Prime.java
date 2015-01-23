package myclass.util;

public class Prime {



	final static private int
	ONE = 9,
	TWO = 99,
	THREE = 999,
	FOUR = 9999,
	FIVE = 99999,
	SIX = 999999,
	SEVEN = 9999999,
	EIGHT = 99999999,
	NINE = 999999999;

	private static int[] PRIME_LIST = null,

	MAX_TABLE = {
		1993,
		20231,
		206411,
		2097671,
		21242369,
		53877419,
		109560533,
		161565329,
		214481611,
		268084997,
		322262459
	},

	LENGTH_TABLE = {
		1000,
		10000,
		100000,
		1000000,
		10000000,
		25000000,
		50000000,
		75000000,
		100000000,
		125000000,
		150000000
	},

	COUNT_TABLE = {
		100,
		1000,
		10000,
		100000,
		1000000,
		2500000,
		5000000,
		7500000,
		10000000,
		12500000,
		15000000,
		17500000
	},

	MAX_TABLE2 = {
		557,
		7933,
		104759,
		1299743,
		15485917,
		41161781,
		86028221,
		132276719,
		179424697,
		227254217,
		275604577,
		324407143
	};

	/**
	 * x個目の素数を返す
	 * @param x
	 * @return
	 */
	public static int getByCount(final int x) {
		checkCountList(x);
		return PRIME_LIST[x-1];
	}


	/**
	 * 素数のx桁目の文字を返す
	 * <br>9を指定した場合3が返る
	 * <br>2(1),3(2),5(3),7(4),9(5),11(6,7),13(8,9)
	 * <br>数値は素数(桁数)
	 *
	 * @param x
	 * @return
	 */
	public static char getChar(final int x) {
		if(x < 1){
			throw new RuntimeException("引数が1未満");
		}
		checkLengthList(x);
		for(int i =0,k = 0;; ++i){
			if((k += getLength(PRIME_LIST[i])) >= x ){
				i = PRIME_LIST[i];
				return Integer.toString(i).charAt(getLength(i) - 1 -(k-x));
			}
		}
	}

	private static void checkLengthList(final int x) {
		for(int i = 0, len = MAX_TABLE.length;i < len ;++i){
			if(LENGTH_TABLE[i] > x){createList(MAX_TABLE[i]);return;}
		}
		createList(Integer.MAX_VALUE -1);
		return;
	}

	private static void checkCountList(final int x) {
		for(int i = 0, len = MAX_TABLE.length;i < len ;++i){
			if(COUNT_TABLE[i] > x){createList(MAX_TABLE2[i]);return;}
		}
		createList(Integer.MAX_VALUE -1);
		return;
	}

	private static void createList(final int x) {
		if (PRIME_LIST == null) {
			getListByMax(x);
		}else if (PRIME_LIST[PRIME_LIST.length-1] < x) {
			getListByMax(x);
		}
	}

	/**
	 * x個の素数配列を返す。<br>
	 * 引数に7を渡した場合<br>
	 * {2,3,5,7,11,13,17}を返す。
	 * @param x
	 * @return
	 */
	public static int[] getListByCount(final int x) {
		checkCountList(x);
		int[] primes = new int[x];
		for(int i = 0;i < x; ++i){
			primes[i] = PRIME_LIST[i];
		}
		return primes;
	}

	/**
	 * 素数かどうかを判定する
	 * @param x
	 * @return
	 */
	public static boolean isPrime(final int x) {
		final int r= (int)Math.sqrt((double)x);

	    if( x == 2 ) return true;
	    if( x < 2 || x % 2 == 0 ) return false;
	    for(int n = 3; n <= r; n += 2 ){
	        if( x % n == 0 ) return false;
	    }
	    return true;
	}

	/**
	 * 素数かどうかを判定する
	 * @param x
	 * @return
	 */
	public static boolean isPrime(final long x) {
		final long r= (long)Math.sqrt((double)x);

	    if( x == 2 ) return true;
	    if( x < 2 || x % 2 == 0 ) return false;
	    for(long n = 3; n <= r; n += 2 ){
	        if( x % n == 0 ) return false;
	    }
	    return true;
	}

	/**
	 * x以下の最大の素数を返す
	 * @param x
	 * @return
	 */
	public static int getByMax(int x) {
		if(x<2){ throw new RuntimeException("引数が1以下") ;}
		else if (x < 3) {
			return 2;
		}
		for(;;--x){
			if (isPrime(x)) {
				return x;
			}
		}
	}
	/**
	 * x以下の最大の素数を返す
	 * @param x
	 * @return
	 */
	public static long getByMax(long x) {
		if(x<2){ throw new RuntimeException("引数が1以下") ;}
		else if (x < 3) {
			return 2;
		}
		for(;;--x){
			if (isPrime(x)) {
				return x;
			}
		}
	}

	/**
	 * x以下の素数が何個あるか返す
	 * @param x
	 * @return
	 */
	public static int getCount(final int x) {
		if(PRIME_LIST == null || PRIME_LIST[PRIME_LIST.length -1] < x){
			getListByMax(x);
			return PRIME_LIST.length;
		}
		int i = 0;
		for(;PRIME_LIST[i] < x;++i){}
		return i+1;
	}

	/**
	 * x以下の素数の配列を返す
	 * @param x
	 * @return
	 */
	public static int[] getListByMax(int x) {
		if (x<2) {
			throw new RuntimeException("引数が1以下");
		}else if (x < 3) {
			return new int[]{2};
		}else if (x < 5) {
			return new int[]{2,3};
		}else if(x < 7){
			return new int[]{2,3,5};
		}
		if(PRIME_LIST != null){
			int y = getByMax(x);
			if(PRIME_LIST[PRIME_LIST.length -1] >= y){
				int i = 0;
				for(;PRIME_LIST[i] < y;++i){}
				int[] primes = new int[1+i];
				for(;i>=0 ; --i){
					primes[i] = PRIME_LIST[i];
				}
				return primes;
			}
		}

		final int end = (int)Math.sqrt((double)x)/2;
		x = (x + 1)/2 - 1;
		boolean[] table = new boolean[x];
		for(int n = 0; n<=end ; ++n){
			for(int add = 3 + n + n,i = n + add; i<x;i+=add){
				table[i] = true;
			}
		}
		int c = 1;
		for(int n = 0; n < x; ++n){
			if(table[n]){continue;}
			++c;
		}
		int[] primes = new int [c];
		primes[0]=2;
		c = 0;
		for(int n = 0; n < x;++n){
			if(table[n]){continue;}
			primes[++c] = 3 + n + n;
		}
		return PRIME_LIST = primes;
	}


	private static int getLength(final int x) {
		if (x <= ONE) {
			return 1;
		}else if (x <= TWO) {
			return 2;
		}else if (x <= THREE) {
			return 3;
		}else if (x <= FOUR) {
			return 4;
		}else if (x <= FIVE) {
			return 5;
		}else if (x <= SIX) {
			return 6;
		}else if (x <= SEVEN) {
			return 7;
		}else if (x <= EIGHT) {
			return 8;
		}else if (x <= NINE) {
			return 9;
		}else{
			return 10;
		}

	}


}
