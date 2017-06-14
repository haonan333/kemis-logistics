package cn.kemis.service;

import cn.kemis.dao.mapper.SysRoleHasSysModuleMapper;
import cn.kemis.model.SysRoleHasSysModuleExample;
import cn.kemis.model.SysRoleHasSysModuleKey;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-28
 */
@Component
public class SysRoleHasSysModuleService extends BaseService<SysRoleHasSysModuleMapper, SysRoleHasSysModuleKey, SysRoleHasSysModuleExample> {

    int deleteByRoleId(Integer roleId) {
        SysRoleHasSysModuleExample example = new SysRoleHasSysModuleExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        return mapper.deleteByExample(example);
    }

    int saveRoleHasModule(Integer roleId, Integer[] moduleIds) {
        if (moduleIds.length > 0) {
            SysRoleHasSysModuleKey roleHasSysModule = null;
            List<SysRoleHasSysModuleKey> roleHasSysModuleList = new ArrayList<>();

            for (Integer moduleId : moduleIds) {
                roleHasSysModule = new SysRoleHasSysModuleKey();
                roleHasSysModule.setModuleId(moduleId);
                roleHasSysModule.setRoleId(roleId);

                roleHasSysModuleList.add(roleHasSysModule);
            }
            return mapper.batchInsertSelectiveByProvider(roleHasSysModuleList, SysRoleHasSysModuleKey.class);
        }
        return 0;
    }
}
