package com.blog.demo.controller;

import com.blog.demo.entities.Image;
import com.blog.demo.entities.Post;
import com.blog.demo.repositories.PostRepository;
import com.blog.demo.services.ImageService;
import com.blog.demo.services.PostService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Optional;

/**
 * Created by damiass on Oct, 2019
 */
@Slf4j
@Controller
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final ImageService imageService;

    public PostController(PostService postService,PostRepository postRepository,
                          ImageService imageService) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.imageService = imageService;
    }

    @GetMapping("/read/post")
    public String loadPostToRead(Model model) {

        return "readpost";
    }


    @GetMapping("/post")
    public String loadPostPage(Model model) {
        model.addAttribute("post", new Post());
        return "post";
    }

    @PostMapping("/post")
    public String addNewPost(@Valid Post post, BindingResult bindingResult,
                             Model model, RedirectAttributes redirectAttributes) {
        log.info("POST MAPPING TRIGGERED ");
        if(bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "post";
        } else {

        }
        log.info("CREATING NEW POST  ");
        Post newPost = postService.addNewPost(post);
        redirectAttributes.addAttribute("id", newPost.getId())
                .addFlashAttribute("success", true);
        return "redirect:/post";
    }

    // GET SINGLE POST
    @GetMapping("/post/{id}")
    public String getSinglePost(Model model, @PathVariable Long id) {
        Optional<Post> getPost = postRepository.findById(id);



        String postContent = (getPost.get().getPostContent());



        model.addAttribute("postContent", postContent);


        return "readpost";
    }









    // UPLOAD AND GET IMAGE
    @PostMapping("/fileupload")
    public String fileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
        try {

            byte[] image = file.getBytes();
            Image model = new Image(name, image);
            int saveImage = imageService.saveImage(model);
            if (saveImage == 1) {
                return "success";
            } else {
                return "error";
            }
        } catch (Exception e) {
            log.info("BLAD W KONTROLERZE " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/image/{id}")
    public String getImageDetails(@PathVariable Long id, Model model) {
        try {

            Image imagesObj = imageService.getImage(id); // getting image
           // model.addAttribute("id", imagesObj.getId());
            model.addAttribute("name", imagesObj.getName());

            byte[] encode = Base64.getEncoder().encode(imagesObj.getImage());
            model.addAttribute("image", new String(encode, "UTF-8"));

            return "imagedetails";
        } catch (Exception e) {

            model.addAttribute("message", "Error in getting image");
            return "redirect:/";
        }
    }


}





















