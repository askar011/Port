package com.askar.port.controller;

import com.askar.port.entity.Port;
import com.askar.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class LoadController {

    private static final Logger LOGGER = LogManager.getLogger(LoadController.class);

    public void loadFromPort(Port port, Ship ship) {
        try {
            TimeUnit.SECONDS.sleep(ship.getWaitTime());
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        port.unloadContainers(ship.getCapacity());
        ship.loadContainers(ship.getCapacity());
        LOGGER.info("Finished loading to ship " + ship.getModel() + ":" + ship.getContainerCount());
        LOGGER.info("portContainer Count" + ":" + port.getContainerCount());
    }
}
