package cn.kemis.service;

import cn.kemis.dao.mapper.ExportFileMapper;
import cn.kemis.model.ExportFile;
import cn.kemis.model.ExportFileExample;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-20
 */
@Service
public class ExportFileService extends BaseService<ExportFileMapper, ExportFile, ExportFileExample> {


    public PageInfo<ExportFile>  searchExportFileList(PageInfo<ExportFile> pageInfo) {

        ExportFile exportFile = pageInfo.getList().get(0);

        ExportFileExample example = new ExportFileExample();
        ExportFileExample.Criteria criteria = example.createCriteria();

        // Map<String,Object> param = new HashMap<>();

        if (StringUtils.isNotBlank(exportFile.getBatchNo())) {
            criteria.andBatchNoEqualTo(exportFile.getBatchNo());
            // param.put("batchNo", exportFile.getBatchNo());
        }

        if (StringUtils.isNotBlank(exportFile.getFileGuid())) {
            criteria.andFileGuidEqualTo(exportFile.getFileGuid());
            // param.put("fileGuid", exportFile.getFileGuid());
        }

        if (StringUtils.isNotBlank(exportFile.getType())) {
            criteria.andTypeEqualTo(exportFile.getType());
            // param.put("type", exportFile.getType());

        }

        example.setOrderByClause("exportFileId desc");

        return super.selectPageByExample(example, pageInfo.getPageNum(), pageInfo.getSize());
    }
}
