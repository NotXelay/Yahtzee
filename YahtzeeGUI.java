// By Alex Xiang & Alex Yang

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class YahtzeeGUI implements ActionListener {
  // Alex Xiang made the variables

  // set up default GUI variables
  JFrame frame;
  JPanel contentPane;
  JLabel label;
  JLabel label2;
  JButton startButton;

  // make specific labels and buttons for the dice images and the reroll buttons
  // without specific variables for each, we would not be able to access each one, which is important in being able to change the image and button properties
  JLabel die1, die2, die3, die4, die5;
  JButton re1, re2, re3, re4, re5;

  // more default GUI variables
  JComboBox options;
  JButton button;

  // diceRolls[] is an array of length 5 that stores the faces of the dice rolled
  int[] diceRolls;
  // indices[] is an array of length 5. Each value is either 0 or 1, and represents if the dice at that index should be rerolled. Basically represents which dice that the player wants to reroll
  int[] indices;

  // rerolls represents how many rerolls the player has. Default is 2 rerolls, for a total of 3 rolls.
  int rerolls;
  JLabel rerollLabel;

  // text fields for input for players' names
  JTextField name1, name2;

  // Scorecard objects to represent the two players
  Scorecard player1;
  Scorecard player2;


  // boolean to see if it is the first turn. This is important in handling the display of the cards for the first turn.
  boolean firstTurn = true;


  // turn represents whose turn it is. If turn = 1, it is player1's turn. If turn = 2, it is player2's turn.
  int turn = 1;


  // GUI constructor, we worked together
  public YahtzeeGUI() {
    frame = new JFrame("Yahtzee");   
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // we use grid layout in order to make the yahtzee screen look nicer
    // we also use "0" rows, which just means the rows changes with how many are necessary
    GridLayout layout = new GridLayout(0, 2, 30, 0);

    // normal contentPane setup
    contentPane = new JPanel();
    contentPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    contentPane.setLayout(layout);  

    JLabel title = new JLabel("YAHTZEE");
    contentPane.add(title);

    //Alex Yang
    startButton = new JButton("Start");
    startButton.setActionCommand("Start");
    startButton.addActionListener(this);
    contentPane.add(startButton);

    //Alex Xiang
    // Allow players to input their name in an input box
    JLabel question = new JLabel("Enter player 1 name:");
    contentPane.add(question);
    name1 = new JTextField(20);
    contentPane.add(name1);

    question = new JLabel("Enter player 2 name:");
    contentPane.add(question);
    name2 = new JTextField(20);
    contentPane.add(name2);

    // create content pane
    frame.setContentPane(contentPane);
    // fullscreen the frame, as otherwise, there may be too much text on the screen for non-fullscreen
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    
    frame.pack();
    frame.setVisible(true);
  }

  // function to simply add an empty label, just helps to save code
  public void addEmptyLabel() {
    // create an empty label to create whitespace
    label = new JLabel ("");
    contentPane.add(label);
  }

  
  // runGUI function, just runs the GUI
  public static void runGUI() {
    JFrame.setDefaultLookAndFeelDecorated(true);
    YahtzeeGUI gui = new YahtzeeGUI();
  }


  // function to display the scorecard of one of the players

  // Preconditions: sc is a valid scorecard for one of the players
  // Postconditions: nothing is returned, but the GUI displays the scorecard correctly
  // Alex Xiang wrote this function
  
  public void displayCard(Scorecard sc) {
    // use .removeAll() to clear the screen and resetup the GUI
    contentPane.removeAll();

    // these are all the labels for each scoring section of the scorecard
    String[] labels = {"Upper Section", "Aces", "Twos", "Threes", "Fours", "Fives", "Sixes", "Lower Section", "3 of a kind", "4 of a kind", "Full House", "Sm Straight", "Lg Straight", "YAHTZEE", "Chance", "BONUS", "Upper Total", "Lower Total", "Grand Total"};


    // add the player's name at the top
    label = new JLabel(sc.playerName);
    contentPane.add(label);
    addEmptyLabel();


    // for each label, add each label to the display
    for (int i = 0; i < labels.length; i++) {
      label = new JLabel(labels[i]);
      contentPane.add(label);

      // if the label is either Upper Section or Lower Section, there is no need to print the score, since it isn't a category to score. Upper and lower section totals are at the bottom
      if (i == 0 || i == 7) {
        label = new JLabel(" ");
        contentPane.add(label);
      } else {
        // print "None" if there is no score, as it hasn't been scored yet
        // this is different from putting 0, since that tells that the category was scored, but was unable to gain any points
        if(sc.scores[i] == 0) {
          label = new JLabel("None");
        }
        else {
          // a score of -1 means the player tried to score the category but received no points, thus getting a 0 in the category. This is different from the "None" label above.
          if (sc.scores[i] == -1) label = new JLabel("0");
          // otherwise, print the score of the category of that label
          else label = new JLabel(String.valueOf(sc.scores[i]));
        }
        contentPane.add(label);
      }
    }

    // use revalidate and repaint to remake the screen again, as otherwise, things don't show up correctly after using .removeAll()
    contentPane.revalidate();
    contentPane.repaint();

    // add a Next button to the next page
    button = new JButton("Next");
    button.setActionCommand("Next");
    button.addActionListener(this);
    contentPane.add(button);
  }

  // function to reroll the dice rolls in diceRolls[]
  // indices is an array of length 5 that stores only either 1 or 0's.
  // each index that is a 1 should be rerolled, and 0's should be skipped
  
  // Alex Xiang wrote this function
  public void reroll(int[] indices) {
    for (int i = 0; i < 5; i++) {
      if (indices[i] == 1) {
      // if the number is 1, it should be rerolled
        int num1 = (int) (Math.random() * 6) + 1;
        diceRolls[i] = num1;
      } 
    }
    // diceRolls[] should now store rerolled indices
  } 

  
  // function to make the roll screen GUI and set up the player's rolls
  // Preconditions: card is either player1 or player2's card
  // Postconditions: The screen displays the dice rolls correctly and is able to handle dice rolls. 
    
  // We worked together for this function, especially since it is quite long
  
  public void roll(Scorecard card) {
    // set up variables for the roll. rerolls is the number of rerolls the player has, which is 2, for a total of 3 rolls. diceRolls[] is an array with the numbers that are rolled, between 1-6. indicies[] is an array of 1s and 0s that represent whether or not the index at that position should be rerolled, if it is 1, the index should be rerolled, if 0, it shouldn't.
    
    rerolls = 2;
    diceRolls = new int[5];
    indices = new int[5];

    
    // reset the indices to reroll. 
    for (int i = 0; i < 5; i++) indices[i] = 1;
    // reroll all to generate 5 dice.
    reroll(indices);

    // clear the screen
    contentPane.removeAll();
    // reset the reroll indices
    for (int i = 0; i < 5; i++) indices[i] = 0;

    
    //Inform the player whos turn it is using a label
    if (turn == 1) {
      label = new JLabel("It is " + player1.playerName + "\'s turn");
      contentPane.add(label);
      addEmptyLabel();
    } else {
      label = new JLabel("It is " + player2.playerName + "\'s turn");
      contentPane.add(label);
      addEmptyLabel();
    }

    //Allows users to reroll dice

    // Alex Yang did the following labels
    
    // Make 5 dice images and 5 buttons to allow the player to see and select to reroll the dice sleected
    die1 = new JLabel(new ImageIcon ("die" + diceRolls[0] + ".jpg"));
    contentPane.add(die1);
    // We use buttons for the player to signify which dice to reroll
    re1 = new JButton("Select to reroll dice 1");
    re1.setActionCommand("Reroll1");
    re1.addActionListener(this);
    contentPane.add(re1);

    die2 = new JLabel(new ImageIcon ("die" + diceRolls[1] + ".jpg"));
    contentPane.add(die2);
    re2 = new JButton("Select to reroll dice 2");
    re2.setActionCommand("Reroll2");
    re2.addActionListener(this);
    contentPane.add(re2);

    die3 = new JLabel(new ImageIcon ("die" + diceRolls[2] + ".jpg"));
    contentPane.add(die3);
    re3 = new JButton("Select to reroll dice 3");
    re3.setActionCommand("Reroll3");
    re3.addActionListener(this);
    contentPane.add(re3);

    die4 = new JLabel(new ImageIcon ("die" + diceRolls[3] + ".jpg"));
    contentPane.add(die4);
    re4 = new JButton("Select to reroll dice 4");
    re4.setActionCommand("Reroll4");
    re4.addActionListener(this);
    contentPane.add(re4);

    die5 = new JLabel(new ImageIcon ("die" + diceRolls[4] + ".jpg"));
    contentPane.add(die5);
    re5 = new JButton("Select to reroll dice 5");
    re5.setActionCommand("Reroll5");
    re5.addActionListener(this);
    contentPane.add(re5);

    addEmptyLabel();
    addEmptyLabel();

    // Add a button to reroll all that are selected in order to allow the user to reroll all at the same time. 
    button = new JButton("Reroll All Selected");
    button.setActionCommand("RerollAll");
    button.addActionListener(this);
    contentPane.add(button);

    // create a temp[] array to store the current player's options to score.
    
    String[] temp = new String[card.options.size()];
    for (int i = 0; i < card.options.size(); i++) {
      temp[i] = card.options.get(i);
    }

    
    //Alex Xiang did this part

    // if there are no options for the user to choose from, then the game should end, as there are no possible options to choose from. 
    if (temp.length == 0) {
      end();
    } else {
      // display label for the number of rerolls the player has left
      rerollLabel = new JLabel("You have " + rerolls + " rerolls left");
      contentPane.add(rerollLabel);

      // display label to prompt which category to score
      label = new JLabel("Choose a category to score: ");
      contentPane.add(label);
      addEmptyLabel();

      // make a combo box with the options that the user has, by using temp, which is an array
      options = new JComboBox(temp);
      options.setSelectedIndex(0);
      contentPane.add(options);

      // make confirm button to allow the use to confirm option.
      button = new JButton("Confirm");
      button.setActionCommand("Confirm");
      button.addActionListener(this);
      contentPane.add(button);

      // use revalidate and repaint to remake the screen correctly. 
  
      contentPane.revalidate();
      contentPane.repaint();
    }
  }

  // function to handle the game's end, and display who won. 

  // we worked together to make the following function
  public void end() {
    // clear screen
    contentPane.removeAll();

    // display player1's points & player2's points
    label = new JLabel(String.valueOf(player1.playerName) + "\'s total points: ");
    contentPane.add(label);

    label = new JLabel(String.valueOf(player1.scores[18]));
    contentPane.add(label);

    label = new JLabel(String.valueOf(player2.playerName) + "\'s total points: ");
    contentPane.add(label);

    label = new JLabel(String.valueOf(player2.scores[18]));
    contentPane.add(label);

    //After all the spots are filled and the game is over, notifies the user(s) who has won or if there was a tie
    if (player1.scores[18] > player2.scores[18]) {
      label = new JLabel(player1.playerName + " wins!!!!");
      contentPane.add(label);
    } else if (player1.scores[18] == player2.scores[18]) {
      label = new JLabel("You tied with each other.");
      contentPane.add(label);
    } else {
      label = new JLabel(player2.playerName + " wins!!!!");
      contentPane.add(label);
    }

    // repaint and revalidate to make sure the screen is set up correctly
    contentPane.revalidate();
    contentPane.repaint();
  }

  
  //Alex Xiang

  // default action handler, allows the program to handle actions
  public void actionPerformed(ActionEvent event) {
    String eventName = event.getActionCommand();


    // if the action is to start the game, then initialize the scorecards for each player and display the card of the first player. 
    if (eventName.equals("Start")) {
      player1 = new Scorecard(name1.getText());
      player2 = new Scorecard(name2.getText());

      displayCard(player1);
    } 

    // actions to handle the rerolls
    if (eventName.equals("RerollAll")) {
      // make sure player has enough rerolls
      if (rerolls > 0) {
        reroll(indices);
        // reset each dice icon
        die1.setIcon(new ImageIcon("die" + diceRolls[0] + ".jpg"));
        die2.setIcon(new ImageIcon("die" + diceRolls[1] + ".jpg"));
        die3.setIcon(new ImageIcon("die" + diceRolls[2] + ".jpg"));
        die4.setIcon(new ImageIcon("die" + diceRolls[3] + ".jpg"));
        die5.setIcon(new ImageIcon("die" + diceRolls[4] + ".jpg"));

        // reset the colors of the buttons so that they indicate there are no rerolls
        re1.setBackground(null);
        re2.setBackground(null);
        re3.setBackground(null);
        re4.setBackground(null);
        re5.setBackground(null);
        // reset the values of indices[] so nothing is rerolled currently
        for (int i = 0; i < 5; i++) indices[i] = 0;

        // subtract one reroll and reset the text
        rerolls--;
        rerollLabel.setText("You have " + rerolls + " rerolls left");
      } 

    // the following six events are very similar, as they all just handle the reroll buttons. For each one, it checks the color of the button. If it is red, then the user clicking the button again should deactivate the reroll, and reset indices. Otherwise, it should activate the reroll and put 1 in indices[]

    // We also worked together for the following 6 buttons

    } else if (eventName.equals("Reroll1")) {
      button = (JButton) event.getSource();

      // check button color
      if (button.getBackground() == Color.red) {
        // reset value in indices[] and button color
        indices[0] = 0;
        button.setBackground(null);
      } else {
        // make value in indices and button color to indicate reroll
        indices[0] = 1;
        // set color to red in order to indicate the button is selected
        button.setBackground(Color.red);
      }
      
    } else if (eventName.equals("Reroll2")) {

      // do the same for all the other reroll buttons
      button = (JButton) event.getSource();
      if (button.getBackground() == Color.red) {
        indices[1] = 0;
        button.setBackground(null);
      } else {
        indices[1] = 1;
        button.setBackground(Color.red);
      }
    } else if (eventName.equals("Reroll3")) {
      button = (JButton) event.getSource();
      
      if (button.getBackground() == Color.red) {
        indices[2] = 0;
        button.setBackground(null);
      } else {
        indices[2] = 1;
        button.setBackground(Color.red);
      }
    } else if (eventName.equals("Reroll4")) {
      button = (JButton) event.getSource();
      
      if (button.getBackground() == Color.red) {
        indices[3] = 0;
        button.setBackground(null);
      } else {
        indices[3] = 1;
        button.setBackground(Color.red);
      }
    } else if (eventName.equals("Reroll5")) {
      button = (JButton) event.getSource();
      
      if (button.getBackground() == Color.red) {
        indices[4] = 0;
        button.setBackground(null);
      } else {
        indices[4] = 1;
        button.setBackground(Color.red);
      }
    } else if (eventName.equals("Reroll6")) {
      button = (JButton) event.getSource();
      
      if (button.getBackground() == Color.red) {
        indices[5] = 0;
        button.setBackground(null);
      } else {
        indices[5] = 1;
        button.setBackground(Color.red);
      }


      // Alex Xiang did the following else if statement
    } else if (eventName.equals("Next")) {

      // used to handle the first next button, which should display the second player's card before it goes to the dice rolls turn.
      if (firstTurn) {
        turn = 2;
        displayCard(player2);
        firstTurn = false;
      } else {
        // otherwise, use to handle the turn change and rolling
        if (turn == 1) {
          turn = 2;
          roll(player2);
        } else {
          turn = 1;
          roll(player1);
        }
      }

      // the following is used to handle the scoring of the dice rolls.

      
      // We worked together for the following part. 
    } else if (eventName.equals("Confirm")) {
      // make card = to whatever player's scorecard the turn is, as otherwise, we would have to make a huge if statement and copy code.
      Scorecard card = new Scorecard("");

      // make sure the card corresponds to the correct turn
      if (turn == 1) card = player1;
      if (turn == 2) card = player2;


      // get the selected option
      String option = (String) options.getSelectedItem();
      // remove from the options, as it cannot be used twice
      int index = options.getSelectedIndex();
      card.options.remove(index);

      // freqList[] is a frequency list of how many times each number comes up. Much easier to analyze a frequency list and score it, instead of using diceRolls[]
      int[] freqList = {0, 0, 0, 0, 0, 0};
      

      // import into freqList[]
      for (int i : diceRolls) {
        freqList[i-1]++;
      }

      // total represents the points scored for the chosen category
      // total is -1, which in terms of display scoring, means a score of 0,
      // total cannot be 0, as that would signify the player hasn't scored this category yet. 
      int total = -1;

      // bonus rules
      // check if the yahtzee is scored already, and isn't -1 or 0
      if (card.scores[13] != 0 && card.scores[13] != -1) {
        // check if there is a 5 in freqList[], which means one number came up 5 times, which means a YAHTZEE
        for (int i = 0; i < 6; i++) {
          if (freqList[i] == 5) {
            total = 50; 
            // add points to lower section total
            card.scores[17] += total;
          } 
        }
      }
      // make the score whatever the point are. 
      card.scores[15] = total;


      // reset score, as bonus rules are just bonus, and otherwise, could affect the result of other rolls. 
      total = -1;


      // the following categories are for single number and upper section scoring
      // for each category, take the amount of whatever number from the freqList, and multiply by that number to get the points


      // Alex Yang did this part

    
      if (option == "Aces") {
        total = freqList[0];
        // the points is just the number of 1's
        // add to score and lower section total.
        card.scores[1] = total;
        card.scores[16] += total;
      } else if (option == "Twos") {
        // the points is the number of 2's multiplied by 2
        // add to score and lower section total.
        total = freqList[1] * 2;
        card.scores[2] = total;
        card.scores[16] += total;
      } else if (option == "Threes") {
        total = freqList[2] * 3;
        // add to score and lower section total.
        card.scores[3] = total;
        card.scores[16] += total;
      } else if (option == "Fours") {
        total = freqList[3] * 4;
        // add to score and lower section total.
        card.scores[4] = total;
        card.scores[16] += total;
      } else if (option == "Fives") {
        total = freqList[4] * 5;
        // add to score and lower section total.
        card.scores[5] = total;
        card.scores[16] += total;
      } else if (option == "Sixes") {
        total = freqList[5] * 6;
        // add to score and lower section total.
        card.scores[6] = total;
        card.scores[16] += total;




        // Alex Xiang did the rest of the program

        
      } else if (option == "3 of a kind") {
        // if it is 3 of a kind, use the frequency list to see if there is an index with value 3. If so, that means that one of the rolls had to have shown up 3 times
        int sum = 0;
        // sum is the sum of the dice faces, as that's the score for 3 of a kind
        boolean good = false;
        // good is a boolean to see if 3 of a kind is valid
        for (int i = 0; i < 6; i++) {
          // if the value is >= 3, it means it was rolled at least 3 times, and 3 of a kind is valid
          if (freqList[i] >= 3) {
            good = true;
          }
          sum += freqList[i] * (i+1);
        }
        // if 3 of a kind is valid, add to scoring category
        if (good) {
          total = sum;
          card.scores[17] += total;
        }
        card.scores[8] = total;
      } else if (option == "4 of a kind") {
        // 4 of a kind acts the same way, but checks for >= 4 instead of >= 3
        int sum = 0;
        boolean good = false;
        for (int i = 0; i < 6; i++) {
          // check if the frequency of something is >= 4
          if (freqList[i] >= 4) {
            good = true;
          }
          sum += freqList[i] * (i+1);
        }
        if (good){
          total = sum;
          card.scores[17] += total;
        } 
        card.scores[9] = total;
      } else if (option == "Full House") {

        // booleans to represent the full house, as there needs to 3 of some number and 2 of some other number.
        boolean three = false;
        boolean two = false;
        for (int i = 0; i < 6; i++) {
          // check frequencies to be either 2 or 3
          if (freqList[i] == 3) {
            three = true;
          } else if (freqList[i] == 2) {
            two = true;
          }
        }
        // if both booleans are satisfied, it is valid.
        if (three && two) {
          total = 25;
          card.scores[17] += total;
        }
        card.scores[10] = total;
      } else if (option == "Sm Straight") {
        // small straight is 4 in a row, so we simply calculate a streak of at least 4 frequencies 
        int streak = 0;
        boolean good = false;

        for (int i = 0; i < 6; i++) {
          // if the frequency of the value is 0, the streak is broken
          if (freqList[i] == 0) {
            streak = 0;
          } else {
            // otherwise, add 1 to the current streak and check if the streak is at least 4, in which case small straight is satisfied
            streak++;
            if (streak >= 4) good = true;
          }
        }
        // check if small straight is valid
        if (good) {
          total = 30; 
          card.scores[17] += total;
        }
        card.scores[11] = total;
      } else if (option == "Lg Straight") {
        // large straight is the same thing as small straight, but the streak must be 5 instead of 4
        int streak = 0;
        boolean good = false;

        for (int i = 0; i < 6; i++) {
          if (freqList[i] == 0) {
            streak = 0;
          } else {
            streak++;
            // check if streak is >= 5
            if (streak >= 5) good = true;
          }
        }
        if (good) {
          total = 40; 
          card.scores[17] += total;
        }
        card.scores[12] = total;

      } else if (option == "YAHTZEE") {
        // Yahtzee is valid if there is a number with 5 rolls
        for (int i = 0; i < 6; i++) {
          // check if the frequency of some number is 5, if so, it is Yahtzee
          if (freqList[i] == 5) {
            // score becomes 50 points
            total = 50; 
            card.scores[17] += total;
          } 
        }
        card.scores[13] = total;
      } else if (option == "Chance") {
        // chance is simply the sum of the dice faces
        total = 0;
        for (int i = 0; i < 6; i++) {
          total += freqList[i] * (i+1);
        }
        // add to total
        card.scores[17] += total;
        card.scores[14] = total;
      } 


      // if the upper section is >= 63, the player gets 35 point bonus
      if (card.scores[16] >= 63) card.scores[16] += 35;

      // add upper and lower section to get grand total
      card.scores[18] = card.scores[16] + card.scores[17];

      // display the card at the end
      displayCard(card);
    } 
  }
}
