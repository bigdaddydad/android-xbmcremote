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

package org.xbmc.api.business;

import org.xbmc.api.data.IControlClient.ICurrentlyPlaying;
import org.xbmc.api.type.SeekType;

import android.content.Context;

/**
 * This is the interface between the presentation layer and the business layer.
 * All the controller of the presentation layer gets to see is this interface.
 *  
 * @author Team XBMC
 */
public interface IControlManager extends IManager {
	
	/**
	 * Starts playing the media file <code>filename</code> .
	 * @param response Response object
	 * @param filename File to play
	 */
	public void playFile(final DataResponse<Boolean> response, final String filename, final Context context);
	
	/**
	 * Start playing the media file at the given URL
	 * @param response Response object
	 * @param url An URL pointing to a supported media file
	 * @return true on success, false otherwise.
	 */
	public void playUrl(final DataResponse<Boolean> response, String url, final Context context);
	
	/**
	 * Plays the next item in the playlist.
	 * @param response Response object
	 * @return true on success, false otherwise.
	 */
	public void playNext(final DataResponse<Boolean> response, final Context context);
	
	/**
	 * Adds a file or folder (<code>fileOrFolder</code> is either a file or a folder) to the current playlist.
	 * @param response Response object
	 * @param fileOrFolder File to play
	 */
	public void addToPlaylist(final DataResponse<Boolean> response, final String fileOrFolder, final Context context);
	
	/**
	 * Seeks to a position. If type is
	 * <ul>
	 * 	<li><code>absolute</code> - Sets the playing position of the currently 
	 *		playing media as a percentage of the media's length.</li>
	 *  <li><code>relative</code> - Adds/Subtracts the current percentage on to
	 *		the current position in the song</li>
	 * </ul> 
	 * @param response Response object
	 * @param type     Seek type, relative or absolute
	 * @param progress Progress
	 * @return true on success, false otherwise.
	 */
	public void seek(final DataResponse<Boolean> response, SeekType type, int progress, final Context context);
	
	/**
	 * Takes either "video" or "music" as a parameter to begin updating the 
	 * corresponding database.
	 * 
	 * @param response Response object
	 * @param mediaType
	 */
	public void updateLibrary(final DataResponse<Boolean> response, final String mediaType, final Context context);

	/**
	 * Displays the image <code>filename</code> .
	 * @param response Response object
	 * @param filename File to show
	 */
	public void showPicture(final DataResponse<Boolean> response, final String filename, final Context context);
	
	/**
	 * Returns what's currently playing.
	 * @param response Response object
	 */
	public void getCurrentlyPlaying(final DataResponse<ICurrentlyPlaying> response, final Context context);
	
	/**
	 * Returns the current playlist identifier
	 * @param response Response object
	 */
	public void getPlaylistId(final DataResponse<Integer> response, final Context context);
	
	/**
	 * Sets the current playlist identifier
	 * @param response Response object
	 * @param id Playlist identifier
	 */
	public void setPlaylistId(final DataResponse<Boolean> response, final int id, final Context context);
	
	/**
	 * Sets the current playlist position
	 * @param response Response object
	 * @param position New playlist position
	 */
	public void setPlaylistPos(final DataResponse<Boolean> response, final int position, final Context context);
	
}