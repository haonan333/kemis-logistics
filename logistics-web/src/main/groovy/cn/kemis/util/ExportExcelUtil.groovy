package cn.kemis.util

import cn.kemis.domain.ExpressWorkProcessDomain
import cn.kemis.domain.ShipOrderAndGoodsDomain
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Workbook
import org.gsheets.ExcelFile
/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-08-16
 */
class ExportExcelUtil {

    /**
     * 生成快递单 Excel 文件
     * @param filename
     * @param shipOrderList
     */
    public static void createDeliveryWorkbook(String filename, List<ShipOrderAndGoodsDomain> shipOrderList, String expressCompany) {
        File excel = new File(filename)
        if (!excel.exists()) excel.createNewFile()

        Workbook workbook = new ExcelFile().workbook {
            styles {
                font("bold") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD)
                }

                font("normal") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_NORMAL)
                }

                cellStyle("header") { CellStyle cellStyle ->
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
                }
            }

            data {
                // data
                sheet("Export") {
                    header(["快递单ID", "物流公司", "物流单号", "类型", "配送方式", "是否导回", "物流价格", "收货人", "收货人电话", "学校名称", "省", "市", "区", "详细地址", "序列", "重量", "备注"])

                    shipOrderList.eachWithIndex {shipOrder, index ->
                        row([shipOrder.expressId, "EMS".equalsIgnoreCase(shipOrder.delivery) ? "中国邮政EMS" : expressCompany, "", shipOrder.express.type,
                             shipOrder.delivery, shipOrder.express.exportTag ? "是" : "否", shipOrder.express.price,
                             shipOrder.express.expressConsignee, shipOrder.express.expressMobile, shipOrder.express.expressSchool,
                             shipOrder.express.expressProvince, shipOrder.express.expressCity, shipOrder.express.expressDistrict,
                             shipOrder.express.expressAddress, index + 1, "", ""])
                    }
                }
            }

            commands {
                applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..17)
                applyCellStyle(font: "normal", rows: 2..(shipOrderList.size() + 2), columns: 1..17)
                applyColumnWidth(columns: 10, width: 10000)
                applyColumnWidth(columns: 14, width: 10000)
                // mergeCells(rows: 1, columns: 1..3)
            }
        }

        def excelOut = new FileOutputStream(excel)
        workbook.write(excelOut)
        workbook.close()
        excelOut.close()
    }

    /**
     * 生成发货单 老师 Excel 文件
     * @param filename
     * @param shipOrderList
     */
    public static void createTeacherShipOrderWorkbook(String filename, List<ShipOrderAndGoodsDomain> shipOrderAndGoodsDomainList, Map<Long, Integer> sequenceMap) {
        File excel = new File(filename)
        if (!excel.exists()) excel.createNewFile()

        Workbook workbook = new ExcelFile().workbook {
            styles {
                font("bold") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD)
                }

                font("normal") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_NORMAL)
                }

                cellStyle("header") { CellStyle cellStyle ->
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
                }
            }

            data {
                // data
                sheet("Export") {
                    header(["收货地址", "学校", "教师信息", "总数", "奖品明细", "配送方式", "序列"])

                    shipOrderAndGoodsDomainList.eachWithIndex {shipOrderAndGoodsDomain, index ->
                        def orderGoodsList = shipOrderAndGoodsDomain.shipOrderGoodsList
                        def goodsString = new StringBuilder();
                        orderGoodsList.each {
                            goodsString.append(it.goodsName).append("_X").append(it.goodsCount)
                            if (it != orderGoodsList.last()) {
                                goodsString.append("\n")
                            }
                        }
                        row([shipOrderAndGoodsDomain.address, shipOrderAndGoodsDomain.school,
                             shipOrderAndGoodsDomain.consignee + "\n" + shipOrderAndGoodsDomain.mobile,
                             shipOrderAndGoodsDomain.totalCount, goodsString.toString(),
                             shipOrderAndGoodsDomain.delivery, sequenceMap.get(shipOrderAndGoodsDomain.getExpressId())])
                    }
                }
            }

            commands {
                applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..7)
                applyCellStyle(font: "normal", rows: 2..(shipOrderAndGoodsDomainList.size() + 2), columns: 1..7)
                applyColumnWidth(columns: 1, width: 10000)
                applyColumnWidth(columns: 2, width: 10000)
                applyColumnWidth(columns: 3, width: 5000)
                applyColumnWidth(columns: 5, width: 10000)
                // mergeCells(rows: 1, columns: 1..3)
            }
        }

        def excelOut = new FileOutputStream(excel)
        workbook.write(excelOut)
        workbook.close()
        excelOut.close()
    }

    /**
     * 生成发货单测试用 Excel 文件
     * @param filename
     * @param shipOrderList
     */
    public static void createShipOrderTestWorkbook(String filename, List<ShipOrderAndGoodsDomain> shipOrderAndGoodsDomainList) {
        File excel = new File(filename)
        if (!excel.exists()) excel.createNewFile()

        Workbook workbook = new ExcelFile().workbook {
            styles {
                font("bold") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD)
                }

                font("normal") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_NORMAL)
                }

                cellStyle("header") { CellStyle cellStyle ->
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
                }
            }

            data {
                // data
                sheet("Export") {
                    header(["用户ID", "用户姓名","省", "市", "区", "学校", "班级", "收货人", "电话", "配送方式", "详细地址",
                            "奖品明细", "奖品数量", "单位", "老师科目", "是否校园大使", "快递单ID"])

                    shipOrderAndGoodsDomainList.eachWithIndex {shipOrderAndGoodsDomain, index ->
                        def orderGoodsList = shipOrderAndGoodsDomain.shipOrderGoodsList
                        def goodsString = new StringBuilder();
                        orderGoodsList.each {
                            goodsString.append(it.goodsName).append("_X").append(it.goodsCount)
                            if (it != orderGoodsList.last()) {
                                goodsString.append("\n")
                            }
                        }
                        row([shipOrderAndGoodsDomain.userId, shipOrderAndGoodsDomain.userName, shipOrderAndGoodsDomain.province,
                             shipOrderAndGoodsDomain.city, shipOrderAndGoodsDomain.district, shipOrderAndGoodsDomain.school,
                             shipOrderAndGoodsDomain.theClass, shipOrderAndGoodsDomain.consignee, shipOrderAndGoodsDomain.mobile,
                             shipOrderAndGoodsDomain.delivery, shipOrderAndGoodsDomain.address, goodsString.toString(),
                             shipOrderAndGoodsDomain.totalCount, shipOrderAndGoodsDomain.unitCredits, shipOrderAndGoodsDomain.subject,
                             shipOrderAndGoodsDomain.campusAmbassador ? "是" : "否", shipOrderAndGoodsDomain.expressId])
                    }
                }
            }

            commands {
                applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..17)
                applyCellStyle(font: "normal", rows: 2..(shipOrderAndGoodsDomainList.size() + 2), columns: 1..17)
                applyColumnWidth(columns: 6, width: 10000)
                applyColumnWidth(columns: 11, width: 3000)
                applyColumnWidth(columns: 12, width: 10000)
                // mergeCells(rows: 1, columns: 1..3)
            }
        }

        def excelOut = new FileOutputStream(excel)
        workbook.write(excelOut)
        workbook.close()
        excelOut.close()
    }

    /**
     * 生成快递单测试用 Excel 文件
     * @param filename
     * @param shipOrderList
     */
    public static void createDeliveryTestWorkbook(String filename, List<ShipOrderAndGoodsDomain> shipOrderAndGoodsDomainList) {
        File excel = new File(filename)
        if (!excel.exists()) excel.createNewFile()

        Workbook workbook = new ExcelFile().workbook {
            styles {
                font("bold") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD)
                }

                font("normal") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_NORMAL)
                }

                cellStyle("header") { CellStyle cellStyle ->
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
                }
            }

            data {
                // data
                sheet("Export") {
                    header(["快递单ID","省", "市", "区", "学校", "序列", "收货人", "电话", "详细地址", "配送方式", "物流公司", "物流单号", "价格", "重量", "备注"])

                    shipOrderAndGoodsDomainList.eachWithIndex {shipOrderAndGoodsDomain, index ->
                        row([shipOrderAndGoodsDomain.expressId, shipOrderAndGoodsDomain.province, shipOrderAndGoodsDomain.city, shipOrderAndGoodsDomain.district,
                             shipOrderAndGoodsDomain.school, index + 1, shipOrderAndGoodsDomain.consignee, shipOrderAndGoodsDomain.mobile, shipOrderAndGoodsDomain.address,
                             shipOrderAndGoodsDomain.delivery, "圆通快递", shipOrderAndGoodsDomain.express != null ? shipOrderAndGoodsDomain.express.expressNumber : "", "", "", ""])
                    }
                }
            }

            commands {
                applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..12)
                applyCellStyle(font: "normal", rows: 2..(shipOrderAndGoodsDomainList.size() + 2), columns: 1..12)
                applyColumnWidth(columns: 5, width: 10000)
                applyColumnWidth(columns: 8, width: 3000)
                applyColumnWidth(columns: 9, width: 10000)
                // mergeCells(rows: 1, columns: 1..3)
            }
        }

        def excelOut = new FileOutputStream(excel)
        workbook.write(excelOut)
        workbook.close()
        excelOut.close()
    }

    /**
     * 生成发货结果 Excel 文件
     * @param filename
     * @param expresses
     */
    public static void createDispatchedWorkbook(String filename, List<ExpressWorkProcessDomain> expresses) {
        File excel = new File(filename)
        if (!excel.exists()) excel.createNewFile()

        Workbook workbook = new ExcelFile().workbook {
            styles {
                font("bold") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD)
                }

                font("normal") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_NORMAL)
                }

                cellStyle("header") { CellStyle cellStyle ->
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
                }
            }

            data {
                // data
                sheet("Export") {
                    header(["快递单ID", "物流公司", "物流单号", "类型", "配送方式", "是否导回", "物流价格", "收货人", "收货人电话", "学校名称", "省", "市", "区", "详细地址", "重量"])

                    expresses.each{express ->
                        row([express.expressId, express.expressCompany, express.expressNumber, express.type, express.delivery,
                             "是", express.price != null ? express.price/100 : "", express.expressConsignee, express.expressMobile, express.expressSchool,
                             express.expressProvince, express.expressCity, express.expressDistrict, express.expressAddress, express.weight])
                    }
                }
            }

            commands {
                applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..14)
                applyCellStyle(font: "normal", rows: 2..(expresses.size() + 2), columns: 1..14)
                applyColumnWidth(columns: 3, width: 5000)
                applyColumnWidth(columns: 10, width: 10000)
                applyColumnWidth(columns: 14, width: 10000)
                // mergeCells(rows: 1, columns: 1..3)
            }
        }

        def excelOut = new FileOutputStream(excel)
        workbook.write(excelOut)
        workbook.close()
        excelOut.close()
    }

    /**
     * 生成发货单 学生 Excel 文件
     * @param filename
     * @param shipOrderList
     */
    public static void createStudentShipOrderWorkbook(String filename, List<ShipOrderAndGoodsDomain> shipOrderAndGoodsDomainList, Integer sequence) {
        File excel = new File(filename)
        if (!excel.exists()) excel.createNewFile()

        Workbook workbook = new ExcelFile().workbook {
            styles {
                font("bold") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD)
                }

                font("normal") { Font font ->
                    font.setBoldweight(Font.BOLDWEIGHT_NORMAL)
                }

                cellStyle("header") { CellStyle cellStyle ->
                    cellStyle.setAlignment(CellStyle.ALIGN_CENTER)
                }
            }

            data {
                // data
                sheet("Export") {
                    header(["收货地址", "学校名称", "教师信息", "下单人姓名", "班级", "总数", "奖品明细", "配送方式", "序列", "收件人", "收件电话"])

                    shipOrderAndGoodsDomainList.eachWithIndex {shipOrderAndGoodsDomain, index ->
                        def orderGoodsList = shipOrderAndGoodsDomain.shipOrderGoodsList
                        def goodsString = new StringBuilder();
                        orderGoodsList.each {
                            goodsString.append(it.goodsName).append("_X").append(it.goodsCount)
                            if (it != orderGoodsList.last()) {
                                goodsString.append("\n")
                            }
                        }
                        row([shipOrderAndGoodsDomain.address, shipOrderAndGoodsDomain.school,
                             shipOrderAndGoodsDomain.consignee + "\n" + shipOrderAndGoodsDomain.mobile + "\n" + shipOrderAndGoodsDomain.subject,
                             shipOrderAndGoodsDomain.userName, shipOrderAndGoodsDomain.theClass, shipOrderAndGoodsDomain.totalCount,
                             goodsString.toString(), shipOrderAndGoodsDomain.delivery, sequence,
                             shipOrderAndGoodsDomain.express.expressConsignee, shipOrderAndGoodsDomain.express.expressMobile])
                    }
                }
            }

            commands {
                applyCellStyle(cellStyle: "header", font: "bold", rows: 1, columns: 1..11)
                applyCellStyle(font: "normal", rows: 2..(shipOrderAndGoodsDomainList.size() + 2), columns: 1..11)
                applyColumnWidth(columns: 1, width: 10000)
                applyColumnWidth(columns: 2, width: 10000)
                applyColumnWidth(columns: 3, width: 5000)
                applyColumnWidth(columns: 5, width: 5000)
                applyColumnWidth(columns: 7, width: 10000)
                // mergeCells(rows: 1, columns: 1..3)
            }
        }

        def excelOut = new FileOutputStream(excel)
        workbook.write(excelOut)
        workbook.close()
        excelOut.close()
    }
}
