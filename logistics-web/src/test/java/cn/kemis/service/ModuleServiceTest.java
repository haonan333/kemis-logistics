package cn.kemis.service;

import cn.kemis.domain.ModuleTreeNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertThat;


/**
 * Created by liutiyang on 16/7/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModuleServiceTest {

    @Autowired
    private ModuleService moduleService;

    @Test
    public void getModuleTree() {
        List<ModuleTreeNode> tree = (List<ModuleTreeNode>) moduleService.getTree();
        assertThat(tree, anything());

    }


}
