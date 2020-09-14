package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        ArrayList<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();

        for(UserMeal um : meals){
            LocalDate ld = um.getDateTime().toLocalDate();
            /*if(map.containsKey(ld)){
                map.put(ld, (map.get(ld) + um.getCalories()));
            }else{
                map.put(ld, um.getCalories());
            }*/

            map.merge(ld, um.getCalories(), (a, b) -> a + b);
        }

        for(UserMeal m : meals){
            LocalDate localDate = m.getDateTime().toLocalDate();
            LocalTime lt = m.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(lt, startTime, endTime)){
                UserMealWithExcess userMealWithExcess = null;
                int sumCalories = map.get(localDate);
                if(sumCalories > caloriesPerDay){
                    userMealWithExcess = new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), true);
                }else{
                    userMealWithExcess = new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), false);
                }
                result.add(userMealWithExcess);
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        ArrayList<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();

        meals.stream()
                .forEach(c -> {
                    if(map.containsKey(c.getDateTime().toLocalDate())){
                        map.put(c.getDateTime().toLocalDate(), (map.get(c.getDateTime().toLocalDate()) + c.getCalories()));
                }else {
                        map.put(c.getDateTime().toLocalDate(), c.getCalories());
                    }
        }
                );

        meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .forEach(t -> {
                        if (map.get(t.getDateTime().toLocalDate()) > caloriesPerDay){
                            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(t.getDateTime(), t.getDescription(), t.getCalories(), true);
                            result.add(userMealWithExcess);
                        }else{
                            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(t.getDateTime(), t.getDescription(), t.getCalories(), false);
                            result.add(userMealWithExcess);
                        }
                        }
                );
        return result;
    }
}
