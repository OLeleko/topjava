package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id = {}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    // startTime value is LocalTime.MIN or value from view. endTime as well.
    public List<MealTo> getFilteredDateBeforeList(LocalDate endDate, LocalTime startTime, LocalTime endTime){
        log.info("getFilteredDateBeforeList", endDate, startTime, endTime);
        return MealsUtil.getFilteredTos(service.getFilteredDateBeforeList(endDate, SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public List<MealTo> getFilteredDateAfterList(LocalDate startDate, LocalTime startTime, LocalTime endTime){
        log.info("getFilteredDateAfterList", startDate, startTime, endTime);
        return MealsUtil.getFilteredTos(service.getFilteredDateAfterList(startDate, SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public List<MealTo> getFilteredDateBetweenList(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime){
        log.info("getFilteredDateBetweenList", startDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredTos(service.getFilteredDateBetweenList(startDate, endDate, SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}