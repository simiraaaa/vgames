package myclass.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletWrapper {
    public void action(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;

    public void setServlet(HttpServlet servlet);
}
