package cn.kemis.batch;

import cn.kemis.service.batch.BatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBatchTest {

	@Autowired
	BatchService batchService;

	@Test
	public void contextLoads() {
		batchService.convertShipOrder("201606");
	}

}
