package cn.kemis.controller.admin

import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.service.WorkProcessService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest

/**
 * Created by liutiyang on 16/8/3.
 */
@Controller
@Slf4j
@RequestMapping("/admin/scanException")
class ScanExceptionController {

    @Autowired
    private WorkProcessService workProcessService;

    /**
     * 进入录入单号页面
     * @return string
     */
    @GetMapping("/inputNumber")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String inputNumber() {
        "admin/scanException/inputNumber"
    }

    /**
     * 进入修正工序页面
     * @return
     */
    @GetMapping("/revision")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String revision() {
        "admin/scanException/revision"
    }

    /**
     * 录入单号
     * @param userId                指定的用户
     * @param workProcess           指定的工序
     * @param expressNumber         单号
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveExpressNumber")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveExpressNumber(@RequestParam(value = "userId") String userId,
                                   @RequestParam(value = "workProcess") String workProcess,
                                   @RequestParam(value = "expressNumber") String expressNumber){
        workProcessService.saveExpressNumber(userId,workProcess,expressNumber);
    }

    /**
     * 根据单号显示工序
     * @return baseResponse
     */
    @GetMapping("/revisionList")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse revisionList(HttpServletRequest request) {
        workProcessService.revisionList(request);
    }

    /**
     * 修正工序 更改处理者
     * @param userId                指定的用户
     * @param workProcessId         工序 id
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveTargetUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveTargetUser(@RequestParam(value = "targetUserId") String userId,
                               @RequestParam(value = "workProcessId") String workProcessId){
        workProcessService.saveTargetUser(workProcessId,userId);
    }

    /**
     * 修正工序 改工序
     * @param workProcess           指定的工序
     * @param workProcessId         工序 Id
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveTargetWorkProcess")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveTargetWorkProcess(@RequestParam(value = "targetWorkProcess") String workProcess,
                                @RequestParam(value = "workProcessId") String workProcessId){
        workProcessService.saveTargetWorkProcess(workProcessId,workProcess);
    }

    /**
     * 修正工序 改状态
     * @param  status               状态
     * @param workProcessId         工序 Id
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveTargetStatus")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveTargetStatus(@RequestParam(value = "targetStatus") String status,
                                       @RequestParam(value = "workProcessId") String workProcessId){
        workProcessService.saveTargetStatus(workProcessId,status);
    }

    /**
     * 批量修改工序
     * @param workProcess           指定的工序
     * @param workProcessId         工序 Ids
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveBatchWorkProcess")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveBatchWorkProcess(@RequestParam(value = "targetBatchWorkProcess") String workProcess,
                                       @RequestParam(value = "workBatchProcessId") String workProcessIds){
        workProcessService.saveBatchWorkProcess(workProcessIds,workProcess);
    }

    /**
     * 修正工序  批量更改处理者
     * @param userId                指定的用户
     * @param workProcessIds        工序 ids
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveBatchTargetUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveBatchTargetUser(@RequestParam(value = "targetUserId") String userId,
                                @RequestParam(value = "workBatchProcessId") String workProcessIds){
        workProcessService.saveBatchTargetUser(workProcessIds, userId)
    }

    /**
     * 修正工序  批量更改状态
     * @param userId                指定的用户
     * @param workProcessIds        工序 ids
     * @return baseResponse         统一返回体
     */
    @PostMapping("/saveBatchTargetStatus")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    BaseResponse saveBatchTargetStatus(@RequestParam(value = "targetStatus") String targetStatus,
                                @RequestParam(value = "workBatchProcessId") String workProcessIds){
        workProcessService.saveBatchTargetStatus(workProcessIds, targetStatus)
    }
}
