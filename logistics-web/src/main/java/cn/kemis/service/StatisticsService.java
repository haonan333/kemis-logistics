package cn.kemis.service;

import cn.kemis.Constants;
import cn.kemis.dao.mapper.ExpressMapper;
import cn.kemis.dao.mapper.SysConfigurationMapper;
import cn.kemis.dao.mapper.SysUserMapper;
import cn.kemis.dao.mapper.WorkProcessMapper;
import cn.kemis.domain.StatisticSalaryDomain;
import cn.kemis.domain.StatisticsDomain;
import cn.kemis.domain.StatisticsInfoDomain;
import cn.kemis.domain.api.base.BasePageResponse;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.domain.excelEntity.SalaryEntity;
import cn.kemis.domain.request.ExportSalaryRequest;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.*;
import cn.kemis.util.FileUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by xuhailong on 16/8/24.
 */
@Service
public class StatisticsService {


    Logger log = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    private WorkProcessMapper workProcessMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SysConfigurationMapper sysConfigurationMapper;

    @Autowired
    private ExpressMapper expressMapper;

    @Value("${file.server.url.download}")
    private String downloadPath;

    @Autowired
    private ExportFileService exportFileService;

    /**
     * 计件统计
     * @param request
     * @return
     */
    public Map<String,Object> statistics(HttpServletRequest request) {
        String username = request.getParameter("username");
        String realName = request.getParameter("realName");
        String workProcess = request.getParameter("workProcess");
        String queryDate = request.getParameter("querydate");

        String orderName = request.getParameter("orderColumn");
        String orderDir = request.getParameter("orderDir");

        Map<String,Object> paramMap = new HashMap<>();

        //获取 userId, ROLE_USER 只能查询自己, ROLE_ADMIN 可以指定 username 查询,为空 默认查所有
        Integer userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
        if (roles.contains("ROLE_USER")) {
            //如果是 user角色 则只能统计自己的数量
            SysUser user = (SysUser) auth.getPrincipal();
            userId = user.getSysUserId();
        } else {
            //如果是admin角色 查询 username 对应的 userId
            SysUserExample sysUserExample = new SysUserExample();
            SysUserExample.Criteria criteria = sysUserExample.createCriteria();

            boolean hasSearch = false;

            if (StringUtils.isNotBlank(username)) {
                criteria.andUsernameEqualTo(username.trim());
                hasSearch = true;
            }

            if (StringUtils.isNotBlank(realName)) {
                criteria.andRealNameEqualTo(realName);
                hasSearch = true;
            }

            if (hasSearch) {
                List<SysUser> user = sysUserMapper.selectByExample(sysUserExample);
                if (user == null || user.size() <= 0) {
                    throw new KemisException("用户不存在");
                }

                userId = user.get(0).getSysUserId();
            }
        }
        paramMap.put("userId",userId);
        if (StringUtils.isNotBlank(workProcess)) {
            paramMap.put("workProcess",workProcess);
        }

        //获取查询时间
        if (StringUtils.isBlank(queryDate)) {
            throw new KemisException("请指定时间范围");
        }

        if (StringUtils.isNotBlank(queryDate)) {
            String[] dateStr = queryDate.trim().split("/");
            paramMap.put("startTime",dateStr[0].trim());
            paramMap.put("endTime",dateStr[1].trim());
        }

        int limit = Integer.parseInt(request.getParameter("limit"));
        int start = Integer.parseInt(request.getParameter("start"));
        int drawCount = Integer.parseInt(request.getParameter("draw"));

        int count = workProcessMapper.countUser(paramMap);
        List<StatisticsDomain> list = new ArrayList<>();
        if (count > 0) {
            paramMap.put("start",start);
            paramMap.put("limit",limit);
            paramMap.put("orderName",orderName);
            paramMap.put("orderDir",orderDir);
            list = workProcessMapper.selectInfoForSalaryByMap(paramMap);
        }

        List<SysConfiguration> sysConfigurations = sysConfigurationMapper.selectByExample(null);
        Map<String,String> map = new HashMap<>();
        sysConfigurations.forEach( sysConfiguration -> {
            map.put(sysConfiguration.getPropertyKey(),sysConfiguration.getPropertyValue());
        });

        list.forEach(statisticsDomain -> {
            switch (statisticsDomain.getWorkProcess()){
                case "划单":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.student.small.price")));
                        }
                    }
                    statisticsDomain.setCountProcess(statisticsDomain.getShipCutOrderCount());
                    break;
                case "捡货":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.student.big.price")));
                        }
                    }
                    statisticsDomain.setCountProcess(statisticsDomain.getShipPickingGoodsCount());
                    break;
                case "包装":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.student.small.price")));
                        }
                    }
                    statisticsDomain.setCountProcess(statisticsDomain.getShipWrappCount());
                    break;
                case "打包":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPackageCount(), map.get("workProcess.package.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPackageCount(), map.get("workProcess.package.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPackageCount(), map.get("workProcess.package.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPackageCount(), map.get("workProcess.package.piece.student.small.price")));
                        }
                    }
                    statisticsDomain.setCountProcess(statisticsDomain.getShipPackageCount());
                    break;
            }
        });



        Map<String,Object> result = new HashMap<String,Object>();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data",list);
        result.put("total",count);
        result.put("draw",drawCount);

        return result;
    }

    public List<StatisticSalaryDomain> statisticsForSalary(ExportSalaryRequest request) {
        String realName = request.getRealName();
        String workProcess = request.getWorkProcess();
        String queryDate = request.getQueryDate();

        Map<String,Object> paramMap = new HashMap<>();

        if (StringUtils.isNotBlank(workProcess)) {
            paramMap.put("workProcess",workProcess);
        }

        //获取查询时间
        if (StringUtils.isBlank(queryDate)) {
            throw new KemisException("请指定时间范围");
        }

        if (StringUtils.isNotBlank(queryDate)) {
            String[] dateStr = queryDate.trim().split("/");
            paramMap.put("startTime",dateStr[0].trim());
            paramMap.put("endTime",dateStr[1].trim());
        }
        List<StatisticsDomain> list = workProcessMapper.selectInfoForSalaryByMap(paramMap);


        List<SysConfiguration> sysConfigurations = sysConfigurationMapper.selectByExample(null);
        Map<String,String> map = new HashMap<>();
        sysConfigurations.forEach( sysConfiguration -> {
            map.put(sysConfiguration.getPropertyKey(),sysConfiguration.getPropertyValue());
        });

        Map<String,StatisticSalaryDomain> resultMap = new HashedMap();
        list.forEach(statisticsDomain -> {
            StatisticSalaryDomain resultRecord = resultMap.get(statisticsDomain.getUsername());
            if(resultRecord == null){
                resultRecord = new StatisticSalaryDomain();
                BeanUtils.copyProperties(statisticsDomain, resultRecord);
                resultMap.put(statisticsDomain.getUsername(),resultRecord);
            }
            switch (statisticsDomain.getWorkProcess()){
                case "划单":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipCutOrderCount(), map.get("workProcess.cutOrder.piece.student.small.price")));
                        }
                    }
                    resultRecord.setCutOrderCount(statisticsDomain.getCutOrderCount() + resultRecord.getCutOrderCount());
                    resultRecord.setShipCutOrderCount(statisticsDomain.getShipCutOrderCount() + resultRecord.getShipCutOrderCount());
                    resultRecord.setCutOrderSalary(calFloat(statisticsDomain.getSalary(), resultRecord.getCutOrderSalary()));
                    break;
                case "捡货":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.picking.piece.student.big.price")));
                        }
                    }
                    resultRecord.setPickingGoodsCount(statisticsDomain.getPickingGoodsCount()+resultRecord.getPickingGoodsCount());
                    resultRecord.setShipPickingGoodsCount(statisticsDomain.getShipPickingGoodsCount()+resultRecord.getShipPickingGoodsCount());
                    resultRecord.setPickingGoodsSalary(calFloat(statisticsDomain.getSalary(), resultRecord.getCutOrderSalary()));
                    break;
                case "包装":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipWrappCount(), map.get("workProcess.wrapp.piece.student.small.price")));
                        }
                    }
                    resultRecord.setWrappCount(statisticsDomain.getWrappCount()+resultRecord.getWrappCount());
                    resultRecord.setShipWrappCount(statisticsDomain.getShipWrappCount()+resultRecord.getShipWrappCount());
                    resultRecord.setWrappSalary(calFloat(statisticsDomain.getSalary(), resultRecord.getCutOrderSalary()));
                    break;
                case "打包":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.package.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.package.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.package.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getShipPickingGoodsCount(), map.get("workProcess.package.piece.student.small.price")));
                        }
                    }
                    resultRecord.setPackageCount(statisticsDomain.getPackageCount()+resultRecord.getPackageCount());
                    resultRecord.setShipPackageCount(statisticsDomain.getShipPackageCount()+resultRecord.getShipPackageCount());
                    resultRecord.setPackageSalary(calFloat(statisticsDomain.getSalary(), resultRecord.getCutOrderSalary()));
                    break;
            }
            //
            resultMap.put(statisticsDomain.getUsername(), resultRecord);
        });


        List<StatisticSalaryDomain> resultList = new ArrayList<>(resultMap.size());
        for (StatisticSalaryDomain domain : resultMap.values()) {
            domain.setSumSalary(calFloat(domain.getCutOrderSalary(),domain.getPickingGoodsSalary(),domain.getWrappSalary()
            ,domain.getPackageSalary()));
            resultList.add(domain);
        }

        return resultList;
    }

    /**
     * 计件详情查询
     * @param request
     * @return
     */
    public BasePageResponse infoSearch(HttpServletRequest request) {
        BasePageResponse result = new BasePageResponse();
        BaseResponse response = new BaseResponse();

        try {
            String realName = request.getParameter("realName");
            String expressSchool = request.getParameter("expressSchool");
            String expressConsignee = request.getParameter("expressConsignee");
            String expressMobile = request.getParameter("expressMobile");
            String expressNumber = request.getParameter("expressNumber");
            String type = request.getParameter("type");
            String delivery = request.getParameter("delivery");
            String queryDate = request.getParameter("querydate");
            Map<String,Object> paramMap = new HashMap<>();

            if(StringUtils.isNotBlank(realName))
                paramMap.put("realName", realName);
            //获取查询时间
            if (StringUtils.isNotBlank(queryDate)) {
                String[] dateStr = queryDate.trim().split("/");
                paramMap.put("startTime",dateStr[0].trim());
                paramMap.put("endTime",dateStr[1].trim());
            }
            if (StringUtils.isNotBlank(expressSchool))
                paramMap.put("expressSchool",expressSchool);
            if (StringUtils.isNotBlank(expressConsignee))
                paramMap.put("expressConsignee",expressConsignee);
            if (StringUtils.isNotBlank(expressMobile))
                paramMap.put("expressMobile",expressMobile);

            if (StringUtils.isNotBlank(expressNumber))
                paramMap.put("expressNumber",expressNumber);
            if (StringUtils.isNotBlank(type))
                paramMap.put("type",type);
            if (StringUtils.isNotBlank(delivery))
                paramMap.put("delivery",delivery);

            int limit = Integer.parseInt(request.getParameter("limit"));
            int start = Integer.parseInt(request.getParameter("start"));
            int drawCount = Integer.parseInt(request.getParameter("draw"));

            int count = workProcessMapper.countWorkProcessInfoByMap(paramMap);

            List<StatisticsInfoDomain> list = new ArrayList<>();
            if (count > 0) {
                paramMap.put("start", start);
                paramMap.put("limit", limit);
                list = workProcessMapper.selectWorkProcessInfoByMap(paramMap);
            }

            response.setCode("0");
            response.setMsg("success");
            response.setData(list);
            response.setTotal(count);
            response.setDraw(drawCount);

            result.setData(response);
            return result;
        } catch (NumberFormatException e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-1");
            response.setMsg("未知异常");
            result.setData(response);
            return result;
        }
    }

    /**
     * 工资统计
     * @param request
     * @return
     */
    @Deprecated
    public BasePageResponse salarySearch(HttpServletRequest request) {
        BasePageResponse result = new BasePageResponse();
        BaseResponse response = new BaseResponse();

        try {
            String realName = request.getParameter("realName");
            String workProcess = request.getParameter("workProcess");
            String queryDate = request.getParameter("querydate");
            Map<String,Object> paramMap = new HashMap<>();

            if(StringUtils.isNotBlank(realName))
                paramMap.put("realName", realName);
            if(StringUtils.isNotBlank(workProcess))
                paramMap.put("workProcess", workProcess);
            //获取查询时间
            if (StringUtils.isNotBlank(queryDate)) {
                String[] dateStr = queryDate.trim().split("/");
                paramMap.put("startTime",dateStr[0].trim());
                paramMap.put("endTime",dateStr[1].trim());
            }


            int limit = Integer.parseInt(request.getParameter("limit"));
            int start = Integer.parseInt(request.getParameter("start"));
            int drawCount = Integer.parseInt(request.getParameter("draw"));

            int count = workProcessMapper.countSalaryByMap(paramMap);

            List<StatisticSalaryDomain> list = new ArrayList<>();
            if (count > 0) {
                paramMap.put("start", start);
                paramMap.put("limit", limit);
                list = workProcessMapper.selectSalaryByMap(paramMap);
            }

            List<SysConfiguration> sysConfigurations = sysConfigurationMapper.selectByExample(null);
            Map<String,String> map = new HashMap<>();
            sysConfigurations.forEach( sysConfiguration -> {
                map.put(sysConfiguration.getPropertyKey(),sysConfiguration.getPropertyValue());
            });


            list.forEach(entity -> {
                    float cutOrderSalary = calSalary(entity.getCutOrderCount(), map.get("workProcess.cutOrder.piece.price"));
                    float pickingGoodsSalary = calSalary(entity.getPickingGoodsCount(), map.get("workProcess.picking.piece.price"));
                    float wrappSalary = calSalary(entity.getWrappCount(), map.get("workProcess.wrapp.piece.price"));
                    float sumSalary = cutOrderSalary + pickingGoodsSalary + wrappSalary ;
                    entity.setCutOrderSalary(floatToString(cutOrderSalary));
                    entity.setPickingGoodsSalary(floatToString(pickingGoodsSalary));
                    entity.setWrappSalary(floatToString(wrappSalary));
                    entity.setSumSalary(floatToString(sumSalary));
                }

            );


            response.setCode("0");
            response.setMsg("success");
            response.setData(list);
            response.setTotal(count);
            response.setDraw(drawCount);

            result.setData(response);
            return result;
        } catch (NumberFormatException e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-1");
            response.setMsg("未知异常");
            result.setData(response);
            return result;
        }
    }

    /**
     * 计件统计 工资导出
     * @param response
     * @param request
     * @throws Exception
     */
    public void exportSalary(HttpServletResponse response, ExportSalaryRequest request) throws Exception {
        // 生成文件数据
        ExportFile exportFile = getExportFile(request);
        List<StatisticSalaryDomain> list = statisticsForSalary(request);

        // 生成文件
        if (list != null && list.size() > 0) {
            String path = downloadPath + FileUtil.getFilePath(exportFile.getFileGuid());
            String fileName = path + exportFile.getFileName() ;

            createExcelForAnnotation(path,fileName, list);

            // 入库
            exportFileService.insertSelective(exportFile);

            if (exportFile != null) {
                OutputStream os = response.getOutputStream();
                try {
                    String excelFileName = exportFile.getFileName();
                    String file = path + exportFile.getFileName();
                    response.reset();
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelFileName, "UTF-8"));
                    response.setContentType("application/octet-stream; charset=utf-8");
                    os.write(FileUtils.readFileToByteArray(new File(file)));
                    os.flush();
                } finally {
                    if (os != null) {
                        os.close();
                    }
                }
            }
        }
    }

    /**
     * createExcel
     * @throws Exception
     */
    public void createExcelForAnnotation(String path,String fileName,List<StatisticSalaryDomain> list) throws Exception {
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);

        List<SalaryEntity> rows = new ArrayList<SalaryEntity>();
        for (StatisticSalaryDomain domain : list) {
            SalaryEntity row = new SalaryEntity();
            BeanUtils.copyProperties(domain, row);
            rows.add(row);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(params,SalaryEntity.class, rows);

        File savefile = new File(path);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
    }
    /**
     * 根据查询条件生成文件名,组装导出文件信息
     * @return
     */
    private ExportFile getExportFile(ExportSalaryRequest request) {
        String queryDate = request.getQueryDate();

        //获取查询时间
        String[] dateStr = queryDate.trim().split("/");
        String startStr = dateStr[0].trim();
        String endStr = dateStr[1].trim();


        String fileGuid = UUID.randomUUID().toString();
        String fileSavePath = FileUtil.getFilePath(fileGuid);
        String connector = "_";
        String suffix = ".xlsx";

        ExportFile exportFile = new ExportFile();
        exportFile.setFileGuid(fileGuid);

        StringBuilder fileName = new StringBuilder();
        fileName.append(startStr);
        fileName.append("至").append(endStr);
        fileName.append("_工资统计");
        fileName.append(suffix);

        exportFile.setFileName(fileName.toString());
        exportFile.setFileUrl(fileSavePath + fileGuid + suffix);
        exportFile.setFileSuffix("xlsx");
        exportFile.setType(Constants.EXPORT_FILE_TYPE_SALARY);
        exportFile.setBatchNo("");
        return exportFile;
    }
    /**
     * 工资统计
     * @param request
     * @return
     */
    @Deprecated
    public List<StatisticSalaryDomain> exportSalarySearch(ExportSalaryRequest request) {

        try {
            String realName = request.getRealName();
            String workProcess = request.getWorkProcess();
            String queryDate = request.getQueryDate();
            Map<String,Object> paramMap = new HashMap<>();

            if(StringUtils.isNotBlank(realName))
                paramMap.put("realName", realName);
            if(StringUtils.isNotBlank(workProcess))
                paramMap.put("workProcess", workProcess);
            //获取查询时间
            if (StringUtils.isNotBlank(queryDate)) {
                String[] dateStr = queryDate.trim().split("/");
                paramMap.put("startTime",dateStr[0].trim());
                paramMap.put("endTime",dateStr[1].trim());
            }
            List<StatisticSalaryDomain> list =  workProcessMapper.selectSalaryByMap(paramMap);

            List<SysConfiguration> sysConfigurations = sysConfigurationMapper.selectByExample(null);
            Map<String,String> map = new HashMap<>();
            sysConfigurations.forEach( sysConfiguration -> {
                map.put(sysConfiguration.getPropertyKey(),sysConfiguration.getPropertyValue());
            });

//            list.forEach(entity -> {
//                        float cutOrderSalary = calSalary(entity.getCutOrderCount(), map.get("workProcess.cutOrder.piece.price"));
//                        float pickingGoodsSalary = calSalary(entity.getPickingGoodsCount(), map.get("workProcess.picking.piece.price"));
//                        float wrappSalary = calSalary(entity.getWrappCount(), map.get("workProcess.wrapp.piece.price"));
//                        float sumSalary = cutOrderSalary + pickingGoodsSalary + wrappSalary ;
//                        entity.setCutOrderSalary(floatToString(cutOrderSalary));
//                        entity.setPickingGoodsSalary(floatToString(pickingGoodsSalary));
//                        entity.setWrappSalary(floatToString(wrappSalary));
//                        entity.setSumSalary(floatToString(sumSalary));
//                    }
//            );
            return list;

        } catch (NumberFormatException e) {
            log.error("exception msg " + e.getMessage());
        }
        return null;
    }

    /**
     * 获取当前用户
     */
    private SysUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SysUser user = (SysUser) auth.getPrincipal();
        if (user == null){
            throw new KemisException("登录超时");
        }

        user = userService.findById(user.getSysUserId());
        if (user == null || user.getStatus() != 0){
            throw new KemisException("非法用户");
        }
        return user;
    }

    public Map<String, Object> userStatistics(HttpServletRequest request) {
        String queryDate = request.getParameter("querydate");

        Map<String, Object> paramMap = new HashMap<>();

        SysUser user = this.getCurrentUser();
        paramMap.put("userId", user.getSysUserId());

        //获取查询时间
        if (StringUtils.isBlank(queryDate)) {
            throw new KemisException("请指定时间范围");
        }

        if (StringUtils.isNotBlank(queryDate)) {
            String[] dateStr = queryDate.trim().split("/");
            paramMap.put("startTime", dateStr[0].trim());
            paramMap.put("endTime", dateStr[1].trim());
        }

        List<SysConfiguration> sysConfigurations = sysConfigurationMapper.selectByExample(null);
        Map<String,String> map = new HashMap<>();
        sysConfigurations.forEach( sysConfiguration -> {
            map.put(sysConfiguration.getPropertyKey(),sysConfiguration.getPropertyValue());
        });
        //查询 划单、捡货、包装是关联查 ship_order的数量 
        paramMap.put("workProcess", "cutOrder,pickingGoods,wrapp");
        List<StatisticsDomain> list = workProcessMapper.countProcessForSalary(paramMap);
        // 打包是快递单号的数量
        paramMap.put("workProcess", "package");
        List<StatisticsDomain> packageList = workProcessMapper.countProcessForSalary(paramMap);
        list.addAll(packageList);

        list.forEach(statisticsDomain -> {
            switch (statisticsDomain.getWorkProcess()){
                case "划单":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.cutOrder.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.cutOrder.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.cutOrder.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.cutOrder.piece.student.small.price")));
                        }
                    }
                    break;
                case "捡货":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.picking.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.picking.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.picking.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.picking.piece.student.big.price")));
                        }
                    }
                    break;
                case "包装":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.wrapp.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.wrapp.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.wrapp.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.wrapp.piece.student.small.price")));
                        }
                    }
                    break;
                case "打包":
                    if(statisticsDomain.getOrderType().equals("老师快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.package.piece.tech.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.package.piece.tech.small.price")));
                        }
                    }
                    if(statisticsDomain.getOrderType().equals("学生快递")){
                        if(statisticsDomain.getPackageType().equals("大包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.package.piece.student.big.price")));
                        }
                        if(statisticsDomain.getPackageType().equals("小包")){
                            statisticsDomain.setSalary(getSalary(statisticsDomain.getCount(), map.get("workProcess.package.piece.student.small.price")));
                        }
                    }
                    break;
            }
        });


        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("msg", "success");
        result.put("data", list);

        return result;
    }

    private String getSalary(int count, String price) {
        String result = "0.00";
        if (StringUtils.isBlank(price)) {
            return result;
        }
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        float i = (float) (count * Integer.parseInt(price)) / 100;
        return df.format(i);
    }
    private float calSalary(int count,String price) {
        return (float) (count * Integer.parseInt(price)) ;
    }

    private String floatToString(float price) {
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        return df.format(price/100);
    }


    private String calFloat(String price1,String price2) {
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        if (StringUtils.isBlank(price1)) {
            price1 = "0";
        }
        if (StringUtils.isBlank(price2)) {
            price2 = "0";
        }
        Float price = Float.parseFloat(price1) + Float.parseFloat(price2);
        return df.format(price);
    }
    private String calFloat(String price1,String price2,String price3,String price4) {
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        if (StringUtils.isBlank(price1)) {
            price1 = "0";
        }
        if (StringUtils.isBlank(price2)) {
            price2 = "0";
        }
        if (StringUtils.isBlank(price3)) {
            price3 = "0";
        }
        if (StringUtils.isBlank(price4)) {
            price4 = "0";
        }
        Float price = Float.parseFloat(price1) + Float.parseFloat(price2)
                + Float.parseFloat(price3)
                + Float.parseFloat(price4);
        return df.format(price);
    }

    /**
     * 未打包数据统计
     * @param request
     * @return response
     */
    public BasePageResponse statisticsUnPackage(HttpServletRequest request) {
        String expressId = request.getParameter("expressId");
        String batchNo = request.getParameter("batchNo");
        String expressNumber = request.getParameter("expressNumber");
        String expressConsignee = request.getParameter("expressConsignee");
        String expressMobile = request.getParameter("expressMobile");
        String expressProvince = request.getParameter("expressProvince");
        String expressCity = request.getParameter("expressCity");
        String expressDistrict = request.getParameter("expressDistrict");
        String queryDate = request.getParameter("querydate");

        Map<String,Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(expressId)) {
            paramMap.put("expressId",expressId.trim());
        }
        if (StringUtils.isNotBlank(batchNo)) {
            paramMap.put("batchNo",batchNo.trim());
        }
        if (StringUtils.isNotBlank(expressNumber)) {
            paramMap.put("expressNumber",expressNumber.trim());
        }
        if (StringUtils.isNotBlank(expressConsignee)) {
            paramMap.put("expressConsignee",expressConsignee.trim());
        }
        if (StringUtils.isNotBlank(expressMobile)) {
            paramMap.put("expressMobile",expressMobile.trim());
        }
        if (StringUtils.isNotBlank(expressProvince)) {
            paramMap.put("expressProvince",expressProvince.trim());
        }
        if (StringUtils.isNotBlank(expressCity)) {
            paramMap.put("expressCity",expressCity.trim());
        }
        if (StringUtils.isNotBlank(expressDistrict)) {
            paramMap.put("expressDistrict",expressDistrict.trim());
        }
        //获取查询时间
        if (StringUtils.isNotBlank(queryDate)) {
            String[] dateStr = queryDate.trim().split("/");
            paramMap.put("startTime",dateStr[0].trim());
            paramMap.put("endTime",dateStr[1].trim());
        }

        int count = expressMapper.countUnPackageByMap(paramMap);
        List<Express> list = new ArrayList<>();
        if (count > 0) {
            paramMap.put("start",Integer.parseInt(request.getParameter("start")));
            paramMap.put("limit",Integer.parseInt(request.getParameter("limit")));
            list = expressMapper.selectUnPackageByMap(paramMap);
        }

        BasePageResponse result = new BasePageResponse();
        BaseResponse response = new BaseResponse();
        response.setCode("0");
        response.setMsg("success");
        response.setTotal(count);
        response.setDraw(Integer.parseInt(request.getParameter("draw")));
        response.setData(list);

        result.setData(response);
        return result;
    }
}
