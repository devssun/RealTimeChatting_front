package com.example.choihyesun.realtimechatting;

import java.io.Serializable;

/**
 * Created by choihyesun on 16. 8. 10..
 * 값을 임시로 저장해두는 클래스 Data Transfer Object
 */
public class UserDTO implements Serializable{

    private String userId;
    private String pswd;
    private Integer studentNumber;
    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
