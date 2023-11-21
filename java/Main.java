import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BossDrop {
  private String dropName;
  private String rarity;

  public BossDrop(String dropName, String rarity) {
    this.dropName = dropName;
    this.rarity = rarity;
  }

  public String getDropName() {
    return dropName;
  }

  public String getRarity() {
    return rarity;
  }
}

class Main {
  // Method to retrieve a map of boss names to their associated drops
  public static Map<String, List<BossDrop>> retrieveDropsMap() {
    Map<String, List<BossDrop>> bossDropsMap = new HashMap<>();

    // Drops for the King Black Dragon boss
    List<BossDrop> kingBlackDragonDrops = new ArrayList<>();
    kingBlackDragonDrops.add(new BossDrop("Dragon Bones", "common"));
    kingBlackDragonDrops.add(new BossDrop("Dragonhide", "common"));
    kingBlackDragonDrops.add(new BossDrop("Draconic Visage", "rare"));

    bossDropsMap.put("King Black Dragon", kingBlackDragonDrops);

    // Drops for the Corporeal Beast boss
    List<BossDrop> corporealBeastDrops = new ArrayList<>();
    corporealBeastDrops.add(new BossDrop("Spirit Shield", "common"));
    corporealBeastDrops.add(new BossDrop("Holy Elixir", "common"));
    corporealBeastDrops.add(new BossDrop("Arcane Sigil", "rare"));

    bossDropsMap.put("Corporeal Beast", corporealBeastDrops);

    return bossDropsMap;
  }

  // Method to find the rarest drops for a given boss
  public static List<String> findRarestDrops(String bossName) {
    Map<String, List<BossDrop>> bossDropsMap = retrieveDropsMap();
    List<BossDrop> dropList = bossDropsMap.get(bossName);

    if (dropList == null) {
      System.out.println("Invalid boss name!");
      return Collections.emptyList();
    }

    List<String> rarestDrops = new ArrayList<>();
    String rarestRarity = null;

    for (BossDrop drop : dropList) {
      if (rarestRarity == null || drop.getRarity().equals("rare")) {
        rarestRarity = drop.getRarity();
        rarestDrops.clear();
        rarestDrops.add(drop.getDropName());
      } else if (drop.getRarity().equals(rarestRarity)) {
        rarestDrops.add(drop.getDropName());
      }
    }

    return rarestDrops;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Please enter the name of the boss:");
    String bossName = scanner.nextLine();

    List<String> rarestDrops = findRarestDrops(bossName);

    if (rarestDrops.isEmpty()) {
      System.out.println("Invalid boss name!");
    } else {
      System.out.println("The rarest drop from " + bossName + " is: " + String.join(", ", rarestDrops) + ".");
    }

    scanner.close();
  }
}