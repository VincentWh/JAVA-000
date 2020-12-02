package dynamicswitch.service;


import dynamicswitch.entity.User;

public interface UserOp {
    User getUserById(int id);
    void addUser(User user);
}