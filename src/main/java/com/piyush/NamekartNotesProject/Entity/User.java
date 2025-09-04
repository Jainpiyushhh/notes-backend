package com.piyush.NamekartNotesProject.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private  String username;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private  String password;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles",joinColumns =@JoinColumn(name = "user_id") )
    @Column(name = "role")
    private Set<String > roles =new HashSet<>();

}
