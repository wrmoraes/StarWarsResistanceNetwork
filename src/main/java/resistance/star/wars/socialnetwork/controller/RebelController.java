package resistance.star.wars.socialnetwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resistance.star.wars.socialnetwork.exception.RebelNotFoundException;
import resistance.star.wars.socialnetwork.model.entity.Location;
import resistance.star.wars.socialnetwork.model.entity.Rebel;
import resistance.star.wars.socialnetwork.service.RebelService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("rebel")
@AllArgsConstructor
public class RebelController {
  private final RebelService service;

  @PostMapping
  public void saveRebel(@RequestBody @Valid Rebel rebel){
    service.save(rebel);
  }

  @GetMapping
  public List<Rebel> findAllRebels(){
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Rebel findRebelById(@PathVariable Long id) throws RebelNotFoundException {
    return service.findById(id);
  }

  @GetMapping("/{id}/location")
  public Location findRebelLocation(@PathVariable Long id) throws RebelNotFoundException {
   return service.findRebelLocation(id);
  }

  @PatchMapping("/{id}/location")
  public void updateRebelLocation(@PathVariable Long id, @Valid @RequestBody Location location) throws RebelNotFoundException {
    service.updateRebelLocation(id, location);
  }

  @PatchMapping("/{id}/traitor/report")
  public void updateRebelTraitorLevel(@PathVariable Long id) throws RebelNotFoundException {
    service.updateTraitorCounter(id);
  }

  @GetMapping("/{id}/traitor")
  public int findRebelTraitorLevel(@PathVariable Long id) throws RebelNotFoundException {
    return service.findTraitorLevel(id);
  }
}
