package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
/*@RequestMapping("/meals")*/
public class JspMealController {
    @Autowired
    MealService mealService;

   @GetMapping("/meals")
    public String getAll(Model model) {
       int userId = SecurityUtil.authUserId();
       List<Meal> mealList = new ArrayList<>();
       mealList = mealService.getAll(userId);
       model.addAttribute("meals", MealsUtil.getTos(mealList, MealsUtil.DEFAULT_CALORIES_PER_DAY));
       return "meals";
   }

   @GetMapping(value = "/meals/delete/{id}")
   public String deleteMeal(@PathVariable String id, Model model){
       int userId = SecurityUtil.authUserId();
       int mealId = Integer.parseInt(id);
       mealService.delete(mealId, userId);
       return "redirect:/meals";
   }

   @GetMapping(value = "/meals/filter")
   public String filterMeal(Model model, HttpServletRequest request){
       int userId = SecurityUtil.authUserId();
       LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
       LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
       List<Meal> mealList = new ArrayList<>();
       mealList = mealService.getBetweenInclusive(startDate, endDate,userId);
       model.addAttribute("meals", MealsUtil.getTos(mealList, MealsUtil.DEFAULT_CALORIES_PER_DAY));
       return "/meals";
   }

    @GetMapping(value = "/meals", params = "action=update")
    public void updateMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = SecurityUtil.authUserId();
        final Meal meal= mealService.get(getId(request), userId);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm").forward(request, response);
    }

    @GetMapping(value = "/meals", params = "action=create")
    public void createMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = SecurityUtil.authUserId();
        final Meal meal= new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm").forward(request, response);
    }

   @GetMapping(value = "/mealForm")
    public String createUpdate(HttpServletRequest request){
        int userId = SecurityUtil.authUserId();
        if(StringUtils.hasText(request.getParameter("id"))){
            Meal meal = mealService.get(Integer.parseInt(request.getParameter("id")), userId);
        }else {
            final Meal meal= new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        }
        return "/mealForm";
    }

    @PostMapping("/meals")
    public void createUpdatePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int userId = SecurityUtil.authUserId();

        if(StringUtils.hasText(request.getParameter("id"))){
            Meal meal = new Meal(
                    Integer.parseInt(request.getParameter("id")),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            mealService.update(meal, userId);
        }else {
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            mealService.create(meal, userId);
        }
        response.sendRedirect("meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);

    }
    }



