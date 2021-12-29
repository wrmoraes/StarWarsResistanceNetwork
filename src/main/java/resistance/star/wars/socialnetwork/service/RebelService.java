package resistance.star.wars.socialnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import resistance.star.wars.socialnetwork.converter.UpdateLocationFunction;
import resistance.star.wars.socialnetwork.exception.RebelNotFoundException;
import resistance.star.wars.socialnetwork.model.entity.Location;
import resistance.star.wars.socialnetwork.model.entity.Rebel;
import resistance.star.wars.socialnetwork.repository.RebelRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RebelService {

  private final RebelRepository repository;
  private final UpdateLocationFunction updateLocationFunction;

  public void save(Rebel rebel){
    repository.save(rebel);
  }

  public List<Rebel> findAll(){
    return repository.findAll();
  }

  public Rebel findById(Long id) throws RebelNotFoundException {
    return repository.findById(id).orElseThrow(() ->
        new RebelNotFoundException(id)
    );
  }

  public void updateRebelLocation(Long id, Location newLocation) throws RebelNotFoundException {
    Rebel rebel = findById(id);
    rebel.setLocation(updateLocationFunction.apply(rebel.getLocation(), newLocation));
    save(rebel);
  }

  public Location findRebelLocation(Long id) throws RebelNotFoundException {
    return findById(id).getLocation();
  }

  public int findTraitorLevel(Long id) throws RebelNotFoundException {
    return findById(id).getTraitorLevel();
  }

  public void updateTraitorCounter(Long id) throws RebelNotFoundException {
    Rebel rebel = findById(id);
    rebel.raiseTraitorLevel();
    save(rebel);
  }
}
