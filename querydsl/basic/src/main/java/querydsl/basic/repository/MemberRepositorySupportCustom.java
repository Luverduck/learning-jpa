package querydsl.basic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import querydsl.basic.dto.MemberSearchCondition;
import querydsl.basic.entity.Member;

import java.util.List;

public interface MemberRepositorySupportCustom {

    List<Member> searchWithSupport(MemberSearchCondition condition);

    Page<Member> searchPageWithSupport(MemberSearchCondition condition, Pageable pageable);

}
