package resistance.star.wars.socialnetwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resistance.star.wars.socialnetwork.exception.TradeException;
import resistance.star.wars.socialnetwork.model.dto.TradeDTO;
import resistance.star.wars.socialnetwork.service.TradeService;

import javax.validation.Valid;

@RestController
@RequestMapping("trade")
@AllArgsConstructor
public class TradeController {

  private final TradeService service;

  @PostMapping
  public void trade(@RequestBody @Valid TradeDTO tradeDTO) throws TradeException {
    service.tradeItems(tradeDTO);
  }
}
