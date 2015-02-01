package myclass.file;

import java.io.File;
import java.io.FileNotFoundException;

public class Directry {

    /**
     * 出力先ディレクトリを作成<br>
     * このメソッドはZipDecoderのための実装になっています。
     *
     * @param file
     *            解凍対象のZip　File
     * @param outputFile
     *            出力先ディレクトリ
     * @return　出力先ディレクトリ
     * @throws FileNotFoundException
     */
    public static File createOutputDirectry(final File file, final String outputFile) throws FileNotFoundException {
        File outputDirectry;
        if (outputFile != null) {
            outputDirectry = new File(outputFile);
        } else {
            // 出力先ディレクトリが指定されない場合、 対象のZipファイルと同名のディレクトリを作成
            outputDirectry = directryFromName(file);
        }
        if (!outputDirectry.exists()) {
            // 出力先ディレクトリが存在しない場合、作成
            createDirectry(outputDirectry);
        }
        return outputDirectry;
    }

    /**
     * dirの位置にディレクトリを作成します
     *
     * @param dir
     * @throws FileNotFoundException
     */
    public static void createDirectry(File dir) throws FileNotFoundException {
        if (!dir.mkdir()) {
            throw new FileNotFoundException(dir + "の生成に失敗しました。");
        }
    }

    /**
     * Fileオブジェクトのnameから拡張子を省いたFileを生成 <br>
     * つまり指定したfileと同名のディレクトリを返します。
     *
     * @param file
     * @return
     */
    public static File directryFromName(File file) {
        String fileName = file.getName();
        int indexOfExtention = fileName.lastIndexOf(".");
        String baseDirectryName = fileName.substring(0, indexOfExtention);
        return new File(file.getParent(), baseDirectryName);
    }

}
