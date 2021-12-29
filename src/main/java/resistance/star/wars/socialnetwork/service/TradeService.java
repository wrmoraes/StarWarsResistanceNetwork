package resistance.star.wars.socialnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import resistance.star.wars.socialnetwork.exception.RebelNotHaveEnoughResourcesException;
import resistance.star.wars.socialnetwork.exception.TradeException;
import resistance.star.wars.socialnetwork.exception.TradeItemNotFoundOnRebelInventoryException;
import resistance.star.wars.socialnetwork.exception.TraitorTradeException;
import resistance.star.wars.socialnetwork.model.dto.ResourceDTO;
import resistance.star.wars.socialnetwork.model.dto.TradeDTO;
import resistance.star.wars.socialnetwork.model.entity.Rebel;
import resistance.star.wars.socialnetwork.model.entity.Resource;
import resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum;
import resistance.star.wars.socialnetwork.model.enumeration.RebelStatusEnum;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;
import resistance.star.wars.socialnetwork.util.MessageUtils;

import javax.persistence.Transient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Service
@AllArgsConstructor
public class TradeService {

  private final RebelService rebelService;
  private static final int COUNT_ZERO = 0;

  @Transient
  public void tradeItems(TradeDTO trade) throws TradeException {
    try {
      Rebel tradeOneRebel = rebelService.findById(trade.getTraderOne().getUser());
      if(RebelStatusEnum.TRAITOR.equals(tradeOneRebel.getStatus())){
        throw new TraitorTradeException(tradeOneRebel.getName());
      }
      List<Resource> tradeOneResources = groupResourcesAndConvert(trade.getTraderOne().getTradeResources());
      validateTradeResources(tradeOneRebel, tradeOneResources);
      int tradeOneScore = calculateTradeScore(tradeOneResources);

      Rebel tradeTwoRebel = rebelService.findById(trade.getTraderTwo().getUser());
      if(RebelStatusEnum.TRAITOR.equals(tradeTwoRebel.getStatus())){
        throw new TraitorTradeException(tradeTwoRebel.getName());
      }
      List<Resource> tradeTwoResources = groupResourcesAndConvert(trade.getTraderTwo().getTradeResources());
      validateTradeResources(tradeTwoRebel, tradeTwoResources);
      int tradeTwoScore = calculateTradeScore(tradeTwoResources);

      if (tradeOneScore != tradeTwoScore) {
        throw new TradeException(MessageUtils.getMessage(MessageCodeEnum.ERROR_TRADE_SCORE_INVALID, tradeOneScore, tradeTwoScore));
      }

      rebelTradeResources(tradeOneRebel, tradeOneResources, tradeTwoResources);
      rebelTradeResources(tradeTwoRebel, tradeTwoResources, tradeOneResources);

    } catch (Exception exception){
      throw new TradeException(exception.getMessage(), exception);
    }
  }

  private void rebelTradeResources(Rebel rebel, List<Resource> removeResources, List<Resource> addResources){
    removeRebelResources(rebel, removeResources);
    addRebelResources(rebel, addResources);

    rebelService.save(rebel);
  }

  private void removeRebelResources(Rebel rebel, List<Resource> resources){
    for(Resource changeResource: resources){
      rebel.getInventory().stream()
          .filter(rebelResourceFilter -> rebelResourceFilter.getType().equals(changeResource.getType()))
          .findFirst().ifPresent(rebelResource -> {
            int quantityResult = rebelResource.getQuantity() - changeResource.getQuantity();
            if(quantityResult == COUNT_ZERO){
              rebel.getInventory().remove(rebelResource);
            } else {
              rebelResource.setQuantity(quantityResult);
            }
          });
    }
  }

  private void addRebelResources(Rebel rebel, List<Resource> resources){
    rebel.getInventory().addAll(resources);
  }

  private static List<Resource> groupResourcesAndConvert(List<ResourceDTO> resources){
    Map<String, Integer> groupResources = resources.stream()
        .collect(groupingBy(ResourceDTO::getType, summingInt(ResourceDTO::getQuantity)));

    return groupResources.entrySet().stream().map(entry-> Resource.builder().quantity(entry.getValue()).type(entry.getKey()).build())
        .collect(Collectors.toList());
  }

  public int calculateTradeScore(List<Resource> requestedResources){
    return requestedResources.stream()
        .mapToInt(resource -> resource.getQuantity() * ResourceTypeEnum.valueOf(resource.getType()).getScore())
        .sum();
  }

  private void validateTradeResources(Rebel rebel, List<Resource> requestedResources)
      throws TradeItemNotFoundOnRebelInventoryException, RebelNotHaveEnoughResourcesException {
    for(Resource resourceRequested: requestedResources){
      Resource resourceFromInventory = rebel.getInventory().stream()
          .filter(item -> item.getType().equals(resourceRequested.getType()))
          .findFirst()
          .orElseThrow(() -> new TradeItemNotFoundOnRebelInventoryException(rebel.getName(), resourceRequested.getType()));

      if(resourceRequested.getQuantity() > resourceFromInventory.getQuantity()){
        throw new RebelNotHaveEnoughResourcesException(rebel.getName(),
            resourceRequested.getType(), resourceFromInventory.getQuantity(), resourceRequested.getQuantity());
      }
    }
  }
}
