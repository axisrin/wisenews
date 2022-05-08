package site.magazine.wisenews.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import site.magazine.wisenews.Models.Magazine;

public interface MagazineRepo extends JpaRepository<Magazine, Long> {



}
