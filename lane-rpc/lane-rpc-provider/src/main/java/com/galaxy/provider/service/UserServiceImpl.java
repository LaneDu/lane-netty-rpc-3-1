package com.galaxy.provider.service;

import com.galaxy.provider.anno.RPCService;
import com.galaxy.rpc.api.IUserService;
import com.galaxy.rpc.pojo.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lane
 * @date 2021年06月04日 下午7:16
 */
@RPCService
@Service
public class UserServiceImpl implements IUserService {
    Map<Object, User> userMap = new HashMap();


    @Override
    public User getById(int id) {
    if (userMap.isEmpty()){

        User user1 = new User();
        user1.setId(1);
        user1.setName("张三");
        User user2 = new User();
        user2.setId(2);
        user2.setName("李四");
        userMap.put(user1.getId(),user1);
        userMap.put(user2.getId(),user2);

    }
        return userMap.get(id);
    }
}
