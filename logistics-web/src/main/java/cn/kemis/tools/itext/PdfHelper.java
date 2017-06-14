package cn.kemis.tools.itext;

import cn.kemis.tools.freemarker.FreemarkerTemplateHelper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by liutiyang on 16/8/7.
 */
public class PdfHelper {

    /**
     * 获取Freemarker配置
     *
     * @param templatePath
     * @return
     * @throws IOException
     */
    private static Configuration getFreemarkerConfig(String templatePath) throws IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_23);
        config.setDirectoryForTemplateLoading(new File(templatePath));
        config.setEncoding(Locale.CHINA, "utf-8");
        return config;
    }

    /**
     * 获取类路径
     *
     * @return
     */
    public static String getPath() {
        return cn.kemis.tools.flying.PdfHelper.class.getResource("/").getPath();
    }

    public static void generate(String ftlPath, String ftlName, String imageDiskPath, Object data, String outputFile) throws Exception {
        String html = FreemarkerTemplateHelper.getContent(ftlPath, ftlName, data);
        String path = getPath();

        Document doc = new Document(PageSize.A4);
        //如果是web做下载，后面的传入response的输出流，同时需要设置response的ContentType以及指定Content-Disposition为attachment同时设定fileName，fileName记得URLEncoder
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outputFile));
        //设置页面边距
        doc.setMargins(20, 20, 20, 20);
        doc.open();


        //这里可以代码创建pdf的部分页面（如更加精细的封面等）
        //......
        //设置HTML转PDF时页面边距（建议放在open方法前面，如果不放在open方法前面则必须后面加上newPage才能生效）
        // doc.setMargins(20, 20, 20, 20);
        // doc.newPage();

        BaseFont baseCnFont = BaseFont.createFont(path + "fonts/Songti.ttc,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        BaseFont cnF1 = BaseFont.createFont(path + "fonts/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        BaseFont cnF2 = BaseFont.createFont(path + "fonts/SIMSUN.TTC,1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        XMLWorkerHelper.getInstance().parseXHtml(writer, doc, new ByteArrayInputStream(html.getBytes()), Charset.forName("UTF-8"), new FontProvider() {
            @Override
            public boolean isRegistered(String s) {
                return false;
            }

            public Font getFont(String fontFamily, String charset, boolean arg2, float size, int style, BaseColor color) {
                //根据CSS中不同的字体名返回不同的字体实例
                if("cssFontName1".equalsIgnoreCase(fontFamily)){
                    return new Font(cnF1,size,style,color);
                }else if("cssFontName2".equalsIgnoreCase(fontFamily)){
                    return new Font(cnF2,size,style,color);
                }else{
                    //默认字体
                    return new Font(baseCnFont,size,style,color);
                }
            }
        });
        doc.close();
    }
}
