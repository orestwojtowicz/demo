package com.blog.demo.controller;

import com.blog.demo.entities.Image;
import com.blog.demo.entities.User;
import com.blog.demo.repositories.CommentRepository;
import com.blog.demo.repositories.PostRepository;
import com.blog.demo.repositories.UserRepository;
import com.blog.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

/**
 * Created by damiass on Oct, 2019
 */
@Slf4j
@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;


    public ProfileController(UserRepository userRepository,CommentRepository commentRepository, UserService userService) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    /*@Secured("ROLE_ADMIN, ROLE_USER")*/
    @GetMapping("/profile")
    public String getUserName(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = auth.getName();
        Optional<User> userData = userRepository.findByEmail(loggedUserName);
        String nick = userData.get().getNick();
        model.addAttribute("userName", loggedUserName);
        model.addAttribute("nick", nick);
        model.addAttribute("image", userData.get().getUserAvatar());
        model.addAttribute("commentCount", userData.get().getUserCommentCount());

        return "profile";
    }



    // change user avatar, max size avatar is 1 mb

    @PostMapping("/avatar/upload")
    public String uploadUserAvatar(MultipartFile file, User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = auth.getName();
        Optional<User> userData = userRepository.findByEmail(loggedUserName);
        String nick = userData.get().getNick();

        try {
            byte[] avatar = file.getBytes();
            int imageSize = avatar.length;
            log.info(imageSize + " IMAGE SIZE ");

            if (imageSize > 200000) {
                return "/profile/error";
            } else {
                byte[] encodingAvatar = Base64.getEncoder().encode(avatar);
                String saveEncodedAvatar = new String(encodingAvatar, "UTF-8");
                userRepository.updateUserAvatar(saveEncodedAvatar, userData.get().getId());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile";
    }


}












