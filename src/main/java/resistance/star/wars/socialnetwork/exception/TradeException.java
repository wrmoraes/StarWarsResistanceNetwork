package resistance.star.wars.socialnetwork.exception;

public class TradeException extends BusinessException {
  public TradeException(String message, Throwable cause) {
    super(message, cause);
  }

  public TradeException(String message) {
    super(message);
  }
}