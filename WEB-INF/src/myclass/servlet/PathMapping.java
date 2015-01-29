package myclass.servlet;

public interface PathMapping {
    public default ServletWrapper load(String pathInfo) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String classPath = pathInfo.substring(1);
        return ((ServletWrapper) Class.forName(classPath).newInstance());
    }
}
