package engineering.thesis.PSR.Entities;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.List;

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
    @NotNull
    private Integer xCordOfZone;
    @NotNull
    private Integer yCordOfZone;
    @NotNull
    @ElementCollection
    private List<String> userChoices;
}
