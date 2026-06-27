package springdatajpa.basic.repository;

// 외부 프로젝션 인터페이스
public interface UsernameWithTeam {

    // 외부 프로젝션 메소드
    String getUsername();
    TeamInfo getTeam();

    // 내부 프로젝션 인터페이스
    interface TeamInfo {
        // 내부 프로젝션 메소드
        String getName();
    }

}