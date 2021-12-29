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
import resistance.star.wars.socialnetwork.exception.TradeException;
import resistance.star.wars.socialnetwork.exception.TradeItemNotFoundOnRebelInventoryException;
import resistance.star.wars.socialnetwork.model.dto.ResourceDTO;
import resistance.star.wars.socialnetwork.model.dto.TradeDTO;
import resistance.star.wars.socialnetwork.model.dto.TradeRebelDTO;
import resistance.star.wars.socialnetwork.model.entity.Location;
import resistance.star.wars.socialnetwork.model.entity.Rebel;
import resistance.star.wars.socialnetwork.model.entity.Resource;
import resistance.star.wars.socialnetwork.model.enumeration.GenderEnum;
import resistance.star.wars.socialnetwork.model.enumeration.RebelStatusEnum;
import resistance.star.wars.socialnetwork.model.enumeration.ResourceTypeEnum;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TradeControllerTest {

  @Rule
  public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Autowired
  private TradeController controller;
  @Autowired
  private RebelController rebelController;


  @Test
  public void TradeRebelNotFoundException() throws TradeException {
    TradeException exception = assertThrows(TradeException.class, () -> {
      controller.trade(tradeMockedOk());
    });
    assertEquals(exception.getCause().getClass(), RebelNotFoundException.class);
  }

  @Test
  public void TradeItemNotFoundOnRebelInventoryException() throws TradeException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());
    TradeException exception = assertThrows(TradeException.class, () -> {
      controller.trade(tradeMockedItemNotFound());
    });

    assertEquals(exception.getCause().getClass(), TradeItemNotFoundOnRebelInventoryException.class);
  }

  @Test
  public void TradeRebelNotHaveEnoughResourcesException() throws TradeException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());
    TradeException exception = assertThrows(TradeException.class, () -> {
      controller.trade(tradeMockedRebelNotHaveEnoughResources());
    });
  }

    @Test
    public void TradeItemsScoreDontMatch() throws TradeException {
      rebelController.saveRebel(mockedRebelOne());
      rebelController.saveRebel(mockedRebelTwo());
      TradeException exception = assertThrows(TradeException.class, () -> {
        controller.trade(tradeMockedRebelItemsScoreDontMatch());
      });

    assertTrue(exception.getMessage().contains("The trade cant be done because the score of items dont match"));
  }

  @Test
  public void tradeOk() throws TradeException, RebelNotFoundException {
    rebelController.saveRebel(mockedRebelOne());
    rebelController.saveRebel(mockedRebelTwo());

    Rebel rebelOne = rebelController.findRebelById(1L);
    assertEquals(getResourceTypeQuantity(rebelOne.getInventory(), ResourceTypeEnum.WEAPON.toString()), 1);
    assertEquals(getResourceTypeQuantity(rebelOne.getInventory(), ResourceTypeEnum.WATER.toString()), 1);
    assertEquals(getResourceTypeQuantity(rebelOne.getInventory(), ResourceTypeEnum.FOOD.toString()), 0);
    assertEquals(getResourceTypeQuantity(rebelOne.getInventory(), ResourceTypeEnum.AMMUNITION.toString()), 0);

    Rebel rebelTwo = rebelController.findRebelById(2L);
    assertEquals(getResourceTypeQuantity(rebelTwo.getInventory(), ResourceTypeEnum.WEAPON.toString()), 0);
    assertEquals(getResourceTypeQuantity(rebelTwo.getInventory(), ResourceTypeEnum.WATER.toString()), 0);
    assertEquals(getResourceTypeQuantity(rebelTwo.getInventory(), ResourceTypeEnum.FOOD.toString()), 6);
    assertEquals(getResourceTypeQuantity(rebelTwo.getInventory(), ResourceTypeEnum.AMMUNITION.toString()), 0);

    controller.trade(tradeMockedOk());

    Rebel rebelOneTraded = rebelController.findRebelById(1L);
    assertEquals(getResourceTypeQuantity(rebelOneTraded.getInventory(), ResourceTypeEnum.WEAPON.toString()), 0);
    assertEquals(getResourceTypeQuantity(rebelOneTraded.getInventory(), ResourceTypeEnum.WATER.toString()), 0);
    assertEquals(getResourceTypeQuantity(rebelOneTraded.getInventory(), ResourceTypeEnum.FOOD.toString()), 6);
    assertEquals(getResourceTypeQuantity(rebelOneTraded.getInventory(), ResourceTypeEnum.AMMUNITION.toString()), 0);

    Rebel rebelTwoTraded = rebelController.findRebelById(2L);
    assertEquals(getResourceTypeQuantity(rebelTwoTraded.getInventory(), ResourceTypeEnum.WEAPON.toString()), 1);
    assertEquals(getResourceTypeQuantity(rebelTwoTraded.getInventory(), ResourceTypeEnum.WATER.toString()), 1);
    assertEquals(getResourceTypeQuantity(rebelTwoTraded.getInventory(), ResourceTypeEnum.FOOD.toString()), 0);
    assertEquals(getResourceTypeQuantity(rebelTwoTraded.getInventory(), ResourceTypeEnum.AMMUNITION.toString()), 0);

  }

  private int getResourceTypeQuantity(List<Resource> resources, String resourceType){
    return resources.stream()
        .filter(resource -> resource.getType().equals(resourceType))
        .findFirst().map(Resource::getQuantity)
        .orElse(0);
  }

  private ResourceDTO tradeResourceDTOMMocked(int quantity, ResourceTypeEnum resourceType){
    return ResourceDTO.builder()
        .quantity(quantity)
        .type(resourceType.name())
        .build();
  }

  private TradeRebelDTO tradeRebelDTOMocked(Long id, List<ResourceDTO> resources){
    return TradeRebelDTO.builder()
        .user(id)
        .tradeResources(resources)
        .build();
  }

  private TradeDTO tradeMockedRebelItemsScoreDontMatch(){
    return TradeDTO.builder()
        .traderOne(tradeRebelDTOMocked(1L, List.of(
                tradeResourceDTOMMocked(1, ResourceTypeEnum.WEAPON)
            ))
        )
        .traderTwo(tradeRebelDTOMocked(2L, List.of(
                tradeResourceDTOMMocked(5, ResourceTypeEnum.FOOD)
            ))
        ).build();
  }

  private TradeDTO tradeMockedRebelNotHaveEnoughResources(){
    return TradeDTO.builder()
        .traderOne(tradeRebelDTOMocked(1L, List.of(
                tradeResourceDTOMMocked(2, ResourceTypeEnum.WEAPON)
            ))
        )
        .traderTwo(tradeRebelDTOMocked(2L, List.of(
                tradeResourceDTOMMocked(7, ResourceTypeEnum.FOOD)
            ))
        ).build();
  }

  private TradeDTO tradeMockedItemNotFound(){
    return TradeDTO.builder()
        .traderOne(tradeRebelDTOMocked(1L, List.of(
                tradeResourceDTOMMocked(1, ResourceTypeEnum.AMMUNITION)
            ))
        )
        .traderTwo(tradeRebelDTOMocked(2L, List.of(
                tradeResourceDTOMMocked(1, ResourceTypeEnum.AMMUNITION)
            ))
        ).build();
  }

  private TradeDTO tradeMockedOk(){
    return TradeDTO.builder()
        .traderOne(tradeRebelDTOMocked(1L, List.of(
            tradeResourceDTOMMocked(1, ResourceTypeEnum.WATER),
            tradeResourceDTOMMocked(1, ResourceTypeEnum.WEAPON)
                ))
        )
        .traderTwo(tradeRebelDTOMocked(2L, List.of(
                tradeResourceDTOMMocked(6, ResourceTypeEnum.FOOD)
            ))
        ).build();
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
