package resistance.star.wars.socialnetwork.exception;

import resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum;
import resistance.star.wars.socialnetwork.util.MessageUtils;

public class RebelNotHaveEnoughResourcesException extends BusinessException {
  public RebelNotHaveEnoughResourcesException(String rebelName, String itemName, int quantityOnInventory, int quantityRequested) {
    super(MessageUtils.getMessage(MessageCodeEnum.ERROR_REBEL_NOT_HAVE_ENOUGH_RESOURCES,
        rebelName, itemName, quantityOnInventory, quantityRequested));
  }
}