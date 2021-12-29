package resistance.star.wars.socialnetwork.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TradeDTO {
  @NotNull(message = "{trade.one.mandatory}")
  @Valid
  TradeRebelDTO traderOne;
  @NotNull(message = "{trade.two.mandatory}")
  @Valid
  TradeRebelDTO traderTwo;
}
