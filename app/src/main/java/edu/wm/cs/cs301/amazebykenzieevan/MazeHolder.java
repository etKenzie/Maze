package edu.wm.cs.cs301.amazebykenzieevan;

import android.util.Log;

import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;

/**
 * Class Used to Store instance of maze to use throughout the activities
 */
public class MazeHolder {
    private static final String TAG = "MazeHolder";
    public Maze mazeInstance;
    public static volatile MazeHolder holder = new MazeHolder();

    private MazeHolder() {}

    public static MazeHolder getInstance() {
        return holder;
    }

//    public static MazeHolder getInstance () {
//        if (holder == null ) {
//            synchronized(MazeHolder.class) {
//                if (holder == null) {
//                    holder = new MazeHolder();
//                }
//            }
//        }
//        return holder;
//    }
    

    public Maze getData() {
        Log.d(TAG, "getData: ");
        return this.mazeInstance;
    }

    public void setData(Maze maze) {
        Log.d(TAG, "setData: ");
        this.mazeInstance = maze;
    }







}
