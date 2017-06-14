package cn.kemis.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 处理文件工具类
 * @Author
 */
public class FileUtil {

    static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 计算 guid对应的hash path
     *
     * @param guid
     * @return
     */
    public static String getHashPath(String guid) {
        return getHashPath(Math.abs(guid.hashCode()));
    }

    /**
     * 计算 id对应的hash path
     *
     * @param id
     * @return
     */
    public static String getHashPath(int id) {
        return getHashPath((long) id);
    }

    /**
     * 计算 id对应的hash path
     *
     * @param id
     * @return id hash后每两位以路径分隔符的路径
     */
    public static String getHashPath(long id) {
        String hexString = Long.toHexString(id ^ 127); // Strong prime
        if (hexString.length() < 8)
            hexString = StringUtils.leftPad(hexString.toLowerCase(), 8, '0');
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            // FILE_SEPARATOR
            sb.append('/').append(hexString.substring(i * 2, (i + 1) * 2));
        }
        // 128 -> 0x80 -> 0x00000080
        return sb.toString(); // [00]00/00/80
    }

    /**
     * 取得文件访问地址
     *
     * @param path
     * @param guid
     * @param suffix
     * @return
     */
    public static String getFileURL(String path, String guid, String suffix) {
        if (null == guid || "".equals(guid)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        // 获取服务地址
        builder.append(path);
        // 获取文件路径
        builder.append(getFilePath(guid));
        // 获取图片名字
        builder.append(getFileName(guid, "", suffix));

        return builder.toString();
    }

    public static String getRemotePath(String path, String guid) {
        StringBuilder builder = new StringBuilder();
        builder.append(path);
        builder.append(getFilePath(guid));
        return builder.toString();
    }

    public static String getRemotePath(String path, String guid, String suffix) {
        StringBuilder builder = new StringBuilder();
        builder.append(path);
        builder.append(getFilePath(guid)).append(guid).append(suffix);
        return builder.toString();
    }

    public static String getFilePath(String guid) {
        StringBuilder builder = new StringBuilder();
        builder.append(getHashPath(guid)).append("/");
        return builder.toString();
    }

    /**
     * 生成文件的名字
     *
     * @param name      生成的名，可以是guid,id,cpName
     * @param imageSize 图片大小，可是以s,b,m，其他文件保持此值为空
     * @param suffix    文件的类型
     * @return
     */
    public static String getFileName(String name, String imageSize,
                                     String suffix) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(imageSize)) {
            builder.append(name).append("-").append(imageSize).append(suffix);
        } else {
            builder.append(name).append(suffix);
        }

        return builder.toString();
    }

    /**
     * 删除文件
     *
     * @param delpath
     * @return
     */
    public static boolean deletefile(String delpath) {
        try {
            File file = new File(delpath);
            if (file.exists()) {
                if (!file.isDirectory()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    String[] filelist = file.list();
                    for (int i = 0; i < filelist.length; i++) {
                        File delfile = new File(delpath + "/" + filelist[i]);
                        if (!delfile.isDirectory())
                            delfile.delete();
                        else if (delfile.isDirectory())
                            deletefile(delpath + "/" + filelist[i]);
                    }
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Java文件操作 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 对单个zip文件进行解压
     *
     * @param zipFilePath       解压输入路径
     * @param outputDirectory   解压输出路径
     * @throws IOException
     */
    public static void unZip(String zipFilePath, String outputDirectory)
            throws IOException {
        ZipFile zipFile = new ZipFile(new File(zipFilePath));
        Enumeration e = zipFile.getEntries();
        ZipEntry zipEntry = null;
        while (e.hasMoreElements()) {
            zipEntry = (ZipEntry) e.nextElement();
            File destFile = new File(outputDirectory + zipEntry.getName());
            if (zipEntry.isDirectory()) {
                destFile.mkdirs();
            } else {
                File parent = destFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                InputStream is = zipFile.getInputStream(zipEntry);
                FileUtils.copyInputStreamToFile(is, destFile);
                is.close();
            }
        }
    }

    /**
     *  指定文件夹如果不存在,就创建
     * @param path  文件夹路径
     * @return      文件夹
     */
    public static File touchDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }



    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        String hashPath = getHashPath(uuid);
        System.out.println(hashPath);

        String fileURL = getFileURL("/test", uuid, ".xls");
        System.out.println(fileURL);

        String filePath = getRemotePath("/test/t", uuid);
        System.out.println(filePath);

        filePath = getRemotePath(uuid,".xls");
        System.out.println(filePath);

        System.out.println(getExtensionName("aa.xls"));
    }
}
