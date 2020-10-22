package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_1 = START_SEQ + 2;
    public static final int MEAL_ID_2 = START_SEQ + 3;
    public static final int MEAL_ID_3 = START_SEQ + 4;
    public static final int MEAL_ID_4 = START_SEQ + 5;
    public static final int MEAL_ID_5 = START_SEQ + 6;
    public static final int MEAL_ID_6 = START_SEQ + 7;
    public static final int MEAL_ID_7 = START_SEQ + 8;
    public static final int MEAL_ID_8 = START_SEQ + 9;
    public static final int MEAL_ID_9 = START_SEQ + 10;

    public static final Meal meal_1 = new Meal (MEAL_ID_1, LocalDateTime.of(2020, 1,30,10, 0), "Завтрак", 500);
    public static final Meal meal_2 = new Meal (MEAL_ID_2, LocalDateTime.of(2020,01,30,9,00), "Zavtrak", 511);
    public static final Meal meal_3 = new Meal (MEAL_ID_3, LocalDateTime.of(2020,01,30,13,00), "Обед", 1000);
    public static final Meal meal_4 = new Meal (MEAL_ID_4, LocalDateTime.of(2020,01,30,20,00), "Ужин", 500);
    public static final Meal meal_5 = new Meal (MEAL_ID_5, LocalDateTime.of(2020,01,31,0,00), "Еда на граничное значение", 100);
    public static final Meal meal_6 = new Meal (MEAL_ID_6, LocalDateTime.of(2020,01,31,10,00), "Завтрак", 1000);
    public static final Meal meal_7 = new Meal (MEAL_ID_7, LocalDateTime.of(2020,01,31,13,00), "Обед", 500);
    public static final Meal meal_8 = new Meal (MEAL_ID_8, LocalDateTime.of(2020,01,31,20,00), "Ужин", 410);
    public static final Meal meal_9 = new Meal (MEAL_ID_9, LocalDateTime.of(2020,01,30,13,10), "Obed", 1456);

    public static Meal getNew(){
        return new Meal(LocalDateTime.of(2020, 01, 28, 10,00),"New", 300);
    }

    public static Meal getNewIdenticalDate(){
        return new Meal(LocalDateTime.of(2020, 01, 30, 13,00),"New", 300);
    }

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
        assertThat(actual).usingElementComparatorIgnoringFields("dateTime").isEqualTo(expected);
    }
}
