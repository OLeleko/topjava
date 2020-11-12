package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Controller
/*@RequestMapping("/meals")*/
public class JspMealController extends AbstractMealController {

    @GetMapping("/meals")
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        List<Meal> mealList = new ArrayList<>();
        mealList = service.getAll(userId);
        model.addAttribute("meals", MealsUtil.getTos(mealList, MealsUtil.DEFAULT_CALORIES_PER_DAY));
        log.info("JspMealController getAll for user {}", userId);
        return "meals";
    }

    @GetMapping(value = "/meals/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
        int userId = SecurityUtil.authUserId();
        int mealId = Integer.parseInt(id);
        service.delete(mealId, userId);
        log.info("JspMealController delete meal {} for user {}", mealId, userId);
        return "redirect:/meals";
    }

    @GetMapping(value = "/meals/filter")
    public String filter(Model model, HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        List<Meal> mealList = new ArrayList<>();
        mealList = service.getBetweenInclusive(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getTos(mealList, MealsUtil.DEFAULT_CALORIES_PER_DAY));
        log.info("JspMealController filter dates ({} - {}) for user {}", startDate, endDate, userId);
        return "/meals";
    }

    @GetMapping(value = "/meals", params = "action=update")
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = SecurityUtil.authUserId();
        int mealId = getId(request);
        final Meal meal = service.get(mealId, userId);
        log.info("JspMealController update meal {} for user {}", mealId, userId);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm").forward(request, response);
    }

    @GetMapping(value = "/meals", params = "action=create")
    public void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = SecurityUtil.authUserId();
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("JspMealController create meal for user {}", userId);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/mealForm").forward(request, response);
    }

    @GetMapping(value = "/mealForm")
    public String createUpdateGet(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasText(request.getParameter("id"))) {
            Meal meal = service.get(Integer.parseInt(request.getParameter("id")), userId);
        } else {
            final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        }
        log.info("JspMealController createUpdateGet meal for user {}", userId);
        return "/mealForm";
    }

    @PostMapping("/meals")
    public void createUpdatePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int userId = SecurityUtil.authUserId();

        if (StringUtils.hasText(request.getParameter("id"))) {
            Meal meal = new Meal(
                    Integer.parseInt(request.getParameter("id")),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            service.update(meal, userId);
        } else {
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            service.create(meal, userId);
        }
        log.info("JspMealController createUpdatePost meal for user {}", userId);
        response.sendRedirect("meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);

    }
}



