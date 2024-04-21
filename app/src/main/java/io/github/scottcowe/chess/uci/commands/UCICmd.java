package io.github.scottcowe.chess.uci.commands;

import io.github.scottcowe.chess.uci.*;

import java.util.HashMap;

public class UCICmd extends UCICommand {
  public UCICmd() {
    super("uci", new String[0], new String[0]);
  }

  @Override
  public void doStuff(HashMap<String, String> args) {
    UCI uci = UCI.getInstance(); 
    uci.sendOutput("id name shite-chess");
    uci.sendOutput("id author ScottCowe");
    // options
    uci.sendOutput("uciok");
  }
}
