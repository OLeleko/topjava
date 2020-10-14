package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (Meal m : MealsUtil.meals) {
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
        if (repository.containsKey(meal.getId())) {
            if (repository.get(meal.getId()).getUserId() == userId) {
                return repository.put(meal.getId(), meal);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public Boolean delete(int id, int userId) {
        if (repository.get(id).getUserId() != userId) {
            return null;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.values().stream()
                .filter(meal -> (meal.getId() == id && meal.getUserId() == userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Meal> getFilteredDateBetweenList(LocalDate startDate, LocalDate endDate, int userId) {
        Predicate<Meal> predicate = m -> ((m.getUserId() == userId) && (m.getDate().equals(startDate) || m.getDate().isAfter(startDate))) && (m.getDate().equals(endDate) || m.getDate().isBefore(endDate));
        return resultCompute(predicate);
    }

    @Override
    public List<Meal> getFilteredDateAfterList(LocalDate startDate, int userId) {
        Predicate<Meal> predicate = m -> ((m.getUserId() == userId) && (m.getDate().equals(startDate) || m.getDate().isAfter(startDate)));
        return resultCompute(predicate);
    }

    @Override
    public List<Meal> getFilteredDateBeforeList(LocalDate endDate, int userId) {
        Predicate<Meal> predicate = m -> ((m.getUserId() == userId) && (m.getDate().equals(endDate) || m.getDate().isBefore(endDate)));
        return resultCompute(predicate);
    }

    private List<Meal> resultCompute(Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        Predicate<Meal> predicate = meal -> userId == meal.getUserId();
        return resultCompute(predicate);
    }
}

