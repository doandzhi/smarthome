/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.binding.pxfplugin.handler;

import static org.eclipse.smarthome.binding.pxfplugin.PXFpluginBindingConstants.CHANNEL_1;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.binding.pxfplugin.internal.PXFpluginConfiguration;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.io.transport.mqtt.MqttException;
import org.eclipse.smarthome.io.transport.mqtt.MqttService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PXFpluginHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Erdo - Initial contribution
 */
public class PXFpluginHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(PXFpluginHandler.class);
    private static MqttService mqttService;

    @Nullable
    private PXFpluginConfiguration config;

    public PXFpluginHandler(Thing thing) {
        super(thing);
    }

    @SuppressWarnings("null")
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (channelUID.getId().equals(CHANNEL_1)) {
            if (command.equals(OnOffType.ON)) {
                try {
                    mqttService.getBrokerConnection("mosquitto").publish("pxf/switch", "---- switch is ON".getBytes(),
                            null);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    mqttService.getBrokerConnection("mosquitto").publish("pxf/switch", "---- switch is OFF".getBytes(),
                            null);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void bindMqttService(MqttService mqttService) {
        PXFpluginHandler.mqttService = mqttService;
    }

    @Override
    public void initialize() {
        config = getConfigAs(PXFpluginConfiguration.class);
        updateStatus(ThingStatus.ONLINE);
    }
}
