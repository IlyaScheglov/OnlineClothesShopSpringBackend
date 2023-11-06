package com.example.NewProject.controllers;

import com.example.NewProject.services.LikesService;
import com.example.NewProject.services.ProductsService;
import com.example.NewProject.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesPageRestController {

    private final LikesService likesService;

    private final UsersService usersService;

    @GetMapping("/get-likes-for-page")
    public ResponseEntity getLikesForPage(Principal principal){

        long userId = usersService.findUserByPrincipal(principal).getId();
        List<Object> allLikes = likesService.getAllLikes(userId);
        return ResponseEntity.ok(allLikes);
    }

    @DeleteMapping("/delete-like-from-page")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLikeFromPage(@RequestParam long likeId){

        likesService.deleteLikesByItsId(likeId);
    }
}
