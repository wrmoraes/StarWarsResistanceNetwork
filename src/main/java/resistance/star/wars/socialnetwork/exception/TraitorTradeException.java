package resistance.star.wars.socialnetwork.exception;

import resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum;
import resistance.star.wars.socialnetwork.util.MessageUtils;

public class TraitorTradeException extends TradeException {
  public TraitorTradeException(String rebelName) {
    super(MessageUtils.getMessage(MessageCodeEnum.ERROR_TRADE_TRAITOR, rebelName));
  }
}