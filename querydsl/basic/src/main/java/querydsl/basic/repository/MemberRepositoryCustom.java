package querydsl.basic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    // 동적 쿼리
    List<MemberTeamDto> search(MemberSearchCondition condition);

    // 페이징
    Page<MemberTeamDto> searchPage(MemberSearchCondition condition, Pageable pageable);

}
