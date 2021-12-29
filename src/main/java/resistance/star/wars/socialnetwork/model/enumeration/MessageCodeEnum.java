package resistance.star.wars.socialnetwork.model.enumeration;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum MessageCodeEnum {
  ERROR_REBEL_NOT_FOUND("error.rebel.not.found"),
  ERROR_REBEL_NOT_HAVE_ENOUGH_RESOURCES("error.rebel.not.have.enough.resources"),
  ERROR_TRADE_ITEM_NOT_FOUND_ON_REBEL_INVENTORY("error.trade.item.not.found.on.rebel.inventory"),
  ERROR_TRADE_TRAITOR("error.trade.traitor"),
  ERROR_TRADE_SCORE_INVALID("error.trade.score.invalid"),
  REPORT_REBELS_PERCENTAGE_TITLE("report.rebels.percentage.title"),
  REPORT_REBELS_PERCENTAGE_DETAILS("report.rebels.percentage.details"),
  REPORT_TRAITORS_PERCENTAGE_TITLE("report.traitors.percentage.title"),
  REPORT_TRAITORS_PERCENTAGE_DETAILS("report.traitors.percentage.details"),
  REPORT_TRAITORS_LOST_POINTS_TITLE("report.traitors.lost.points.title"),
  REPORT_TRAITORS_LOST_POINTS_DETAILS("report.traitors.lost.points.details"),
  REPORT_RESOURCES_PER_REVEL_AVERAGE_TITLE("report.resources.per.rebel.average.title"),
  REPORT_RESOURCES_PER_REVEL_AVERAGE_DETAILS("report.resources.per.rebel.average.details"),
  @JsonEnumDefaultValue UNKNOWN("unknown");

  private final String messageKey;

  MessageCodeEnum(final String messageKey) {
    this.messageKey = messageKey;
  }

  public String getMessageKey() {
    return messageKey;
  }

  @Override
  public String toString() {
    return messageKey;
  }
}
