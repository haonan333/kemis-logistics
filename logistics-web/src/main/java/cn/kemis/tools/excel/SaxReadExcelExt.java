package cn.kemis.tools.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.CellValueType;
import org.jeecgframework.poi.excel.entity.sax.SaxReadCellEntity;
import org.jeecgframework.poi.excel.imports.sax.parse.ISaxRowRead;
import org.jeecgframework.poi.excel.imports.sax.parse.SaxRowRead;
import org.jeecgframework.poi.exception.excel.ExcelImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class SaxReadExcelExt {


    private static final Logger LOGGER = LoggerFactory.getLogger(SaxReadExcelExt.class);

    /**
     * 单元格节点为v的数据类型
     */
    enum xssfDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX,NUMBER,
    }

    int countrows = 0;

    /**
     * 自定义数据处理Handler
     */
    class MyXSSFSheetHandler extends DefaultHandler {

        /**
         * 表格样式
         */
        private StylesTable stylesTable;

        /**
         * Table with unique strings
         */
        private ReadOnlySharedStringsTable sharedStringsTable;
        /**
         * 导入后的数据集合
         */
        private List<SaxReadCellEntity> rowlist = new ArrayList<>();
        private ISaxRowRead read;
        //当前行
        private int                     curRow  = 0;
        //每次从单元格内取值时,放到value中,下次清空
        private StringBuffer value = new StringBuffer();

//        /**
//         * Number of columns to read starting with leftmost
//         */
//        private final int minColumnCount =1;
//
//        // Set when V start element is seen
        private boolean vIsOpen;

        //下一单元格数据类型
        private xssfDataType nextDataType;

        // Used to format numeric cell values.
        private short formatIndex;
        private String formatString;
        private final DataFormatter formatter;

        private int thisColumn = -1;
        // 上一次读取的列标
        private int lastColumnNumber = -1;

        /**
         * 初始化构造方法
         * @param styles
         * @param strings
         * @param rowRead
         */
        public MyXSSFSheetHandler(StylesTable styles,ReadOnlySharedStringsTable strings,ISaxRowRead rowRead) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
            this.read = rowRead;
        }


        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {

            if ("inlineStr".equals(name) || "v".equals(name) || "t".equals(name)) {
                vIsOpen = true;
                // Clear contents cache
                value.setLength(0);
            }
            //c 代表单元格
            else if ("c".equals(name)) {
                // 获得单元格的引用
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));

                // 设置默认值.
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType))
                    nextDataType = xssfDataType.BOOL;
                else if ("e".equals(cellType))
                    nextDataType = xssfDataType.ERROR;
                else if ("inlineStr".equals(cellType))
                    nextDataType = xssfDataType.INLINESTR;
                else if ("s".equals(cellType))
                    nextDataType = xssfDataType.SSTINDEX;
                else if ("str".equals(cellType))
                    nextDataType = xssfDataType.FORMULA;
                else if (cellStyleStr != null) {
                    // It's a number, but almost certainly one
                    //  with a special style or format
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null)
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                }
            }

        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            String thisStr = null;
//            PrintStream output = System.out;

            // v 代表单元格内容
            if ("v".equals(name) || "t".equals(name)) {

                switch (nextDataType) {

                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;

                    case FORMULA:
                        thisStr = value.toString() ;
                        break;

                    case INLINESTR:
                        XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                        thisStr = rtsi.toString();
                        break;

                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                            thisStr = rtss.toString() ;
                        } catch (NumberFormatException ex) {
                            LOGGER.error("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
                        }
                        break;

                    case NUMBER:
                        String n = value.toString();

                        if (n.contains("E") || n.contains("e")) {
                            DecimalFormat df = new DecimalFormat("0");
                            thisStr = df.format(Double.parseDouble(n));
                        } else {
                            if (this.formatString != null)
                                thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                            else
                                thisStr = n;
                        }
                        break;

                    default:
                        thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                        break;
                }

                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
                //填充空值到集合
                int temp = 0;
                if(curRow >0 && thisColumn > 0){
                    if(lastColumnNumber ==0) {
                        temp = thisColumn - lastColumnNumber -1;
                        if(temp < 0 )
                            temp = 0;
                    }else{
                        temp = thisColumn - lastColumnNumber - 1;
                    }
                }
//                for (int i = lastColumnNumber; i < thisColumn; ++i) {
                if(temp>0) {
                    if(thisColumn > 0 && rowlist.size() == 0)
                        temp++;
                    for (int i = 0; i < temp; i++) {
//                    output.print(',');
                        //表头不填充空值
                        if (curRow > 0) {
                            rowlist.add(new SaxReadCellEntity(CellValueType.String, ""));
                        }
                    }
                }

                //添加内容到集合
//                output.print(thisStr);
                rowlist.add(new SaxReadCellEntity(CellValueType.String, thisStr));

                // 更新前一列标
                if (thisColumn > -1)
                    lastColumnNumber = thisColumn;

            } else if ("row".equals(name)) {// row 代表一行结束

//                if (minColumns > 0) {
//                    // Columns are 0 based
//                    if (lastColumnNumber == -1) {
//                        lastColumnNumber = 0;
//                    }
//                    for (int i = lastColumnNumber; i < (this.minColumnCount); i++) {
////                        output.print(',');
//                        rowlist.add(new SaxReadCellEntity(CellValueType.String, ""));
//                    }
//                }
                //将当前行解析成实体对象
                read.parse(curRow, rowlist);
                rowlist.clear();
                curRow++;
//                output.println(countrows++);
                lastColumnNumber = -1;
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("curRow = " + curRow);
                }

            }

        }

        /**
         * 取得单元格内容 节点v 或 t
         * @param ch
         * @param start
         * @param length
         * @throws SAXException
         */
        public void characters(char[] ch, int start, int length)
                throws SAXException {
//            if (vIsOpen)
                value.append(ch, start, length);
        }


        /**
         * 转成数字索引 A1 -> 0
         * @param name
         * @return
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }

    }

    ///////////////////////////////////////

    /**
     * 解析工作表
     *
     * @param styles
     * @param sharedStringsTable
     * @param sheetInputStream
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable sharedStringsTable,
            InputStream sheetInputStream,ISaxRowRead rowRead)
            throws IOException, ParserConfigurationException, SAXException {

        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader parser = saxParser.getXMLReader();
        ContentHandler handler = new MyXSSFSheetHandler(styles, sharedStringsTable,rowRead);
        parser.setContentHandler(handler);
        parser.parse(sheetSource);
    }

    /**
     * 导入数据并转换成集合对象.
     *
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public  <T> List<T>  process(InputStream inputstream,Class<?> pojoClass, ImportParams params) {

        try {
            OPCPackage opcPackage = OPCPackage.open(inputstream);

            ReadOnlySharedStringsTable sharedStringsTable = new ReadOnlySharedStringsTable(opcPackage);
            XSSFReader xssfReader = new XSSFReader(opcPackage);

            StylesTable styles = xssfReader.getStylesTable();
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            ISaxRowRead rowRead = new SaxRowRead(pojoClass, params, null);
            while (iter.hasNext()) {
                InputStream stream = iter.next();
                processSheet(styles, sharedStringsTable, stream, rowRead);
                stream.close();
            }
            return rowRead.getList();
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            throw new ExcelImportException("SAX导入数据失败");
        }
    }


}
