package com.galaxy.rmi.service;

import com.galaxy.rmi.pojo.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lane
 * @date 2021年06月04日 下午4:00
 */
public class UserServiceImpl extends UnicastRemoteObject implements IUserService {

    private Map<Object,User> userMap = new HashMap<>();

    public UserServiceImpl() throws RemoteException {
        super();
        User user = new User();
        user.setId(1);
        user.setName("凝墨");
        User user1 = new User();
        user1.setId(2);
        user1.setName("凝眸");
        userMap.put(user.getId(), user);
        userMap.put(user1.getId(), user1);

    }

    @Override
    public User getUserById(Object id) throws RemoteException {
        return userMap.get(id);
    }
}
