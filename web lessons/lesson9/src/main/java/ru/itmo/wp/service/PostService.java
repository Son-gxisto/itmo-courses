package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.repository.PostRepository;
import ru.itmo.wp.repository.TagRepository;

import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public Post findById(Long id) {
        return id == null ? null : postRepository.findById(id).orElse(null);
    }

    public void addComment(Post post, Comment comment) {
        post.getComments().add(comment);
        postRepository.save(post);
    }

    public Post makePost(PostForm postForm) {
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setText(postForm.getText());
        String[] tmp = postForm.getTags().split(" +");
        Set<String> tagNames = new HashSet<>();
        List<Tag> tagList = new ArrayList<>();
        for (String s : tmp) {
            if (s.equals("")) continue;
            tagNames.add(s.toLowerCase());
        }
        for (String s : tagNames) {
            Tag tag = tagRepository.findByName(s);
            if (tag == null) {
                tag = new Tag();
                tag.setName(s);
            }
            tagList.add(tag);
        }
        post.setTags(tagList);
        return post;
    }
}
