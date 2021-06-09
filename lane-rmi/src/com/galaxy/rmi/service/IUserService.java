package com.galaxy.rmi.service;

import com.galaxy.rmi.pojo.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author lane
 * @date 2021年06月04日 下午3:58
 */
public interface IUserService extends Remote {

    public User getUserById(Object id) throws RemoteException;

}
