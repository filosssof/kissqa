package org.fiodorov.view;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.utils.Defaults;
import org.fiodorov.utils.MD5Utils;
import org.fiodorov.validators.api.NotNullOrBlank;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
public class UserView {

    private Long id;

    @NotNullOrBlank(code = ErrorCode.FIRST_NAME_NOT_NULL_OR_BLANK)
    private String firstName;

    @NotNullOrBlank(code = ErrorCode.LAST_NAME_NOT_NULL_OR_BLANK)
    private String lastName;

    @NotNullOrBlank(code = ErrorCode.PASSWORD_NOT_NULL_OR_BLANK)
    private String email;

    @NotNullOrBlank(code = ErrorCode.EMAIL_NOT_NULL_OR_BLANK)
    private String password;

    @NotNull
    private Instant dateOfBirth;

    //for sign up
    private String username;

    private String accessToken;

    private Integer rank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGravatarImageSource(){
        String hash = MD5Utils.md5Hex(email);
        return Defaults.GRAVATAR_LINK.replace("{}",hash);
    }
}
