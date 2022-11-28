package edu.wm.cs.cs301.amazebykenzieevan.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import edu.wm.cs.cs301.amazebykenzieevan.R;

/**
 * @author kenzieevan
 * Used for MazePanel in Play Manual Activity
 *
 */
public class MazePanel extends View {


    private Rect mRectSquare;
    private Paint mPaintSquare;

    private Canvas canvas;
    private Bitmap mainBitmap;
    private Paint paint;

    private int imageWidth;
    private int imageHeight;

    public MazePanel(Context context) {
        super(context);

        init(null);
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }
    private void init(@Nullable AttributeSet set){
        mRectSquare = new Rect();
        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);


    }


    @Override
    protected void onDraw(Canvas canvas) {

        this.canvas = canvas;
        canvas.save();



        mainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maze_wall);
        mainBitmap = Bitmap.createScaledBitmap(mainBitmap, 530, 530, false);
        canvas.drawBitmap(mainBitmap, 0, 0, null);


        // Drawing My maze on the Canvas then save and convert to Bitmap by making png of it
//        mRectSquare.top = 0;
//        mRectSquare.left = 0;
//        mRectSquare.right = 530;
//        mRectSquare.bottom = 265;
//        mPaintSquare.setColor(Color.GRAY);
//
//
//        canvas.drawRect(mRectSquare, mPaintSquare);
//
//        mRectSquare.top = 265;
//        mRectSquare.left = 0;
//        mRectSquare.right = 530;
//        mRectSquare.bottom = 530;
//        mPaintSquare.setColor(Color.BLACK);
//
//        canvas.drawRect(mRectSquare, mPaintSquare);

//        // Draw Maze Wall
//        Paint wallpaint = new Paint();
//        wallpaint.setColor(Color.YELLOW);
//        wallpaint.setStyle(Paint.Style.FILL);
//
//        Path wallpath = new Path();
//        wallpath.reset(); // only needed when reusing this path for a new build
//        wallpath.moveTo(0, 450); // used for first point
//        wallpath.lineTo(0, 180);
//        wallpath.lineTo(200, 215);
//        wallpath.lineTo(200, 350);
//        wallpath.close(); // there is a setLastPoint action but i found it not to work as expected
//
//        canvas.drawPath(wallpath, wallpaint);
//
//        wallpaint = new Paint();
//        wallpaint.setColor(Color.GREEN);
//        wallpaint.setStyle(Paint.Style.FILL);
//
//        wallpath = new Path();
//        wallpath.reset(); // only needed when reusing this path for a new build
//        wallpath.moveTo(530, 450); // used for first point
//        wallpath.lineTo(530, 180);
//        wallpath.lineTo(200, 215);
//        wallpath.lineTo(200, 350);
//        wallpath.close(); // there is a setLastPoint action but i found it not to work as expected
//
//        canvas.drawPath(wallpath, wallpaint);




    }

}
