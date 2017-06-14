package cn.kemis.controller.admin

import cn.kemis.domain.api.base.BasePageResponse
import cn.kemis.domain.api.base.BaseResponse
import cn.kemis.exceptions.KemisException
import cn.kemis.model.ReplaceKeywords
import cn.kemis.service.ReplaceKeywordsService
import com.github.pagehelper.PageInfo
import groovy.json.JsonBuilder
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-10-30
 */
@RestController
@Slf4j
@RequestMapping("admin/systemConfig/keywordManager")
class KeywordManagerController {

    @Autowired
    private ReplaceKeywordsService keywordsService

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView keywordManager() {
        new ModelAndView("admin/systemConfig/keywordManager")
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BasePageResponse list(
            @RequestParam("start") int start, @RequestParam("limit") int limit, @RequestParam("draw") int draw,
            @RequestParam("keyword") String keyword, @RequestParam("type") String type,
            @ModelAttribute("orderColumn") String orderColumn, @ModelAttribute("orderDir") String orderDir) {
        def map = new HashMap<String, Object>();

        PageInfo<ReplaceKeywords> pageInfo = new PageInfo<>()

        try {
            pageInfo.setPages(draw)
            pageInfo.setPageNum(start)
            pageInfo.setSize(limit)

            if (StringUtils.isNotBlank(orderColumn) && StringUtils.isNotBlank(orderDir)) {
                if ("replaceKeywordsId" == orderColumn) {
                    pageInfo.orderBy =  orderColumn + " " + orderDir
                } else {
                    pageInfo.orderBy = "CONVERT(" + orderColumn + " USING gbk ) " + orderDir
                }
            }

            ReplaceKeywords replaceKeywords = new ReplaceKeywords()
            replaceKeywords.keyword = keyword
            replaceKeywords.type = type

            def list = new ArrayList<ReplaceKeywords>()
            list.add(replaceKeywords)
            pageInfo.setList(list)

            pageInfo = keywordsService.searchKeywordList(pageInfo)

        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> search error || " + e.message)
            map.code = -1
            map.msg = e.getMessage()
        }

        BasePageResponse basePageResponse = new BasePageResponse()
        basePageResponse.data = pageInfo

        basePageResponse
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse save(@RequestBody ReplaceKeywords replaceKeywords){
        keywordsService.save(replaceKeywords)
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse update(@RequestBody ReplaceKeywords replaceKeywords){
        keywordsService.update(replaceKeywords)
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    BaseResponse delete(@RequestBody ReplaceKeywords replaceKeywords){
        keywordsService.delete(replaceKeywords)
    }

    @PostMapping("uploadKeywords")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    String uploadKeywords(@RequestParam(value = "inputFile",required = true) MultipartFile file,
                                  @RequestParam(value="batchNo", required=true) String batchNo,
                                  @RequestParam(value="type", required=true) String type) {
        def json  = new JsonBuilder()
        def map = new HashMap<String,Object>()
        try {
            keywordsService.dealFileKeywords(file, batchNo, type)
            map.code = 0
            map.msg = "成功"
        } catch (KemisException e) {
            map.code = -2
            map.msg = e.getMessage()
        } catch (Exception e) {
            e.printStackTrace()
            log.error("--> uploadKeywords || " + e.message)
            map.code = -1
            map.msg = e.getMessage()
        }

        json{
            data map
        }
        StringEscapeUtils.unescapeJava(json.toPrettyString())
    }
}
