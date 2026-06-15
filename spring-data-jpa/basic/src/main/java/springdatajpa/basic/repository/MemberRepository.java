package springdatajpa.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springdatajpa.basic.dto.MemberDto;
import springdatajpa.basic.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

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

}