package site.magazine.wisenews.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.magazine.wisenews.Models.Magazine;
import site.magazine.wisenews.Models.User;
import site.magazine.wisenews.Repos.MagazineRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MagazineRepo magazineRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/main")
    public String main(@RequestParam(name="name", required = false, defaultValue = "User") String name,
                       @RequestParam(required = false, defaultValue = "") String filter,
                       Model model) {

        List<Magazine> magazines = magazineRepo.findByTags(filter);
        if (magazines.isEmpty())
            magazines = magazineRepo.findAll();

        model.addAttribute("magazines", magazines);
        model.addAttribute("filter", filter);
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
                                      @RequestParam("file") MultipartFile file,
                                      Model model) throws IOException {
        Magazine magazine = new Magazine(name, contains, link, tag, user);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            magazine.setFilename(resultFilename);
        }
        magazineRepo.save(magazine);
        Iterable<Magazine> magazines = magazineRepo.findAll();
        model.addAttribute("magazines", magazines);
        return "main";
    }



}
