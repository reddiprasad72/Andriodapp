package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Process;
import android.widget.Toast;


public class UnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents {
    public String TournamentId;
    public String auth_key;
    private String roomCode = "";
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);

//        TournamentId = getIntent().getStringExtra("TournamentId");
//        auth_key = getIntent().getStringExtra("auth_key");
//
//        Log.i("ForeGround", "TournamentId == >" + TournamentId + "   auth_key ==>" + auth_key);


        mUnityPlayer = new UnityPlayer(this, this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }

    public void UnityBack() {
        finish();
    }

    public void UnityQuit(){
        finishAffinity();
    }

    public void SendRoomCode(String roomcode) {

//        Toast.makeText(this, roomcode, Toast.LENGTH_SHORT).show();
//        if (this.roomCode.equals("")) {
//            this.roomCode = roomcode;
//            Log.i("ForeGround", "Room Code ==>" + roomCode);


//            // ORIGINAL
//            Intent intents = new Intent();
//            intents.setPackage("com.sikandarji.android");
//            intents.putExtra("sAuthKey", auth_key);
//            intents.putExtra("sTournamentID", TournamentId);
//            intents.putExtra("sRoomCode", roomcode);
//            intents.setAction("SEND_DATA");
//            sendBroadcast(intents);




//            SharedPreferences sh = getSharedPreferences(AppConstant.SHARED_PREF, Context.MODE_PRIVATE);
//            String authKeys = sharedpreferences.getString("sAuthKey", "");
//            String tournment = sharedpreferences.getString("sTournamentID", "");
//            String myRoom = sharedpreferences.getString("sRoomCode", "");
//            Log.i("ForeGround", tournment);
//            Log.i("ForeGround", myRoom);


//        }
    }

    // When Unity player unloaded move task to background
    @Override
    public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // Callback before Unity player process is killed
    @Override
    public void onUnityPlayerQuitted() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.destroy();
        super.onDestroy();
//        Log.i("Capermint", "onDestroy Unity");
//        Toast toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
//        View view = toast.getView();
//        view.setBackgroundResource(R.drawable.top_rounded_transparent);
//        TextView text = (TextView) view.findViewById(android.R.id.message);
//        /*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
//        toast.show();

//        if (!TournamentId.equals("") ) {   //&& !roomCode.equals("")
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://sikandarji.staging-server.in/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        WebApiCall webApiCall = retrofit.create(WebApiCall.class);
//        Call<UserEndGameRs> call = webApiCall.userLeaveGameApi(auth_key, TournamentId, roomCode);
//        Log.i("Capermint", "CAll API From Unity");
//        call.enqueue(new Callback<UserEndGameRs>() {
//            @Override
//            public void onResponse(Call<UserEndGameRs> call, Response<UserEndGameRs> response) {
//                if (response.isSuccessful()) {
//                    TournamentId = "";
//                    roomCode = "";
////                        SharedPreferences.Editor editor = sh.edit();
////                        editor.putString("sAuthKey", "");
////                        editor.putString("sTournamentID", "");
////                        editor.putString("sRoomCode", "");
////                        editor.apply();
//
//                    Log.i("Capermint", "Success - >" + response.body());
//                } else {
////                Toast.makeText(MainActivity.this, ""+response.errorBody(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserEndGameRs> call, Throwable t) {
//                Log.i("Capermint", "Error - >" + t.getMessage());
//            }
//        });
//        }



    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
}