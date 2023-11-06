package com.example.NewProject.controllers;

import com.example.NewProject.services.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
public class NewProductRestController {

    @Value("${upload.path}")
    private String uploadPath;

    private final ImagesService imagesService;

    private final ProductsService productsService;

    private final ProductOnStockService productOnStockService;

    private final CategoriesService categoriesService;

    private final ColorsService colorsService;

    @GetMapping("/get-all-categories-to-create-product")
    public ResponseEntity getAllCategories(){

        return ResponseEntity.ok(categoriesService.findAllCategories());
    }

    @GetMapping("/get-all-colors-to-create-product")
    public ResponseEntity getAllColors(){

        return ResponseEntity.ok(colorsService.findAllColors());
    }

    @PostMapping(value = "/add_photo", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity addrecipe(@RequestParam("file")MultipartFile photo) throws IOException {

        if (!photo.isEmpty()){

            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "-" + photo.getOriginalFilename();

            photo.transferTo(new File(uploadPath + "/" + resultFileName));

            return ResponseEntity.ok("/img/" + resultFileName);
        }
        else{
            return ResponseEntity.ok("/img/nophoto.jpg");
        }
    }

    @PostMapping("/add-new-product")
    public ResponseEntity addNewProduct(@RequestParam String title, @RequestParam String smallDescription,
                                        @RequestParam String bigDescription, @RequestParam String cost,
                                        @RequestParam String sizes, @RequestParam int categoryId,
                                        @RequestParam int colorId, @RequestParam String[] photoArray){

        try{
            long productId = productsService.addNewProduct(title, smallDescription, bigDescription,
                    cost, (long)categoryId, (long)colorId);
            if(categoryId == 2){
                productOnStockService.addProductToStock(productId, 1);
            }
            else{
                List<String> strSizes = List.of(sizes.split(" "));
                strSizes.forEach(ss -> productOnStockService.addProductToStock(productId, Integer.parseInt(ss)));
            }
            List<String> waysToImages = Arrays.stream(photoArray).toList();
            waysToImages.forEach(wti -> imagesService.addNewImage(productId, wti));
            return ResponseEntity.ok("Товар загружен");
        }
        catch (Exception e){
            return ResponseEntity.ok("Что-то пошло не так");
        }
    }
}
