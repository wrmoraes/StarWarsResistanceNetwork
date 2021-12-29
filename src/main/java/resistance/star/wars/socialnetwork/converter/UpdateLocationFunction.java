package resistance.star.wars.socialnetwork.converter;

import org.springframework.stereotype.Service;
import resistance.star.wars.socialnetwork.model.entity.Location;

import java.util.function.BiFunction;

@Service
public class UpdateLocationFunction implements BiFunction<Location, Location, Location> {

  @Override
  public Location apply(Location oldLocation, Location newLocation) {
    oldLocation.setLatitude(newLocation.getLatitude());
    oldLocation.setLongitude(newLocation.getLongitude());
    oldLocation.setName(newLocation.getName());
    return oldLocation;
  }
}
