package springdatajpa.basic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import springdatajpa.basic.dto.MemberDto;
import springdatajpa.basic.entity.Member;
import springdatajpa.basic.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    public void basicCRUD() {
        // 저장 검증
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 단일 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);
        // 전체 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all).hasSize(2);
        Long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2L);
        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long countAfter = memberRepository.count();
        Assertions.assertThat(countAfter).isEqualTo(0L);
    }

    @Test
    public void findByUsernameAndAgeGraterThan() {
        // 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 쿼리 메소드 검증
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void testNamedQuery() {
        // 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 쿼리 메소드 검증
        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() {
        // 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // @Query 쿼리 메소드 검증
        List<Member> result = memberRepository.findUser("AAA", 10);
        Assertions.assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() {
        // 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // @Query 쿼리 메소드 검증
        List<String> result = memberRepository.findUsernameList();
        for (String username : result) {
            System.out.println("username = " + username);
        }
    }

    @Test
    public void findMemberDto() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        memberRepository.save(member1);
        // Team 엔티티 저장
        Team teamA = new Team("teamA");
        member1.setTeam(teamA);
        teamRepository.save(teamA);
        // @Query 쿼리 메소드 검증
        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        memberRepository.save(member1);
        // Team 엔티티 저장
        Team teamA = new Team("teamA");
        member1.setTeam(teamA);
        teamRepository.save(teamA);
        // @Query 쿼리 메소드 검증
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnMember() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        Member result = memberRepository.findMemberByUsername("AAA");
        System.out.println("result = " + result);
    }

    @Test
    public void returnMemberNoResult() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        // > 조회 결과가 없을 경우 null 반환
        Member result = memberRepository.findMemberByUsername("CCC");
        System.out.println("result = " + result);
    }

    @Test
    public void returnMemberInvalidResult() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        // > 조회 결과가 둘 이상일 때 IncorrectResultSizeDataAccessException 발생
        Member result = memberRepository.findMemberByUsername("AAA");
        System.out.println("result = " + result);
    }

    @Test
    public void returnCollection() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        List<Member> result = memberRepository.findListByUsername("AAA");
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnCollectionNoResult() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        // > 조회 결과가 없을 경우 빈 컬렉션 반환
        List<Member> result = memberRepository.findListByUsername("CCC");
        System.out.println("result = " + result);
        System.out.println("result.size() = " + result.size());
    }

    @Test
    public void returnOptional() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        Optional<Member> result = memberRepository.findOptionalByUsername("AAA");
        System.out.println("result = " + result);
    }

    @Test
    public void returnOptionalNoResult() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        Optional<Member> result = memberRepository.findOptionalByUsername("CCC");
        // > 조회 결과가 없을 경우 Optional.empty 반환
        System.out.println("result = " + result);
    }

    @Test
    public void returnOptionalInvalidResult() {
        // Member 엔티티 저장
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 반환형 검증
        // > 조회 결과가 둘 이상일 경우 IncorrectResultSizeDataAccessException 발생
        Optional<Member> result = memberRepository.findOptionalByUsername("AAA");
        System.out.println("result = " + result);
    }

    @Test
    public void testSlicing() {
        // 엔티티 저장
        for (int i = 1; i <= 5; ++i) {
            memberRepository.save(new Member("member" + i, 10));
        }
        // 조건절 값
        int age = 10;
        // 페이징과 정렬을 위한 값
        // 페이지 번호(offset), 개수(limit), 정렬
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // 쿼리 메소드 검증
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);
        List<Member> content = slice.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        // 현재 페이지 번호
        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        // 조건을 만족하는 데이터 중 현재 페이지에 조회된 데이터 개수
        Assertions.assertThat(content.size()).isEqualTo(3);
        // 현재 페이지가 첫 번째 페이지인지 여부 (0번 페이지인지 여부)
        Assertions.assertThat(slice.isFirst()).isTrue();
        // 다음 페이지가 존재하는지 여부
        Assertions.assertThat(slice.hasNext()).isTrue();
    }

    @Test
    public void testPaging() {
        // 엔티티 저장
        for (int i = 1; i <= 5; ++i) {
            memberRepository.save(new Member("member" + i, 10));
        }
        // 조건절 값
        int age = 10;
        // 페이징과 정렬을 위한 값
        // 페이지 번호(offset), 개수(limit), 정렬
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // 쿼리 메소드 검증
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);
        // 조건을 만족하는 데이터 총 개수
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        // 전체 페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        // 현재 페이지 번호
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        // 조건을 만족하는 데이터 중 현재 페이지에 조회된 데이터 개수
        Assertions.assertThat(content.size()).isEqualTo(3);
        // 현재 페이지가 첫 번째 페이지인지 여부 (0번 페이지인지 여부)
        Assertions.assertThat(page.isFirst()).isTrue();
        // 다음 페이지가 존재하는지 여부
        Assertions.assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void testPagingCount() {
        // 엔티티 저장
        for (int i = 1; i <= 5; ++i) {
            memberRepository.save(new Member("member" + i, 10));
        }
        // 조건절 값
        int age = 10;
        // 페이징과 정렬을 위한 값
        // 페이지 번호(offset), 개수(limit), 정렬
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // 쿼리 메소드 검증
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
    }

    @Test
    public void bulkUpdate() {
        // 엔티티 저장
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
        // 벌크 연산 검증
        Integer resultCount = memberRepository.bulkAgePlus(20);
        Assertions.assertThat(resultCount).isEqualTo(3);
        // 영속성 컨텍스트 비우기 전
        List<Member> membersBefore = memberRepository.findByUsername("member5");
        Member member5 = membersBefore.get(0);
        System.out.println("member5 = " + member5);
        // 영속성 컨텍스트 비우기
        /*
        entityManager.flush();
        entityManager.clear();
        */
        // 영속성 컨텍스트 비우기 후
        List<Member> membersAfter = memberRepository.findByUsername("member5");
        member5 = membersAfter.get(0);
        System.out.println("member5 = " + member5);
    }

    @Test
    public void findMemberLazy() {
        // 엔티티 저장
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 영속성 컨텍스트 비우기
        entityManager.flush();
        entityManager.clear();
        // 엔티티 조회
        List<Member> members = memberRepository.findAllById(Arrays.asList(member1.getId(), member2.getId()));
        System.out.println("members = " + members);
        System.out.println("members.size = " + members.size());
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberUsingEntityGraph1() {
        // 엔티티 저장
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 영속성 컨텍스트 비우기
        entityManager.flush();
        entityManager.clear();
        // 엔티티 조회
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberUsingEntityGraph2() {
        // 엔티티 저장
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 영속성 컨텍스트 비우기
        entityManager.flush();
        entityManager.clear();
        // 엔티티 조회
        List<Member> members = memberRepository.findAllUsingEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberUsingEntityGraph3() {
        // 엔티티 저장
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 영속성 컨텍스트 비우기
        entityManager.flush();
        entityManager.clear();
        // 엔티티 조회
        List<Member> members = memberRepository.findAllUsingEntityGraph();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberUsingEntityGraph4() {
        // 엔티티 저장
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // 영속성 컨텍스트 비우기
        entityManager.flush();
        entityManager.clear();
        // 엔티티 조회
        List<Member> members = memberRepository.findAllUsingNamedEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void findReadOnlyByUsername() {
        // 엔티티 저장
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        // 영속성 컨텍스트 비우기
        entityManager.flush();
        entityManager.clear();
        // 엔티티 조회
        // Member findMember = memberRepository.findById(member.getId()).get();
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        // 더티 체크 여부 확인
        findMember.setUsername("member2");
        entityManager.flush();
    }

}