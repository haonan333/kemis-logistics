package cn.kemis.service;

import cn.kemis.dao.mapper.SysModuleMapper;
import cn.kemis.domain.ModuleTreeNode;
import cn.kemis.domain.api.base.BaseResponse;
import cn.kemis.model.SysModule;
import cn.kemis.model.SysModuleExample;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liutiyang on 16/8/2.
 */
@Service
public class ModuleService extends BaseService<SysModuleMapper, SysModule, SysModuleExample> {

    private static Logger logger = LoggerFactory.getLogger(ModuleService.class);

    public PageInfo<SysModule> searchModuleList(PageInfo<SysModule> pageInfo) {
        SysModule module = pageInfo.getList().get(0);

        SysModuleExample example = new SysModuleExample();
        SysModuleExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(module.getModuleName())) {
            criteria.andModuleNameLike("%" + module.getModuleName() + "%");
        }

        if (StringUtils.isNotBlank(module.getModuleUrl())) {
            criteria.andModuleNameLike("%" + module.getModuleUrl() + "%");
        }

        if (module.getModuleParentId() != null) {
            criteria.andModuleParentIdEqualTo(module.getModuleParentId());
        }

        example.setOrderByClause("sysModuleId asc");

        PageInfo<SysModule> modulePageInfo = super.selectPageByExample(example, pageInfo.getPageNum(), pageInfo.getSize());
        modulePageInfo.setPages(pageInfo.getPages());
        return modulePageInfo;
    }

    public List<SysModule> getAll() {
        SysModuleExample example = new SysModuleExample();
        example.setOrderByClause("`moduleParentId` asc, sysModuleId asc");
        return super.selectByExample(example);
    }

    /**
     * 查询模块地址相同的模块
     * @param moduleUrl 等于模块地址
     * @return 模块
     */
    private SysModule findByModuleUrl(String moduleUrl) {
        return findByModuleUrl(moduleUrl, null);
    }

    /**
     * 查询模块地址相同而 ID 同的模块
     * @param moduleUrl 等于模块地址
     * @param sysModuleId 不等于模块 ID
     * @return 模块
     */
    private SysModule findByModuleUrl(String moduleUrl, Integer sysModuleId) {
        SysModuleExample example = new SysModuleExample();
        SysModuleExample.Criteria criteria = example.createCriteria();
        criteria.andModuleUrlEqualTo(moduleUrl);

        if (sysModuleId != null) {
            criteria.andSysModuleIdNotEqualTo(sysModuleId);
        }

        List<SysModule> sysModuleList = super.selectByExample(example);
        SysModule sysModule = null;
        if (sysModuleList != null && sysModuleList.size() > 0) {
            sysModule = sysModuleList.get(0);
        }
        return sysModule;
    }

    public BaseResponse save(SysModule sysModule) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysModule.getModuleName())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("模块名不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysModule.getModuleUrl())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("模块地址不能为空！");
            return baseResponse;
        }

        SysModule module = this.findByModuleUrl(sysModule.getModuleUrl());
        if (module != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("模块地址已存在！");
            return baseResponse;
        }

        super.insertSelective(sysModule);

        if (sysModule.getSysModuleId() != null) {
            baseResponse.setCode("0");
            baseResponse.setMsg("新增模块成功！");
        } else {
            baseResponse.setCode("453");
            baseResponse.setMsg("保存模块失败！");
        }

        return baseResponse;
    }

    public BaseResponse update(SysModule sysModule) {
        BaseResponse baseResponse = new BaseResponse();

        if (StringUtils.isBlank(sysModule.getModuleName())) {
            baseResponse.setCode("450");
            baseResponse.setMsg("模块名不能为空！");
            return baseResponse;
        }

        if (StringUtils.isBlank(sysModule.getModuleUrl())) {
            baseResponse.setCode("451");
            baseResponse.setMsg("模块地址不能为空！");
            return baseResponse;
        }

        SysModule module = this.findByModuleUrl(sysModule.getModuleUrl(), sysModule.getSysModuleId());
        if (module != null) {
            baseResponse.setCode("452");
            baseResponse.setMsg("模块地址已存在！");
            return baseResponse;
        }

        super.updateByPrimaryKeySelective(sysModule);

        baseResponse.setCode("0");
        baseResponse.setMsg("编辑模块成功！");

        return baseResponse;
    }

    public Collection<ModuleTreeNode> getTree() {
        List<SysModule> moduleList = getAll();
        Map<Integer, ModuleTreeNode> map = new HashMap<>();
        ModuleTreeNode node = null;

        for (SysModule sysModule : moduleList) {
            node = new ModuleTreeNode();
            node.setId(sysModule.getSysModuleId());
            node.setTitle(sysModule.getModuleName());

            if (sysModule.getModuleParentId() == null) {
                node.setLevel(0);
                map.put(sysModule.getSysModuleId(), node);
            } else {
                if (map.containsKey(sysModule.getModuleParentId())) {
                    node.setLevel(1);
                    node.setHas_children(false);
                    map.get(sysModule.getModuleParentId()).setHas_children(true);
                    map.get(sysModule.getModuleParentId()).addChildren(node);
                } else {
                    // TODO 多级下钻暂不实现
                }
            }
        }

        return map.values();
    }
}
