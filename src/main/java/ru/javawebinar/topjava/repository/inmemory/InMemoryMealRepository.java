package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.chrono.ChronoLocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for(Meal m: MealsUtil.meals){
            this.save(m, 1);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if(repository.containsKey(meal.getId())){
            if(meal.getUserId() == userId){
                return repository.put(meal.getId(), meal);
            }else{
                return null;
            }
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public Boolean delete(int id, int userId) {
        if(repository.get(id).getUserId() != userId){
            return null;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (!repository.containsKey(id)) {
            return null;
        }
        if (repository.get(id).getUserId() == userId) {
            return repository.get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> dateFilter(ChronoLocalDate startDate, ChronoLocalDate endDate, int userId) {
        return repository.values().stream()
                .filter(m -> ((m.getDate().equals(startDate)||m.getDate().isAfter(startDate)))&&(m.getDate().equals(endDate)||m.getDate().isBefore(endDate))&&(m.getUserId()==userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(m -> userId == m.getUserId())
                .sorted(Comparator.comparing(Meal::getDate).reversed().thenComparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
    }
}

