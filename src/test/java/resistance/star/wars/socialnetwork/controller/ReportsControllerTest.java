package resistance.star.wars.socialnetwork.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import resistance.star.wars.socialnetwork.exception.RebelNotFoundException;
import resistance.star.wars.socialnetwork.model.dto.ReportDTO;
import resistance.star.wars.socialnetwork.model.entity.Location;
import resistance.star.wars.socialnetwork.model.entity.Rebel;
import resistance.star.wars.socialnetwork.model.entity.Resource;
import resistance.star.wars.socialnetwork.model.enumeration.GenderEnum;
import resistance.star.wars.socialnetwork.model.enumeration.RebelStatusEnum;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ReportsControllerTest {

  @Rule
  public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Autowired
  private ReportsController controller;
  @Autowired
  private RebelController rebelController;


  @Test
  public void GenerateRebelsPercentageReportWithoutData(){
    ReportDTO reportDTO = controller.generateRebelsPercentageReport();
    assertEquals(reportDTO.getReportName(), "REBELS PERCENTAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Percentage of rebels");
    assertEquals(reportDTO.getResult(), "0%");
  }

  @Test
  public void GenerateRebelsPercentageReportWithOneRebel(){
    rebelController.saveRebel(mockedRebelOne());
    ReportDTO reportDTO = controller.generateRebelsPercentageReport();
    assertEquals(reportDTO.getReportName(), "REBELS PERCENTAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Percentage of rebels");
    assertEquals(reportDTO.getResult(), "100%");
  }

  @Test
  public void GenerateRebelsPercentageReportWithOneRebelAndOneTraitor() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    ReportDTO reportDTO = controller.generateRebelsPercentageReport();
    assertEquals(reportDTO.getReportName(), "REBELS PERCENTAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Percentage of rebels");
    assertEquals(reportDTO.getResult(), "50%");
  }

  @Test
  public void GenerateTraitorsPercentageReportWithOneRebel(){
    rebelController.saveRebel(mockedRebelOne());
    ReportDTO reportDTO = controller.generateTraitorsPercentageReport();
    assertEquals(reportDTO.getReportName(), "TRAITORS PERCENTAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Percentage of traitors");
    assertEquals(reportDTO.getResult(), "0%");
  }

  @Test
  public void GenerateTraitorsPercentageReportWithOneTraitor() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    ReportDTO reportDTO = controller.generateTraitorsPercentageReport();
    assertEquals(reportDTO.getReportName(), "TRAITORS PERCENTAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Percentage of traitors");
    assertEquals(reportDTO.getResult(), "100%");
  }

  @Test
  public void GenerateTraitorsPercentageReportWithOneTraitorAndOneRebel() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    ReportDTO reportDTO = controller.generateTraitorsPercentageReport();
    assertEquals(reportDTO.getReportName(), "TRAITORS PERCENTAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Percentage of traitors");
    assertEquals(reportDTO.getResult(), "50%");
  }

  @Test
  public void GenerateTraitorLostPointsReportWithoutTraitor() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    ReportDTO reportDTO = controller.generateTraitorLostPointsReport();
    assertEquals(reportDTO.getReportName(), "TRAITORS LOST POINTS REPORT");
    assertEquals(reportDTO.getReportDetails(), "Points lost due to traitors");
    assertEquals(reportDTO.getResult(), 0);
  }

  @Test
  public void GenerateTraitorLostPointsReportWithOneTraitor() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    ReportDTO reportDTO = controller.generateTraitorLostPointsReport();
    assertEquals(reportDTO.getReportName(), "TRAITORS LOST POINTS REPORT");
    assertEquals(reportDTO.getReportDetails(), "Points lost due to traitors");
    assertEquals(reportDTO.getResult(), 6);
  }

  @Test
  public void GenerateResourcesAveragePerRebelReportWithoutRebel() throws RebelNotFoundException {
    ReportDTO reportDTO = controller.generateResourcesAveragePerRebelReport();
    assertEquals(reportDTO.getReportName(), "RESOURCES PER REBEL AVERAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Average amount of each resource type per rebel");
    assertEquals(reportDTO.getResult(), mockResourcesAverageMap(0.0, 0.0, 0.0, 0.0));
  }

  @Test
  public void GenerateResourcesAveragePerRebelReportWithOneRebel() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    ReportDTO reportDTO = controller.generateResourcesAveragePerRebelReport();
    assertEquals(reportDTO.getReportName(), "RESOURCES PER REBEL AVERAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Average amount of each resource type per rebel");
    assertEquals(reportDTO.getResult(), mockResourcesAverageMap(1.0, 0.0, 1.0, 0.0));
  }

  @Test
  public void GenerateResourcesAveragePerRebelReportWithTwoRebel() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());
    ReportDTO reportDTO = controller.generateResourcesAveragePerRebelReport();
    assertEquals(reportDTO.getReportName(), "RESOURCES PER REBEL AVERAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Average amount of each resource type per rebel");
    assertEquals(reportDTO.getResult(), mockResourcesAverageMap(0.5, 0.0, 0.5, 3.0));
  }

  @Test
  public void GenerateResourcesAveragePerRebelReportWithOneRebelAndOneTraitor() throws RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    rebelController.updateRebelTraitorLevel(1L);
    ReportDTO reportDTO = controller.generateResourcesAveragePerRebelReport();
    assertEquals(reportDTO.getReportName(), "RESOURCES PER REBEL AVERAGE REPORT");
    assertEquals(reportDTO.getReportDetails(), "Average amount of each resource type per rebel");
    assertEquals(reportDTO.getResult(), mockResourcesAverageMap(0.0, 0.0, 0.0, 6.0));
  }

  private Map<String, Double> mockResourcesAverageMap(double weapon, double ammunition, double water, double food){
    Map<String, Double> map = new HashMap<>();
    map.put(ResourceTypeEnum.WEAPON.toString(), weapon);
    map.put(ResourceTypeEnum.AMMUNITION.toString(), ammunition);
    map.put(ResourceTypeEnum.WATER.toString(), water);
    map.put(ResourceTypeEnum.FOOD.toString(), food);
    return map;
  }



  private Rebel mockedRebelOne () {
    return Rebel.builder()
        .name("Name Of Rebel One")
        .age(50)
        .gender(GenderEnum.MALE.name())
        .status(RebelStatusEnum.CONFEDERATE)
        .location(
            Location.builder()
                .latitude(-27.631758)
                .longitude(-48.6737598)
                .name("Galaxy Location").build()
        )
        .inventory(
            List.of(
                Resource.builder().quantity(1).type(ResourceTypeEnum.WEAPON.name()).build(),
                Resource.builder().quantity(1).type(ResourceTypeEnum.WATER.name()).build()
            )
        ).build();
  }

  private Rebel mockedRebelTwo () {
    return Rebel.builder()
        .name("Name Of Rebel Two")
        .age(50)
        .gender(GenderEnum.MALE.name())
        .status(RebelStatusEnum.CONFEDERATE)
        .location(
            Location.builder()
                .latitude(-27.631758)
                .longitude(-48.6737598)
                .name("Galaxy Location").build()
        )
        .inventory(
            List.of(
                Resource.builder().quantity(6).type(ResourceTypeEnum.FOOD.name()).build()
            )
        ).build();
  }
}
