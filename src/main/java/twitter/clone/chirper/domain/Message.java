package twitter.clone.chirper.domain;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    private int id;
    @NotEmpty
    @Size(max = 140)
    private String content;
    @NotEmpty
    private Date create_date;
    private String attachments;
    @ManyToMany(mappedBy = "messages")
    private List<ChirperUser> authors;

    public Message(String content, Date create_date, String attachements) {
        this.content = content;
        this.create_date = create_date;
        this.attachments = attachements;
    }
}