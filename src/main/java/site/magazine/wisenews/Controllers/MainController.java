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
import site.magazine.wisenews.Repos.UserRepo;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private MagazineRepo magazineRepo;

    @Autowired
    private UserRepo userRepo;

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

    @GetMapping("/main/addLikeMagazine")
    public String addLikeMagazine(@AuthenticationPrincipal User user,
                                  @RequestParam String magazid,
                                  @RequestParam(required = false, defaultValue = "") String filter,
                                  Model model) {
        if (userRepo.findByUsername(user.getUsername()).getLikedTagsMagazines().contains(magazid)) {
            return "redirect:/main";
        } else {
            User userFromDB = userRepo.findByUsername(user.getUsername());
            Set<String> likedMagazine;
            likedMagazine = userFromDB.getLikedTagsMagazines();
//            System.out.println("User old liked tags" + user.getLikedTagsMagazines());
            userFromDB.getLikedTagsMagazines().add(magazid);
//            System.out.println("User new liked tags" + user.getLikedTagsMagazines());
            userRepo.save(userFromDB);
        }

        List<Magazine> magazines = magazineRepo.findByTags(filter);
        if (magazines.isEmpty())
            magazines = magazineRepo.findAll();

        model.addAttribute("magazines", magazines);
        model.addAttribute("filter", filter);

        return "redirect:/main";
    }

    @GetMapping("/main/addRecommendedMagazine")
    public String addRecommendedMagazine(@AuthenticationPrincipal User user,
                                  @RequestParam String magazid,
                                  Model model) {
        if (userRepo.findByUsername(user.getUsername()).getLikedTagsMagazines().contains(magazid)) {
            return "redirect:/main/recommendedMagazine";
        } else {
            User userFromDB = userRepo.findByUsername(user.getUsername());
            Set<String> likedMagazine;
            likedMagazine = userFromDB.getLikedTagsMagazines();
//            System.out.println("User old liked tags" + user.getLikedTagsMagazines());
            userFromDB.getLikedTagsMagazines().add(magazid);
//            System.out.println("User new liked tags" + user.getLikedTagsMagazines());
            userRepo.save(userFromDB);
        }

        return "redirect:/main/recommendedMagazine";
    }

    @GetMapping("/main/likedMagazine")
    public String addLikeMagazine(@AuthenticationPrincipal User user,
                                  @RequestParam(required = false) String magazid,
                                  Model model) {
        Set<String> idLikedMagazine = userRepo.findByUsername(user.getUsername()).getLikedTagsMagazines();
//        System.out.println(idLikedMagazine);
        Set<Magazine> likedMagazine = new HashSet<Magazine>(Collections.EMPTY_SET);
//        System.out.println("1");
        for (String idMag : idLikedMagazine) {
//            System.out.println("2");
//            System.out.println(idMag);
//            System.out.println(!idMag.equals("0"));
            if (!idMag.equals("0")) {
//                System.out.println(magazineRepo.findById(Long.valueOf(idMag)).get());
                likedMagazine.add(magazineRepo.findById(Long.valueOf(idMag)).get());
            }
        }
//        System.out.println(idLikedMagazine);
        model.addAttribute("magazines", likedMagazine);
        return "likedJournals";
    }

    @GetMapping("/main/recommendedMagazine")
    public String recommendedMagazine(@AuthenticationPrincipal User user,
                                  @RequestParam(required = false) String magazid,
                                  Model model) {
        Set<String> idLikedMagazine = userRepo.findByUsername(user.getUsername()).getLikedTagsMagazines();
//        System.out.println(idLikedMagazine);
        Set<Magazine> likedMagazine = new HashSet<Magazine>(Collections.EMPTY_SET);
        Set<Magazine> recommendedMagazine = new HashSet<Magazine>(Collections.EMPTY_SET);
        Set<String> likedTags = new HashSet<String>(Collections.EMPTY_SET);
        //        System.out.println("1");
        for (String idMag : idLikedMagazine) {
//            System.out.println("2");
//            System.out.println(idMag);
//            System.out.println(!idMag.equals("0"));
            if (!idMag.equals("0")) {
//                System.out.println(magazineRepo.findById(Long.valueOf(idMag)).get());
                likedMagazine.add(magazineRepo.findById(Long.valueOf(idMag)).get());
            }
        }

        if (!likedMagazine.isEmpty()) {
            for (Magazine tMagazine : likedMagazine) {
                List<String> tags = Arrays.asList(tMagazine.getTags().split("\\s*,\\s*"));
                System.out.println(tags);
                for (String newTag : tags) {
                    if (!likedTags.contains(newTag)) {
                        likedTags.add(newTag);
                    }
                }
            }
        }

        if (!likedTags.isEmpty()) {
            for (String nTags : likedTags) {
                List<Magazine> magTags = magazineRepo.findByTags(nTags);
                if (!magTags.isEmpty()){
                    for (Magazine nMag : magTags) {
                        if (!recommendedMagazine.contains(nMag)) {
                            recommendedMagazine.add(nMag);
                        }
                    }
                }
            }
        }

        if (!recommendedMagazine.isEmpty()) {
            for (Magazine nMagazine : likedMagazine) {
                if (recommendedMagazine.contains(nMagazine))
                    recommendedMagazine.remove(nMagazine);
            }
        }

//        System.out.println(idLikedMagazine);
        model.addAttribute("magazines", recommendedMagazine);
        return "recomendedJournals";
    }

    @GetMapping("/main/deleteMagazine")
    public String deleteMagazine(@AuthenticationPrincipal User user,
                                 @RequestParam(required = false) String magazid,
                                 Model model) {
//        System.out.println(userRepo.findByUsername(user.getUsername()).getLikedTagsMagazines());
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userRepo.findByUsername(user.getUsername()).getLikedTagsMagazines().contains(magazid)) {
            Set<String> likedMagazine;
            likedMagazine = userFromDB.getLikedTagsMagazines();
//            System.out.println("Before delete is " + likedMagazine);
            likedMagazine.remove(magazid);
//            System.out.println("After delete is " + likedMagazine);
//            System.out.println("Clear tags from user is " + userFromDB.getLikedTagsMagazines());
            for (String idMag : likedMagazine) {
                userFromDB.getLikedTagsMagazines().add(idMag);
                }
//            System.out.println("New is " + userFromDB.getLikedTagsMagazines());
        }
//        System.out.println(userRepo.findByUsername(userFromDB.getUsername()).getLikedTagsMagazines());
        userRepo.save(userFromDB);

        return "redirect:/main/likedMagazine";
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
