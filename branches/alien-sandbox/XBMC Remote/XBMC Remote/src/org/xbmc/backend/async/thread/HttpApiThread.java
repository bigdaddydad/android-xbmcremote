/*
 *      Copyright (C) 2005-2009 Team XBMC
 *      http://xbmc.org
 *
 *  This Program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2, or (at your option)
 *  any later version.
 *
 *  This Program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XBMC Remote; see the file license.  If not, write to
 *  the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *  http://www.gnu.org/copyleft/gpl.html
 *
 */

package org.xbmc.backend.async.thread;

import org.xbmc.backend.async.wrapper.ControlWrapper;
import org.xbmc.backend.async.wrapper.InfoWrapper;
import org.xbmc.backend.async.wrapper.MusicWrapper;
import org.xbmc.backend.async.wrapper.VideoWrapper;

import android.os.Handler;
import android.os.Looper;

/**
 * Spawned on first access, then looping. Takes all HTTP API commands and
 * synchronously returns the result.
 * 
 * @author Team XBMC
 */
public class HttpApiThread extends Thread {
	private static HttpApiThread sHttpApiThread;
	private final InfoWrapper mInfoWrapper;
	private final ControlWrapper mControlWrapper;
	private final MusicWrapper mMusicWrapper;
	private final VideoWrapper mVideoWrapper;
	private Handler mHandler;
	private HttpApiThread() {
		super("HTTP API Connection Thread");
		mInfoWrapper = new InfoWrapper();
		mControlWrapper = new ControlWrapper();
		mMusicWrapper = new MusicWrapper();
		mVideoWrapper = new VideoWrapper();
	}
	public static HttpApiThread get() {
		if (sHttpApiThread == null) {
			sHttpApiThread = new HttpApiThread();
			sHttpApiThread.start();
			// thread must be entirely started
			while (sHttpApiThread.mHandler == null) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return sHttpApiThread;
	}
	public void run() {
		Looper.prepare();
		mHandler = new Handler();
		mInfoWrapper.setHandler(mHandler);
		mControlWrapper.setHandler(mHandler);
		mMusicWrapper.setHandler(mHandler);
		mVideoWrapper.setHandler(mHandler);
		Looper.loop();
	}
	public static InfoWrapper info() {
		return get().mInfoWrapper;
	}
	public static ControlWrapper control() {
		return get().mControlWrapper;
	}
	public static MusicWrapper music() {
		return get().mMusicWrapper;
	}
	public static VideoWrapper video() {
		return get().mVideoWrapper;
	}
}