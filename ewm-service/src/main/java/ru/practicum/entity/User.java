package ru.practicum.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@EqualsAndHashCode(exclude = "subscribers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscriptions",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")})
    private Set<User> subscriptions = new HashSet<>();

    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers = new HashSet<>();


    public void removeSubscription(User user) {
        this.getSubscriptions().remove(user);
        user.getSubscribers().remove(this);
    }

}
