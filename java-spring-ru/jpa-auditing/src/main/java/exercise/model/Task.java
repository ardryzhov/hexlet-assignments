package exercise.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

// BEGIN
@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"title", "description"})
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;
    private String description;

    @CreatedDate
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;
}
// END
