package resistance.star.wars.socialnetwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resistance.star.wars.socialnetwork.model.dto.ReportDTO;
import resistance.star.wars.socialnetwork.service.ReportsService;

@RestController
@RequestMapping("reports")
@AllArgsConstructor
public class ReportsController {
  ReportsService service;

  @GetMapping("/rebels/percentage")
  public ReportDTO generateRebelsPercentageReport(){
    return service.generateRebelsPercentageReport();
  }

  @GetMapping("/traitor/percentage")
  public ReportDTO generateTraitorsPercentageReport(){
    return service.generateTraitorPercentageReport();
  }

  @GetMapping("/traitor/score/lost")
  public ReportDTO generateTraitorLostPointsReport(){
    return service.generateTraitorLostPointsReport();
  }

  @GetMapping("/resources/average")
  public ReportDTO generateResourcesAveragePerRebelReport(){
    return service.generateResourcesRebelAverage();
  }
}
