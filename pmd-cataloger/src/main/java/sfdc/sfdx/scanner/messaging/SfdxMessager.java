package sfdc.sfdx.scanner.messaging;

import com.google.gson.Gson;
import java.time.Instant;
import java.util.List;

import sfdc.sfdx.scanner.EventKey;

enum MessageType {
  WARNING
}

enum MessageHandler {
  UX
}

public class SfdxMessager {
  // The START string gives us something to scan for when we're processing output.
  private static final String START = "SFDX-START";
  // The END string lets us know when a message stops, which should prevent bugs involving multi-line output.
  private static final String END = "SFDX-END";

  private static SfdxMessager INSTANCE = null;

  public static SfdxMessager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SfdxMessager();
    }
    return INSTANCE;
  }

  public void uxWarn(EventKey key, List<String> args) {
    uxWarn(key, args, false);
  }

  public void uxWarn(EventKey key, List<String> args, boolean verbose) {
    System.err.println(formatMessage(key, args, MessageType.WARNING, MessageHandler.UX, verbose));
  }

  private String formatMessage(EventKey key, List<String> args, MessageType type, MessageHandler handler, boolean verbose) {
    // A message is created by serializing an SfdxMessage instance and sandwiching it between the START and END strings.
    return START + new SfdxMessage(key, args, type, handler, verbose).toJson() + END;
  }
}

class SfdxMessage {
  private EventKey key;
  private List<String> args;
  private MessageType type;
  private MessageHandler handler;
  private boolean verbose;
  private long time;

  SfdxMessage(EventKey key, List<String> args, MessageType type, MessageHandler handler, boolean verbose) {
    this.key = key;
    this.args = args;
    this.type = type;
    this.handler = handler;
    this.time = Instant.now().toEpochMilli();
    this.verbose = verbose;
  }

  String toJson() {
    return new Gson().toJson(this);
  }
}
