/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

public class XboxController360 {
	public static final int BUTTON_X = 2;
	public static final int BUTTON_Y = 3;
	public static final int BUTTON_A = 0;
	public static final int BUTTON_B = 1;
	public static final int BUTTON_BACK = 6;
	public static final int BUTTON_START = 7;
	public static final int BUTTON_LB = 4;
	public static final int BUTTON_L3 = 8;
	public static final int BUTTON_RB = 5;
	public static final int BUTTON_R3 = 9;
	public static final int AXIS_LEFT_X = 1; // -1 is left | +1 is right
	public static final int AXIS_LEFT_Y = 0; // -1 is up | +1 is down
	public static final int AXIS_LEFT_TRIGGER = 4; // value 0 to 1f
	public static final int AXIS_RIGHT_X = 3; // -1 is left | +1 is right
	public static final int AXIS_RIGHT_Y = 2; // -1 is up | +1 is down
	public static final int AXIS_RIGHT_TRIGGER = 4; // value 0 to -1f
	public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
	public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
	public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
	public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;

	public static boolean isButtonPressed(Controller ctrl, Buttons button) {
		switch (button) {
		case A:
			return ctrl.getButton(XboxController360.BUTTON_A);
		case B:
			return ctrl.getButton(XboxController360.BUTTON_B);
		case X:
			return ctrl.getButton(XboxController360.BUTTON_X);
		case Y:
			return ctrl.getButton(XboxController360.BUTTON_Y);
		case DPAD_UP:
			return ctrl.getAxis(XboxController360.AXIS_LEFT_Y) < -0.5;
		case DPAD_DOWN:
			return ctrl.getAxis(XboxController360.AXIS_LEFT_Y) > 0.5;
		case DPAD_LEFT:
			return ctrl.getAxis(XboxController360.AXIS_LEFT_X) < -0.5;
		case DPAD_RIGHT:
			return ctrl.getAxis(XboxController360.AXIS_LEFT_X) > 0.5;
		default:
			throw new RuntimeException("Unknown button: " + button);
		}
	}
}