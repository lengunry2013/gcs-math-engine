package com.gcs.game.engine;

import com.gcs.game.engine.math.model1010802.Model1010802Engine;
import com.gcs.game.engine.math.model1260130.Model1260130Engine;
import com.gcs.game.engine.math.model20260103.Model20260103Engine;
import com.gcs.game.engine.math.model20260104.Model20260104Engine;
import com.gcs.game.engine.math.model20260201.Model20260201Engine;
import com.gcs.game.engine.math.model20260507.Model20260507Engine;
import com.gcs.game.engine.math.model20260508.Model20260508Engine;
import com.gcs.game.engine.math.model20260520.Model20260520Engine;
import com.gcs.game.engine.math.model20260530.Model20260530Engine;
import com.gcs.game.engine.math.model20260531.Model20260531Engine;
import com.gcs.game.engine.math.model20260618.Model20260618Engine;
import com.gcs.game.engine.math.model20260625.Model20260625Engine;
import com.gcs.game.engine.math.model5070530.Model5070530Engine;
import com.gcs.game.engine.math.model6060630.Model6060630Engine;
import com.gcs.game.engine.math.model6080630.Model6080630Engine;
import com.gcs.game.engine.math.model8140802.Model8140802Engine;
import com.gcs.game.engine.math.modelGCBJ00101.ModelGCBJ00101Engine;
import com.gcs.game.engine.math.modelGCBJ00102.ModelGCBJ00102Engine;

public class GameEngineFactory {

    public static IGameEngine getGameEngine(int payback, String mmID) {
        switch (mmID) {
            case "GCBJ00101":
                return new ModelGCBJ00101Engine(payback, mmID);
            case "GCBJ00102":
                return new ModelGCBJ00102Engine(payback, mmID);
            case "8140802":
                return new Model8140802Engine(payback, mmID);
            case "1260130":
                return new Model1260130Engine(payback, mmID);
            case "1010802":
                return new Model1010802Engine(payback, mmID);
            case "6080630":
                return new Model6080630Engine(payback, mmID);
            case "20260201":
                return new Model20260201Engine(payback, mmID);
            case "20260103":
                return new Model20260103Engine(payback, mmID);
            case "20260104":
                return new Model20260104Engine(payback, mmID);
            case "20260507":
                return new Model20260507Engine(payback, mmID);
            case "20260508":
                return new Model20260508Engine(payback, mmID);
            case "20260520":
                return new Model20260520Engine(payback, mmID);
            case "20260618":
                return new Model20260618Engine(payback, mmID);
            case "20260530":
                return new Model20260530Engine(payback, mmID);
            case "20260531":
                return new Model20260531Engine(payback, mmID);
            case "20260625":
                return new Model20260625Engine(payback, mmID);
            case "6060630":
                return new Model6060630Engine(payback, mmID);
            case "5070530":
                return new Model5070530Engine(payback, mmID);
            default:
                return null;
        }
    }

}
