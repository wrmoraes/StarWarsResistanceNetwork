package resistance.star.wars.socialnetwork.exception;

import resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum;
import resistance.star.wars.socialnetwork.util.MessageUtils;

public class RebelNotFoundException extends NotFoundException {
  public RebelNotFoundException(Long id) {
    super(MessageUtils.getMessage(MessageCodeEnum.ERROR_REBEL_NOT_FOUND, id));
  }
}