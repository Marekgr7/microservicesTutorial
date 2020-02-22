package gryszq.service.microservices.service;

import gryszq.service.microservices.model.Post;
import gryszq.service.microservices.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class PostUserDaoService {

    private static List<Post> posts = new ArrayList<>();
    private static int postsCount = 3;

    static {
        posts.add(new Post(1,"auto","mitsubishi"));
        posts.add(new Post(2,"human","human mark"));
        posts.add(new Post(3,"factory","lear"));
    }

    public List<Post> findAll(){
        return posts;
    }

    public Post getOne(int id){
        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    public Post deletePost(int id) {
        Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()){
            Post post = iterator.next();
            if(post.getId() == id){
                iterator.remove();
                return post;
            }
        }
        return null;
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(++postsCount);
        }
        posts.add(post);
        return post;
    }
}
