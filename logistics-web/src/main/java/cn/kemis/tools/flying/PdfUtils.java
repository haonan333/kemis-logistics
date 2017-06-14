package cn.kemis.tools.flying;

import cn.kemis.tools.freemarker.FreemarkerTemplateHelper;
import com.lowagie.text.DocumentException;
import freemarker.template.TemplateException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PDF生成工具类
 *
 * @author Goofy <a href="http://www.xdemo.org">http://www.xdemo.org</a>
 */
public class PdfUtils {

    public static void main(String[] args) {
        try {
            Map<Object, Object> data = new HashMap<>();
            data.put("title", "发货单");
            data.put("school", "安定镇西卢小学.EMS");

            List<Map<Object, Object>> records = new ArrayList<>();
            Map<Object, Object> record = null;

            Set<String> teachers = new HashSet<>();

            for (int i = 0; i < 20; i++) {
                record = new HashMap<>();
                record.put("address", "北京市辖区大兴区安定镇西芦小学");
                record.put("school", "安定镇西卢小学");
                record.put("consignee", "姜帆<br/><b>13683214783</b><br/>英语");
                record.put("userName", "陈佳依");
                record.put("theClass", "六年级1班");
                record.put("totalCount", "奖品总数:1");
                record.put("goodsName", "一起作业网专属圆印章_X1<br/>一起作业网专属圆印章_X1<br/>一起作业网专属圆印章_X1<br/>一起作业网专属圆印章_X1<br/>一起作业网专属圆印章_X1");

                records.add(record);

                teachers.add("姜帆" + i);
            }

            data.put("data", records);
            data.put("teachers", teachers);

            String path = PdfUtils.class.getResource("/").getPath();

            System.out.println(path);
            System.out.println(System.getProperty("user.dir"));

            generateToFile(path + "templates/pdfTemplates/", "shipOrderTest.ftl", path + "/", data, System.getProperty("user.dir") + "/freemarker.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成PDF到文件
     *
     * @param ftlPath       模板文件路径（不含文件名）
     * @param ftlName       模板文件吗（不含路径）
     * @param imageDiskPath 图片的磁盘路径
     * @param data          数据
     * @param outputFile    目标文件（全路径名称）
     * @throws Exception
     */
    public static void generateToFile(String ftlPath, String ftlName, String imageDiskPath, Object data, String outputFile) throws Exception {
        String html = FreemarkerTemplateHelper.getContent(ftlPath, ftlName, data);
        OutputStream out = null;
        ITextRenderer render = null;
        out = new FileOutputStream(outputFile);
        render = PdfHelper.getRender(ftlPath);
        render.setDocumentFromString(html);
        if (imageDiskPath != null && !imageDiskPath.equals("")) {
            //html中如果有图片，图片的路径则使用这里设置的路径的相对路径，这个是作为根路径
            render.getSharedContext().setBaseURL("file:/" + imageDiskPath);
        }
        render.layout();
        render.createPDF(out);
        render.finishPDF();
        render = null;
        out.close();
    }

    /**
     * 生成PDF到输出流中（ServletOutputStream用于下载PDF）
     *
     * @param ftlPath       ftl模板文件的路径（不含文件名）
     * @param ftlName       ftl模板文件的名称（不含路径）
     * @param imageDiskPath 如果PDF中要求图片，那么需要传入图片所在位置的磁盘路径
     * @param data          输入到FTL中的数据
     * @param response      HttpServletResponse
     * @return
     * @throws IOException
     * @throws TemplateException
     * @throws DocumentException
     */
    public static OutputStream generateToServletOutputStream(String ftlPath, String ftlName, String imageDiskPath, Object data, HttpServletResponse response) throws IOException, TemplateException, DocumentException {
        String html = FreemarkerTemplateHelper.getContent(ftlPath, ftlName, data);
        OutputStream out = null;
        ITextRenderer render = null;
        out = response.getOutputStream();
        render = PdfHelper.getRender(ftlPath);
        render.setDocumentFromString(html);
        if (imageDiskPath != null && !imageDiskPath.equals("")) {
            //html中如果有图片，图片的路径则使用这里设置的路径的相对路径，这个是作为根路径
            render.getSharedContext().setBaseURL("file:/" + imageDiskPath);
        }
        render.layout();
        render.createPDF(out);
        render.finishPDF();
        render = null;
        return out;
    }

}
