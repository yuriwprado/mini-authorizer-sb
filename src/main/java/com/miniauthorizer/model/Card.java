package com.miniauthorizer.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MA_CARD")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String number;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Double balance;

}
