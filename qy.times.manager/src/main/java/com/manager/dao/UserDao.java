package com.manager.dao;

import com.manager.entity.User;

public interface UserDao {

	public User getByName(String username);

	public Integer addUser(User user);

	public Integer updateUser(User user);

	public User getUserById(Long id);

	public void changePassword(User user);

	public void changeUser(User user);

}