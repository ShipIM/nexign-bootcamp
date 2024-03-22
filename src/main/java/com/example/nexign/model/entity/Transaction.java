package com.example.nexign.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "call_start")
    private Long start;

    @Column(name = "call_end")
    private Long end;

    private Short type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Customer customer;

}
