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
@RequestMapping("werknemers")
public class WerknemerController {

    private final BestellingService bestellingService;
    private final WerknemerService werknemerService;

    public WerknemerController(BestellingService bestellingService, WerknemerService werknemerService) {
        this.bestellingService = bestellingService;
        this.werknemerService = werknemerService;
    }

    @GetMapping("{id}")
    public ModelAndView toonWerknemer(@PathVariable long id) {
        var mv = new ModelAndView("werknemer");
        werknemerService.findById(id).ifPresent(mv::addObject);
        return mv;
    }

    @GetMapping("{id}/vorigebestellingen")
    public ModelAndView toonBestellingenVanWerknemer(@PathVariable long id) {
        var mv = new ModelAndView("vorigeBestellingen");
        //geef werknemer mee om te tonen tot wie de bestellingen toebehoren
        werknemerService.findById(id).ifPresent(werknemer -> mv.addObject(werknemer));
        return mv
                .addObject("bestellingen", bestellingService.findBestellingenByWerknemerId(id));
    }

    @GetMapping("{id}/nieuwebestelling")
    public ModelAndView toonForm(@PathVariable long id) {
        var mv = new ModelAndView("nieuweBestelling");
        //(mv::addObject)
        werknemerService.findById(id).ifPresent(werknemer -> mv.addObject(werknemer));
        return mv
                .addObject(new Bestelling(0, id, "", null));
    }

    @PostMapping("bestel")
    public ModelAndView bestel(@Valid Bestelling bestelling, Errors errors, RedirectAttributes redirect) {
        if (errors.hasErrors()) {
            //toon terug de form
            var mv = new ModelAndView("nieuweBestelling");
            werknemerService.findById(bestelling.getWerknemerId()).ifPresent(werknemer -> mv.addObject(werknemer));
            return mv;
//                    .addObject(new Bestelling(0, bestelling.getWerknemerId(), "", null));
        }
        //doe een try catch, in geval van exceptions toon werknemer pagina met foutmelding
        try {
            bestellingService.createBestellingEnWijzigBudget(bestelling);
            redirect.addAttribute("id", bestelling.getWerknemerId());
            return new ModelAndView("redirect:/werknemers/{id}");
        } catch (WerknemerNietGevondenException ex) {
            redirect.addAttribute("werknemerNietGevonden", true);
            return new ModelAndView("redirect:/");
        } catch (OnvoldoendeBudgetException ex) {
            redirect.addAttribute("onvoldoendeBudget", true);
            redirect.addAttribute("id", bestelling.getWerknemerId());
            return new ModelAndView("redirect:/werknemers/{id}");
        }
    }
}
