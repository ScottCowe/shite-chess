package io.github.scottcowe.chess.uci;

import java.util.HashMap;

public class UCICommand {
  private String name;
  private String[] argNames;
  private String[] optionalArgNames;

  public UCICommand(String name, String[] argNames, String[] optionalArgNames) {
    this.name = name;
    this.argNames = argNames;
    this.optionalArgNames = optionalArgNames;
  }

  public void doStuff(HashMap<String, String> args) {

  }

  public String getName() {
    return this.name;
  }

  public String[] getArgs() {
    return this.args;
  }

  public String[] getOptionalArgs() {
    return this.optionalArgs;
  }
}
