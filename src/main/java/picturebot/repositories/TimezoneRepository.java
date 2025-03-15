package picturebot.repositories;

import picturebot.entities.timezone.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimezoneRepository extends JpaRepository<Timezone, Long> {
}
