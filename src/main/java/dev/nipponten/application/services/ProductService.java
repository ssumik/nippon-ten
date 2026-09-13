package dev.nipponten.application.services;

import dev.nipponten.application.exceptions.InvalidRequestException;
import dev.nipponten.application.exceptions.ProductNotFoundException;
import dev.nipponten.domain.models.Product;
import dev.nipponten.domain.repositories.AdditionalIngredientRepository;
import dev.nipponten.domain.repositories.ComboProductRepository;
import dev.nipponten.domain.repositories.ProductIngredientRepository;
import dev.nipponten.domain.repositories.ProductRepository;
import dev.nipponten.domain.repositories.ProductSizeRepository;
import dev.nipponten.domain.repositories.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject ProductRepository repository;

    @Inject ProductSizeRepository productSizeRepository;

    @Inject ProductIngredientRepository productIngredientRepository;

    @Inject AdditionalIngredientRepository additionalIngredientRepository;

    @Inject ComboProductRepository comboProductRepository;

    @Inject PromotionRepository promotionRepository;

    public Product create(Product model) {
        return repository.save(model);
    }

    public Product getById(Long id) {
        Product model = repository.getById(id);
        if (model == null) throw new ProductNotFoundException(id);
        return model;
    }

    public List<Product> getAll() {
        return repository.getAll();
    }

    public Product update(Long id, Product model) {
        getById(id);
        return repository.save(model);
    }

    @Transactional
    public void delete(Long id) {
        Product model = getById(id);
        int combos = comboProductRepository.getByProduct(id).size();
        int promotions = promotionRepository.getByProduct(id).size();
        if (combos > 0 || promotions > 0) {
            throw new InvalidRequestException(
                    "Product "
                            + id
                            + " cannot be deleted: used by "
                            + combos
                            + " combo(s) and "
                            + promotions
                            + " promotion(s)");
        }
        productSizeRepository.getByProduct(id).forEach(productSizeRepository::remove);
        productIngredientRepository.getByProduct(id).forEach(productIngredientRepository::remove);
        additionalIngredientRepository
                .getByProduct(id)
                .forEach(additionalIngredientRepository::remove);
        repository.remove(model);
    }
}
