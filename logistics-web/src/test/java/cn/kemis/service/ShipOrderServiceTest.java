package cn.kemis.service;

import cn.kemis.Constants;
import cn.kemis.dao.mapper.ExpressMapper;
import cn.kemis.dao.mapper.ShipOrderMapper;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.Express;
import cn.kemis.model.ShipOrder;
import cn.kemis.model.ShipOrderExample;
import cn.kemis.tools.excel.ExcelImportForSax;
import org.hamcrest.Matchers;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;


/**
 * Created by liutiyang on 16/7/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShipOrderServiceTest {

    @Autowired
    private ShipOrderService shipOrderService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ShipOrderMapper shipOrderMapper;

    @Test
    public void batchInsertSelective() {
        List<ShipOrder> shipOrderList = new ArrayList<>();
        ShipOrder shipOrder = null;
        for (int i = 0; i < 5; i++) {
            shipOrder = new ShipOrder();
            shipOrder.setOutExpress(true);
            shipOrder.setPrintExpress(false);
            shipOrder.setProvince("江苏");

            shipOrderList.add(shipOrder);
        }

        // int selective = shipOrderService.batchInsertSelective(shipOrderList, ShipOrder.class);
        // System.out.println(selective);

    }

    @Test
    public void selectTeacherVerify() {
        List<ShipOrder> teacherVerifyList = shipOrderService.selectTeacherVerify("201608");
        assertThat(teacherVerifyList, Matchers.notNullValue());
    }

    @Test
    public void selectTeacherDelivery() {
        List<ShipOrder> teacherNormalDeliveryList = shipOrderService.selectTeacherDelivery("201608", Constants.SHIP_ORDER_DELIVERY_NORMAL);
        assertThat(teacherNormalDeliveryList, Matchers.notNullValue());
        List<ShipOrder> teacherEmsDeliveryList = shipOrderService.selectTeacherDelivery("201608", Constants.SHIP_ORDER_DELIVERY_EMS);
        assertThat(teacherEmsDeliveryList, Matchers.notNullValue());
    }

    @Test
    public void selectStudentDelivery() {
        List<String> provinceList = shipOrderService.selectProvinceListByBatchNo("201608");
        for (String province : provinceList) {
            List<ShipOrder> studentNormalDeliveryList = shipOrderService.selectStudentDelivery("201608", Constants.SHIP_ORDER_DELIVERY_NORMAL, province);
            assertThat(studentNormalDeliveryList, Matchers.notNullValue());
            List<ShipOrder> studentEmsDeliveryList = shipOrderService.selectStudentDelivery("201608", Constants.SHIP_ORDER_DELIVERY_EMS, province);
            assertThat(studentEmsDeliveryList, Matchers.notNullValue());
        }
    }

    @Test
    public void selectProvinceListByBatchNo() {
        List<String> provinceList = shipOrderService.selectProvinceListByBatchNo("201608");
        assertThat(provinceList, Matchers.notNullValue());
    }

    @Test
    public void selectSchoolListByBatchNo() {
        List<String> schoolList = shipOrderService.selectSchoolListByBatchNo("201608");
        assertThat(schoolList, Matchers.notNullValue());
        for (String school : schoolList) {
            List<ShipOrder> shipOrderList = shipOrderService.selectByScholl("201608", school);
            assertThat(shipOrderList, Matchers.notNullValue());
            System.out.println(String.format("School: %s size: %d.", school, shipOrderList.size()));
        }
    }

    @Test
    public void testImportDUppiceExpress() {

        String fileSavePath = "/Users/zhangyutong/Downloads/594快递单数据EMS.xlsx";
            //从文件中取读的待处理内容 到list中
            List<ExpressTestEntity> list;
            try {
                list = ExcelImportForSax.importExcelBySax(new FileInputStream(fileSavePath),
                        ExpressTestEntity.class, new ImportParams());
            } catch (Exception e) {
                e.printStackTrace();
                throw new KemisException("excel 解析失败");
            }

        List<Express> batchInsertList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                    try {
                        ExpressTestEntity expressTestEntity = list.get(i);

                        List<ShipOrder> shipOrderList = shipOrderMapper.selectByExpressIdGroupByDelivery(expressTestEntity.getOldExpressId());
                        for (ShipOrder shipOrder : shipOrderList) {
                            Express express = new Express();
                            if(shipOrder.getDelivery().equals("普通快递")){
                                express.setExpressId(expressTestEntity.getOldExpressId());
                                express.setExpressCompany("圆通速递");
                                express.setExpressNumber(expressTestEntity.getYtNumber());
                                express.setDelivery("普通快递");
                            }else{
                                express.setExpressId(expressTestEntity.getExpressId());
                                express.setExpressCompany("中国邮政EMS");
                                express.setExpressNumber(expressTestEntity.getEmsNumber());
                                express.setDelivery("EMS");
                                //更新 ship_order
                                ShipOrderExample shipOrderExample = new ShipOrderExample();
                                shipOrderExample.createCriteria().andExpressIdEqualTo(expressTestEntity.getOldExpressId())
                                .andDeliveryEqualTo("EMS");
                                ShipOrder order = new ShipOrder();
                                order.setExpressId(expressTestEntity.getExpressId());
                                this.shipOrderMapper.updateByExampleSelective(order,shipOrderExample);
                            }

                            express.setExportTag(false);
                            express.setStatus((byte) 0);
                            express.setType(expressTestEntity.getType());
                            express.setExpressConsignee(expressTestEntity.getConsignee());
                            express.setExpressMobile(expressTestEntity.getMobile());
                            express.setExpressSchool(expressTestEntity.getSchool());
                            express.setExpressProvince(expressTestEntity.getProvince());
                            express.setExpressCity(expressTestEntity.getCity());
                            express.setExpressDistrict(expressTestEntity.getDistrict());
                            express.setExpressAddress(expressTestEntity.getAddress());
                            System.out.println("express = " + express);
                            batchInsertList.add(express);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        //批量插入
        if (batchInsertList.size() > 0) {
            expressService.batchInsertSelective(batchInsertList, Express.class);
        }

    }



}
