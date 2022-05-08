package site.magazine.wisenews.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.magazine.wisenews.Models.Magazine;
import site.magazine.wisenews.Repos.MagazineRepo;

import java.util.List;

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

    @PostMapping("/")
    public String mainAddMagazineCard(@RequestParam String name,
                                      @RequestParam String contains,
                                      @RequestParam String link,
                                      @RequestParam String tag,
                                      Model model) {
        Magazine magazine = new Magazine(name, contains, link, tag);
        magazineRepo.save(magazine);
        Iterable<Magazine> magazines = magazineRepo.findAll();
        model.addAttribute("magazines", magazines);
        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter,
                         Model model) {
        List<Magazine> magazines = magazineRepo.findByTags(filter);
        if (magazines.isEmpty())
            magazines = magazineRepo.findAll();
        model.addAttribute("magazines", magazines);
        return "main";
    }

}
