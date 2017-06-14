package cn.kemis.service.batch;

import cn.kemis.model.ShipOrderSource;
import cn.kemis.tools.batch.mapper.ShipOrderSourceRowMapper;
import cn.kemis.tools.batch.processor.ShipOrderSourceItemProcessor;
import cn.kemis.tools.batch.writer.ShipOrderItemWriter;
import cn.kemis.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;
import java.util.Map;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-13
 */
@Component
public class BatchService {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ShipOrderItemWriter itemWriter;

    @Autowired
    private ShipOrderSourceItemProcessor itemProcessor;

    private PagingQueryProvider queryProvider(String batchNo) {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select *");
        queryProvider.setFromClause("from ship_order_source");
        queryProvider.setSortKey("shipOrderSourceId");
        queryProvider.setWhereClause("where batchNo = " + (StringUtils.isNotBlank(batchNo) ? batchNo : DateUtil.date2String(new Date(), DateUtil.FORMAT_MONTH)));

        try {
            return queryProvider.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JdbcPagingItemReader<ShipOrderSource> reader(String batchNo) {
        JdbcPagingItemReader<ShipOrderSource> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setQueryProvider(queryProvider(batchNo));
        reader.setRowMapper(new ShipOrderSourceRowMapper());
        reader.setPageSize(1000);

        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }
    public void convertShipOrder(String batchNo) {

        Step step = stepBuilderFactory.get("convertShipOrderStep1").<ShipOrderSource, Map<String, Object>>chunk(1000)
                .reader(reader(batchNo))
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        Job job = jobBuilderFactory.get("convertShipOrderJob1").
                incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();


        JobParameters parameters = new JobParametersBuilder()
                .addString("runMinute", DateUtil.date2String(new Date(), DateUtil.FORMAT_DATE_MINUTE))  //以日期小时分钟为参数
                // .addString("runDay", "2016-08-13")                                                   //以日期为参数，可保证一天只能执行一次
                // .addString("runMonth", DateUtil.date2String(new Date(), DateUtil.FORMAT_MONTH))
                .toJobParameters();

        try {
            JobExecution execution = jobLauncher.run(job, parameters);
            System.out.println("Exit status: " + execution.getStatus());
            System.out.println("Exit status: " + execution.getAllFailureExceptions());
        } catch (JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
