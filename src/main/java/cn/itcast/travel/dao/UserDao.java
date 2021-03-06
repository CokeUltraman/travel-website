package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    public User findByUsername(String username);
    public void save(User user);

    User findByUserCode(String code);

    void updateStatus(User user);

    User findByUsernameandPassword(String username, String password);
}
