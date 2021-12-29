package resistance.star.wars.socialnetwork.model.enumeration;

import lombok.Getter;

@Getter
public enum ResourceTypeEnum {
  WEAPON(4),
  AMMUNITION(3),
  WATER(2),
  FOOD(1);

  private int score;

  ResourceTypeEnum(int score) {
    this.score = score;
  }
}
