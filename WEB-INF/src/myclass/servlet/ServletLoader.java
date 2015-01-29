package myclass.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletLoader extends HttpServlet {
    private static final String MAPPING_CLASS_NAME = "myclass.servlet.Path";
    private static PathMapping path = null;

    @Override
    public void init() throws ServletException {
        // TODO 自動生成されたメソッド・スタブ
        try {
            Class<PathMapping> c = (Class<PathMapping>) Class.forName(MAPPING_CLASS_NAME);
            path = c.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            path = new PathMapping() {};
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ

        /**
         * いずれの例外も404へ
         */
        try {
            ServletWrapper servletWrapper = path.load(req.getPathInfo());
            servletWrapper.setServlet(this);
            servletWrapper.action(req, res);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            req.getRequestDispatcher("/404.html").forward(req, res);
            return;
        }

    }

    public static Class<ServletWrapper> load(HttpServletRequest req) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        String classPath = req.getPathInfo().substring(1);
        return (Class<ServletWrapper>) Class.forName(classPath);
    }
}
