package com.shellming.zhihu.dao;

import com.shellming.zhihu.models.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 10/5/2016.
 */
@Repository
public class MetaDao {
    private String saveQuery;
    private String updateQuery;
    private String getQuery;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        saveQuery = "insert into zhihu_meta(`key`,`data`) values (?,?)";
        getQuery = "select data from zhihu_meta where `key`=? order by time desc limit 0,1";
        updateQuery = "update zhihu_meta set `data`=? where `key`=?";
    }

    public void saveOrUpdate(String key, String data) {
        String last = getLast(key);
        if (last == null) {
            jdbcTemplate.update(saveQuery,
                    key, data);
        } else {
            jdbcTemplate.update(updateQuery, data, key);
        }
    }

    public String getLast(String key) {
        List<Map<String,Object>> list = jdbcTemplate.queryForList(getQuery, key);
        if (list.size() == 0) {
            return null;
        } else {
            Map map = list.get(0);
            return map.get("data").toString();
        }
//        return jdbcTemplate.queryForObject(getQuery, String.class, key);
    }
}
