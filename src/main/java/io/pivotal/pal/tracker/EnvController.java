package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
public class EnvController {

  private final String port;
  private final String memoryLimit;
  private final String cfInstanceIndex;
  private final String cfInstanceAddress;

  private Map<String, String> envMap;

  //Test Constructor
  public EnvController(@Value("${PORT:NOT SET}") String port, @Value("${MEMORY_LIMIT: NOT SET}") String memoryLimit,
    @Value("${CF_INSTANCE_INDEX:NOT SET}") String cfInstanceIndex, @Value("${CF_INSTANCE_ADDR: NOT SET}") String cfInstanceAddress) {
    this.port = port;
    this.memoryLimit = memoryLimit;
    this.cfInstanceIndex = cfInstanceIndex;
    this.cfInstanceAddress = cfInstanceAddress;

    envMap = new HashMap<>();
    envMap.put("PORT", port);
    envMap.put("MEMORY_LIMIT", memoryLimit);
    envMap.put("CF_INSTANCE_INDEX", cfInstanceIndex);
    envMap.put("CF_INSTANCE_ADDR", cfInstanceAddress);
  }

  @GetMapping("/env")
  public Map<String, String> getEnv() {
    Set<String> keys = envMap.keySet();
    for (String key : keys) {
      System.out.println("key:" + key + " " + envMap.get(key));
    }
    return envMap;
  }
}
