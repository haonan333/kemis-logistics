package cn.kemis.service;

import cn.kemis.dao.BaseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * Created by liutiyang on 16/8/3.
 */
public class BaseService<M extends BaseMapper<T, E>, T, E> {

    @Autowired
    M mapper;

    public int countByExample(E example){
        return mapper.countByExample(example);
    }

    public int deleteByExample(E example){
        return mapper.deleteByExample(example);
    }

    public int deleteByPrimaryKey(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    public int insert(T record) {
        return mapper.insert(record);
    }

    public int insertSelective(T record) {
        return mapper.insertSelective(record);
    }

    public List<T> selectByExample(E example) {
        return mapper.selectByExample(example);
    }

    public T selectByPrimaryKey(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public int updateByExampleSelective(T record, E example) {
        return mapper.updateByExampleSelective(record, example);
    }

    public int updateByExample(T record, E example) {
        return mapper.updateByExample(record, example);
    }

    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return mapper.updateByPrimaryKey(record);
    }

    /**
     * 按条件查询一个对象
     * @param example
     * @return
     */
    public T selectOneByExample(E example) {
        T t = null;
        List<T> ts = mapper.selectByExample(example);
        if (ts != null && ts.size() > 0) {
            t = ts.get(0);
        }
        return t;
    }

    /**
     * 分页查询
     * @param example
     * @param count
     * @param pageSize
     * @return
     */
    public PageInfo<T> selectPageByExample(E example, int count, int pageSize) {
        PageHelper.startPage(count, pageSize);
        return new PageInfo<>(mapper.selectByExample(example));
    }

    /**
     * 批量插入
     * @param collection
     * @return
     */
    public int batchInsertSelective(Collection<T> collection, Class<T> clazz) {
        return mapper.batchInsertSelectiveByProvider(collection, clazz);
    }

    /**
     * 批量更新
     * @param collection
     * @return
     */
    public int batchUpdateSelective(Collection<T> collection, Class<T> clazz) {
        return mapper.batchUpdateSelectiveByProvider(collection, clazz);
    }

}
