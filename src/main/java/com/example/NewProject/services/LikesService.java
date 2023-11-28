package com.example.NewProject.services;

import com.example.NewProject.entities.Likes;
import com.example.NewProject.entities.Products;
import com.example.NewProject.entities.Users;
import com.example.NewProject.repos.LikesRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepo likesRepo;

    private final ProductsService productsService;

    private final ImagesService imagesService;

    public void addLike(Users user, Products product){

        Likes like = new Likes();
        like.setUser(user);
        like.setProduct(product);
        likesRepo.save(like);

    }

    public void deleteLike(long userId, long productId){

        Likes likeToDelete = likesRepo.checkLikedByUser(userId, productId).get(0);
        likesRepo.delete(likeToDelete);

    }

    public List<Likes> checkLikedByUser(long userId, long productId){
        return likesRepo.checkLikedByUser(userId, productId);
    }

    public void deleteLikesByItsId(long likeId){

        Likes like = likesRepo.findByLikeId(likeId);
        likesRepo.delete(like);
    }

    public List<Object> getAllLikes(long userId){

        List<Likes> likes = likesRepo.findByUsId(userId);
        return convertLikesToShowFormat(likes);
    }

    @Getter
    @Setter
    private final class LikesShowFormat{

        private long id;

        private long productId;

        private String image;

        private String title;

        private String cost;
    }

    private List<Object> convertLikesToShowFormat(List<Likes> likes){

        List<Object> likesShowFormat = new ArrayList<>();
        likes.forEach(l -> {
            Products product = l.getProduct();
            LikesShowFormat likeNewFormat = new LikesShowFormat();
            likeNewFormat.setId(l.getId());
            likeNewFormat.setProductId(l.getProduct().getId());
            likeNewFormat.setImage(imagesService.getFirstImageOfProduct(l.getProduct().getId()));
            likeNewFormat.setTitle(product.getTitle());
            likeNewFormat.setCost(product.getCost());
            likesShowFormat.add(likeNewFormat);
        });
        return likesShowFormat;
    }
}
