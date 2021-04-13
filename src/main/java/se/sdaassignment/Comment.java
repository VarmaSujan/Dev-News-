package se.sdaassignment;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String body;
    @Column(nullable = false)
    @NotBlank
    private String authorName;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(nullable = false)
    @NotNull
    private Article commentArticle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String commBody) {
        this.body = commBody;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String commAuthor) {
        this.authorName = commAuthor;
    }

    public Article getCommentArticle() {
        return commentArticle;
    }

    public void setCommentArticle(Article commentArticle) {
        this.commentArticle = commentArticle;
    }
}
