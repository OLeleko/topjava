package ru.javawebinar.topjava.web.meal;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/js/meals")
public class MealUIController extends AbstractMealController{

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value= HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void create(@RequestParam String description,
                       @RequestParam Integer calories){
        super.create(new Meal(null, LocalDateTime.now(), description, calories));
    }
}
