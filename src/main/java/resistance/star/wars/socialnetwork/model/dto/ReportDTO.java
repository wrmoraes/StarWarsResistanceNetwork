package resistance.star.wars.socialnetwork.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class ReportDTO {
  String reportName;
  String reportDetails;
  Object result;
}
