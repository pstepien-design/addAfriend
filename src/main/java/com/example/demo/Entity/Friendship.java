package com.example.demo.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friendships", schema = "addafriend", uniqueConstraints={
        @UniqueConstraint(columnNames = {"sourceEmail", "destinationEmail"})
})
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String sourceEmail;

    @Column(nullable = false)
    private String destinationEmail;

    @Column(nullable = false)
    private String destinationHost;

    @Column(nullable = false)
    private String sourceHost;

    @Column()
    private String status = "pending";

    @Column()
    private LocalDate dateEstablished;

    public Friendship(String sourceEmail, String destinationEmail, LocalDate dateEstablished, String destinationHost, String sourceHost) {
        this.sourceEmail = sourceEmail;
        this.destinationEmail = destinationEmail;
        this.dateEstablished = dateEstablished;
        this.destinationHost = destinationHost;
        this.sourceHost = sourceHost;
    }
    public Friendship(String sourceEmail, String destinationEmail, LocalDate dateEstablished) {
        this.sourceEmail = sourceEmail;
        this.destinationEmail = destinationEmail;
        this.dateEstablished = dateEstablished;
    }

    public Friendship(String sourceEmail, String destinationEmail, String destinationHost, LocalDate dateEstablished) {
        this.sourceEmail = sourceEmail;
        this.destinationEmail = destinationEmail;
        this.destinationHost = destinationHost;
        this.dateEstablished = dateEstablished;
    }

    public Friendship(String sourceEmail, String destinationEmail, LocalDate dateEstablished, String destinationHost, String status, String sourceHost) {
        this.sourceEmail = sourceEmail;
        this.destinationEmail = destinationEmail;
        this.dateEstablished = dateEstablished;
        this.destinationHost = destinationHost;
        this.status = status;
        this.sourceHost = sourceHost;
    }

}
