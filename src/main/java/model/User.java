package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseModel implements IModel {
    @JsonProperty("id")
    private Number id; // int or long
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("userStatus")
    private int userStatus;

    private User(User.Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.password = builder.password;
        this.phone = builder.phone;
        this.userStatus = builder.userStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                (isZero(id) ? "" : " id=" + id + ", ") +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }

    public static class Builder {
        private Number id;
        private String username;
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private String phone;
        private int userStatus;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder setLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setUserStatus(int userStatus) {
            this.userStatus = userStatus;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public Object getId() {
        return id;
    }

    public void updateId(Object id) {
        this.id = (Number)id;
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void updateFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void updateLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void updateUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
