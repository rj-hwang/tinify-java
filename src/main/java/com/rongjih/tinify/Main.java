package com.rongjih.tinify;

import com.tinify.Tinify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author RJ
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    Tinify.setKey("YOUR_API_KEY");

    //logger.debug("the number of compressions this month = {}", Tinify.compressionCount());

    File fromDir = new File("H:\\180228T2BAK\\RJ-Hwang\\Picture\\我的结婚照");
    File toDir = new File("H:\\180228T2BAK\\RJ-Hwang-Converted\\Picture\\我的结婚照");
    int start = 99; // 99/372 IMG_6136.jpg

    // verify
    if (!verify(fromDir, toDir)) return;

    // find pictures
    String[] pictures_ = fromDir.list((dir, name) -> {
      String n = name.toLowerCase();
      return n.endsWith(".png") || n.endsWith(".apng") || n.endsWith(".jpg") || n.endsWith(".jpeg");
    });
    if (pictures_ == null || pictures_.length == 0) {
      logger.warn("no pictures.");
      return;
    } else {
      logger.warn("total picture count is {}", pictures_.length);
    }
    List<String> pictures = Arrays.asList(pictures_);
    Collections.sort(pictures);
    int total = pictures.size();

    // do convert
    int i = 0;
    for (String name : pictures) {
      i++;
      if (i < start) continue;
      logger.info("converting {}/{} {} to {}", i, total, name, toDir);
      convertOne(new File(fromDir, name), new File(toDir, name));
    }
  }

  private static void convertOne(File source, File target) throws IOException {
    Tinify.fromFile(source.getAbsolutePath()).toFile(target.getAbsolutePath());
  }

  private static boolean verify(File fromDir, File toDir) {
    if (!fromDir.exists() && !fromDir.isDirectory()) {
      logger.warn("fromDir is not a exists directory: fromDir={}", fromDir);
      return false;
    }
    if (!toDir.exists() && !toDir.isDirectory()) {
      logger.warn("toDir is not a exists directory: toDir={}", toDir);
      return false;
    }
    if (fromDir.equals(toDir)) {
      logger.warn("fromDir must not equals toDir: fromDir=toDir={}", fromDir);
      return false;
    }
    return true;
  }
}
