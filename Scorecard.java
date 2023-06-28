// By Alex Xiang & Alex Yang

import java.util.*;


// Alex Yang wrote most of this class


public class Scorecard {
  //scores is an array of 19 values, each value storing a category
  //Although there are not 19 scorable categories, the extra indices in the array are used to store the labels, such as "Upper section" or "Grand total", etc.
  int[] scores;

  //This stores the name of the player in a String
  String playerName;

  //This is an ArrayList to store the categories that the player can score. We need this because a category cannot be scored twice. ArrayList is used because we can delete values.
  ArrayList<String> options;


  //Constructor for Scorecard
  // Parameter is name, which will be the player's name
  public Scorecard(String name) {
    // Set playerName to name
    playerName = name;

    // initialize scores array
    scores = new int[19];
    
    //Different scoring options (bonus is not an option because it requires the user to roll two yahtzees first)
    options = new ArrayList<String>();
    options.add("Aces");
    options.add("Twos");
    options.add("Threes");
    options.add("Fours");
    options.add("Fives");
    options.add("Sixes");
    options.add("3 of a kind");
    options.add("4 of a kind");
    options.add("Full House");
    options.add("Sm Straight");
    options.add("Lg Straight");
    options.add("YAHTZEE");
    options.add("Chance");
  }

}
