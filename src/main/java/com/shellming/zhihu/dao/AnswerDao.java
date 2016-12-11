package com.shellming.zhihu.dao;

import com.shellming.zhihu.models.Answer;
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
 * Created by ruluo1992 on 10/5/2016.
 */
@Repository
public class AnswerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String saveQuery;
    private String countQuery;
    private String deleteByUrlQuery;
    private String isExistUrlQuery;
    private String getLastQuery;
    private String updateQuery;
    private String getAnswersQuery;

    @PostConstruct
    private void init() {
        saveQuery = "insert into zhihu_answer(`url`,`user_url`,`question_url`, `token`, `create_time`, `need_check`) values (?,?,?,?,?,?)";;
        countQuery = "select count(*) as count from zhihu_answer";
        deleteByUrlQuery = "delete from zhihu_answer where url=?";
        isExistUrlQuery = "select count(*) as count from zhihu_answer where `url`=?";
        updateQuery = "update zhihu_answer set url=?,user_url=?,question_url=?,token=?,`create_time`=?,`need_check`=? where url=?";
        getLastQuery = "select * from zhihu_answer where `user_url`=? order by `create_time` desc limit 0,1";
        getAnswersQuery = "select * from zhihu_answer where `id`>? order by id limit 0,?";
    }

    public Answer getLastByUserUrl(String userUrl) {
        List<Answer> answers = jdbcTemplate.query(getLastQuery, new AnswerMapper(), userUrl);
        if (answers.size() == 0) {
            return null;
        } else {
            return answers.get(0);
        }
    }

    public List<Answer> getAnswersById(int fromId, int n) {
        List<Answer> answers = jdbcTemplate.query(getAnswersQuery, new AnswerMapper(), fromId, n);
        return answers;
    }

    public void save(Answer answer) {
        jdbcTemplate.update(saveQuery,
                answer.url, answer.userUrl, answer.questionUrl,
                answer.token, new Timestamp(answer.createTime),
                answer.needCheck);
    }

    public void update(Answer answer) {
        jdbcTemplate.update(updateQuery,
                answer.url, answer.userUrl, answer.questionUrl,
                answer.token, new Timestamp(answer.createTime),
                answer.needCheck, answer.url);
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

    static class AnswerMapper implements RowMapper<Answer> {

        @Override
        public Answer mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer id = resultSet.getInt("id");
            String url = resultSet.getString("url");
            String userUrl = resultSet.getString("user_url");
            String questionUrl = resultSet.getString("question_url");
            String token = resultSet.getString("token");
            Timestamp time = resultSet.getTimestamp("create_time");
            Long createTime = time.getTime();
            Integer needCheck = resultSet.getInt("need_check");

            Answer answer = new Answer();
            answer.id = id;
            answer.createTime = createTime;
            answer.needCheck = needCheck;
            answer.questionUrl = questionUrl;
            answer.token = token;
            answer.url = url;
            answer.userUrl = userUrl;

            return answer;
        }
    }
}
