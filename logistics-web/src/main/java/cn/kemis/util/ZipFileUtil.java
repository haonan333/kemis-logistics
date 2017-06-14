package cn.kemis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-17
 */
public class ZipFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);

    public static void main(String[] args) {

        String path = System.getProperty("user.dir");
        String inputFolder = path + "/tmp/";

        String zipFileName = "快递单.zip";

//        zipSimpleFolder(new File(inputFolder), "", path + "/" + zipFileName);
        unzip(inputFolder + zipFileName, inputFolder+"test/");
    }

    public static void zipSimpleFolder(File inputFolder, String parentName, String zipFilePath) {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);

            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            String folderName = parentName + inputFolder.getName() + "/";

            ZipEntry folderZipEntry = new ZipEntry(folderName);
            zipOutputStream.putNextEntry(folderZipEntry);

            File[] contents = inputFolder.listFiles();

            assert contents != null;
            for (File file : contents) {
                if (file.isFile()) {
                    zipFile(file, folderName, zipOutputStream);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Regular file : {}", file.getCanonicalPath());
                    }
                }
            }

            zipOutputStream.closeEntry();
            zipOutputStream.close();

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void zipFile(File inputFile, String parentName, ZipOutputStream zipOutputStream) {

        try {
            // A ZipEntry represents a file entry in the zip archive
            // We name the ZipEntry after the original file's name
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream fileInputStream = new FileInputStream(inputFile);
            byte[] buf = new byte[1024];
            int bytesRead;

            // Read the input file by chucks of 1024 bytes
            // and write the read bytes to the zip stream
            while ((bytesRead = fileInputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, bytesRead);
            }

            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 用apache ant包解压zip, 处理中文问题
     * @param zipFilePath           解压输入路径
     * @param outputDirectory       解压输出路径
     * @return
     */
    public static boolean unzip(String zipFilePath,String outputDirectory) {
        boolean isSuccessful = true;
        try {
            org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(zipFilePath);
            Enumeration emu = zipFile.getEntries();

            File outFile = null;
            while (emu.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry entry = (org.apache.tools.zip.ZipEntry) emu.nextElement();
                String fileName = entry.getName();
                if (!fileName.startsWith("__MACOSX")) {
                    outFile = new File(outputDirectory, fileName);
                    if(!outFile.exists()){
                        (new File(outFile.getParent())).mkdirs();
                    }

                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    //一次读40K
                    int BUFFER=40960;
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputDirectory + fileName),BUFFER);

                    int count;
                    byte data[] = new byte[BUFFER];
                    while ((count = bis.read(data, 0, BUFFER)) != -1) {
                        bos.write(data, 0, count);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();

                }
            }

            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            isSuccessful = false;
        }
        return isSuccessful;
    }

}