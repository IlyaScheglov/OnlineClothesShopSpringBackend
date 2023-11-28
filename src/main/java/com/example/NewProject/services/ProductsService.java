package com.example.NewProject.services;

import com.example.NewProject.entities.Products;
import com.example.NewProject.entities.ProductsToShow;
import com.example.NewProject.repos.ProductsRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepo productsRepo;

    private final ReviewsService reviewsService;

    private final ImagesService imagesService;

    private final ProductOnStockService productOnStockService;

    private final ProductInOrderService productInOrderService;

    private final CategoriesService categoriesService;

    private final ColorsService colorsService;


    public Products findProductById(long productId){

        return productsRepo.findByProductId(productId);

    }

    public List<Products> findProductsOnStock(){

        List<Products> result = new ArrayList<>();

        List<Long> allStockProductsIds = productOnStockService.getProductIdWhoOnStock();

        Collections.shuffle(allStockProductsIds);

        List<Long> stockProductsIdsFive = allStockProductsIds.stream()
                .limit(10).collect(Collectors.toList());

        stockProductsIdsFive.forEach(spi -> result.add(productsRepo.findByProductId(spi)));

        return result;

    }

    @Getter
    @Setter
    private final class ProductsBestSellersFind{

        private long id;

        private int countSells;
    }

    public List<Products> findBestSellers(){

        List<Products> result = new ArrayList<>();

        List<Long> productsOnStockIds = productInOrderService.findAllSelledProductsOnStockIds();

        List<Long> productsSelledId = productOnStockService.findBestSellersProductIds(productsOnStockIds);

        if (productsSelledId.stream().distinct().collect(Collectors.toList()).size() >= 6){

            List<ProductsBestSellersFind> bestSellers = new ArrayList<>();

            productsSelledId.forEach(prod -> {

                int countDublicates = Collections.frequency(productsSelledId, prod);

                if (bestSellers.size() < 6){

                    ProductsBestSellersFind bestSeller = new ProductsBestSellersFind();
                    bestSeller.setId(prod);
                    bestSeller.setCountSells(countDublicates);
                    bestSellers.add(bestSeller);

                }
                else{

                    List<ProductsBestSellersFind> sortedList = bestSellers.stream()
                            .sorted(Comparator.comparing(ProductsBestSellersFind::getCountSells))
                            .collect(Collectors.toList());

                    ProductsBestSellersFind minElement = sortedList.get(0);

                    if(minElement.getCountSells() < countDublicates){

                        bestSellers.remove(minElement);

                        ProductsBestSellersFind bestSeller = new ProductsBestSellersFind();
                        bestSeller.setId(prod);
                        bestSeller.setCountSells(countDublicates);
                        bestSellers.add(bestSeller);

                    }

                }

            });

            bestSellers.forEach(bs -> result.add(productsRepo.findByProductId(bs.getId())));

        }
        else{

            List<Products> allProducts = productsRepo.findAll();

            Collections.shuffle(allProducts);

            allProducts.stream().limit(6).collect(Collectors.toList()).
                    forEach(allPrd -> result.add(allPrd));

        }

        return result;

    }

    public List<Products> findAllProducts(){
        return productsRepo.findAll();
    }



    public List<ProductsToShow> convertListProductsToShowFormat(List<Products> products){
        List<ProductsToShow> result = new ArrayList<>();

        products.forEach(p -> result.add(convertProductsToShowFormat(p)));

        return result;
    }

    public ProductsToShow convertProductsToShowFormat(Products products){

        ProductsToShow newProductForm = new ProductsToShow();

        newProductForm.setId(products.getId());
        newProductForm.setTitle(products.getTitle());
        newProductForm.setCost(products.getCost());
        newProductForm.setSmallDescription(products.getSmallDescription());
        newProductForm.setBigDescription(products.getBigDescription());
        newProductForm.setColorId(products.getColor().getId());
        newProductForm.setCategoryId(products.getCategory().getId());
        newProductForm.setSizes(productOnStockService.findSizesOfProduct(products.getId()));
        newProductForm.setCountReviews(reviewsService.getCountOfReviewsOnProduct(products.getId()));
        newProductForm.setStars(reviewsService.getListOfMiddleStarsOnProduct(products.getId()));
        newProductForm.setImages(imagesService.getAllImagesOnProduct(products.getId()));

        return newProductForm;

    }

    public List<Products> filterProducts(int sortingType, int costFilter, int colorFilter,
                                       int categoryFilter, int sizeFilter, String searchFilter,
                                       List<Products> products){


        int minCostValue = getMinValueForCostFilter(costFilter);
        int maxCostFilter = getMaxValueForCostFilter(costFilter);
        List<Long> productIdsListForSizeFilter = productOnStockService.findProductIdBySize(sizeFilter);


        List<Products> filteredProducts = products.stream()
                .filter(p -> (int)Double.parseDouble(p.getCost()) > minCostValue)
                .filter(p -> (int)Double.parseDouble(p.getCost()) < maxCostFilter)
                .filter(p -> colorFilter > 0 ? p.getColor().getId() == (long)colorFilter : p.getColor().getId() > 0L)
                .filter(p -> categoryFilter > 0 ? p.getCategory().getId() == (long)categoryFilter : p.getCategory().getId() > 0L)
                .filter(p -> p.getTitle().toLowerCase().contains(searchFilter.toLowerCase()))
                .filter(p -> sizeFilter > 0 ? productIdsListForSizeFilter.contains(p.getId()) : p.getId() > 0L)
                .collect(Collectors.toList());

        return sortProducts(filteredProducts, sortingType);

    }


    private List<Products> sortProducts(List<Products> products, int sortingType){

        List<Products> result = new ArrayList<>();

        if(sortingType == 0){
            result = products.stream().sorted(new IdComparator())
                    .collect(Collectors.toList());
        }
        else if(sortingType == 1){
            result = products.stream().sorted(new IdComparatorDesc())
                    .collect(Collectors.toList());
        }
        else if(sortingType == 2){
            result = products.stream().sorted(new CostComparator())
                    .collect(Collectors.toList());
        }
        else if(sortingType == 3){
            result = products.stream().sorted(new CostComparatorDesc())
                    .collect(Collectors.toList());
        }

        return result;

    }

    class IdComparator implements Comparator<Products>{
        @Override
        public int compare(Products a, Products b){

            long aLong = a.getId();
            long bLong = b.getId();

            return (aLong < bLong) ? -1 : ((aLong == bLong) ? 0 : 1);

        }

    }

    class IdComparatorDesc implements Comparator<Products>{
        @Override
        public int compare(Products a, Products b){

            long aLong = a.getId();
            long bLong = b.getId();

            return (aLong < bLong) ? 1 : ((aLong == bLong) ? 0 : -1);

        }

    }

    class CostComparator implements Comparator<Products>{

        @Override
        public int compare(Products a, Products b){

            int aInt = (int)Double.parseDouble(a.getCost());
            int bInt = (int)Double.parseDouble(b.getCost());

            return (aInt < bInt) ? -1 : ((aInt == bInt) ? 0 : 1);

        }

    }

    class CostComparatorDesc implements Comparator<Products>{

        @Override
        public int compare(Products a, Products b){

            int aInt = (int)Double.parseDouble(a.getCost());
            int bInt = (int)Double.parseDouble(b.getCost());

            return (aInt < bInt) ? 1 : ((aInt == bInt) ? 0 : -1);

        }

    }



    private int getMinValueForCostFilter(int id){

        if (id == 0){
            return 0;
        }
        else if (id == 1){
            return 0;
        }
        else if(id == 2){
            return 250;
        }
        else if(id == 3){
            return 500;
        }
        else if(id == 4) {
            return 1000;
        }
        else if(id == 5){
            return 3000;
        }
        else {
            return 0;
        }

    }

    private int getMaxValueForCostFilter(int id){

        if (id == 0){
            return 100000;
        }
        else if (id == 1){
            return 250;
        }
        else if(id == 2){
            return 500;
        }
        else if(id == 3){
            return 1000;
        }
        else if(id == 4) {
            return 3000;
        }
        else if(id == 5){
            return 100000;
        }
        else {
            return 100000;
        }

    }

    public Products addNewProduct(String title, String smallDesc, String bigDesc,
                              String cost, long categoryId, long colorId){

        Products product = new Products();
        product.setTitle(title);
        product.setSmallDescription(smallDesc);
        product.setBigDescription(bigDesc);
        product.setCost(cost + ".00");
        product.setCategory(categoriesService.findCategoryById(categoryId));
        product.setColor(colorsService.findColorById(colorId));
        productsRepo.save(product);
        return product;
    }


}
