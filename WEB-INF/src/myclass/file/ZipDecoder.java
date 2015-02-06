package myclass.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zipファイルの解凍と配置
 *
 * @author yuki
 *
 */
public class ZipDecoder {
    public static void main(String[] args) throws IOException {
        unzip("C:\\Users\\yuki\\Desktop\\sm.zip", "C:\\ziptest", "008");
    }

    public static void unzip(String inputZipFile) throws IOException {
        ZipDecoder.unzip(inputZipFile, null);
    }

    public static void unzip(String inputZipFile, String outputFile, String zipFileName) throws IOException {
        File zipFile = new File(inputZipFile);
        // 出力先ディレクトリを作成
        File outputDirectry = Directry.createOutputDirectry(zipFile, outputFile);
        ZipDecoder.unzip(zipFile, outputDirectry, zipFileName);

    }

    public static void unzip(String inputZipFile, String outputFile) throws IOException {
        unzip(inputZipFile, outputFile, null);
    }

    public static void unzip(File zipFile, File baseDirectry, String zipFileName) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        writeStream(zip, baseDirectry, zipFileName);

    }

    public static void unzip(File zipFile, File baseDirectry) throws IOException {
        unzip(zipFile, baseDirectry, null);
    }

    public static void writeStream(ZipFile zip, File baseDirectry, String zipFileName) throws IOException {
        boolean isDefaultName = zipFileName == null;
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();

            String pn = zipEntry.getName();
            if (!isDefaultName) {
                pn = zipFileName + pn.substring(pn.indexOf("/"));
            }
            File outFile = new File(baseDirectry, pn);
            if (zipEntry.isDirectory()) {
                // ZipEntry がディレクトリの場合はディレクトリを作成。
                outFile.mkdirs();
            } else {
                try (InputStream inputStream = zip.getInputStream(zipEntry);
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);) {
                    if (!outFile.getParentFile().exists()) {
                        // 出力先ファイルの保存先ディレクトリが存在しない場合は、
                        // ディレクトリを作成しておく。
                        outFile.getParentFile().mkdirs();
                    }
                    try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));) {
                        // 入力ストリームから読み込み、出力ストリームへ書き込む。
                        ZipDecoder.writeStream(bufferedInputStream, bufferedOutputStream);
                    }
                }
            }
        }
    }

    public static void writeStream(ZipFile zip, File baseDirectry) throws IOException {
        writeStream(zip, baseDirectry, null);
    }

    private static void writeStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        int availableByteNumber;
        while ((availableByteNumber = inputStream.available()) > 0) {
            byte[] buffers = new byte[availableByteNumber];
            int readByteNumber = inputStream.read(buffers);
            if (readByteNumber < 0) {
                break;
            }
            outputStream.write(buffers, 0, readByteNumber);
        }
    }
}