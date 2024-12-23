package com.gcs.game.engine;


import com.gcs.game.engine.keno.model.BaseKenoModel;
import com.gcs.game.engine.math.model1010802.Model1010802;
import com.gcs.game.engine.math.model1260130.Model1260130;
import com.gcs.game.engine.math.model1260130.Model1260130Bonus;
import com.gcs.game.engine.math.model5070530.Model5070530;
import com.gcs.game.engine.math.model6060630.Model6060630;
import com.gcs.game.engine.math.model6060630.Model6060630Bonus;
import com.gcs.game.engine.math.model6080630.Model6080630;
import com.gcs.game.engine.math.model6080630.Model6080630Bonus;
import com.gcs.game.engine.math.model8140802.Model8140802;
import com.gcs.game.engine.math.modelGCBJ00101.ModelGCBJ00101;
import com.gcs.game.engine.blackJack.model.BaseBlackJackModel;
import com.gcs.game.engine.math.modelGCBJ00102.ModelGCBJ00102;
import com.gcs.game.engine.poker.bonus.PokerBonus;
import com.gcs.game.engine.poker.model.BasePokerModel;
import com.gcs.game.engine.slots.bonus.BaseBonus;
import com.gcs.game.engine.slots.model.BaseSlotModel;
import com.gcs.game.utils.SimulationConfReader;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class GameModelFactory {
    private static GameModelFactory instance = null;

    public static GameModelFactory getInstance() {
        if (GameModelFactory.instance == null) {
            String className = SimulationConfReader.getStringValue(SimulationConfReader.KEY_CLASS_PATH_MODEL_FACTORY);
            if (className != null) {
                try {
                    Class c1 = Class.forName(className);
                    GameModelFactory.instance = (GameModelFactory) c1.getConstructor().newInstance();
                } catch (ClassNotFoundException e) {
                    log.error("", e);
                } catch (IllegalAccessException e) {
                    log.error("", e);
                } catch (InstantiationException e) {
                    log.error("", e);
                } catch (NoSuchMethodException e) {
                    log.error("", e);
                } catch (InvocationTargetException e) {
                    log.error("", e);
                }
            }
        }
        if (GameModelFactory.instance == null) {
            GameModelFactory.instance = new GameModelFactory();
        }
        return GameModelFactory.instance;
    }

    public BaseBlackJackModel getBlackJackModel(String mathModel) {
        BaseBlackJackModel model = null;
        switch (mathModel) {
            case "GCBJ00101":
                model = new ModelGCBJ00101();
                break;
            case "GCBJ00102":
                model = new ModelGCBJ00102();
                break;
            default:
                break;
        }
        return model;
    }

    public BasePokerModel getPokerModel(String mathModel) {
        BasePokerModel model = null;
        switch (mathModel) {
            case "6080630":
                model = new Model6080630();
                break;
            case "6060630":
                model = new Model6060630();
                break;
            default:
                break;
        }
        return model;
    }


    public BaseSlotModel getSlotsModel(String mathModel) {
        BaseSlotModel model = null;
        switch (mathModel) {
            case "8140802":
                model = new Model8140802();
                break;
            case "1260130":
                model = new Model1260130();
                break;
            case "1010802":
                model = new Model1010802();
                break;
            default:
                break;
        }
        return model;
    }

    public BaseBonus getSlotsBonusModel(String gameModel, String bonusAsset) {
        BaseBonus model = null;
        switch (gameModel) {
            case "1260130":
                model = new Model1260130Bonus();
                break;
            default:
                break;
        }
        return model;
    }


    public PokerBonus getPokerBonusModel(String gameModel, String bonusAsset) {
        PokerBonus bonusModel = null;
        switch (gameModel) {
            case "6080630":
                bonusModel = new Model6080630Bonus();
                break;
            case "6060630":
                bonusModel = new Model6060630Bonus();
                break;
            default:
                break;
        }
        return bonusModel;
    }

    public BaseKenoModel getKenoModel(String mathModel) {
        BaseKenoModel model = null;
        switch (mathModel) {
            case "5070530":
                model = new Model5070530();
                break;
            default:
                break;
        }
        return model;
    }
}
