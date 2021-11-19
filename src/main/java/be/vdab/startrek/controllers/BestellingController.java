package be.vdab.startrek.controllers;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.exceptions.OnvoldoendeBudgetException;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import be.vdab.startrek.services.BestellingService;
import be.vdab.startrek.services.WerknemerService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("bestellingen")
public class BestellingController {

    private final BestellingService bestellingService;
    private final WerknemerService werknemerService;

    public BestellingController(BestellingService bestellingService, WerknemerService werknemerService) {
        this.bestellingService = bestellingService;
        this.werknemerService = werknemerService;
    }

    @GetMapping("{id}")
    public ModelAndView toonBestellingenVanWerknemer(@PathVariable long id) {
        return new ModelAndView("bestellingen", "bestellingen", bestellingService.findBestellingenByWerknemerId(id));
    }

    @GetMapping("{id}/nieuwebestelling")
    public ModelAndView toonForm(@PathVariable long id) {
        var mv = new ModelAndView("nieuweBestelling");
        //(mv::addObject)
        werknemerService.findById(id).ifPresent(werknemer -> mv.addObject(werknemer));
        return mv
                .addObject(new Bestelling(0, id, "", null));
    }

    @PostMapping
    public ModelAndView toevoegen(@Valid Bestelling bestelling, Errors errors, RedirectAttributes redirect) {
        if (errors.hasErrors()) {
            //toon terug de form
            var mv = new ModelAndView("nieuweBestelling");
            werknemerService.findById(bestelling.getWerknemerId()).ifPresent(werknemer -> mv.addObject(werknemer));
            return mv
                    .addObject(new Bestelling(0, bestelling.getWerknemerId(), "", null));
        }
        //doe een try catch, in geval van exceptions toon werknemer pagina met foutmelding
        try {
            bestellingService.createBestellingEnWijzigBudget(bestelling);
            redirect.addAttribute("id", bestelling.getWerknemerId());
            return new ModelAndView("redirect:/bestellingen/{id}");
        } catch (WerknemerNietGevondenException ex) {
            redirect.addAttribute("werknemerNietGevondenException", true);
            return new ModelAndView("redirect:/");
        } catch (OnvoldoendeBudgetException ex) {
            redirect.addAttribute("onvoldoendeBudgetException", true);
            redirect.addAttribute("id", bestelling.getWerknemerId());
            return new ModelAndView("redirect:/werknemers/{id}");
        }
    }
}
