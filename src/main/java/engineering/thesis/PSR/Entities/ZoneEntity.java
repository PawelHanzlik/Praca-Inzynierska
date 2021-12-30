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
@Table(name = "Zone")
public class ZoneEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;

    @NotNull
    private String city;

    @NotNull
    private Integer CordX;

    @NotNull
    private Integer CordY;

    @NotNull
    private Double occupiedRatio;

    @NotNull
    private Double attractivenessRatio;


    public long getPriority(){return (Math.round(5*occupiedRatio+attractivenessRatio));}
}
