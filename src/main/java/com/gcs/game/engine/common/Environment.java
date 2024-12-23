package com.gcs.game.engine.common;

import com.gcs.game.exception.ServiceInMaintenanceException;
import com.gcs.game.exception.ServiceOfflineException;
import com.gcs.game.vo.ServiceState;

public class Environment {

    private Environment(){}

    /**
     * Service State
     */
    private static ServiceState serviceState;

    public static ServiceState getServiceState() {
        return serviceState;
    }

    public static void setServiceState(ServiceState serviceState) {
        Environment.serviceState = serviceState;
    }

    public static void checkOffline() throws ServiceOfflineException {
        if(ServiceState.OFFLINE.equals(Environment.getServiceState())) throw new ServiceOfflineException();
    }

    public static void checkContinueBetAllowed() throws ServiceInMaintenanceException, ServiceOfflineException {
        checkOffline();
        if(ServiceState.MAINTENANCE.equals(Environment.getServiceState())) throw new ServiceInMaintenanceException();
    }

    public static void checkNewBetAllowed() throws ServiceInMaintenanceException, ServiceOfflineException {
        checkContinueBetAllowed();
        if(ServiceState.PREPARE_FOR_MAINTENANCE.equals(Environment.getServiceState())) throw new ServiceInMaintenanceException();
    }
}
