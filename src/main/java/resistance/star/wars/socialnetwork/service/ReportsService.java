package resistance.star.wars.socialnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import resistance.star.wars.socialnetwork.model.dto.ReportDTO;
import resistance.star.wars.socialnetwork.model.entity.Resource;
import resistance.star.wars.socialnetwork.model.enumeration.RebelStatusEnum;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_REBELS_PERCENTAGE_DETAILS;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_REBELS_PERCENTAGE_TITLE;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_RESOURCES_PER_REVEL_AVERAGE_DETAILS;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_RESOURCES_PER_REVEL_AVERAGE_TITLE;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_TRAITORS_LOST_POINTS_DETAILS;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_TRAITORS_LOST_POINTS_TITLE;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_TRAITORS_PERCENTAGE_DETAILS;
import static resistance.star.wars.socialnetwork.model.enumeration.MessageCodeEnum.REPORT_TRAITORS_PERCENTAGE_TITLE;
import static resistance.star.wars.socialnetwork.util.MessageUtils.getMessage;


@Service
@AllArgsConstructor
public class ReportsService {

  private final RebelService rebelService;
  private final TradeService tradeService;

  private static final DecimalFormat decFormat = new DecimalFormat("#%");
  private static final int COUNT_ONE = 1;
  private static final int COUNT_ZERO = 0;

  public ReportDTO generateRebelsPercentageReport(){
    double rebelsPercentage = rebelService.findAll().stream()
        .mapToInt(rebel -> RebelStatusEnum.CONFEDERATE.equals(rebel.getStatus()) ? COUNT_ONE : COUNT_ZERO)
        .summaryStatistics()
        .getAverage();

    return ReportDTO.builder()
        .reportName(getMessage(REPORT_REBELS_PERCENTAGE_TITLE))
        .reportDetails(getMessage(REPORT_REBELS_PERCENTAGE_DETAILS))
        .result(decFormat.format(rebelsPercentage))
        .build();
  }
  public ReportDTO generateTraitorPercentageReport(){
    double traitorsPercentage = rebelService.findAll().stream()
        .mapToInt(rebel -> RebelStatusEnum.TRAITOR.equals(rebel.getStatus()) ? COUNT_ONE : COUNT_ZERO)
        .summaryStatistics()
        .getAverage();

    return ReportDTO.builder()
        .reportName(getMessage(REPORT_TRAITORS_PERCENTAGE_TITLE))
        .reportDetails(getMessage(REPORT_TRAITORS_PERCENTAGE_DETAILS))
        .result(decFormat.format(traitorsPercentage))
        .build();
  }

  public ReportDTO generateTraitorLostPointsReport(){
    int traitorLostPoints = rebelService.findAll().stream()
        .filter(rebel -> RebelStatusEnum.TRAITOR.equals(rebel.getStatus()))
        .mapToInt(rebel -> tradeService.calculateTradeScore(rebel.getInventory()))
        .sum();

    return ReportDTO.builder()
        .reportName(getMessage(REPORT_TRAITORS_LOST_POINTS_TITLE))
        .reportDetails(getMessage(REPORT_TRAITORS_LOST_POINTS_DETAILS))
        .result(traitorLostPoints)
        .build();
  }

  public ReportDTO generateResourcesRebelAverage(){
    Map<String, Double> mapResourcesAverage = Arrays.stream(ResourceTypeEnum.values()).collect(
        toMap(ResourceTypeEnum::toString, resourceTypeEnum -> {
          return generateResourcesRebelAverageByType(resourceTypeEnum.toString());
        })
    );

    return ReportDTO.builder()
        .reportName(getMessage(REPORT_RESOURCES_PER_REVEL_AVERAGE_TITLE))
        .reportDetails(getMessage(REPORT_RESOURCES_PER_REVEL_AVERAGE_DETAILS))
        .result(mapResourcesAverage)
        .build();
  }

  public double generateResourcesRebelAverageByType(String resourceType){
    return rebelService.findAll().stream()
        .filter(rebel -> RebelStatusEnum.CONFEDERATE.equals(rebel.getStatus()))
        .mapToInt(rebel -> getResourceQuantityByType(rebel.getInventory(), resourceType))
        .summaryStatistics()
        .getAverage();
  }

  private static Integer getResourceQuantityByType(List<Resource> resources, String resourceType){
    return resources.stream().filter(resource -> resourceType.equals(resource.getType()))
        .findFirst()
        .map(Resource::getQuantity)
        .orElse(COUNT_ZERO);
  }
}
