package se.sdaassignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    CommentRepository commentRepository;
    ArticleRepository articleRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository){
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("articles/{articleId}/comments") //return all comments on article given by articleId
    public ResponseEntity <List<Comment>> findAllArticleComments(@PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article.getComments());
    }

    @GetMapping("/comments") //return all comments made by author given by authorName.
    public ResponseEntity<List<Comment>> viewAllCommentsByAuthor(
            @RequestParam(value = "authorName", required = true) String authorName) {
        return ResponseEntity.ok(commentRepository.findByAuthorName(authorName));
    }

    @PutMapping("/comments/{id}") //update the given comment
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment){
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(comment.getId());
        updatedComment.setCommentArticle(comment.getCommentArticle());
        return ResponseEntity.ok(commentRepository.save(updatedComment));
    }

    @DeleteMapping("/comments/{id}") //delete the given comment
    public ResponseEntity<Comment> deleteComment(@PathVariable Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        commentRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/articles/{articleId}/comments") //create a new comment on article given by articleId.
    public ResponseEntity<Comment> createArticleComment(@PathVariable Long articleId, @RequestBody Comment comment){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        comment.setCommentArticle(article);
        return ResponseEntity.ok(commentRepository.save(comment));
    }

//    @PostMapping ("/comments")
//    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
//        commentRepository.save(comment);
//        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
//    }
}
