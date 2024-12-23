package com.gcs.game.vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GameInfo {

    public String formFactor();

    public GameClass mathType();

    public long minBet();

    public long maxBet();

    public long minLine();

    public long maxLine();

    public long reelsCount() default 0;

    public long rowsCount() default 0;

    public GamePayType payType() default GamePayType.TABLE_GAME;

    public long[] betSteps() default {};

    public int[] paybacks() default {};

}
