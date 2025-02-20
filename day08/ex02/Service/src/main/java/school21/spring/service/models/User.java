package school21.spring.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    public User(){}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}