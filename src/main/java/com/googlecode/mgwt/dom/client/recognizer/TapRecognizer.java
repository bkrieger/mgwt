/*
 * Copyright 2012 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.mgwt.dom.client.recognizer;

import com.google.gwt.event.shared.HasHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;

public class TapRecognizer implements TouchHandler {

	public static final int DEFAULT_DISTANCE = 15;

	private final int distance;

	private boolean touchCanceled;

	private boolean hasMoved;

	private int start_x;

	private int start_y;

	private final HasHandlers source;

	public TapRecognizer(HasHandlers source) {
		this(source, DEFAULT_DISTANCE);
	}

	public TapRecognizer(HasHandlers source, int distance) {
		if (source == null)
			throw new IllegalArgumentException("source can not be null");
		if (distance < 0)
			throw new IllegalArgumentException("distance has to be greater than zero");
		this.source = source;
		this.distance = distance;

	}

	@Override
	public void onTouchStart(TouchStartEvent event) {
		touchCanceled = false;
		hasMoved = false;
		start_x = event.getTouches().get(0).getPageX();
		start_y = event.getTouches().get(0).getPageY();

	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		Touch touch = event.getTouches().get(0);
		if (Math.abs(touch.getPageX() - start_x) > distance || Math.abs(touch.getPageY() - start_y) > distance) {
			hasMoved = true;
		}

	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		if (!hasMoved && !touchCanceled) {
			TapEvent tapEvent = new TapEvent(source, start_x, start_y);
			source.fireEvent(tapEvent);
		}

	}

	@Override
	public void onTouchCanceled(TouchCancelEvent event) {
		touchCanceled = true;

	}

	public int getDistance() {
		return distance;
	}

}