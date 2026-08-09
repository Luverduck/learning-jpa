package querydsl.basic.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.dto.MemberTeamDto;
import querydsl.basic.dto.QMemberTeamDto;

import java.util.List;
import java.util.Optional;

import static querydsl.basic.entity.QMember.member;
import static querydsl.basic.entity.QTeam.team;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    // JPAQueryFactory
    private final JPAQueryFactory queryFactory;

    // 생성자 주입
    public MemberRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        // 조건 조회
        return queryFactory
                    .select(
                        new QMemberTeamDto(
                            member.id.as("memberId"),
                            member.username,
                            member.age,
                            team.id.as("teamId"),
                            team.name.as("teamName")
                        )
                    )
                    .from(member)
                    .leftJoin(member.team, team)
                    .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeMin()),
                        ageLoe(condition.getAgeMax())
                    )
                    .fetch();
    }

    // 페이징
    @Override
    public Page<MemberTeamDto> searchPage(MemberSearchCondition condition, Pageable pageable) {
        // 조건 조회
        List<MemberTeamDto> content = queryFactory
                    .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                    ))
                    .from(member)
                    .leftJoin(member.team, team)
                    .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeMin()),
                        ageLoe(condition.getAgeMax())
                    )
                    .orderBy(member.id.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        // 조건 조회 결과 수 조회
        JPAQuery<Long> countQuery = queryFactory
                    .select(member.count())
                    .from(member)
                    .leftJoin(member.team, team)
                    .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeMin()),
                        ageLoe(condition.getAgeMax())
                    );
        // PageableExecutionUtils을 통해 조회 결과 수 계산 최적화
        // - PageableExecutionUtils.getPage()는 전체 데이터 수를 확실히 계산할 수 있을 때 count 쿼리를 생략
        //   i) 첫 페이지이고 결과 개수가 페이지 크기보다 작을 때
        //   ii) 첫 페이지 이후이고, 결과가 존재하면서 페이지 크기보다 작을 때
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
