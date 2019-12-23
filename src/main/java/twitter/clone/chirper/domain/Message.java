package twitter.clone.chirper.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial", name = "msg_id")
    private int id;
    @NotEmpty
    @Size(max = 140)
    private String content;
    @DateTimeFormat
    private Timestamp create_date;
    private String attachments;
    @ManyToMany
    private List<ChirperUser> authors;

    public Message() {
        this.create_date = new Timestamp((new java.util.Date().getTime()));
    }
}