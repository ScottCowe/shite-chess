package io.github.scottcowe.chess.uci;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UCI {
  private List<UCICommand> commands = new ArrayList<UCICommand>():

  public void recieveInput(String input) {
    String[] split = input.trim().split("\\s+");

    UCICommand command = null;
    int argIndex = 0;
    
    for (int i = 0; i < split.length; i++) {
      UCICommand cmd = this.getCommandByName(split[i]);

      if (cmd != null) {
        command = cmd;
        argIndex = i + 1;
        break;
      }
    }

    if (command == null) {
      return;
    }

    String[] requiredArgs = cmd.getArgs();
    String[] optionalArgs = cmd.getOptionalArgs();
    List<String> allArgs = new ArrayList<String>();
    allArgs.addAll(Arrays.asList(requiredArgs));
    allArgs.addAll(Arrays.asList(optionalArgs));

    HashMap<String, String> args = new HashMap<String, String>();

    // until next arg (optional or otherwise) is found
    for (int i = argIndex; i < split.length; i++) {
      if (!allArgs.contains(split[i])) {
        continue; 
      }


    }
  }

  public void sendOutput(String output) {
  
  }

  public void recieveCommand(UCICommand cmd) {

  }

  public void init() {

  }

  public UCICommand getCommandByName(String name) {
    for (UCICommand cmd : this.commands) {
      if (cmd.getName() == name) {
        return cmd;
      }
    }

    return null;
  }
}
