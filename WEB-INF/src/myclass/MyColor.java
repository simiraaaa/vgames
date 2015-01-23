package myclass;






/**
 *
 * @author yuki
 *
 */
public class MyColor {

    public static final int WHITE = -1;
    public static final int LIGHT_GRAY = -4144960;
    public static final int GRAY = -8355712;
    public static final int DARK_GRAY = -12566464;
    public static final int BLACK = -16777216;
    public static final int RED = -65536;
    public static final int PINK = -20561;
    public static final int ORANGE = -14336;
    public static final int YELLOW = -256;
    public static final int GREEN = -16711936;
    public static final int MAGENTA = -65281;
    public static final int CYAN = -16711681;
    public static final int BLUE = -16776961;

	private int value;

	public MyColor(final int r,final int g,final int b,final int a) {
		setValue(r, g, b, a);
	}
	public MyColor(final int r,final int g,final int b) {
		setValue(r, g, b, 255);
	}

	public MyColor(final int argb) {
		value=argb;
	}

	public MyColor() {

	}

	public MyColor setValue(final String s) {
		String[] ar = s.split(",");
		int l = ar.length;
		int[] cs=new int[l];
		for(int i=0;i<l;++i){
			cs[i]=Integer.parseInt(ar[i]);
		}
		if(l == 4){
			setValue(cs[0], cs[1], cs[2], cs[3]);
		}else {
			setValue(cs[0], cs[1], cs[2]);
		}

		return this;
	}
	public MyColor setValue(final int r,final int g,final int b,final int a) {
		value=((validate(a) & 0xFF) << 24) | ((validate(r) & 0xFF) << 16) | ((validate(g) & 0xFF) << 8) | ((validate(b) & 0xFF) << 0);
		return this;
	}
	public MyColor setValue(final int r,final int g,final int b) {
		setValue(r, g, b, 255);
		return this;
	}

	public MyColor setValue(final int rgba) {
		value=rgba;
		return this;
	}
	public int getValue() {
		return value;
	}

	private int validate(final int i) {
		if(i>255){
			return 255;
		}else if (i<0) {
			return 0;
		}else{
			return i;
		}
	}
}
