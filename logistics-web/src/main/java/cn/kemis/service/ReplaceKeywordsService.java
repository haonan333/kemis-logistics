package cn.kemis.service;

import cn.kemis.dao.mapper.ReplaceKeywordsMapper;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.domain.excelEntity.KeywordEntity;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.ReplaceKeywords;
import cn.kemis.model.ReplaceKeywordsExample;
import cn.kemis.tools.excel.ExcelImportForSax;
import cn.kemis.util.ZipFileUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-13
 */
@Service
public class ReplaceKeywordsService extends BaseService<ReplaceKeywordsMapper, ReplaceKeywords, ReplaceKeywordsExample> {

    private static Logger logger = LoggerFactory.getLogger(ReplaceKeywordsService.class);

    @Value("${file.server.url.upload}")
    private String uploadFilePath;

    @Autowired
    private UploadFileService uploadFileService;

    public List<ReplaceKeywords> selectByType(String type) {
        ReplaceKeywordsExample example = new ReplaceKeywordsExample();
        example.createCriteria().andTypeEqualTo(type);

        return mapper.selectByExample(example);
    }

    public PageInfo<ReplaceKeywords> searchKeywordList(PageInfo<ReplaceKeywords> pageInfo) {
        ReplaceKeywords replaceKeywords = pageInfo.getList().get(0);

        ReplaceKeywordsExample example = new ReplaceKeywordsExample();
        ReplaceKeywordsExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(replaceKeywords.getKeyword())) {
            criteria.andKeywordLike("%" + replaceKeywords.getKeyword() + "%");
        }

