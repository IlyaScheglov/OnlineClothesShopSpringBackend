package com.example.NewProject.services;

import com.example.NewProject.entities.Images;
import com.example.NewProject.repos.ImagesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepo imagesRepo;

    public List<String> getAllImagesOnProduct(long productId){

        List<String> result = new ArrayList<>();

        List<Images> images = imagesRepo.findByProductId(productId);

        images.forEach(im -> result.add(im.getWayToFile()));

        return  result;

    }

    public void addNewImage(long productId, String wayToFile){

        Images image = new Images();
        image.setProductId(productId);
        image.setWayToFile(wayToFile);
        imagesRepo.save(image);
    }
}
