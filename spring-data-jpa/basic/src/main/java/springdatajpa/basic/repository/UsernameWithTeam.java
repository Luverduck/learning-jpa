package springdatajpa.basic.repository;

import springdatajpa.basic.entity.Team;

public interface UsernameWithTeam {

    String getUsername();
    Team getTeam();

    interface TeamInfo {
        String getName();
    }

}
