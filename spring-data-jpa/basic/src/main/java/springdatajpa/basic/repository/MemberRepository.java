package springdatajpa.basic.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import springdatajpa.basic.dto.MemberDto;
import springdatajpa.basic.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    // 쿼리 메소드
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 쿼리 메소드 (네임드 쿼리)
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 쿼리 메소드 (@Query)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 쿼리 메소드 (@Query - 값 조회)
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 쿼리 메소드 (@Query - DTO 조회)
    @Query("select new springdatajpa.basic.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 쿼리 메소드 (파라미터 바인딩 - 컬렉션)
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 쿼리 메소드 (반환형 - 단일)
    Member findMemberByUsername(String username);

    // 쿼리 메소드 (반환형 - 컬렉션)
    List<Member> findListByUsername(String username);

    // 쿼리 메소드 (반환형 - Optional)
    Optional<Member> findOptionalByUsername(String username);

    // 쿼리 메소드 (페이징과 정렬 - Slice)
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    // 쿼리 메소드 (페이징과 정렬 - Page)
    Page<Member> findPageByAge(int age, Pageable pageable);

    // 쿼리 메소드 (페이징과 정렬 - count 쿼리 분리)
    @Query(
        value = "select m from Member m left join fetch m.team t",
        countQuery = "select count(m) from Member m"
    )
    Page<Member> findByAge(int age, Pageable pageable);

    // 쿼리 메소드 (벌크 연산)
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    Integer bulkAgePlus(@Param("age") int age);

    // 쿼리 메소드 (페치 조인)
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();

    // 쿼리 메소드 (@EntityGraph - JpaRepository 쿼리 메소드)
    @EntityGraph(attributePaths = {"team"})
    @Override
    List<Member> findAll();

    // 쿼리 메소드 (@EntityGraph - 이름 기반 쿼리 메소드)
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAllUsingEntityGraphByUsername(@Param("username") String username);

    // 쿼리 메소드 (@EntityGraph - @Query 쿼리 메소드)
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findAllUsingEntityGraph();

    // 쿼리 메소드 (@EntityGraph - @NamedEntityGraph 참조)
    @EntityGraph("Member.all")
    List<Member> findAllUsingNamedEntityGraphByUsername(@Param("username") String username);

    // 쿼리 메소드 (@QueryHint)
    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true"),})
    Member findReadOnlyByUsername(String username);

    // 쿼리 메소드 (@Lock)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}