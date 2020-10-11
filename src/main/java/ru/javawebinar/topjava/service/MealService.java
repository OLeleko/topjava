package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;


@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal){
        return repository.save(meal);
    }

    public void delete(int id, int userId){
        userCheck(id, userId);
        checkNotFoundWithId(repository.get(id), id);
    }

    public Meal get(int id, int userId){
        userCheck(id, userId);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Collection<Meal> getAll(int userId){
        return repository.getAll(userId);
    }

    public void update (Meal meal, int userId){
        userCheck(meal.getUserId(), userId);
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    private void userCheck(int id, int userId){
        if(repository.get(id).getUserId() != userId){
            throw new NotFoundException("User is not meal owner");
        }
    }
}