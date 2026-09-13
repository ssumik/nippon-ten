package dev.nipponten.application.services;

import dev.nipponten.application.exceptions.ComboNotFoundException;
import dev.nipponten.domain.models.Combo;
import dev.nipponten.domain.repositories.ComboProductRepository;
import dev.nipponten.domain.repositories.ComboRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ComboService {

    @Inject ComboRepository repository;

    @Inject ComboProductRepository comboProductRepository;

    public Combo create(Combo model) {
        return repository.save(model);
    }

    public Combo getById(Long id) {
        Combo model = repository.getById(id);
        if (model == null) throw new ComboNotFoundException(id);
        return model;
    }

    public List<Combo> getAll() {
        return repository.getAll();
    }

    public Combo update(Long id, Combo model) {
        getById(id);
        return repository.save(model);
    }

    @Transactional
    public void delete(Long id) {
        Combo model = getById(id);
        comboProductRepository.getByCombo(id).forEach(comboProductRepository::remove);
        repository.remove(model);
    }
}
