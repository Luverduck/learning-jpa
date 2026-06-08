package springdatajpa.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springdatajpa.basic.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}