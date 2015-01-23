package myclass.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myclass.util.Compare;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public abstract class MyServlet extends HttpServlet {
    protected String methodType = null;
    protected HttpServletRequest request = null;
    protected HttpServletResponse response = null;
    protected HashMap<String, ArrayList<String>> parameterMap = null;
    protected HashMap<String, ArrayList<FileItem>> fileItemMap = null;
    protected boolean isMultipart = false;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        methodType = req.getMethod();
        setting(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        methodType = req.getMethod();
        setting(req, res);
    }

    protected void setting(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String contenttype = req.getContentType();
        if (!Compare.isEmpty(contenttype)) {
            isMultipart = contenttype.startsWith("multipart/form-data");
        }
        res.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        request = req;
        response = res;
        if (isMultipart) {
            multipartParameterSetting();
        }
        action(req,res);
    }

    private void multipartParameterSetting() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        factory.setSizeThreshold(1024);
        upload.setSizeMax(-1);
        upload.setHeaderEncoding("UTF-8");
        parameterMap = new HashMap<String, ArrayList<String>>();
        fileItemMap = new HashMap<String, ArrayList<FileItem>>();

        try {
            // FileItemのList
            List list = upload.parseRequest(request);

            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                FileItem fItem = (FileItem) iterator.next();
                String fieldName = fItem.getFieldName();
                // 普通のフォームフィールド
                if (fItem.isFormField()) {
                    if (!parameterMap.containsKey(fieldName)) {
                        parameterMap.put(fieldName, new ArrayList<String>());
                    }
                    parameterMap.get(fieldName).add(fItem.getString("UTF-8"));
                } else {
                    // file
                    if (!fileItemMap.containsKey(fieldName)) {
                        fileItemMap.put(fieldName, new ArrayList<FileItem>());
                    }
                    fileItemMap.get(fieldName).add(fItem);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fileitemの1個目のみを返す
     * 
     * @param key
     * @return
     */
    protected FileItem getFileItem(String key) {
        return fileItemMap.get(key).get(0);
    }

    /**
     * fileitemのArrayListを返す
     * 
     * @param key
     * @return
     */
    protected ArrayList<FileItem> getFileItems(String key) {
        return fileItemMap.get(key);
    }

    /**
     * ファイルを書き込む
     * 
     * @param fi
     *            FileItemオブジェクト
     * @param pathname
     *            書き込むパス
     * @return 成功ならtrue
     */
    public static boolean writeFile(FileItem fi, String pathname) {
        boolean success = true;
        try {
            fi.write(new File(pathname));
        } catch (Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * multipart/form-dataのときrequest.getParameterと同じ
     * 
     * @param key
     * @return
     */
    protected String getFormParam(String key) {
        return parameterMap.get(key).get(0);
    }

    /**
     * multipart/form-dataのときrequest.getParameterValuesと同じ
     * 
     * @param key
     * @return
     */
    protected ArrayList<String> getFormParams(String key) {
        return parameterMap.get(key);
    }

    abstract protected void action(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;

    /**
     * request.getParameterValues(s);と同じ
     * 
     * @param s
     * @return
     */
    protected String[] getParams(String s) {
        return request.getParameterValues(s);
    }

    /**
     * request.getParameter(s);と同じ
     * 
     * @param s
     * @return
     */
    protected String getParam(String s) {
        return request.getParameter(s);
    }

    /**
     * Integer.parseIntしてから返す
     * 
     * @param s
     * @return
     */
    protected int getIntParam(String s) {
        return Integer.parseInt(getParam(s));
    }


    public static String getParam(HttpServletRequest req, String s) {
        return req.getParameter(s);
    }

    public static int getIntParam(HttpServletRequest req, String s) {
        return Integer.parseInt(getParam(req, s));
    }

    public static String[] getParamas(HttpServletRequest req, String s) {
        return req.getParameterValues(s);
    }

}
