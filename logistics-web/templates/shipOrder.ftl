<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>${title}</title>
    <style type="text/css">
        body {
            margin: 0;
            /*font-family: Arial Unicode MS;*/
            /*font-family: "Songti SC";*/
            font-family: NSimSun;
            font-size: 12pt;
        }

        table {
            margin: auto;
            width: 100%;
            border-collapse: collapse;
            border: 1px solid #444444;
        }

        th, td {
            border: 1px solid #444444;
            font-size: 11pt;
            /*margin-left: 5px;*/
            padding: 2pt 2pt;
        }

        thead {
            text-align: center;
        }

        div.header-left {display: none}
        div.header-right {display: none}
        div.footer-left {display: none}
        div.footer-right {display: none}
        @media print {
            div.header-left {
                display: block;
                position: running(header-left);
            }
            div.header-right {
                display: block;
                position: running(header-right);
            }
            div.footer-left {
                display: block;
                position: running(footer-left);
            }
            div.footer-center {
                display: block;
                position: running(footer-center);
            }
            div.footer-right {
                display: block;
                position: running(footer-right);
            }
        }

        @page {
            margin:25pt;
            size: 8.5in 11.5in;
            @top-left{
                content:element(header-left);
            };
            @top-right {
                content: element(header-right)
            };
            @bottom-left {
                content: element(footer-left)
            };
            @bottom-center {
                content: "page " counter(page) " of  " counter(pages);
            }
            @bottom-right {
                content: element(footer-right)
            };
        }

        /* flying-saucer-pdf tr 跨页问题 */
        table { page-break-inside:auto; -fs-table-paginate:paginate;border-spacing: 0;border: 1px solid white; }
        tr    { page-break-inside:avoid; page-break-after:auto;}
        thead { display:table-header-group; }
        tfoot { display:table-footer-group; }
    </style>
</head>
<body>
<div id="header">
    <!--***************页眉_start*****************-->
    <div id="header-left" class="header-left" align="left">
        ${express.expressConsignee?default("")} ${express.expressMobile?default("")}
    </div>
    <div id="header-right" class="header-right" align="right">
        ${express.expressId?default("")}.${school?default("")}
    </div>
    <!--***************页眉_end*****************-->
</div>
<div id="footer">
    <!--***************页脚_start*****************-->
    <div id="footer-left" class="footer-left" align="left"></div>
    <div id="footer-center" class="footer-center" align="left"></div>
    <div id="footer-right" class="footer-right" align="right">
    </div>
    <!--***************页脚_endt*****************-->
</div>

<div>
    <table style="repeat-header:yes;repeat-footer:yes;" cellspacing="0" cellpadding="0">
        <thead>
        <tr>
            <th style="width: 21%;border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;">收货地址</th>
            <th style="width: 14%;border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;">学校名称</th>
            <th style="width: 11%;padding-right: 5pt; border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;border-right: 1px solid #000000;">教师信息</th>
            <th style="width: 9%;padding-left: 5pt; border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;">下单人姓名</th>
            <th style="width: 11%;border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;">班级</th>
            <th style="width: 6%;border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;">总数</th>
            <th style="width: 28%;border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-top: 1px solid #000000;border-right: 1px solid #000000;">奖品明细</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <th colspan="7" style="border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-right: 1px solid #000000;">
            ${teachers?size}
            <#list teachers as teacher>
            ${teacher}<#if teacher_has_next>,</#if>
            </#list>
            </th>
        </tr>
        </tfoot>
	<#list data as record>
        <tr>
            <td style="border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;">${record.province?default("")}${record.city?default("")}${record.district?default("")}${record.address?default("")}</td>
            <td style="border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;">${record.school?default("")}</td>
            <td style="border: 0px; padding-right: 5pt; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-right: 1px solid #000000;">${record.consignee?default("")}<br/><b>${record.mobile?default("")}</b><br/>${record.subject?default("")}</td>
            <td style="border: 0px; padding-left: 5pt; border-bottom: 1px solid #000000; border-left: 1px solid #000000;">${record.userName?default("")}</td>
            <td style="border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;">${record.theClass?default("")}</td>
            <td style="border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;">${record.totalCount?default("")}</td>
            <td style="border: 0px; border-bottom: 1px solid #000000; border-left: 1px solid #000000;border-right: 1px solid #000000;">
                <#list record.shipOrderGoodsList as shipOrderGoods>
                    ${shipOrderGoods.goodsName}_X${shipOrderGoods.goodsCount}<br/>
                </#list>
            </td>
        </tr>
	</#list>
    </table>
</div>
</body>
</html>
