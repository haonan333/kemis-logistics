package cn.kemis.service;

import cn.kemis.dao.mapper.SysConfigurationMapper;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.model.SysConfiguration;
import cn.kemis.model.SysConfigurationExample;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liutiyang on 16/8/2.
 */
@Service
public class SysConfigurationService extends BaseService<SysConfigurationMapper, SysConfiguration, SysConfigurationExample> {

    private static Logger logger = LoggerFactory.getLogger(SysConfigurationService.class);

    public SysConfiguration selectByPrimaryKey(String propertyKey) {
        return mapper.selectByPrimaryKey(propertyKey);
    }

    public int deleteByPrimaryKey(String propertyKey) {
        return mapper.deleteByPrimaryKey(propertyKey);
    }

    public PageInfo<SysConfiguration> searchConfigurationList(PageInfo<SysConfiguration> pageInfo) {
        SysConfiguration sysConfiguration = pageInfo.getList().get(0);

        SysConfigurationExample example = new SysConfigurationExample();
        SysConfigurationExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(sysConfiguration.getPropertyKey())) {
            criteria.andPropertyKeyLike("%" + sysConfiguration.getPropertyKey() + "%");
        }

        if (StringUtils.isNotBlank(sysConfiguration.getPropertyValue())) {
            criteria.andPropertyValueLike("%" + sysConfiguration.getPropertyValue() + "%");
        }

        if (StringUtils.isNotBlank(pageInfo.getOrderBy())) {
            example.setOrderByClause(pageInfo.getOrderBy());
        } else {
            example.setOrderByClause("propertyKey asc");
        }

        PageInfo<SysConfiguration> modulePageInfo = super.selectPageByExample(example, pageInfo.getPageNum(), pageInfo.getSize());
        modulePageInfo.setPages(pageInfo.getPages());
        return modulePageInfo;
    }

    /**
     * 查询模块地址相同的模块
     * @param propertyValue 等于模块地址
     * @return 模块
     */
    private SysConfiguration findByKey(String propertyValue) {
        SysConfigurationExample example = new SysConfigurationExample();
        SysConfigurationExample.Criteria criteria = example.createCriteria();
        criteria.andPropertyValueEqualTo(propertyValue);

        List<SysConfiguration> sysConfigurationList = super.selectByExample(example);
        SysConfiguration sysConfiguration = null;
        if (sysConfigurationList != null && sysConfigurationList.size() > 0) {
            sysConfiguration = sysConfigurationList.get(0);
        }
        return sysConfiguration;
    }

    public BaseResponse save(SysConfiguration sysConfiguration) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysConfiguration.getPropertyKey())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("键不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysConfiguration.getPropertyValue())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("值不能为空！");
            return baseResponse;
        }

        SysConfiguration configuration = this.findByKey(sysConfiguration.getPropertyKey());
        if (configuration != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("键已存在！");
            return baseResponse;
        }

        super.insertSelective(sysConfiguration);

        baseResponse.setCode("0");
        baseResponse.setMsg("新增配置项成功！");

        return baseResponse;
    }

    public BaseResponse update(SysConfiguration sysConfiguration) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysConfiguration.getPropertyKey())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("键不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysConfiguration.getPropertyValue())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("值不能为空！");
            return baseResponse;
        }

        super.updateByPrimaryKeySelective(sysConfiguration);

        baseResponse.setCode("0");
        baseResponse.setMsg("编辑关键字成功！");

        return baseResponse;
    }

    public BaseResponse delete(SysConfiguration sysConfiguration) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysConfiguration.getPropertyKey())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("键不能为空！");
            return baseResponse;
        }

        this.deleteByPrimaryKey(sysConfiguration.getPropertyKey());

        if (logger.isInfoEnabled()) {
            logger.info("删除关键字 {}", sysConfiguration.toString());
        }

        baseResponse.setCode("0");
        baseResponse.setMsg("删除配置项成功！");

        return baseResponse;
    }
}
