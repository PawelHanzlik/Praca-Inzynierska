package engineering.thesis.PSR.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Table(name = "Trip")
public class TripEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @NotNull
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private UserEntity userId;

    @NotNull
    private Instant time;

    @NotNull
    private Integer xCordOfZone;

    @NotNull
    private Integer yCordOfZone;

    @NotNull
    @ElementCollection
    private List<String> userChoices;

}
