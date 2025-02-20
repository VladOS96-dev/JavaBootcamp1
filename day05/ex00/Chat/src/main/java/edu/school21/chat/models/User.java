package edu.school21.chat.models;

import java.util.List;
import java.util.Objects;

public class User {
    private Long userID;
    private String login;
    private String password;
    private List<Chatroom> createRooms;
    private List<Chatroom> usedRooms;

    public  User(Long id,String login,String password,List<Chatroom> createRooms,List<Chatroom> usedRooms)
    {
        userID=id;
        this.usedRooms=usedRooms;
        this.createRooms=createRooms;
        this.password=password;
        this.login=login;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    public List<Chatroom> getCreateRooms() {
        return createRooms;
    }

    public List<Chatroom> getUsedRooms() {
        return usedRooms;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setCreateRooms(List<Chatroom> createRooms) {
        this.createRooms = createRooms;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUsedRooms(List<Chatroom> usedRooms) {
        this.usedRooms = usedRooms;
    }
    @Override
    public  boolean equals(Object obj)
    {
        if (obj==this)return true;
        if(obj==null||this.getClass()!=obj.getClass())return false;
        User user=(User)obj;
        return Objects.equals(userID,user.userID)&&Objects.equals(login,user.login)&&Objects.equals(password,user.password)&&Objects.equals(createRooms,user.createRooms)&&Objects.equals(usedRooms,user.usedRooms);
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(userID,login,password,createRooms,usedRooms);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", createRooms=" + createRooms +
                ", usedRooms=" + usedRooms +
                '}';
    }
}
