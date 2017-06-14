package cn.kemis.service;

import cn.kemis.dao.mapper.WorkProcessMapper;
import cn.kemis.domain.RevisionListDomain;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.exceptions.KemisException;
import cn.kemis.model.*;
import cn.kemis.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author yt.zhang
 *         Created on 2016-08-17
 */
@Service
public class WorkProcessService extends BaseService<WorkProcessMapper, WorkProcess, WorkProcessExample> {

    private static final Logger log = LoggerFactory.getLogger(WorkProcessService.class);

    @Autowired
    private ExpressService expressService;
    @Autowired
    private UserService userService;

    /**
     * 扫描计件接口
     * @param code 条形码
     * @param workProcess 工序
     * @return
     */
    public BaseResponse scanCode(String code,String workProcess){
        Integer userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) {
            SysUser user = (SysUser) auth.getPrincipal();
            if(user!=null)
                userId = user.getSysUserId();
        }
        BaseResponse response = new BaseResponse();    // return code 是否成功消息 当前工序
        // -1 超时  -2 快递单号不能为空 -3 订单不存在 -4 重复扫描
        try{
            if( userId == null || StringUtils.isBlank(workProcess)){
                response.setCode("-1");
                response.setMsg("登录超时");
                return response;
            }
            SysUserExample sysUserExample = new SysUserExample();
            sysUserExample.createCriteria().andSysUserIdEqualTo(userId);
            List<SysUser> userList = userService.selectByExample(sysUserExample);
            if (userList == null || userList.size() == 0 ) {
                response.setCode("-1");
                response.setMsg("登录超时");
                return response;
            }
            SysUser user = userList.get(0);

            if(StringUtils.isBlank(code)){
                response.setCode("-2");
                response.setMsg("快递单号不能为空");
                return response;
            }

            ExpressExample expressExample = new ExpressExample();
            expressExample.createCriteria().andExpressNumberEqualTo(code);
            List<Express> expressList = expressService.selectByExample(expressExample);
            if(expressList == null || expressList.size() ==0 ){
                response.setCode("-3");
                response.setMsg("快递单号对应的订单不存在");
                return response;
            }

            WorkProcessExample example = new WorkProcessExample();
            example.createCriteria().andExpressNumberEqualTo(code).andWorkProcessEqualTo(workProcess);
            WorkProcess entity = this.selectOneByExample(example);
            if(entity !=null){
                if(user.getSysUserId().intValue() == entity.getUserId().intValue()){
                    response.setCode("-4");
                    response.setMsg("已在 " + DateUtil.date2String(entity.getCreateTime(),DateUtil.FORMAT_DATE_TIME) + " 扫描");
                    return response;
                }else{
                    SysUserExample otherUserExample = new SysUserExample();
                    otherUserExample.createCriteria().andSysUserIdEqualTo(entity.getUserId());
                    SysUser otherUser = userService.selectOneByExample(otherUserExample);

                    response.setCode("-5");
                    response.setMsg("已在 "+ DateUtil.date2String(entity.getCreateTime(),DateUtil.FORMAT_DATE_TIME)  + " 被 " +
                            (otherUser != null ? otherUser.getRealName() : entity.getUserId())
                            + " 扫描");
                    log.warn("scan {} workPress {} user is {}", code, workProcess, entity.getUserId());
                    return response;
                }
            }
            //计件
            Date now = new Date();
            WorkProcess work = new WorkProcess();
            work.setUserId(user.getSysUserId());
            work.setExpressNumber(code);
            work.setWorkProcess(workProcess);
            work.setStatus((byte) 0);
            work.setCreateTime(now);
            work.setUpdateTime(now);
            this.insert(work);
            switch (workProcess){
                case "cutOrder" :
                    work.setWorkProcess("划单");
                    break;
                case "pickingGoods" :
                    work.setWorkProcess("拣货");
                    break;
                case "package" :
                    work.setWorkProcess("打包");
                    break;
                case "wrapp" :
                    work.setWorkProcess("包装");
                default:
                    break;
            }

            response.setCode("1");
            response.setMsg("扫码计件成功");
            response.setData(work);
            return response;

        }catch (Exception e){
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
            return response;

        }
    }

    /**
     * 录入单号
     * @param targetUserId          指定的用户
     * @param workProcess           指定的工序
     * @param expressNumber         单号
     * @return baseResponse         统一返回体
     */
    public BaseResponse saveExpressNumber(String targetUserId, String workProcess, String expressNumber) {
        BaseResponse response = new BaseResponse();
        try {
            //校检入参
            this.validateParam(targetUserId, workProcess, expressNumber);

            int userId = Integer.parseInt(targetUserId.trim());

            //检查当前用户
            this.checkCurrentUser();

            //检查目标用户是否存在,以及是否正常
            SysUser user = userService.findById(userId);
            if (user == null || user.getStatus() != 0){
                throw new KemisException("用户不存在或状态不正常");
            }

            //检查单号是否正确
            this.checkExpressNumber(expressNumber);

            //检查工序中此单号是否被处理
            this.checkNumberExits(userId, expressNumber, workProcess);

            //添加计件
            this.insertWorkProcess(userId, workProcess, expressNumber);

            response.setCode("1");
            response.setMsg("成功");

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }
        return response;
    }

    /**
     * 添加计件
     * @param userId                指定的用户
     * @param workProcess           指定的工序
     * @param expressNumber         单号
     */
    private void insertWorkProcess(int userId, String workProcess, String expressNumber) {
        Date now = new Date();
        WorkProcess work = new WorkProcess();
        work.setUserId(userId);
        work.setExpressNumber(expressNumber);
        work.setWorkProcess(workProcess);
        work.setStatus((byte) 0);
        work.setCreateTime(now);
        work.setUpdateTime(now);
        this.insert(work);
    }

    /**
     * 检查此单号是否被处理
     * @param expressNumber         单号
     * @param workProcess           工序
     */
    private void checkNumberExits(int userId,String expressNumber,String workProcess) {
        WorkProcessExample example = new WorkProcessExample();
        example.createCriteria().andExpressNumberEqualTo(expressNumber).andWorkProcessEqualTo(workProcess);
        WorkProcess entity = this.selectOneByExample(example);
        if (entity != null) {
            if (userId == entity.getUserId()) {
                if (entity.getStatus() == 0) {
                    throw new KemisException("已被该用户在 " + DateUtil.date2String(entity.getCreateTime(), DateUtil.FORMAT_DATE_TIME) + "扫描");
                }
            } else {
                SysUser otherUser = userService.findById(entity.getUserId());
                String message = "该单号已在 " + DateUtil.date2String(entity.getCreateTime(), DateUtil.FORMAT_DATE_TIME) + " 被 " +
                                    (otherUser != null ? otherUser.getRealName() : entity.getUserId()) + " 扫描";
                throw new KemisException(message);
            }
        }
    }

    /**
     * 检查单号是否正常
     * @param expressNumber         单号
     */
    private void checkExpressNumber(String expressNumber) {
        ExpressExample expressExample = new ExpressExample();
        expressExample.createCriteria().andExpressNumberEqualTo(expressNumber);
        List<Express> expressList = expressService.selectByExample(expressExample);
        if (expressList == null || expressList.size() == 0) {
            throw new KemisException("快递单号错误");
        }
    }

    /**
     * 查当前用户是否有效
     */
    private void checkCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SysUser user = (SysUser) auth.getPrincipal();
        if (user == null){
            throw new KemisException("登录超时");
        }

        user = userService.findById(user.getSysUserId());
        if (user == null || user.getStatus() != 0){
            throw new KemisException("非法用户");
        }
    }

    /**
     * 验正入参
     * @param userId                指定的用户
     * @param workProcess           指定的工序
     * @param expressNumber         单号
     */
    private void validateParam(String userId,String workProcess,String expressNumber) {
        if (StringUtils.isBlank(userId)) {
            throw new KemisException("请选择用户");
        }
        if (StringUtils.isBlank(workProcess)) {
            throw new KemisException("请选择工序");
        }
        if (StringUtils.isBlank(expressNumber)) {
            throw new KemisException("请输入单号");
        }
    }

    /**
     * 查找工序列表
     * @param request
     * @return
     */
    public BaseResponse revisionList(HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        String expressNumber = request.getParameter("expressNumber");
        String realName = request.getParameter("realName");
        String workProcess = request.getParameter("workProcess");
        String reservationTime = request.getParameter("reservationTime");

        Map<String,Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(expressNumber)) {
            paramMap.put("expressNumber",expressNumber.trim());
        }
        if (StringUtils.isNotBlank(realName)) {
            paramMap.put("realName",realName.trim());
        }
        if (StringUtils.isNotBlank(workProcess)) {
            paramMap.put("workProcess",workProcess.trim());
        }
        if (StringUtils.isNotBlank(reservationTime)) {
            String[] dateStr = reservationTime.trim().split("/");
            paramMap.put("startTime",dateStr[0].trim());
            paramMap.put("endTime",dateStr[1].trim());
        }
        if (paramMap.size() <= 0) {
            response.setCode("0");
            response.setData(new ArrayList<>());
            return response;
        }
        int total = mapper.countWorkProcessInfoByMap(paramMap);
        List<RevisionListDomain> list = new ArrayList<>();
        if (total > 0) {
            int limit = Integer.parseInt(request.getParameter("limit"));
            int start = Integer.parseInt(request.getParameter("start"));
            paramMap.put("start",start);
            paramMap.put("limit",limit);
            list = mapper.revisionList(paramMap);
        }
        response.setCode("0");
        response.setData(list);
        response.setTotal(total);
        response.setDraw(Integer.parseInt(request.getParameter("draw")));
        return response;
    }

    /**
     * 修正工序 更改处理者
     * @param workProcessId         工序 id
     * @param targetUserId          更改成为用户
     * @return  baseResponse
     */
    public BaseResponse saveTargetUser(String workProcessId, String targetUserId) {
        BaseResponse response = new BaseResponse();
        try {
            //校检入参
            if (StringUtils.isBlank(workProcessId)) {
                throw new KemisException("缺少Id");
            }
            if (StringUtils.isBlank(targetUserId)) {
                throw new KemisException("请选择用户");
            }

            int userId = Integer.parseInt(targetUserId.trim());

            //检查当前用户
            this.checkCurrentUser();

            //检查目标用户是否存在,以及是否正常
            SysUser user = userService.findById(userId);
            if (user == null || user.getStatus() != 0){
                throw new KemisException("用户不存在或状态不正常");
            }

            //查询当前工序是否存在
            WorkProcess workProcess = mapper.selectByPrimaryKey(Long.parseLong(workProcessId));
            if (workProcess == null) {
                throw new KemisException("记录不存在");
            }

            //更改
            workProcess.setUserId(userId);
            mapper.updateByPrimaryKeySelective(workProcess);

            response.setCode("0");
            response.setMsg("成功");

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }
        return response;
    }

    /**
     * 修正工序 改工序
     * @param workProcessId         工序 id
     * @param targetWorkProcess     目标工序
     * @return baseResponse
     */
    public BaseResponse saveTargetWorkProcess(String workProcessId, String targetWorkProcess) {
        BaseResponse response = new BaseResponse();
        try {
            //校检入参
            if (StringUtils.isBlank(workProcessId)) {
                throw new KemisException("缺少Id");
            }
            if (StringUtils.isBlank(targetWorkProcess)) {
                throw new KemisException("请选择工序");
            }

            //检查当前用户
            this.checkCurrentUser();

            //查询当前工序是否存在
            WorkProcess workProcess = mapper.selectByPrimaryKey(Long.parseLong(workProcessId));
            if (workProcess == null) {
                throw new KemisException("记录不存在");
            }

            //检查工序中此单号是否被处理
            this.checkNumberExits(workProcess.getUserId(), workProcess.getExpressNumber(), targetWorkProcess);

            //更改
            workProcess.setWorkProcess(targetWorkProcess);
            mapper.updateByPrimaryKeySelective(workProcess);

            response.setCode("0");
            response.setMsg("成功");

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }
        return response;
    }

    /**
     * 修正工序 改状态
     * @param targetStatus          状态
     * @param workProcessId         工序 Id
     * @return baseResponse         统一返回体
     */
    public BaseResponse saveTargetStatus(String workProcessId, String targetStatus) {
        BaseResponse response = new BaseResponse();
        try {
            //校检入参
            if (StringUtils.isBlank(workProcessId)) {
                throw new KemisException("缺少Id");
            }
            if (StringUtils.isBlank(targetStatus)) {
                throw new KemisException("状态");
            }

            //检查当前用户
            this.checkCurrentUser();

            //查询当前工序是否存在
            WorkProcess workProcess = mapper.selectByPrimaryKey(Long.parseLong(workProcessId));
            if (workProcess == null) {
                throw new KemisException("记录不存在");
            }

            //更改
            workProcess.setStatus(Byte.valueOf(targetStatus));
            mapper.updateByPrimaryKeySelective(workProcess);

            response.setCode("0");
            response.setMsg("成功");

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }
        return response;
    }

    /**
     * 批量修改工序
      * @param workProcessIds           工序 Ids
     * @param targetWorkProcess         指定的工序
     * @return BaseResponse             统一返回体
     */
    public BaseResponse saveBatchWorkProcess(String workProcessIds, String targetWorkProcess) {
        BaseResponse response = new BaseResponse();
        try {
            //校检入参
            if (StringUtils.isBlank(workProcessIds)) {
                throw new KemisException("请选择批量修改的数据");
            }
            if (StringUtils.isBlank(targetWorkProcess)) {
                throw new KemisException("请选择要修改为的工序");
            }

            //检查当前用户
            this.checkCurrentUser();

            String[] ids = workProcessIds.split(",");
            int successCount = 0,errorCount = 0;
            for (String workProcessId : ids) {
                WorkProcess workProcess = mapper.selectByPrimaryKey(Long.parseLong(workProcessId));
                try {
                    if (workProcess != null) {
                        workProcess.setWorkProcess(targetWorkProcess);
                        mapper.updateByPrimaryKeySelective(workProcess);
                    }
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                }
            }

            response.setCode("0");
            String msg = "";
            if (errorCount > 0) {
                msg = String.format("成功%s条,失败%s条,失败原因:同一单号同一工序不能重复", successCount, errorCount);
            } else {
                msg = String.format("成功%s条,失败%s条", successCount, errorCount);
            }
            response.setMsg(msg);

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }
        return response;
    }

    public BaseResponse saveBatchTargetUser(String workProcessIds, String targetUserId) {
        BaseResponse response = new BaseResponse();

        try {
            //校检入参
            if (StringUtils.isBlank(workProcessIds)) {
                throw new KemisException("请选择批量修改的数据");
            }
            if (StringUtils.isBlank(targetUserId)) {
                throw new KemisException("请选择用户");
            }

            int userId = Integer.parseInt(targetUserId.trim());

            //检查当前用户
            this.checkCurrentUser();

            //检查目标用户是否存在,以及是否正常
            SysUser user = userService.findById(userId);
            if (user == null || user.getStatus() != 0){
                throw new KemisException("用户不存在或状态不正常");
            }

            String[] ids = workProcessIds.split(",");
            int successCount = 0,errorCount = 0;
            for (String workProcessId : ids) {
                WorkProcess workProcess = mapper.selectByPrimaryKey(Long.parseLong(workProcessId));
                try {
                    if (workProcess != null) {
                        workProcess.setUserId(userId);
                        mapper.updateByPrimaryKeySelective(workProcess);
                    }
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                }
            }

            response.setCode("0");
            String msg;
            if (errorCount > 0) {
                msg = String.format("成功%s条,失败%s条", successCount, errorCount);
            } else {
                msg = String.format("成功%s条,失败%s条", successCount, errorCount);
            }
            response.setMsg(msg);

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }
        
        return response;
    }

    public BaseResponse saveBatchTargetStatus(String workProcessIds, String targetStatus) {
        BaseResponse response = new BaseResponse();

        try {
            //校检入参
            if (StringUtils.isBlank(workProcessIds)) {
                throw new KemisException("请选择批量修改的数据");
            }
            if (StringUtils.isBlank(targetStatus)) {
                throw new KemisException("请选择状态");
            }

            String[] ids = workProcessIds.split(",");
            int successCount = 0,errorCount = 0;
            for (String workProcessId : ids) {
                WorkProcess workProcess = mapper.selectByPrimaryKey(Long.parseLong(workProcessId));
                try {
                    if (workProcess != null) {
                        workProcess.setStatus(Byte.valueOf(targetStatus));
                        mapper.updateByPrimaryKeySelective(workProcess);
                    }
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                }
            }

            response.setCode("0");
            String msg;
            if (errorCount > 0) {
                msg = String.format("成功%s条,失败%s条", successCount, errorCount);
            } else {
                msg = String.format("成功%s条,失败%s条", successCount, errorCount);
            }
            response.setMsg(msg);

        } catch (KemisException e) {
            response.setCode("-1");
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("exception msg " + e.getMessage());
            response.setCode("-6");
            response.setMsg("未知异常");
        }

        return response;
    }
}

