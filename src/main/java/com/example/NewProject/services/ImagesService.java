package com.example.NewProject.services;

import com.example.NewProject.entities.Images;
import com.example.NewProject.entities.Products;
import com.example.NewProject.repos.ImagesRepo;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepo imagesRepo;

    private final MinioComponent minioComponent;

    public List<String> getAllImagesOnProduct(long productId){

        List<String> result = new ArrayList<>();

        List<Images> images = imagesRepo.findByPrdImId(productId);

        images.forEach(im -> result.add(minioComponent.getObject(im.getWayToFile())));

        return  result;

    }

    public String getFirstImageOfProduct(long productId){
        List<Images> images = imagesRepo.findByPrdImId(productId);
        return minioComponent.getObject(images.get(0).getWayToFile());
    }

    public void addNewImage(Products product, String wayToFile){

        Images image = new Images();
        image.setProduct(product);
        image.setWayToFile(wayToFile);
        imagesRepo.save(image);
    }
}
