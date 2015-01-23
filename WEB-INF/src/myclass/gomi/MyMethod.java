package myclass.gomi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;





public class MyMethod {
	public int field=0;
	public void test() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class cls=this.getClass();
		Field f=cls.getDeclaredField("field");

		log(f.get(this));
	}

	public Method getMethod(Object o, String mn, Class... cs) throws SecurityException, NoSuchMethodException {
		//log(o.getClass());
		//log(mn);
		//log(cs.toString());
		return o.getClass().getMethod(mn, cs);
	}

	protected MyMethod log(Object o) {
		System.out.println(o);
		return this;
	}
	public void nanimosinai(int i) {

	}
}
