package resistance.star.wars.socialnetwork.exception;

import resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum;
import resistance.star.wars.socialnetwork.util.MessageUtils;

public class TradeItemNotFoundOnRebelInventoryException extends NotFoundException {
  public TradeItemNotFoundOnRebelInventoryException(String rebelName, String itemName) {
    super(MessageUtils.getMessage(MessageCodeEnum.ERROR_TRADE_ITEM_NOT_FOUND_ON_REBEL_INVENTORY, rebelName, itemName));
  }
}