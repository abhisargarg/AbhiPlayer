package com.example.abhiplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {
	
	

	private MediaPlayer player;
	private ArrayList<Song> songs;
	private int songPosn;
	private final IBinder musicBind = new MusicBinder();
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		 return musicBind;
	}
	
	@Override
	public boolean onUnbind(Intent intent){
	  player.stop();
	  player.release();
	  return false;
	}

	public void onCreate(){
		  
		super.onCreate();
		//initialize position
		songPosn=0;
		//create player
		player = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
		
		initMusicPlayer();
		}
	
	public void initMusicPlayer(){
		  //set player properties
		player.setWakeMode(getApplicationContext(),
				  PowerManager.PARTIAL_WAKE_LOCK);
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				
				
		}
	
	public void setList(ArrayList<Song> theSongs){
		  songs=theSongs;
		}
	
	public class MusicBinder extends Binder {
		  MusicService getService() {
		    return MusicService.this;
		  }
		}
	
	public void setSong(int songIndex){
		  songPosn=songIndex;
		}
	
	
	
	public void playSong(){
		  //play a song
		player.reset();
		//get song
		Song playSong = songs.get(songPosn);
		//get id
		long currSong = playSong.getID();
		//set uri
		Uri trackUri = ContentUris.withAppendedId(
		  android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		  currSong);
		
		try{
			  player.setDataSource(getApplicationContext(), trackUri);
			}
			catch(Exception e){
			  Log.e("MUSIC SERVICE", "Error setting data source", e);
			}
		
		player.prepareAsync();
		}
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if(player.getCurrentPosition()==0){
		    mp.reset();
		    playNext();
		  }
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
		
	}
	
	public int getPosn(){
		  return player.getCurrentPosition();
		}
		 
		public int getDur(){
		  return player.getDuration();
		}
		 
		public boolean isPng(){
		  return player.isPlaying();
		}
		 
		public void pausePlayer(){
		  player.pause();
		}
		 
		public void seek(int posn){
		  player.seekTo(posn);
		}
		 
		public void go(){
		  player.start();
		}
		
		public void playPrev(){
			  songPosn--;
			  if(songPosn==0) songPosn=songs.size()-1;
			  playSong();
			}
		
		//skip to next
		public void playNext(){
		  songPosn++;
		  if(songPosn==songs.size()) songPosn=0;
		  playSong();
		}

}
