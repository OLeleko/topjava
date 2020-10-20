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
    public void getWrongUser() throws Exception{
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID_1, ADMIN_ID));
    }

    @Test
    public void delete() throws Exception{
        mealService.delete(100002, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(100002, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meal = mealService.getBetweenInclusive(LocalDate.of(2020,10,19), LocalDate.of(2020, 10, 20), USER_ID);
        assertMatch(meal, meal_1);
    }

    @Test
    public void getAll() {
        List<Meal> mealList = mealService.getAll(USER_ID);
        assertMatch(mealList, meal_1);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void updateWrongUser() throws Exception{
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> mealService.update(updated, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = new Meal("Ужин", 200);
        Meal created = mealService.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(mealService.getAll(USER_ID), newMeal, meal_1);
    }
}