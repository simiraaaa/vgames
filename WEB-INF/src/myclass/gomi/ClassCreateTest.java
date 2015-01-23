package myclass.gomi;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;
import javax.tools.*;


import com.sun.org.apache.bcel.internal.generic.NEW;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;

public class ClassCreateTest{

    static final String DEST_DIR = "";
    static final String PACKAGE_NAME = ClassCreateTest.class.getPackage().getName();
    static final String CLASS_NAME = "Created";
    static final String QUALIFIED_CLASS_NAME = PACKAGE_NAME +"."+ CLASS_NAME;

    static final String SOURCE =
        "package "+ PACKAGE_NAME +";" +
        "public class "+ CLASS_NAME +"{" +
            " public void say(){" +
                "System.out.println(\"Hello, dynamic compilation world! : \"+getClass().getName());" +
            "}" +
        "}";

    private static void dynamicalCompile(){

        // (1) コンパイルに渡すソースコードやオプションを作成
        // コンパイル後にエラーが無いか調べる（ステップ(4)参照）
        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<JavaFileObject>();
        // コンパイル・オプション
        List<String> options = asList("-d",DEST_DIR);
        // Java のソースコード
        List<? extends JavaFileObject> src = asList(
            new DynamicJavaSourceCodeObject(CLASS_NAME, SOURCE)
        );

        // (2) コンパイラ・オブジェクト取得
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // (3) コンパイル実行
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, null, diags,/* options*/null, null, src);

        // (4) コンパイル・エラーのチェック
        if (!compilerTask.call()){
            for (Diagnostic diag : diags.getDiagnostics()){
                System.out.format("Error on line %d in %s", diag.getLineNumber(), diag);
            }
        }
    }
    private static void log(Object object) {
		System.out.println(object);
	}
    public static void main(String... args)throws Exception{
        dynamicalCompile();
        CreatedClass cc=(CreatedClass)Class.forName(QUALIFIED_CLASS_NAME).newInstance();
        //ClassCreate hello = (ClassCreate)Class.forName(QUALIFIED_CLASS_NAME).newInstance();
        //hello.say();
//        String targetMethodName="say";
//        Class c = Class.forName(CLASS_NAME);
//		Object service = (Object)c.newInstance();
//		//Class[] classArgs = new Class[]{String.class};
//		Method mthod = Class.forName(CLASS_NAME).getMethod(targetMethodName, null);
//		Object res = mthod.invoke(service, null);
    }
}