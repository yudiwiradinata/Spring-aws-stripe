package com.yudi.backend.persistence.domain.backend;

import com.yudi.backend.persistence.converters.LocalDateTimeAttributeConverter;
import com.yudi.utils.Constans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Yudi on 02/01/2018.
 */
@Entity
public class PasswordResetToken implements Serializable{

    private static final long serialVersionUID = 1L;

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetToken.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user, LocalDateTime creationDateTime, int expirationInMinutes) {
        if((null == token) || (null == user) || (null == creationDateTime)){
            throw new IllegalArgumentException("token, user and creation date time can not be null");
        }

        if(expirationInMinutes == 0){
            LOG.warn("expirationInMinutes is zero, assigning default value {}", Constans.Login.DEFAULT_TOKEN_LENGTH_IN_MINUTES);
            expirationInMinutes = Constans.Login.DEFAULT_TOKEN_LENGTH_IN_MINUTES;
        }

        this.token = token;
        this.user = user;
        this.expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetToken that = (PasswordResetToken) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

