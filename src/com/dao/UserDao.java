package com.dao;

import com.caiji.domain.User;
import com.caiji.util.JDBCUtils;
import com.caiji.util.PasswordEncryptor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

/**
 * 用户数据库操作类
 */
public class UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 用户注册
     * @param registerUser 要注册的用户对象
     * @return 如果注册成功，返回true；否则，返回false
     */
    public boolean register(User registerUser) {
        String salt = UUID.randomUUID().toString(); // 生成盐值
        String username = registerUser.getUsername();
        String rawPwd = registerUser.getPassword();
        String idcard = registerUser.getIdcard();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor(salt, "sha-256"); // 生成加密器
        // 加盐加密
        String encPwd = passwordEncryptor.encode(rawPwd);
        String encId = passwordEncryptor.encode(idcard);
        Object[] a = {username};
        int u =
            template.queryForObject("SELECT COUNT(*) FROM ra_user WHERE username = ?", a, Integer.class);
        if (u >= 1) {
            return false;
        }
        Object[] insertArgs = {username, encPwd, encId, salt};
        String sql = "INSERT INTO ra_user(username, password, idcard, salt) VALUES (?,?,?,?)";
        template.update(sql, insertArgs);
        return true;
    }

    /**
     * 验证用户登录
     * @param user 要登陆的用户对象
     * @return 如果成功，返回true；否则，返回false
     */
    public boolean verify(User user) {
        String username = user.getUsername();
        String rawPwd = user.getPassword();
        Object[] a = {username};
        String encPwd = null;
        String salt = null;
        try {
            encPwd =
                template.queryForObject("SELECT password FROM ra_user WHERE username = ?", a,
                    String.class); // 获取数据库中用户的密码
            salt =
                template.queryForObject("SELECT salt FROM ra_user WHERE username = ?", a,
                    String.class); // 获取盐值
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor(salt, "sha-256"); // 生成加密器
        boolean isValid = passwordEncryptor.isPasswordValid(encPwd, rawPwd); // 判断密码正确性
        if (isValid) {
            return true;
        } else {
            return false;
        }
    }
}
