package com.evo.evoproject.domain.user;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private int userNo;
    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private String userAddress1;
    private String userAddress2;
    private int userPhone;
    private Timestamp userIndate;
    private char userMarketing;
    private char userWithdrawal;
    private char isAdmin = 'N';


}
