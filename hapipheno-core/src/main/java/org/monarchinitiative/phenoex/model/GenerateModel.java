package org.monarchinitiative.phenoex.model;


import com.essaid.model.dsl.Main;

public class GenerateModel {

  public static void main(String[] args) {
    String[] mainArgs = new String[]{"--dsl-source",
        "src/main/java/org/monarchinitiative/phenoex/model/model.modeldsl", "--out-dir",
        "src-gen/main/java"};
    Main.main(mainArgs);
  }
}
