package org.example.gy;

import java.util.List;

public interface IUserDAO {
    public void addUser(User user);
    public void updateUser(User user);
    public void deleteUser(User user);
    public User getUser(int UserID);
    public List<User> getAllUsers();
}