package engineering.thesis.PSR.Entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Table(name = "UsersHistory")
public class UserHistoryEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    Long userId;

    @NotNull
    private Long preferableZone;

    @NotNull
    private Integer age;
}
