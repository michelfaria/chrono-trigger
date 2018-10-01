package io.michelfaria.chrono.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public final class Ctrl {

    public Ctrl() {
    }

    public static boolean isButtonPressed(int controllerNumber, Buttons button) {
        if (Controllers.getControllers().size <= controllerNumber) {
            // No controller - use keyboard
            return Keyboard.isButtonPressed(button);
        } else {
            final Controller controller = Controllers.getControllers().get(controllerNumber);
            final ControllerType type = controllerType(controller);
            if (type == null) {
                throw new RuntimeException("Unknown controller type");
            }
            switch (type) {
                case XBOX360:
                    return XboxController360.isButtonPressed(controller, button);
            }
        }
        return false;
    }

    private static ControllerType controllerType(Controller controller) {
        if (controller.getName().contains("360")) {
            return ControllerType.XBOX360;
        } else {
            return null;
        }
    }
}
