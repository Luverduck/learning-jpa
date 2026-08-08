package querydsl.basic.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.dto.MemberTeamDto;
import querydsl.basic.dto.QMemberTeamDto;

import java.util.List;

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
        return ageMax != null ? member.age.goe(ageMax) : null;
    }

}
