package cn.kemis.dao;

import cn.kemis.plugins.BatchInsertProvider;
import cn.kemis.plugins.BatchUpdateProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by liutiyang on 16/8/3.
 */
public interface BaseMapper<T, E> {

    int countByExample(E example);

    int deleteByExample(E example);

    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByExample(E example);

    T selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

    int updateByExample(@Param("record") T record, @Param("example") E example);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    @InsertProvider(type = BatchInsertProvider.class, method = "batchInsertSelective")
    @Options(useGeneratedKeys = false, flushCache = Options.FlushCachePolicy.TRUE)
    int batchInsertSelectiveByProvider(@Param("collection") Collection<T> collection, @Param("clazz")Class clazz);

    @InsertProvider(type = BatchUpdateProvider.class, method = "batchUpdateSelective")
    @Options(useGeneratedKeys = false, flushCache = Options.FlushCachePolicy.TRUE)
    int batchUpdateSelectiveByProvider(@Param("collection") Collection<T> collection, @Param("clazz")Class clazz);

}
