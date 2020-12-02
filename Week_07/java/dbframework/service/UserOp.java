package dbframework.service;


import dbframework.entity.User;

public interface UserOp {
    User getUserById(int id);
    void addUser(User user);
}