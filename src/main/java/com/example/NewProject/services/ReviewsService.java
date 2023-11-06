package com.example.NewProject.services;

import com.example.NewProject.entities.Reviews;
import com.example.NewProject.repos.ReviewsRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewsService {

    private final ReviewsRepo reviewsRepo;

    private final UsersService usersService;

    public int getCountOfReviewsOnProduct(long productId){

        return reviewsRepo.findByProductId(productId).size();

    }

    public List<String> getListOfMiddleStarsOnProduct(long productId){

        List<String> result = new ArrayList<>();
        List<Reviews> reviewsOnProduct = reviewsRepo.findByProductId(productId);

        if (reviewsOnProduct.size() > 0){
            int summStars = reviewsOnProduct.stream().mapToInt(Reviews::getStars).sum();
            double middleStarWithFraction = (double) summStars / reviewsOnProduct.size();
            int middleStar = (int)Math.round(middleStarWithFraction);

            for (int i = 0; i < middleStar; i++){
                result.add("star");
            }

        }
        else{
            result.add("noReviews");
        }

        return result;
    }

    @Getter
    @Setter
    private final class ReviewsShowFormat{

        private long id;

        private String userName;

        private List<String> stars;

        private String reviewText;

    }

    private ReviewsShowFormat convertReviewsToShowFormat(Reviews review){

        List<String> listStars = new ArrayList<>();
        for (int i = 0; i < review.getStars(); i++){
            listStars.add("star");
        }

        ReviewsShowFormat reviewsShowFormat = new ReviewsShowFormat();
        reviewsShowFormat.setId(review.getId());
        reviewsShowFormat.setUserName(usersService.findNameById(review.getUserId()));
        reviewsShowFormat.setStars(listStars);
        reviewsShowFormat.setReviewText(review.getReviewText());
        return reviewsShowFormat;
    }

    public List<Object> getProductReviews(long productId){

        List<Object> listToReturn = new ArrayList<>();
        List<Reviews> reviews = reviewsRepo.findByProductId(productId);

        if (reviews.size() > 5){

            List<Reviews> limitedReviews = reviews.stream().limit(5).collect(Collectors.toList());
            limitedReviews.forEach(lr -> listToReturn.add(convertReviewsToShowFormat(lr)));
            listToReturn.add(true);
        }
        else{

            reviews.forEach(r -> listToReturn.add(convertReviewsToShowFormat(r)));
            listToReturn.add(false);
        }

        return listToReturn;
    }

    public List<Object> findAllReviewsOnProduct(long productId){

        List<Object> result = new ArrayList<>();
        List<Reviews> reviews = reviewsRepo.findByProductId(productId);
        reviews.forEach(r -> result.add(convertReviewsToShowFormat(r)));
        return result;
    }

    public boolean notRepeatReview(long userId, long productId){

        List<Reviews> reviews = reviewsRepo.findByUserAndProductIds(userId, productId);
        if (reviews.size() == 0) {
            return true;
        }
        else{
            return false;
        }
    }

    public void addNewReview(long userId, long productId, int stars, String comment){

        Reviews review = new Reviews();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setStars(stars);
        review.setReviewText(comment);
        reviewsRepo.save(review);
    }

}
