package com.hangon.common;

import android.hardware.Camera;

import com.hangon.outer.zxing.CameraManager;


/**
 *
 * @author zzl 闪光灯控制类，用于闪光灯的开启与关闭
 *
 */
public class LightControl {
	boolean m_isOn;
	Camera m_Camera;

	public boolean getIsOn() {
		return m_isOn;
	}

	public LightControl() {
		m_isOn = false;
	}

	public void turnOn() {

		try {
			// m_Camera = Camera.open();
			m_Camera = CameraManager.get().getCamera();
			Camera.Parameters mParameters;
			mParameters = m_Camera.getParameters();
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			m_Camera.setParameters(mParameters);
		} catch (Exception ex) {
		}

	}

	public void turnOff() {

		try {
			m_Camera = CameraManager.get().getCamera();
			Camera.Parameters mParameters;
			mParameters = m_Camera.getParameters();
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			m_Camera.setParameters(mParameters);
			//m_Camera.stopPreview();
			//m_Camera.release();
		} catch (Exception ex) {
		}

	}
}