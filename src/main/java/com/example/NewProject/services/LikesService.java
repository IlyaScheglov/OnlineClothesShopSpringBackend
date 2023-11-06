package com.example.NewProject.services;

import com.example.NewProject.entities.Likes;
import com.example.NewProject.entities.Products;
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

    public void addLike(long userId, long productId){

        Likes like = new Likes();
        like.setUserId(userId);
        like.setProductId(productId);
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

        List<Likes> likes = likesRepo.findByUserId(userId);
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
            Products product = productsService.findProductById(l.getProductId());
            LikesShowFormat likeNewFormat = new LikesShowFormat();
            likeNewFormat.setId(l.getId());
            likeNewFormat.setProductId(l.getProductId());
            likeNewFormat.setImage(imagesService.getAllImagesOnProduct(l.getProductId()).get(0));
            likeNewFormat.setTitle(product.getTitle());
            likeNewFormat.setCost(product.getCost());
            likesShowFormat.add(likeNewFormat);
        });
        return likesShowFormat;
    }
}
