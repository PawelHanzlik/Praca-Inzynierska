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
@Table(name = "Users")
public class UserEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    private Long preferableZone;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private Integer age;

}
