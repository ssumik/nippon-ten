package dev.nipponten.application.services;

import dev.nipponten.application.exceptions.InternalNotFoundException;
import dev.nipponten.domain.models.Internal;
import dev.nipponten.domain.repositories.InternalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class InternalService {

    @Inject InternalRepository repository;

    @Inject InternalRoleService internalRoleService;

    public Internal create(Internal model) {
        internalRoleService.getById(model.internalRoleId());
        return repository.save(model);
    }

    public Internal getById(Long id) {
        Internal model = repository.getById(id);
        if (model == null) throw new InternalNotFoundException(id);
        return model;
    }

    public List<Internal> getAll() {
        return repository.getAll();
    }

    public Internal update(Long id, Internal model) {
        Internal current = getById(id);
        internalRoleService.getById(model.internalRoleId());
        return repository.save(
                new Internal(
                        id,
                        current.userId(),
                        model.internalRoleId(),
                        model.name(),
                        model.lastName(),
                        model.cpf()));
    }

    public void delete(Long id) {
        Internal model = getById(id);
        repository.remove(model);
    }

    public List<Internal> getByUser(Long userId) {
        return repository.getByUser(userId);
    }

    public List<Internal> getByInternalRole(Long internalRoleId) {
        return repository.getByInternalRole(internalRoleId);
    }
}
