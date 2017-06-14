package cn.kemis.service;

import cn.kemis.dao.mapper.ShipOrderSourceMapper;
import cn.kemis.domain.excelEntity.ShipOrderEntity;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.ShipOrderSource;
import cn.kemis.model.ShipOrderSourceExample;
import cn.kemis.tools.excel.ExcelImportForSax;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhailong on 16/8/11.
 */
@Service
public class ShipOrderSourceService extends BaseService<ShipOrderSourceMapper, ShipOrderSource, ShipOrderSourceExample> {

    Logger logger = LoggerFactory.getLogger(ShipOrderSourceService.class);

    /**
     * 读取单个发货单excel,并批量入库
     * @param batchNo       批次号
     * @param fileSavePath  文件所在路径
     */
    void readSourceAndBatchInsert(String batchNo, String fileSavePath) {
        //从文件中取读的待处理内容 到list中
        List<ShipOrderEntity> list;
        try {
            ImportParams importParams = new ImportParams();
            list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath), ShipOrderEntity.class, importParams);

        } catch (Exception e) {
            e.printStackTrace();
            throw new KemisException("excel 解析失败");
        }

        int total = list.size();
        int pageSize = 1000;//每次处理1000
        int page = 0;

        List<ShipOrderSource> batchInsertList = null;
        while (page * pageSize <= total) {
            batchInsertList = new ArrayList<>(pageSize);
            for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
                try {
                    if (i >= total) break;
                    this.importToModel(list.get(i), batchNo,batchInsertList);
                } catch (Exception e) {
                    logger.error("--> ShipOrderEntity to ShipOrderSource error ! row= " + (i+1) + " " + e.getMessage());
                    continue;
                }
            }
            //批量插入
            if (batchInsertList.size() > 0) {
                batchInsertSelective(batchInsertList,ShipOrderSource.class);
            }
            page++;
        }
    }

    /**
     * excel 读取的实体 转化成 model
     * @param shipOrderEntity       excel 读取的实体类
     * @param batchNo               批次号
     * @param batchInsertList       批量对像的列表
     */
    private void importToModel(ShipOrderEntity shipOrderEntity, String batchNo, List<ShipOrderSource> batchInsertList) {
        if (shipOrderEntity == null) return;
        //把从excel 中读取出来的 pojo 转化成 可以入库的 model
        ShipOrderSource shipOrderSource = new ShipOrderSource();
        shipOrderSource.setBatchNo(batchNo);
        shipOrderSource.setUserId(shipOrderEntity.getUserId());
        shipOrderSource.setUserName(shipOrderEntity.getUserName());
        shipOrderSource.setProvince(shipOrderEntity.getProvince());
        shipOrderSource.setCity(shipOrderEntity.getCity());
        shipOrderSource.setDistrict(shipOrderEntity.getDistrict());
        shipOrderSource.setSchoolId(shipOrderEntity.getSchoolId());
        shipOrderSource.setSchool(shipOrderEntity.getSchool());
        shipOrderSource.setTheClass(shipOrderEntity.getTheClass());
        shipOrderSource.setConsignee(shipOrderEntity.getConsignee());
        shipOrderSource.setMobile(shipOrderEntity.getMobile());
        shipOrderSource.setDelivery(shipOrderEntity.getDelivery());
        shipOrderSource.setShipGoods(shipOrderEntity.getShipGoods());
        shipOrderSource.setAddress(shipOrderEntity.getAddress());
        shipOrderSource.setTotalCount((short) shipOrderEntity.getTotalCount());
        shipOrderSource.setUnitCredits(shipOrderEntity.getUnitCredits());
        shipOrderSource.setSubject(shipOrderEntity.getSubject());
        if(org.apache.commons.lang3.StringUtils.isNotBlank(shipOrderEntity.getCampusAmbassador())){
            if(shipOrderEntity.getCampusAmbassador().equals("是")){
                shipOrderSource.setCampusAmbassador(true);
            }else{
                shipOrderSource.setCampusAmbassador(false);
            }
        }else{
            shipOrderSource.setCampusAmbassador(false);
        }
        shipOrderSource.setExpressId(shipOrderEntity.getExpressId());

        batchInsertList.add(shipOrderSource);
    }

}
