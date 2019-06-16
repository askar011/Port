package com.askar.port.controller;

import com.askar.port.entity.Port;
import com.askar.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class DeliveryController {

    private static final Logger LOGGER = LogManager.getLogger(DeliveryController.class);

    public void deliverToPort(Port port, Ship ship) {
        try {
            TimeUnit.SECONDS.sleep(ship.getWaitTime());
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        LOGGER.info(ship.getModel() + " delivering containers to port");
        port.loadContainers(ship.getContainerCount());
        ship.deliverContainers(ship.getContainerCount());
        LOGGER.info("containers in" + ship.getModel() + ":" + ship.getContainerCount());
        LOGGER.info("port" + port.getContainerCount());
        LOGGER.info("Finished delivering to port " + ship.getModel());
    }
}
