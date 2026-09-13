package dev.nipponten.application.services;

import dev.nipponten.application.exceptions.PromotionNotFoundException;
import dev.nipponten.domain.models.Promotion;
import dev.nipponten.domain.models.PromotionPrice;
import dev.nipponten.domain.models.PromotionType;
import dev.nipponten.domain.repositories.PromotionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class PromotionService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Inject PromotionRepository repository;

    @Inject ProductService productService;

    @Inject ProductSizeService productSizeService;

    @Inject PromotionTypeService promotionTypeService;

    public Promotion create(Promotion model) {
        requireReferences(model);
        return repository.save(model);
    }

    public Promotion getById(Long id) {
        Promotion model = repository.getById(id);
        if (model == null) throw new PromotionNotFoundException(id);
        return model;
    }

    public List<Promotion> getAll() {
        return repository.getAll();
    }

    public Promotion update(Long id, Promotion model) {
        getById(id);
        requireReferences(model);
        return repository.save(model);
    }

    public void delete(Long id) {
        Promotion model = getById(id);
        repository.remove(model);
    }

    public List<Promotion> getByProduct(Long productId) {
        return repository.getByProduct(productId);
    }

    public List<Promotion> getByPromotionType(Long promotionTypeId) {
        return repository.getByPromotionType(promotionTypeId);
    }

    public List<PromotionPrice> getPrices(Promotion promotion) {
        PromotionType promotionType = promotionTypeService.getById(promotion.promotionTypeId());
        return productSizeService.getByProduct(promotion.productId()).stream()
                .map(
                        size ->
                                new PromotionPrice(
                                        size.id(),
                                        size.price(),
                                        applyDiscount(size.price(), promotionType)))
                .toList();
    }

    private BigDecimal applyDiscount(BigDecimal price, PromotionType promotionType) {
        BigDecimal discounted =
                switch (promotionType.type()) {
                    case PERCENTAGE_DISCOUNT ->
                            price.multiply(ONE_HUNDRED.subtract(promotionType.value()))
                                    .divide(ONE_HUNDRED);
                    case FIXED_DISCOUNT -> price.subtract(promotionType.value());
                };
        return discounted.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    private void requireReferences(Promotion model) {
        productService.getById(model.productId());
        promotionTypeService.getById(model.promotionTypeId());
    }
}
