package hello.jpql.entity;

import jakarta.persistence.*;

@Entity
@NamedQueries(
    {
        @NamedQuery(
            name = "Member.findByName",
            query = "SELECT m FROM Member m WHERE m.name = :name"
        ),
        @NamedQuery(
            name = "Member.maxAge",
            query = "SELECT max(m.age) FROM Member m"
        ),
    }
)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }

}