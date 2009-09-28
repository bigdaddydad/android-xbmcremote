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

package org.xbmc.android.remote.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xbmc.android.remote.R;
import org.xbmc.android.util.ConnectionManager;
import org.xbmc.android.util.ErrorHandler;
import org.xbmc.httpapi.HttpClient;
import org.xbmc.httpapi.data.MediaLocation;
import org.xbmc.httpapi.type.MediaType;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MediaListActivity extends ListActivity implements Callback, Runnable {
	private final Stack<String> mHistory = new Stack<String>();;
	private final HttpClient mClient = ConnectionManager.getHttpClient(this);

	private List<MediaLocation> fileItems;
	private String currentUrl, gettingUrl;
	private MediaType mMediaType;
	private Handler mediaListHandler;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mediaListHandler = new Handler(this);
		ErrorHandler.setActivity(this);
		final String st = getIntent().getStringExtra("shareType");
		mMediaType = st != null ? MediaType.valueOf(st) : MediaType.music;
		fillUp("");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (fileItems == null)
			return;

		MediaLocation item = fileItems.get(position);
		if (item.isDirectory) {
			mHistory.add(currentUrl);
			fillUp(item.path);
		} else {
			if (mClient.control.playFile(item.path)) {
				startActivityForResult(new Intent(v.getContext(), NowPlayingActivity.class), 0);
			}
		}
	}

	private void fillUp(String url) {
		if (gettingUrl != null)
			return;
		
		gettingUrl = url;
		fileItems = null;
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{ "Loading..." }));
		getListView().setTextFilterEnabled(false);
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !mHistory.isEmpty()) {
			fillUp(mHistory.pop());
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (mMediaType.equals(MediaType.music))
			menu.add(0, 0, 0, "Change view").setIcon(android.R.drawable.ic_menu_view);
		if (!mMediaType.equals(MediaType.music))
			menu.add(0, 1, 0, "Music");
		if (!mMediaType.equals(MediaType.video))
			menu.add(0, 2, 0, "Video");
		if (!mMediaType.equals(MediaType.pictures))
			menu.add(0, 3, 0, "Pictures").setIcon(android.R.drawable.ic_menu_camera);
		
		menu.add(0, 4, 0, "Now Playing").setIcon(android.R.drawable.ic_media_play);
		menu.add(0, 5, 0, "Remote");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent = null;
		
		switch (item.getItemId()) {
		case 0:
			myIntent = new Intent(this, AlbumGridActivity.class);
			break;
		case 1:
			myIntent = new Intent(this, MediaListActivity.class);
			myIntent.putExtra("shareType", MediaType.music.toString());
			break;
		case 2:
			myIntent = new Intent(this, MediaListActivity.class);
			myIntent.putExtra("shareType", MediaType.video.toString());
			break;
		case 3:
			myIntent = new Intent(this, MediaListActivity.class);
			myIntent.putExtra("shareType", MediaType.pictures.toString());
			break;
		case 4:
			myIntent = new Intent(this, NowPlayingActivity.class);
			break;
		case 5:
			myIntent = new Intent(this, RemoteActivity.class);
			break;
		}
		
		if (myIntent != null) {
			startActivity(myIntent);
			return true;
		}
		return false;
	}

	public boolean handleMessage(Message msg) {
		if (msg.what == 1) {
			Bundle data = msg.getData();
			
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data.getStringArrayList("items")));
			//TODO Bug in our getListItem code, if filtered the numbers are really wrong.
			getListView().setTextFilterEnabled(false);
			
			currentUrl = gettingUrl;
			gettingUrl = null;
			return true;
		}
		return false;
	}

	public void run() {
		if (gettingUrl.length() == 0) {
			fileItems = mClient.info.getShares(mMediaType);
		} else {
			fileItems =  mClient.info.getDirectory(gettingUrl);
		}
		
		ArrayList<String> presentationList = new ArrayList<String>();

		for (MediaLocation item : fileItems) {
			presentationList.add(item.name);
		}
		
		Message msg = Message.obtain(mediaListHandler, 1);
		Bundle data = msg.getData();
		data.putStringArrayList("items", presentationList);
		mediaListHandler.sendMessage(msg);
	}
}