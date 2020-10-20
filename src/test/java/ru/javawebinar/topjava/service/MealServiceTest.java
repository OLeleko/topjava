package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID_1, USER_ID);
        assertMatch(meal, meal_1);
    }

    @Test
    public void getWrongId() throws Exception{
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWrongUser() throws Exception{
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID_1, ADMIN_ID));
    }

    @Test
    public void delete() throws Exception{
        mealService.delete(MEAL_ID_1, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID_1, USER_ID));
    }

    @Test
    public void deleteWrongId() throws Exception{
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meal = mealService.getBetweenInclusive(LocalDate.of(2020,01,30), LocalDate.of(2020, 01, 30), USER_ID);
        assertMatch(meal, meal_4, meal_3, meal_1);
    }

    @Test
    public void getAll() {
        List<Meal> mealList = mealService.getAll(ADMIN_ID);
        assertMatch(mealList, meal_9, meal_2);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        Integer updatedId = updated.getId();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(updatedId, USER_ID), updated);
    }

    @Test
    public void updateWrongUser() throws Exception{
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> mealService.update(updated, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = mealService.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }
}