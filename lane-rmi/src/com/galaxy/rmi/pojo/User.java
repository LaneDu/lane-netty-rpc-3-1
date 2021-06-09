package com.galaxy.rmi.pojo;

import java.io.Serializable;

/**
 * 数据是在网络中以二进制流的方式进行传输，必须实现序列化接口
 * @author lane
 * @date 2021年06月04日 下午3:57
 */
public class User implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
