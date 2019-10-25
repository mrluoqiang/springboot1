package com.study.springboot01.com.lq.dao;

import com.study.springboot01.com.lq.pojo.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-06-17 14:04
 * Editored:
 */
public interface UserRepository extends JpaRepository<UserAccount,Integer>{
    @Query(value = "select * from useraccount where useraccount=?1 and password=?2",nativeQuery = true)
    public UserAccount selectdataBypassword(String useraccount,String password);

    public  UserAccount findUserAccountByUseraccountAndPassword(String useraccount,String password);

    public  UserAccount findUserAccountByUseraccount(String useraccount);

}
