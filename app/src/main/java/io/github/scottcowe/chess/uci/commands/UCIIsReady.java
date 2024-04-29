package io.github.scottcowe.chess.uci.commands;

import io.github.scottcowe.chess.uci.*;

import java.util.HashMap;

public class UCIIsReady extends UCICommand {
  public UCIIsReady() {
    super("isready", new String[0], new String[0]);
  }

  @Override
  public void doStuff(HashMap<String, String> args) {
    UCI uci = UCI.getInstance(); 
    uci.sendOutput("readyok");
  }
}
