package com.gcs.game.vo;

public enum ServiceState {
    /**
     * OFFLINE - Reject all request until this service is set to MAINTENANCE.
     */
    OFFLINE(0),
    /**
     * MAINTENANCE - No Bet and no new game session is allowed. Management call are still available.
     */
    MAINTENANCE(-2),
    /**
     * PREPARE_FOR_MAINTENANCE - No new game session is allowed. No new bet can be placed. Ongoing bet can still continue.
     */
    PREPARE_FOR_MAINTENANCE(-1),
    /**
     * ONLINE - All kind of action allowed.
     */
    ONLINE(1);

    public int value;

    ServiceState(int v) { value = v;}

    public static ServiceState parse(int v){
        for (ServiceState value : ServiceState.values()) {
            if(v == value.value) return value;
        }
        return null;
    }
}
