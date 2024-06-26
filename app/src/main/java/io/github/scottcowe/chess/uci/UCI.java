package io.github.scottcowe.chess.uci;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import io.github.scottcowe.chess.uci.commands.*;
import io.github.scottcowe.chess.bot.*;

public class UCI implements Runnable {
  private static final UCI INSTANCE;
  private Bot bot; 

  private List<UCICommand> commands = new ArrayList<UCICommand>();

  public void run() {
    // Init
    commands.add(new UCICmd());
    commands.add(new UCIIsReady());

    Scanner scanner = new Scanner(System.in); 

    System.out.println("awaiting");

    while(true) {
      String input = scanner.nextLine();
      this.recieveInput(input);
    }
  }

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

    String[] requiredArgs = command.getArgs();
    String[] optionalArgs = command.getOptionalArgs();
    List<String> allArgs = new ArrayList<String>();
    allArgs.addAll(Arrays.asList(requiredArgs));
    allArgs.addAll(Arrays.asList(optionalArgs));

    HashMap<String, String> args = new HashMap<String, String>();

    // until next arg (optional or otherwise) is found
    for (int i = argIndex; i < split.length; i++) {
      if (!allArgs.contains(split[i])) {
        continue; 
      }

      String argName = split[i];
      String arg = "";

      int j = i + 1;

      while (!allArgs.contains(split[j])) {
        arg += split[j] + " ";
        j += 1;
      }

      args.put(argName, arg);
    }

    command.doStuff(args);
  }

  public void sendOutput(String output) {
    System.out.println(output); 
  }

  public UCICommand getCommandByName(String name) {
    for (UCICommand cmd : this.commands) {
      if (cmd.getName().equals(name)) {
        return cmd;
      }
    }

    return null;
  }

  public static UCI getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new UCI();
    }

    return INSTANCE;
  }

  public void setBot(Bot bot) {
    this.bot = bot;
  }
}
