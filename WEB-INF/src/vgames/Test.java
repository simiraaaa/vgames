package vgames;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.servlet.ExtendedHttpServlet;

public class Test extends ExtendedHttpServlet {

    @Override
    protected void subAction(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO 自動生成されたメソッド・スタブ
        final String INC = servlet.getServletContext().getRealPath("/include");
        bufPrint(INC + "/head");
        res.getWriter().print("チトル");
        bufPrint(INC + "/title");
        res.getWriter().print("<h1>body</h1>");
        bufPrint(INC + "/foot");
    }

    private void bufPrint(String realPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(realPath));
        br.lines().forEach(response.getWriter()::print);
        br.close();
    }

}
