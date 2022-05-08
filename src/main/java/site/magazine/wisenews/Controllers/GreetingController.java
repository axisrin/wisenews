package site.magazine.wisenews.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.magazine.wisenews.Models.Magazine;
import site.magazine.wisenews.Repos.MagazineRepo;

@Controller
public class GreetingController {

    @Autowired
    private MagazineRepo magazineRepo;

    @GetMapping("/")
    public String main(@RequestParam(name="name", required = false, defaultValue = "User") String name, Model model) {
        Iterable<Magazine> magazines = magazineRepo.findAll();
        model.addAttribute("magazines", magazines);
        return "main";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
