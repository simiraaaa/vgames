package vgames.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

import myclass.MyCreateTag;

public class WrapJspWriter {

    public static void writeScript(JspWriter out, String contextPath, String folder, String... srcs) throws IOException {
        for (int i = 0, len = srcs.length; i < len; ++i) {
            out.println(MyCreateTag.createScript(contextPath + folder + srcs[i]));
        }
    }

    public static void writeScript(JspWriter out, String contextPath, String folder,
            String[]... srcs) throws IOException {
        for (int i = 0, len = srcs.length; i < len; ++i) {
            writeScript(out, contextPath, folder, srcs[i]);
        }

    }

    public static void writeScript(PrintWriter out, String contextPath, String folder,
            String... srcs) {
        for (int i = 0, len = srcs.length; i < len; ++i) {
            out.println(MyCreateTag.createScript(contextPath + folder + srcs[i]));
        }
    }

    public static void writeScript(PrintWriter out, String contextPath, String folder,
            String[]... srcs) {
        for (int i = 0, len = srcs.length; i < len; ++i) {
            writeScript(out, contextPath, folder, srcs[i]);
        }

    }
}