        if (StringUtils.isNotBlank(replaceKeywords.getType())) {
            criteria.andTypeEqualTo(replaceKeywords.getType());
        }

        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            example.setOrderByClause(pageInfo.getOrderBy());
        } else {
            example.setOrderByClause("replaceKeywordsId desc");
        }

        PageInfo<ReplaceKeywords> replaceKeywordsPageInfo = super.selectPageByExample(example, pageInfo.getPageNum(), pageInfo.getSize());
        replaceKeywordsPageInfo.setPages(pageInfo.getPages());
        return replaceKeywordsPageInfo;
    }

    /**
     * 查询模块地址相同的模块
     * @param keyword 等于模块地址
     * @param type 等于模块地址
     * @return 模块
     */
    private ReplaceKeywords findByKeywordAndType(String keyword, String type) {
        return findByKeywordAndType(keyword, type, null);
    }

    /**
     * 查询模块地址相同而 ID 同的模块
     * @param keyword 等于模块地址
     * @param type 等于模块地址
     * @param replaceKeywordsId 不等于模块 ID
     * @return 模块
     */
    private ReplaceKeywords findByKeywordAndType(String keyword, String type, Integer replaceKeywordsId) {
        ReplaceKeywordsExample example = new ReplaceKeywordsExample();
        ReplaceKeywordsExample.Criteria criteria = example.createCriteria();
        criteria.andKeywordEqualTo(keyword);
        criteria.andTypeEqualTo(type);

        if (replaceKeywordsId != null) {
            criteria.andReplaceKeywordsIdNotEqualTo(replaceKeywordsId);
        }

        List<ReplaceKeywords> replaceKeywordsList = super.selectByExample(example);
        ReplaceKeywords replaceKeywords = null;
        if (replaceKeywordsList != null && replaceKeywordsList.size() > 0) {
            replaceKeywords = replaceKeywordsList.get(0);
        }
        return replaceKeywords;
    }

    public BaseResponse save(ReplaceKeywords replaceKeywords) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(replaceKeywords.getKeyword())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("关键字不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(replaceKeywords.getType())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("类型不能为空！");
            return baseResponse;
        }

        ReplaceKeywords keywords = this.findByKeywordAndType(replaceKeywords.getKeyword(), replaceKeywords.getType());
        if (keywords != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("同类型关键字已存在！");
            return baseResponse;
        }

        super.insertSelective(replaceKeywords);

        if (replaceKeywords.getReplaceKeywordsId() != null) {
            baseResponse.setCode("0");
            baseResponse.setMsg("新增关键字成功！");
        } else {
            baseResponse.setCode("453");
            baseResponse.setMsg("保存关键字失败！");
        }

        return baseResponse;
    }

    public BaseResponse update(ReplaceKeywords replaceKeywords) {
        BaseResponse baseResponse = new BaseResponse();

        if (replaceKeywords.getReplaceKeywordsId() == null) {
            baseResponse.setCode("453");
            baseResponse.setMsg("关键字ID不能为空！请刷新后重新尝试操作！");
            return baseResponse;
        }

        if (StringUtils.isBlank(replaceKeywords.getKeyword())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("关键字不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(replaceKeywords.getType())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("类型不能为空！");
            return baseResponse;
        }

        ReplaceKeywords keywords = this.findByKeywordAndType(replaceKeywords.getKeyword(), replaceKeywords.getType(), replaceKeywords.getReplaceKeywordsId());
        if (keywords != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("同类型关键字已存在！");
            return baseResponse;
        }

        super.updateByPrimaryKeySelective(replaceKeywords);

        baseResponse.setCode("0");
        baseResponse.setMsg("编辑关键字成功！");

        return baseResponse;
    }

    public BaseResponse delete(ReplaceKeywords replaceKeywords) {
        BaseResponse baseResponse = new BaseResponse();

        if (replaceKeywords.getReplaceKeywordsId() == null) {
            baseResponse.setCode("450");
            baseResponse.setMsg("关键字ID不能为空！");
            return baseResponse;
        }

        ReplaceKeywordsExample example = new ReplaceKeywordsExample();
        example.createCriteria().andReplaceKeywordsIdEqualTo(replaceKeywords.getReplaceKeywordsId());
        super.deleteByExample(example);

        if (logger.isInfoEnabled()) {
            logger.info("删除关键字 {}", replaceKeywords.toString());
        }

        baseResponse.setCode("0");
        baseResponse.setMsg("删除关键字成功！");

        return baseResponse;
    }

    public void dealFileKeywords(MultipartFile file, String batchNo, String type) {
        //处理文件
        String fileSavePath = uploadFileService.checkAndSaveFile(file, batchNo);

        if (StringUtils.isBlank(fileSavePath)) {
            throw new KemisException("文件上传失败");
        }

        //如果是 zip包 先解压  再循环处理
        if (fileSavePath.endsWith(".zip")) {
            String outZipPath = uploadFilePath + "/unzip/keywords/"+batchNo+"/";
            ZipFileUtil.unzip(fileSavePath, outZipPath);//解压
            File fileList = new File(outZipPath);
            File[] directory = fileList.listFiles();
            if (directory != null && directory.length > 0) {
                for (File excel : directory) {
                    if (excel.isDirectory() || excel.isHidden()) continue;
                    String excelName = excel.getName();
                    readKeywordsAndBatchInsert(type, outZipPath + excelName);
                }
            }
        } else {
            readKeywordsAndBatchInsert(type, fileSavePath);
        }
    }

    /**
     * 解析关键字表格,并批量插入到关键字信息表
     * @param type           关键字类型
     * @param fileSavePath      文件路径
     */
    void readKeywordsAndBatchInsert(String type, String fileSavePath) {
        //从文件中取读的待处理内容 到list中
        List<KeywordEntity> list;
        try {
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), KeywordEntity.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }

        //批量添加到物流信息表
        int total = list.size();
        int pageSize = 1000;//每次处理1000
        int page = 0;

        List<ReplaceKeywords> batchInsertList = null;

        ReplaceKeywords keyword = null;

        while (page * pageSize <= total) {
            batchInsertList = new ArrayList<>(pageSize);
            for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
                try {
                    if (i >= total) break;
                    KeywordEntity keywordEntity = list.get(i);

                    if (!existInList(keywordEntity.getKeyword(), batchInsertList)
                            && !existInTable(type, keywordEntity.getKeyword())) {
                        keyword = new ReplaceKeywords();
                        keyword.setType(type);
                        keyword.setKeyword(keywordEntity.getKeyword());

                        batchInsertList.add(keyword);
                    }
                } catch (Exception e) {
                    logger.error("--> KeywordEntity to ReplaceKeywords error ! row= " + (i+1) + " " + e.getMessage());
                    continue;
                }
            }
            //批量插入
            if (batchInsertList.size() > 0) {
                batchInsertSelective(batchInsertList, ReplaceKeywords.class);
            }

            page++;
        }

    }

    /**
     * 检查 List 里是否已存在相同关键字
     * @param keyword
     * @param keywordsList
     * @return
     */
    boolean existInList(String keyword, List<ReplaceKeywords> keywordsList) {
        boolean isExist = false;

        if (keywordsList != null && keywordsList.size() > 0) {
            for (ReplaceKeywords replaceKeywords : keywordsList) {
                if (keyword.equals(replaceKeywords.getKeyword())) {
                    isExist = true;
                    break;
                }
            }
        }

        return isExist;
    }

    /**
     * 查询表里是否已存在同类同名关键字
     * @param type
     * @param keyword
     * @return
     */
    boolean existInTable(String type, String keyword) {
        boolean isExist = false;

        ReplaceKeywords replaceKeywords = mapper.selectByKeyword(keyword, type);

        if (replaceKeywords != null) {
            isExist = true;
        }

        return isExist;
    }
}
