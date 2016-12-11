package com.shellming.zhihu.dao;

import com.shellming.zhihu.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 10/4/2016.
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String saveQuery;
    private String countQuery;
    private String deleteByUrlQuery;
    private String isExistUrlQuery;
//    private String getLastNQuery;
    private String updateQuery;
    private String getUsersQuery;

    @PostConstruct
    private void init() {
        saveQuery = "insert into zhihu_user(`url`,`hash`,`followee`) values (?,?,?)";
        countQuery = "select count(*) as count from zhihu_user";
        deleteByUrlQuery = "delete from zhihu_user where url=?";
        isExistUrlQuery = "select count(*) as count from zhihu_user where `url`=?";
//        getLastNQuery = "select * from zhihu_user order by last_check limit 0,?";
        updateQuery = "update zhihu_user set url=?,hash=?,followee=? where url=?";
        getUsersQuery = "select * from zhihu_user where `id`>? order by id limit 0,?";
    }

    public void save(User user) {
        jdbcTemplate.update(saveQuery,
                user.url, user.hash, user.followee);
    }

    public void update(User user) {
        jdbcTemplate.update(updateQuery,
                user.url, user.hash, user.followee, user.url);
    }

    public int count() {
        Map result = jdbcTemplate.queryForMap(countQuery);
        return Integer.valueOf(result.get("count").toString());
    }

    public void deleteByUrl(String url) {
        jdbcTemplate.update(deleteByUrlQuery, url);
    }

    public boolean isExistUrl(String url) {
        Integer count = jdbcTemplate.queryForObject(isExistUrlQuery, Integer.class, url);
        return count > 0;
    }

//    public List<User> getLastN(int n) {
//        List<User> users = jdbcTemplate.query(getLastNQuery, new UserMapper(), n);
//        return users;
//    }

    public List<User> getUsersById(int fromId, int n) {
        List<User> users = jdbcTemplate.query(getUsersQuery, new UserMapper(), fromId, n);
        return users;
    }

    class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer id = resultSet.getInt("id");
            String url = resultSet.getString("url");
            String hash = resultSet.getString("hash");
            Integer followee = resultSet.getInt("followee");
//            Long time = resultSet.getLong("last_check");
            User u = new User();
            u.id = id;
            u.url = url;
            u.hash = hash;
            u.followee = followee;
//            u.last_check = time;
            return u;
        }
    }
}
