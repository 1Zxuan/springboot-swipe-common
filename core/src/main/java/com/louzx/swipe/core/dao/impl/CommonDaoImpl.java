package com.louzx.swipe.core.dao.impl;

import com.louzx.swipe.core.dao.ICommonDao;
import com.louzx.swipe.core.jdbc.SqlBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 1Zx.
 * @date 2020/4/20 11:33
 */
@Repository("bootCommonDao")
public class CommonDaoImpl extends BaseDao implements ICommonDao {

    public Integer count(SqlBuilder sqlBuilder) {
        return namedParameterJdbcTemplate.queryForObject(sqlBuilder.getCountSql(), sqlBuilder.getParams(), Integer.class);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> query(SqlBuilder sqlBuilder) {
        return (List<T>) namedParameterJdbcTemplate.query(sqlBuilder.getSelectSql(), sqlBuilder.getParams(),
                BeanPropertyRowMapper.newInstance(sqlBuilder.getCls()));
    }

    public void save(SqlBuilder sqlBuilder, Object obj) {
        sqlBuilder.addParams(obj);
        namedParameterJdbcTemplate.update(sqlBuilder.getInsertSql(), new BeanPropertySqlParameterSource(obj));
    }

    public Long saveReturnKey(SqlBuilder sqlBuilder, Object obj) {
        sqlBuilder.addParams(obj);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sqlBuilder.getInsertSql(), new BeanPropertySqlParameterSource(obj), keyHolder,
                sqlBuilder.getPks());
        return keyHolder.getKey().longValue();
    }

    public void delete(SqlBuilder sqlBuilder) {
        namedParameterJdbcTemplate.update(sqlBuilder.getDeleteSql(), sqlBuilder.getParams());
    }

    public void save(SqlBuilder sqlBuilder, List<?> list) {
        sqlBuilder.setBatchUpdate(true);
        namedParameterJdbcTemplate.batchUpdate(sqlBuilder.getInsertSql(), SqlParameterSourceUtils.createBatch(list.toArray()));
    }

    public void update(SqlBuilder sqlBuilder, Object obj) {
        sqlBuilder.addParams(obj);
        namedParameterJdbcTemplate.update(sqlBuilder.getUpdateSql(), sqlBuilder.getParams());
    }

    public void updateByProperty(SqlBuilder sqlBuilder) {
        namedParameterJdbcTemplate.update(sqlBuilder.getUpdateByPropertySql(), sqlBuilder.getParams());
    }

    @SuppressWarnings("unchecked")
    public <T> T queryForObject(SqlBuilder sqlBuilder) {
        List<T> list = (List<T>) namedParameterJdbcTemplate.query(sqlBuilder.getSelectSql(), sqlBuilder.getParams(),
                BeanPropertyRowMapper.newInstance(sqlBuilder.getCls()));
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public Integer getSequence(String sequence_name) {
        String sql = "select " + sequence_name + ".Nextval from dual";
        return namedParameterJdbcTemplate.queryForObject(sql, new HashMap<String, Object>(), Integer.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> queryBySql(Class<T> cls, String sql, List<Object> params) {
        if (null == params) {
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(cls));
        }
        return jdbcTemplate.query(sql, params.toArray(), BeanPropertyRowMapper.newInstance(cls));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> queryBySql(Class<T> cls, String sql, Map<String, Object> params) {
        if (null == params) {
            return namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(cls));
        }
        return namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(cls));
    }

    @Override
    public <T> T queryForObjectBySql(Class<T> cls, String sql, List<Object> params) {
        List<T> list = queryBySql(cls, sql, params);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public <T> T queryForObjectBySql(Class<T> cls, String sql, Map<String, Object> params) {
        List<T> list = queryBySql(cls, sql, params);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Integer countBySql(String sql, List<Object> params) {
        if (null == params) {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        }
        return jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    @Override
    public Integer countBySql(String sql, Map<String, Object> params) {
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public void updateBySql(String sql, Object[] params) {
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void updateBySql(String sql, Map<String, Object> params) {
        namedParameterJdbcTemplate.update(sql, params);
    }
}
