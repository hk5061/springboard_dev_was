package com.home.board.domain.member;

import com.home.board.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Getter
@ToString
@Table(name = "MEMBER") // 테이블명 변경
@SequenceGenerator( // 시퀀스 생성
        name = "MEM_SEQ_GENERATOR",
        sequenceName = "MEM_SEQ",
        initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 PROTECTED 생성
public class Member extends BaseTimeEntity {

    @Id // 기본키 설정
    @Column(name = "MEM_ID") // 컬럼명 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEM_SEQ_GENERATOR") // 자동생성 시퀀스 적용
    private Long id;

    @Column(name = "ID", length = 100, unique = true, nullable = false) // 컬럼명 "ID", VARCHAR2(100), UNIQUE, NOT NULL
    private String loginId;

    @Column(name = "PW", nullable = false) // 컬럼명 "PW", VARCHAR2(255), NOT NULL
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 20, nullable = false)
    private MemberRole role; // ADMIN, MANAGER, USER

    @Builder
    public Member(String loginId, String password, String name, MemberRole role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
