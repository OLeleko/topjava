package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100001;
    public static final int MEAL_ID_1 = 100002;
    public static final int MEAL_ID_2 = 100003;

    public static final Meal meal_1 = new Meal (MEAL_ID_1, LocalDateTime.of(2020,10,19,10,00), "Завтрак", 500);
    public static final Meal meal_2 = new Meal (MEAL_ID_2, LocalDateTime.of(2020,10,19,13,00), "Zavtrac", 1500);

    public static Meal getUpdated(){
        Meal updated = new Meal(meal_1);
        updated.setDescription("UpdatedЗавтрак");
        updated.setCalories(1250);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected){
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected){
        assertThat(actual).usingElementComparatorIgnoringFields("datetime").isEqualTo(expected);
    }
}
