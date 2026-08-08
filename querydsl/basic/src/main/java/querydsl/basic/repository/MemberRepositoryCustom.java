package querydsl.basic.repository;

import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    // 동적 쿼리
    List<MemberTeamDto> search(MemberSearchCondition condition);

}
