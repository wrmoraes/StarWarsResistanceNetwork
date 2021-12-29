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
import org.springframework.test.context.junit4.SpringRunner;
import resistance.star.wars.socialnetwork.exception.RebelNotFoundException;
import resistance.star.wars.socialnetwork.model.entity.Location;
import resistance.star.wars.socialnetwork.model.entity.Rebel;
import resistance.star.wars.socialnetwork.model.entity.Resource;
import resistance.star.wars.socialnetwork.model.enumeration.GenderEnum;
import resistance.star.wars.socialnetwork.model.enumeration.RebelStatusEnum;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RebelControllerTest {

  @Rule
  public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Autowired
  private RebelController controller;

  @Test
  public void findAllEmpty(){
    assertTrue(controller.findAllRebels().isEmpty());
  }

  @Test
  public void saveAndGetRebelById() throws RebelNotFoundException {
    Rebel mockedRebel = mockedRebel();
    controller.saveRebel(mockedRebel);
    assertFalse(controller.findAllRebels().isEmpty());
    Rebel rebel = controller.findRebelById(1L);
    assertEquals(rebel.getName(), mockedRebel.getName().toUpperCase());
    assertEquals(rebel.getAge(), mockedRebel.getAge());
    assertEquals(rebel.getGender(), mockedRebel.getGender());
    assertEquals(rebel.getLocation().getName(), mockedRebel.getLocation().getName().toUpperCase());
    assertEquals(rebel.getLocation().getLatitude(), mockedRebel.getLocation().getLatitude());
    assertEquals(rebel.getLocation().getLongitude(), mockedRebel.getLocation().getLongitude());
    assertEquals(rebel.getInventory().size(), 2);
    assertEquals(rebel.getInventory().stream()
        .filter(resource -> ResourceTypeEnum.WEAPON.name().equals(resource.getType()))
        .count(), 1);
    assertEquals(rebel.getInventory().stream()
        .filter(resource -> ResourceTypeEnum.WATER.name().equals(resource.getType()))
        .count(), 1);

  }

  @Test(expected = RebelNotFoundException.class)
  public void throwRebelNotFound() throws RebelNotFoundException {
    controller.findRebelById(1L);
  }

  @Test
  public void changeLocationAndGetById() throws RebelNotFoundException {
    Rebel mockedRebel = mockedRebel();
    controller.saveRebel(mockedRebel);
    assertFalse(controller.findAllRebels().isEmpty());
    Rebel rebel = controller.findRebelById(1L);
    assertEquals(rebel.getLocation().getName(), mockedRebel.getLocation().getName().toUpperCase());
    assertEquals(rebel.getLocation().getLatitude(), mockedRebel.getLocation().getLatitude());
    assertEquals(rebel.getLocation().getLongitude(), mockedRebel.getLocation().getLongitude());

    Double newLatitude = -48.6737598;
    Double newLongitude = -27.631758;
    controller.updateRebelLocation(1L,
        Location.builder().name("New Galaxy Location").latitude(newLatitude).longitude(newLongitude)
        .build());

    Location newLocation = controller.findRebelLocation(1L);
    assertEquals(newLocation.getName(), "NEW GALAXY LOCATION");
    assertEquals(newLocation.getLatitude(), newLatitude);
    assertEquals(newLocation.getLongitude(), newLongitude);
  }

  @Test(expected = RebelNotFoundException.class)
  public void throwLocationRebelNotFound() throws RebelNotFoundException {
    controller.findRebelLocation(1L);
  }

  @Test
  public void raiseAndGetTraitorLevel() throws RebelNotFoundException {
    Rebel mockedRebel = mockedRebel();
    controller.saveRebel(mockedRebel);
    assertFalse(controller.findAllRebels().isEmpty());
    Rebel rebel = controller.findRebelById(1L);
    assertEquals(rebel.getTraitorLevel(), 0);
    assertEquals(rebel.getStatus(), RebelStatusEnum.CONFEDERATE);
    controller.updateRebelTraitorLevel(1L);
    assertEquals(controller.findRebelById(1L).getTraitorLevel(), 1);
    assertEquals(controller.findRebelById(1L).getStatus(), RebelStatusEnum.CONFEDERATE);
    controller.updateRebelTraitorLevel(1L);
    assertEquals(controller.findRebelById(1L).getTraitorLevel(), 2);
    assertEquals(controller.findRebelById(1L).getStatus(), RebelStatusEnum.CONFEDERATE);
    controller.updateRebelTraitorLevel(1L);
    assertEquals(controller.findRebelById(1L).getTraitorLevel(), 3);
    assertEquals(controller.findRebelById(1L).getStatus(), RebelStatusEnum.TRAITOR);
  }

  @Test(expected = RebelNotFoundException.class)
  public void throwRaiseTraitorRebelNotFound() throws RebelNotFoundException {
    controller.updateRebelTraitorLevel(1L);
  }

  @Test(expected = RebelNotFoundException.class)
  public void throwFindTraitorRebelNotFound() throws RebelNotFoundException {
    controller.findRebelTraitorLevel(1L);
  }

  private Rebel mockedRebel () {
    return Rebel.builder()
        .name("Name Of Rebel")
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
}
