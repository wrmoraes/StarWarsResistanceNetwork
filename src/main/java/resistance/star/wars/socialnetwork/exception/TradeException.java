package resistance.star.wars.socialnetwork.exception;

import resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum;

public class TradeException extends BusinessException {
  public TradeException(String message, Throwable cause) {
    super(message, cause);
  }

  public TradeException(String message) {
    super(message);
  }
}