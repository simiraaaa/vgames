package myclass;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import java.io.File;
import java.util.*;

/**
 * 簡単にファイルアップロードできる
 * @author yuki
 *
 */
public class MyFileUpload {

	public static void upload(final String path,HttpServletRequest req, ArrayList<String> ss) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		factory.setSizeThreshold(1024);
	    upload.setSizeMax(-1);
	    upload.setHeaderEncoding("utf8");
	    boolean isNotNullSS= ss!=null;
	    int i=0;
	    Escape escape=new Escape();

	    try {
	        //(4)ファイルデータ(FileItemオブジェクト)を取得し、
	        //   Listオブジェクトとして返す
	        List list = upload.parseRequest(req);

	        //(5)ファイルデータ(FileItemオブジェクト)を順に処理
	        Iterator iterator = list.iterator();
	        while(iterator.hasNext()){
	          FileItem fItem = (FileItem)iterator.next();

	          //(6)ファイルデータの場合、if内を実行
	          if(!(fItem.isFormField())){
	            //(7)ファイルデータのファイル名(PATH名含む)を取得
	            String fileName = fItem.getName();
	            if((fileName != null) && (!fileName.equals(""))){
	              //(8)PATH名を除くファイル名のみを取得
	              fileName=(new File(fileName)).getName();
	              String filepathString=path + escape.URLEncode(fileName);
	              //(9)ファイルデータを指定されたファイルに書き出し
	              fItem.write(new File(filepathString));
	              if(isNotNullSS){ss.add(i++, filepathString);};

	            }
	          }
	        }
	      }catch (FileUploadException e) {
	        e.printStackTrace();
	      }catch (Exception e) {
	        e.printStackTrace();
	      }

	}

	public static  void upload(final String path,HttpServletRequest req) {
		upload(path, req, null);
	}
}
