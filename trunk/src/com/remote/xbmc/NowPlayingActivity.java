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

package com.remote.xbmc;

import org.xbmc.httpapi.MediaControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class NowPlayingActivity extends Activity {
	private MediaControl control;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nowplaying);

  	  	control = XBMCControl.getInstance().getMediaControls();
  	  	
  	  	final SeekBar seekBar = (SeekBar) findViewById(R.id.NowPlayingProgress);
  	  	int progress = control.getPercentage();
  	  	seekBar.setProgress(progress);
  	  	
        final ImageButton PlayPrevButton = (ImageButton) findViewById(R.id.MediaPreviousButton);
		PlayPrevButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				control.playPrevious();
			}
		});
		final ImageButton PlayButton = (ImageButton) findViewById(R.id.MediaPlayPauseButton);
		PlayButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				control.pause();
			}
		});
		
		final ImageButton PlayNextButton = (ImageButton) findViewById(R.id.MediaNextButton);
		PlayNextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				control.playNext();
			}
		});
	}
}
