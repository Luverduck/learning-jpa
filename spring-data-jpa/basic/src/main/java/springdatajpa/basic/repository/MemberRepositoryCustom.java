package springdatajpa.basic.repository;

import springdatajpa.basic.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    // 쿼리 메소드 (사용자 정의 리포지토리)
    List<Member> findMemberCustom();

}