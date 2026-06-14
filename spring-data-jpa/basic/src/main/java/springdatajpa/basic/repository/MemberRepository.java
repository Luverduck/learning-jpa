package springdatajpa.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springdatajpa.basic.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리 메소드
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

}