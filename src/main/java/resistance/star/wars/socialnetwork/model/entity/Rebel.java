package resistance.star.wars.socialnetwork.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnTransformer;
import resistance.star.wars.socialnetwork.model.enumeration.GenderEnum;
import resistance.star.wars.socialnetwork.model.enumeration.RebelStatusEnum;
import resistance.star.wars.socialnetwork.validator.EnumValidator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Entity
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Rebel implements Serializable {
  private static final int TRAITOR_MAX_REPORT = 2;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotBlank(message = "{rebel.name.mandatory}")
  @ColumnTransformer(read = "UPPER(name)")
  private String name;
  private RebelStatusEnum status = RebelStatusEnum.CONFEDERATE;
  @Min(message = "{rebel.age.minimum}", value = 1L)
  private int age;
  @NotNull(message = "{rebel.gender.mandatory}")
  @EnumValidator(enumClazz = GenderEnum.class, message = "{rebel.gender.invalid}")
  @ColumnTransformer(read = "UPPER(gender)")
  private String gender;
  @NotNull(message = "{rebel.location.mandatory}")
  @Valid
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "location_id")
  private Location location;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "resource_id")
  private List<@Valid Resource> inventory;
  @JsonIgnore
  private int traitorLevel;

  public void raiseTraitorLevel(){
    this.setTraitorLevel(this.getTraitorLevel() + 1);
    if(this.getTraitorLevel() > TRAITOR_MAX_REPORT){
      this.setStatus(RebelStatusEnum.TRAITOR);
    }
  }

  @PrePersist
  public void preSave() {
    Map<String, Integer> groupResourcesMap = this.getInventory().stream()
        .collect(groupingBy(Resource::getType, summingInt(Resource::getQuantity)));

    List<Resource> groupedResources = groupResourcesMap.entrySet().stream()
        .map(entry-> Resource.builder().quantity(entry.getValue()).type(entry.getKey()).build())
        .collect(Collectors.toList());

    this.setInventory(groupedResources);
  }
}
