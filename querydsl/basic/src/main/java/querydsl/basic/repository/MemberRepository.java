package querydsl.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import querydsl.basic.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);

}
