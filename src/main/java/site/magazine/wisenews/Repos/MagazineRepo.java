package site.magazine.wisenews.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import site.magazine.wisenews.Models.Magazine;

import java.util.List;

public interface MagazineRepo extends JpaRepository<Magazine, Long> {
    List<Magazine> findByTags(String tag);

}
