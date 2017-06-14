package cn.kemis.tools.flying;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PDF生成辅助类
 *
 * @author Goofy <a href="http://www.xdemo.org">http://www.xdemo.org</a>
 */
public class PdfHelper {

    public static ITextRenderer getRender(String path) throws DocumentException, IOException {

        ITextRenderer render = new ITextRenderer();

        // String path = getPath();
        //添加字体，以支持中文
        // render.getFontResolver().addFont(path + "fonts/Songti.ttc,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        // render.getFontResolver().addFont(path + "fonts/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        render.getFontResolver().addFont(path + "fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        return render;
    }

    /*public static void mergePDF(String sourcePath, String newFile) throws IOException {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        File file = new File(sourcePath);
        if (!file.exists()) return ;

        for (File pdfFile : file.listFiles()) {
            mergerUtility.addSource(pdfFile);
        }

        mergerUtility.setDestinationFileName(newFile);
        mergerUtility.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
    }*/

    /**
     * 合并指定文件夹中的 PDF 文件
     * @param sourcePath    待合并的 PDF 文件夹路径
     * @param newFile       合并后新文件的绝对路径
     * @return              成功返回 true 否则 返回 false, 文件夹不存在或空也返回 false
     */
    public static boolean mergePDFFiles(String sourcePath, String newFile) {
        File file = new File(sourcePath);
        if (!file.exists()) return false;

        File[] listFiles = file.listFiles();
        int length = listFiles.length;

        if (!(length > 0)) return false;

        List<File> fileList = Arrays.asList(listFiles);

        Collections.sort(fileList, (file1, file2) -> {
            if (file1.isDirectory() && file2.isFile())
                return -1;
            if (file1.isFile() && file2.isDirectory())
                return 1;
            String[] split1 = file1.getName().split("_");
            String[] split2 = file2.getName().split("_");

            if (split1.length > 1 && split2.length > 1) {
                if (StringUtils.isNumeric(split1[0]) && StringUtils.isNumeric(split2[0])) {
                    return Integer.valueOf(split1[0]).compareTo(Integer.valueOf(split2[0]));
                } else {
                    return file1.getName().compareTo(file2.getName());
                }
            } else {
                return file1.getName().compareTo(file2.getName());
            }

        });

        List<String> filesList = fileList.stream().filter(File::isFile).map(File::getPath).collect(Collectors.toList());
        if (filesList.size() > 0) {
            String[] strings = filesList.stream().toArray(size -> new String[filesList.size()]);

            return mergePDFFiles(strings, newFile);
        }

        return false;
    }

    /**
     * 合并 PDF 文件
     *
     * @param files   要合并的文件绝对路径
     * @param newFile 合并后新文件的绝对路径
     * @return 成功返回 true 否则 返回 false
     */
    public static boolean mergePDFFiles(String[] files, String newFile) {
        boolean retValue = false;
        Document document = null;
        try {
            document = new Document(new PdfReader(files[0]).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(newFile));
            document.open();
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i]);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            retValue = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return retValue;
    }

}
