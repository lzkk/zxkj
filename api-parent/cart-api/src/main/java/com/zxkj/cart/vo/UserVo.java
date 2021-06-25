package com.zxkj.cart.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class UserVo {

    private String id;

    private String username;

    private String password;

    private Long registerTime;

    private Date registerTimeDate;

    private String phone;

    private String name;

    private Short sex;

    private Integer age;

}

