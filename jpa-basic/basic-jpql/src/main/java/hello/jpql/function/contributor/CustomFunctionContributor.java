package hello.jpql.function.contributor;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        // 함수 레지스트리 반환
        SqmFunctionRegistry registry = functionContributions.getFunctionRegistry();
        // 데이터베이스의 사용자 정의 함수를 기반으로 함수 명세 객체 생성
        StandardSQLFunction groupConcatFunction = new StandardSQLFunction("group_concat", StandardBasicTypes.STRING);
        // 함수 레지스트리에 함수 명세 객체 등록
        registry.register("group_concat", groupConcatFunction);
    }

}