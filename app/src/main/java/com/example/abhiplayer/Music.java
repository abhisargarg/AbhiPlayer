package com.example.abhiplayer;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;

import com.example.abhiplayer.MusicService.MusicBinder;

import android.net.Uri;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ListView;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public class Music extends Activity implements MediaPlayerControl {

	private ArrayList<Song> songList;
	private ListView songView;
	
	private Intent playIntent;
	private boolean musicBound=false;
	private boolean paused=false, playbackPaused=false;
	private MusicController controller;
	private MusicService musicSrv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.activity_music);
		
		songList = new ArrayList<Song>();
		songView = (ListView)findViewById(R.id.song_list);
		
		getSongList();
		
		SongAdapter songAdt = new SongAdapter(this, songList);
		songView.setAdapter(songAdt);
		
		setController();
	}
	
	@Override
	protected void onStart() {
	  super.onStart();
	  if(playIntent==null){
	    playIntent = new Intent(this, MusicService.class);
	    bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
	    startService(playIntent);
	  }
	}
	
	
	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection(){
	 
	  @Override
	  public void onServiceConnected(ComponentName name, IBinder service) {
	    MusicBinder binder = (MusicBinder)service;
	    //get service
	    musicSrv = binder.getService();
	    //pass list
	    musicSrv.setList(songList);
	    musicBound = true;
	  }
	 
	  @Override
	  public void onServiceDisconnected(ComponentName name) {
	    musicBound = false;
	  }
	};
	public void getSongList() {
		  //retrieve song info
		
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			  //get columns
			  int titleColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.TITLE);
			  int idColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media._ID);
			  int artistColumn = musicCursor.getColumnIndex
			    (android.provider.MediaStore.Audio.Media.ARTIST);
			  //add songs to list
			  do {
			    long thisId = musicCursor.getLong(idColumn);
			    String thisTitle = musicCursor.getString(titleColumn);
			    String thisArtist = musicCursor.getString(artistColumn);
			    songList.add(new Song(thisId, thisTitle, thisArtist));
			  }
			  while (musicCursor.moveToNext());
			}
		}
	
	public void songPicked(View view){
		  musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
		  musicSrv.playSong();
		  if(playbackPaused){
		    setController();
		    playbackPaused=false;
		  }
		  controller.show(0);
		}
	
	@Override
	protected void onDestroy() {
	  stopService(playIntent);
	  musicSrv=null;
	  super.onDestroy();
	}
	
	private void setController(){
		  //set the controller up
		
		controller = new MusicController(this);
		
		
		controller.setPrevNextListeners(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
			    playNext();
			  }
			}, new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
			    playPrev();
			  }
			});
		
		controller.setMediaPlayer(this);
		controller.setAnchorView(findViewById(R.id.song_list));
		controller.setEnabled(true);
		}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		musicSrv.go();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		playbackPaused=true;
		  musicSrv.pausePlayer();
	}
	
	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub
		musicSrv.seek(pos);
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		if(musicSrv!=null && musicBound && musicSrv.isPng())
		    return musicSrv.getDur();
		  else return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		  if(musicSrv!=null && musicBound && musicSrv.isPng())
			    return musicSrv.getPosn();
			  else return 0;
	}

	

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		 if(musicSrv!=null && musicBound)
			    return musicSrv.isPng();
		 
		 
			  return false;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getAudioSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//play next
	private void playNext(){
		  musicSrv.playNext();
		  if(playbackPaused){
		    setController();
		    playbackPaused=false;
		  }
		  controller.show(0);
	}
	 
	//play previous
	private void playPrev(){
		  musicSrv.playPrev();
		  if(playbackPaused){
		    setController();
		    playbackPaused=false;
		  }
		  controller.show(0);
	}
	
	@Override
	protected void onPause(){
	  super.onPause();
	  paused=true;
	  controller.hide();
	}
	
	@Override
	protected void onResume(){
	  super.onResume();
	  if(paused){
	    setController();
	    paused=false;
	  }
	}
	
	@Override
	protected void onStop() {
	  controller.hide();
	  super.onStop();
	}
}
