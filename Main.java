// By Alex Xiang & Alex Yang

import javax.swing.*;
import java.util.*;

class Main {
  public static void main(String[] args) {
    //Default code for main method for GUI
    
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
    public void run() {
       //Runs GUI
       YahtzeeGUI.runGUI();
      }
    });
    
  }
}
