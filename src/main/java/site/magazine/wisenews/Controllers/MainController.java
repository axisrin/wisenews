package site.magazine.wisenews.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.magazine.wisenews.Models.Magazine;
import site.magazine.wisenews.Models.User;
import site.magazine.wisenews.Repos.MagazineRepo;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private MagazineRepo magazineRepo;

    @GetMapping("/main")
    public String main(@RequestParam(name="name", required = false, defaultValue = "User") String name,
                       Model model) {
        Iterable<Magazine> magazines = magazineRepo.findAll();
        model.addAttribute("magazines", magazines);
        return "main";
    }

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @PostMapping("/main")
    public String mainAddMagazineCard(@AuthenticationPrincipal User user,
                                      @RequestParam String name,
                                      @RequestParam String contains,
                                      @RequestParam String link,
                                      @RequestParam String tag,
                                      Model model) {
        Magazine magazine = new Magazine(name, contains, link, tag, user);
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
