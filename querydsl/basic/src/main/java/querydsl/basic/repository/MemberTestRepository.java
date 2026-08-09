package querydsl.basic.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.entity.Member;
import querydsl.basic.repository.support.QuerydslRepositorySupportCustom;

import java.util.List;

import static querydsl.basic.entity.QMember.member;
import static querydsl.basic.entity.QTeam.team;

@Repository
public class MemberTestRepository extends QuerydslRepositorySupportCustom {

    // 생성자
    protected MemberTestRepository() {
        super(Member.class); // QuerydslRepositorySupportCustom 생성자 호출
    }

    // 엔티티 조회 - select()
    public List<Member> basicSelect() {
        return select(member)
                    .from(member)
                    .fetch();
    }

    // 엔티티 조회 - selectFrom()
    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                    .fetch();
    }

    // 엔티티 조회 - 페이징 (content 쿼리와 count 쿼리 직접 작성)
    public Page<Member> searchPage(MemberSearchCondition condition, Pageable pageable) {
        return applyPagination(
                    pageable,
                    queryFactory -> queryFactory
                                        .selectFrom(member)
                                        .leftJoin(member.team, team)
                                        .where(
                                                usernameEq(condition.getUsername()),
                                                teamNameEq(condition.getTeamName()),
                                                ageGoe(condition.getAgeMin()),
                                                ageLoe(condition.getAgeMax())
                                        ),
                    queryFactory -> queryFactory
                                        .select(member.count())
                                        .from(member)
                                        .leftJoin(member.team, team)
                                        .where(
                                                usernameEq(condition.getUsername()),
                                                teamNameEq(condition.getTeamName()),
                                                ageGoe(condition.getAgeMin()),
                                                ageLoe(condition.getAgeMax())
                                        )
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
