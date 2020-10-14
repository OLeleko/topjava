package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int userId);

    // false if not found
    Boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    List<Meal> getAll(int userId);

    List<Meal> getFilteredDateBeforeList(LocalDate endDate, int userId);

    List<Meal> getFilteredDateAfterList(LocalDate startDate, int userId);

    List<Meal> getFilteredDateBetweenList(LocalDate startDate, LocalDate endDate, int userId);
}
