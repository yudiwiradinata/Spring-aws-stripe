package com.yudi.utils;

import com.yudi.backend.persistence.domain.backend.User;

/**
 * Created by Yudi on 28/12/2017.
 */
public class UserUtils {

    private UserUtils() {
        throw new AssertionError("non instatiable");
    }

    public static User createBasicUser(){
        User user = new User();
        user.setUsername("basicUser");
        user.setPassword("password");
        user.setEmail("ä@gfk.com");
        user.setFirstName("yudi");
        user.setLastName("dinata");
        user.setPhoneNumber("021283929");
        user.setCountry("INA");
        user.setEnabled(true);
        user.setDescription("TES DESC");
        user.setProfileImageUrl("gssfsfdssefe");
        return user;
    }

    public static User createBasicUser(String username, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword("password123");
        user.setEmail(email);
        user.setFirstName("yudi");
        user.setLastName("dinata");
        user.setPhoneNumber("021283929");
        user.setCountry("INA");
        user.setEnabled(true);
        user.setDescription("TES DESC");
        user.setProfileImageUrl("gssfsfdssefe");
        return user;
    }

}
