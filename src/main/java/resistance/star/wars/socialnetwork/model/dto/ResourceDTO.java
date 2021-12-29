package resistance.star.wars.socialnetwork.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;
import resistance.star.wars.socialnetwork.validator.EnumValidator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class ResourceDTO {
  @Min(message = "{resource.quantity.minimum}", value = 1L)
  private int quantity;
  @NotNull(message = "{resource.type.mandatory}")
  @EnumValidator(enumClazz = ResourceTypeEnum.class, message = "{resource.type.invalid}")
  private String type;
}
