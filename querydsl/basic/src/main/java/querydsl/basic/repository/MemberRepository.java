package querydsl.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import querydsl.basic.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, MemberRepositorySupportCustom, QuerydslPredicateExecutor<Member> {

    List<Member> findByUsername(String username);

}
