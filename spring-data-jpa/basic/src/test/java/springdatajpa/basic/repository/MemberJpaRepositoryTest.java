package springdatajpa.basic.repository;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springdatajpa.basic.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());
        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    public void basicCRUD() {
        // 저장 검증
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        // 단일 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);
        // 전체 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all).hasSize(2);
        Long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(2L);
        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long countAfter = memberJpaRepository.count();
        Assertions.assertThat(countAfter).isEqualTo(0L);
    }

    @Test
    public void findByUsernameAndAgeGraterThan() {
        // 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        // 쿼리 메소드 검증
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void testPaging() {
        // 엔티티 저장
        for (int i = 1; i <= 5; ++i) {
            memberJpaRepository.save(new Member("member" + i, 10));
        }
        // 페이징을 위한 값
        int age = 10;
        int offset = 0;
        int limit = 3;
        // 쿼리 메소드 검증
        List<Member> members = memberJpaRepository.findByAge(age, offset, limit);
        Long totalCount = memberJpaRepository.totalCount(age);
        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5L);
    }

}