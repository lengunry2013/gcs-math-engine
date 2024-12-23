package com.gcs.game;

import com.gcs.game.engine.common.Environment;
import com.gcs.game.engine.common.VersionInfo;
import com.gcs.game.engine.common.cache.GameMathCacheStorage;
import com.gcs.game.vo.ServiceState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GcsMathServerApplication {
    public static void main(String[] args) {
        GameMathCacheStorage.getInstance().init();
        VersionInfo versionInfo = new VersionInfo("mathVersionInfo.properties");
        SpringApplication.run(GcsMathServerApplication.class, args);
        //Environment.setServiceState(ServiceState.ONLINE);
    }
}
