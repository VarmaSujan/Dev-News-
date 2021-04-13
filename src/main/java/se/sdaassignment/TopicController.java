package se.sdaassignment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TopicController {

    TopicRepository topicRepository;
    ArticleRepository articleRepository;

    public TopicController(TopicRepository topicRepository, ArticleRepository articleRepository) {
        this.topicRepository = topicRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/topics") //return all topics.
    public List<Topic> listAllTopics(){
        List<Topic> topics = topicRepository.findAll();
        return topics;
    }

    @GetMapping("/articles/{articleId}/topics") //return all topics associated with article given by articleId.
    public ResponseEntity <List<Topic>> findAllArticleTopics(@PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article.getTopics());
    }

    @PostMapping("/articles/{articleId}/topics") //associate the topic with the article given by articleId. Create topic if not existing.
    public ResponseEntity<Article> createArticleTopic(@PathVariable Long articleId, @RequestBody Topic updatedTopic){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        Topic topic = topicRepository.findByName(updatedTopic.getName());
        if(topic==null){
            topicRepository.save(updatedTopic);
            article.getTopics().add(updatedTopic);
        }
        else {
            article.getTopics().add(topic);
        }
        articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @PostMapping("/topics") //create a new topic
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    @PutMapping("/topics/{id}") //update the given topic
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @Valid @RequestBody Topic updatedTopic){
        Topic topic = topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedTopic.setId(id);
        topicRepository.save(updatedTopic);
        return ResponseEntity.ok(updatedTopic);
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Topic> deleteTopic(@PathVariable Long id){
        Topic topic = topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        topicRepository.delete(topic);
        return ResponseEntity.ok(topic);
    }

    @DeleteMapping("/articles/{articleId}/topics/{topicId}") //delete the association of a topic for the given article. The topic & article themselves remain.
    public ResponseEntity<Article>deleteArticleTopic(@PathVariable Long articleId, @PathVariable Long topicId){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        article.getTopics().remove(topic);
        articleRepository.save(article);
        return  ResponseEntity.ok(article);

    }
}
