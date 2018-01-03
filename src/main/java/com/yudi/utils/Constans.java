package com.yudi.utils;

/**
 * Created by Yudi on 27/12/2017.
 */
public class Constans {

    /* contact */
    public class Contact{
        public static final String CONTACT_US_LINK="/contact";
        public static final String FEEDBACK_MODEL_KEY="feedback";
        public static final String CONTACT_US_VIEW_NAME="contact/contact";
    }

    /* login */
    public class Login{
        public static final String LOGIN_LINK="/login";
        public static final String LOGIN_VIEW_NAME="user/login";
        public static final int DEFAULT_TOKEN_LENGTH_IN_MINUTES = 120;
    }

    /* payload */
    public class Payload{
        public static final String PAYLOAD_LINK="/payload";
        public static final String PAYLOAD_VIEW_NAME="payload/payload";
    }

    /* forgot my password */
    public class ForgotPassword{
        public static final String EMAIL_ADDRESS_VIEW_NAME="forgotmypassword/emailForm";
        public static final String FORGOT_PASSWORD_LINK="/forgotmypassword";
        public static final String MAIL_SENT_KEY="mailSent";
        public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";
        public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

    }
}
