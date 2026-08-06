package querydsl.basic.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import querydsl.basic.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void basicJpaTest() {
        // 엔티티 저장
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);
        // 엔티티 단일 조회
        Member result1 = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(result1).isEqualTo(member);
        // 엔티티 전체 조회
        List<Member> result2 = memberJpaRepository.findAll();
        Assertions.assertThat(result2).containsExactly(member);
        // 엔티티 조건 조회
        List<Member> result3 = memberJpaRepository.findByUsername("member1");
        Assertions.assertThat(result3).containsExactly(member);
    }

    @Test
    public void basicQuerydslTest() {
        // 엔티티 저장
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);
        // 엔티티 단일 조회
        Member result1 = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(result1).isEqualTo(member);
        // 엔티티 전체 조회
        List<Member> result2 = memberJpaRepository.findAllQuerydsl();
        Assertions.assertThat(result2).containsExactly(member);
        // 엔티티 조건 조회
        List<Member> result3 = memberJpaRepository.findByUsernameQuerydsl("member1");
        Assertions.assertThat(result3).containsExactly(member);
    }

}