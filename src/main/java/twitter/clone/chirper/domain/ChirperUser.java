package twitter.clone.chirper.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ChirperUser {
    @Id
    @GeneratedValue
    private int id;
    @NotEmpty
    @Size(min = 2)
    private String firstName;
    @NotEmpty
    @Size(min = 2)
    private String lastName;
    @NotEmpty
    @Size(min = 6, max = 24)
    @Column(unique = true)
    private String nick;
    @NotEmpty
    @Size(min = 8, max = 24)
    private String password;
    @Email
    @NotEmpty
    @Column(unique = true)
    private String email;
    @ManyToMany
    private List<Message> messages;

    public ChirperUser(String firstName, String lastName, String nick, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
        this.password = password;
        this.email = email;
    }
}