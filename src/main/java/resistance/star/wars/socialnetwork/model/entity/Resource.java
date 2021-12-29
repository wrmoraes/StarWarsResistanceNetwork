package resistance.star.wars.socialnetwork.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnTransformer;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;
import resistance.star.wars.socialnetwork.validator.EnumValidator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Resource implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @JsonIgnore
  private Long id;

  @Min(message = "{resource.quantity.minimum}", value = 1L)
  private int quantity;
  @NotNull(message = "{resource.type.mandatory}")
  @EnumValidator(enumClazz = ResourceTypeEnum.class, message = "{resource.type.invalid}")
  @ColumnTransformer(read = "UPPER(type)")
  private String type;
}
