package io.michelfaria.chrono.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

public class XboxController360 {
	public static final int BUTTON_X = 2, BUTTON_Y = 3, BUTTON_A = 0, BUTTON_B = 1, BUTTON_BACK = 6, BUTTON_START = 7,
			BUTTON_LB = 4, BUTTON_L3 = 8, BUTTON_RB = 5, BUTTON_R3 = 9, AXIS_LEFT_X = 1, // -1 is left | +1 is right
			AXIS_LEFT_Y = 0, // -1 is up | +1 is down
			AXIS_LEFT_TRIGGER = 4, // value 0 to 1f
			AXIS_RIGHT_X = 3, // -1 is left | +1 is right
			AXIS_RIGHT_Y = 2, // -1 is up | +1 is down
			AXIS_RIGHT_TRIGGER = 4; // value 0 to -1f

	public static final PovDirection BUTTON_DPAD_UP = PovDirection.north, BUTTON_DPAD_DOWN = PovDirection.south,
			BUTTON_DPAD_RIGHT = PovDirection.east, BUTTON_DPAD_LEFT = PovDirection.west;

	public static boolean isButtonPressed(Controller controller, Buttons button) {
		switch (button) {
		case A:
			return controller.getButton(XboxController360.BUTTON_A);
		case B:
			return controller.getButton(XboxController360.BUTTON_B);
		case X:
			return controller.getButton(XboxController360.BUTTON_X);
		case Y:
			return controller.getButton(XboxController360.BUTTON_Y);
		case DPAD_UP:
			return controller.getAxis(XboxController360.AXIS_LEFT_Y) < -0.5;
		case DPAD_DOWN:
			return controller.getAxis(XboxController360.AXIS_LEFT_Y) > 0.5;
		case DPAD_LEFT:
			return controller.getAxis(XboxController360.AXIS_LEFT_X) < -0.5;
		case DPAD_RIGHT:
			return controller.getAxis(XboxController360.AXIS_LEFT_X) > 0.5;
		default:
			throw new RuntimeException("Unknown button: " + button);
		}
	}
}