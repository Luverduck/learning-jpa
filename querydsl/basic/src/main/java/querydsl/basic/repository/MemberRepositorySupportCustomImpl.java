package querydsl.basic.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.entity.Member;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static querydsl.basic.entity.QMember.member;
import static querydsl.basic.entity.QTeam.team;

public class MemberRepositorySupportCustomImpl extends QuerydslRepositorySupport implements MemberRepositorySupportCustom {

    // 생성자
    public MemberRepositorySupportCustomImpl() {
        super(Member.class); // QuerydslRepositorySupport 생성자 호출
    }

    // 엔티티 조회
    @Override
    public List<Member> searchWithSupport(MemberSearchCondition condition) {
        // 조건 조회
        return from(member)
                    .leftJoin(member.team, team)
                    .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeMin()),
                        ageLoe(condition.getAgeMax())
                    )
                    .fetch();
    }

    // 엔티티 조회 - 페이징
    @Override
    public Page<Member> searchPageWithSupport(MemberSearchCondition condition, Pageable pageable) {
        // 조건 조회 쿼리
        JPQLQuery<Member> contentQuery = from(member)
                                    .leftJoin(member.team, team)
                                    .where(
                                        usernameEq(condition.getUsername()),
                                        teamNameEq(condition.getTeamName()),
                                        ageGoe(condition.getAgeMin()),
                                        ageLoe(condition.getAgeMax())
                                    );
        // 페이징 및 정렬 적용
        List<Member> content = getQuerydsl().applyPagination(pageable, contentQuery).fetch();
        // 조건 조회 결과 수 조회
        JPQLQuery<Long> countQuery = from(member)
                                    .leftJoin(member.team, team)
                                    .where(
                                            usernameEq(condition.getUsername()),
                                            teamNameEq(condition.getTeamName()),
                                            ageGoe(condition.getAgeMin()),
                                            ageLoe(condition.getAgeMax())
                                    )
                                    .select(member.count());
        // 조회 결과 반환
        return PageableExecutionUtils
                                    .getPage(
                                        content,
                                        pageable,
                                        () -> Optional.ofNullable(countQuery.fetchOne()).orElse(0L)
                                    );
    }

    // 사용자 이름 조건
    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    // 팀 이름 조건
    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null;
    }

    // 사용자 나이 하한 조건
    private BooleanExpression ageGoe(Integer ageMin) {
        return ageMin != null ? member.age.goe(ageMin) : null;
    }

    // 사용자 나이 상한 조건
    private BooleanExpression ageLoe(Integer ageMax) {
        return ageMax != null ? member.age.loe(ageMax) : null;
    }

}
