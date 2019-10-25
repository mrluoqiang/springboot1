package com.study.springboot01.com.lq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-06-17 13:52
 * Editored:
 */
@Getter
@Setter
@Entity
@ToString
@Table(name = "useraccount")
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "userid")
    private Integer userid;
    @Column(name = "useraccount")
    private String useraccount;
    @Column(name = "password")
    private String password;
    @Column(name = "state")
    private Integer state;
    @Column(name = "md5password")
    private String md5password;
}
