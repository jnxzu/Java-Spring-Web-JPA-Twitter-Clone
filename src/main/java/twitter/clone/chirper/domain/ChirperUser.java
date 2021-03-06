package twitter.clone.chirper.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ChirperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial", name = "user_id")
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
    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Message> messages;

    public ChirperUser(int id, String firstName, String lastName, String nick, String password, String email){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.nick=nick;
        this.password=password;
        this.email=email;
    }
}