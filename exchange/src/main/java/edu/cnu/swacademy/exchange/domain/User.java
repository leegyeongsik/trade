package edu.cnu.swacademy.exchange.domain;

import edu.cnu.swacademy.exchange.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor
@SQLDelete(sql = "Update user set deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

}
