package myclass.gomi;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

class DynamicJavaSourceCodeObject extends SimpleJavaFileObject {

    private final String code;

    protected DynamicJavaSourceCodeObject(String name, String code) {
        super(URI.create(name+".java"), Kind.SOURCE);
        this.code = code ;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)throws IOException {
        return code;
    }
}
