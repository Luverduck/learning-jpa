package hello.jpa.entity;

import hello.jpa.entity.constant.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 5)
// @TableGenerator(name = "MEMBER_SEQ_GENERATOR_TABLE", pkColumnName = "seq_name", valueColumnName = "seq_next_val", pkColumnValue = "MEMBER_SEQ")
public class Member {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    // @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR_TABLE")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // @Temporal은 JPA 3.2+ 기준 deprecated
    private LocalDate createdDate;
    private LocalTime createdTime;
    private LocalDateTime createdDateTime;

    @Lob
    private String description;

    @Transient
    private int temp;

    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
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

}
