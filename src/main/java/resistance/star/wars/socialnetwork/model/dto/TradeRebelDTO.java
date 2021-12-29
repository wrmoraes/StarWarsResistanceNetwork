package resistance.star.wars.socialnetwork.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TradeRebelDTO {
  @NotNull(message = "{trade.user.mandatory}")
  private Long user;
  @NotEmpty(message = "{trade.resources.mandatory}")
  private List<@Valid ResourceDTO> tradeResources;
  @JsonIgnore
  private int tradeScore;
}
