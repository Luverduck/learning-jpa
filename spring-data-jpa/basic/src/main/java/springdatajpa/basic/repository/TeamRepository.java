package springdatajpa.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springdatajpa.basic.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}