package springdatajpa.basic.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import springdatajpa.basic.entity.Member;
import springdatajpa.basic.entity.Team;

public class MemberSpecification {

    // 팀 이름 조건을 만드는 Specification 반환
    public static Specification<Member> teamName(final String teamName) {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                // team이 비어있을 경우 null 반환
                if (!StringUtils.hasText(teamName)) {
                    return null;
                }
                // Member와 Team 조인
                Join<Member, Team> t = root.join("team", JoinType.INNER);
                // 조인한 Team의 name이 입력받은 teamName과 같은지 비교
                return builder.equal(t.get("name"), teamName);
            }
        };
    }

    // 사용자 이름 조건을 만드는 Specification
    public static Specification<Member> username(final String username) {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                // Member의 username이 입력받은 username과 비교
                return builder.equal(root.get("username"), username);
            }
        };
    }

}