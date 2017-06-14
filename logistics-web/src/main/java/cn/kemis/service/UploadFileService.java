package cn.kemis.service;

import cn.kemis.dao.mapper.UploadFileMapper;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.UploadFile;
import cn.kemis.model.UploadFileExample;
import cn.kemis.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by xuhailong on 16/8/9.
 */
@Service
public class UploadFileService extends BaseService<UploadFileMapper,UploadFile,UploadFileExample> {

    @Value("${file.server.url.upload}")
    private String uploadFilePath;

    /**
     * 根据文件名和批次号查询文件
     * @param nameNoEx
     * @param batchNo
     * @return
     */
    public UploadFile checkByNameAndBatchNo(String nameNoEx, String batchNo) {
        UploadFileExample uploadFileExample = new UploadFileExample();
        uploadFileExample.createCriteria().andBatchNoEqualTo(batchNo).andFileNameEqualTo(nameNoEx);
        List<UploadFile> uploadFiles = selectByExample(uploadFileExample);
        if (uploadFiles != null && uploadFiles.size() > 0) {
            return uploadFiles.get(0);
        }
        return null;
    }

    /**
     * 新增
     * @param batchNo       批次号
     * @param fileName      名称
     * @param fileSuffix    后缀名
     * @param uuid          唯一码
     * @param fileUrl       文件路径
     * @param status        状态 数据导入时 老师默认值 0 学生默认值 1，老师单校验后值为1。仅值为1的可以打印快递单发货。值为2已发货，3已签收，4异常件。
     */
    public void saveFile(String batchNo, String fileName, String fileSuffix, String uuid, String fileUrl, Byte status) {
        UploadFile file = new UploadFile();
        file.setBatchNo(batchNo);
        file.setFileName(fileName);
        file.setFileSuffix(fileSuffix);
        file.setFileGuid(uuid);
        file.setFileUrl(fileUrl);
        file.setStatus(status);
        insertSelective(file);
    }

    /**
     * 处理文件
     * @param file
     * @return
     */
    public String checkAndSaveFile(MultipartFile file,String batchNo) {

        if (file == null || "".equals(file.getOriginalFilename())) {
            throw new KemisException("请上传文件");
        }

        //原始文件名
        String originalFileName = file.getOriginalFilename();
        //不带扩展名的文件名
        String nameNoEx = FileUtil.getFileNameNoEx(originalFileName);
        //文件扩展名
        String extName = ".".concat(FileUtil.getExtensionName(originalFileName));
        if (!(extName.endsWith(".xls") || extName.endsWith(".xlsx") || extName.endsWith(".zip"))) {
            throw new KemisException("文件格式错误");
        }
        //检查是否已上传过
        UploadFile uploadFile = this.checkByNameAndBatchNo(nameNoEx,batchNo);
        if (uploadFile != null) {
            throw new KemisException("请不要重复上传");
        }

        //文件保存路径
        String uuid = UUID.randomUUID().toString();
        String fileSavePath = FileUtil.getRemotePath(uploadFilePath, uuid, extName);
        File newFile = new File(fileSavePath);
        try {
            //上传文件
            FileUtils.writeByteArrayToFile(newFile,file.getBytes());
            if (!newFile.exists()) {
                throw new KemisException("上传文件错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("上传文件错误");
        }

        //文件上传信息入库
        this.saveFile(batchNo,nameNoEx,extName,uuid,fileSavePath, (byte) 0);

        return fileSavePath;
    }
}
