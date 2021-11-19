package be.vdab.startrek.controllers;

import be.vdab.startrek.services.WerknemerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class WerknemerController {

    private final WerknemerService werknemerService;

    public WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }


    @GetMapping
    public ModelAndView toonWerknemers() {
        return new ModelAndView("werknemers", "werknemers", werknemerService.findAll());
    }

    @GetMapping("/werknemers/{id}")
    public ModelAndView toonWerknemer(@PathVariable long id) {
        var mv = new ModelAndView("werknemer");
        werknemerService.findById(id).ifPresent(mv::addObject);
        return mv;
    }
}
