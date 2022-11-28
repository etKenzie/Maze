package edu.wm.cs.cs301.amazebykenzieevan.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import edu.wm.cs.cs301.amazebykenzieevan.R;

/**
 * @author kenzieevan
 * Used for MazePanel in Play Animation Activity
 *
 */
public class MazePanel2 extends View {
    private Rect mRectSquare;
    private Paint mPaintSquare;

    private Canvas canvas;
    private Bitmap mainBitmap;
    private Paint paint;

    private int imageWidth;
    private int imageHeight;

    public MazePanel2(Context context) {
        super(context);

        init(null);
    }

    public MazePanel2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public MazePanel2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public MazePanel2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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


        mRectSquare.top = 0;
        mRectSquare.left = 0;
        mRectSquare.right = 530;
        mRectSquare.bottom = 265;
        mPaintSquare.setColor(Color.GRAY);


        canvas.drawRect(mRectSquare, mPaintSquare);

        mRectSquare.top = 265;
        mRectSquare.left = 0;
        mRectSquare.right = 530;
        mRectSquare.bottom = 530;
        mPaintSquare.setColor(Color.BLACK);

        canvas.drawRect(mRectSquare, mPaintSquare);

        mainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_ball);
        mainBitmap = Bitmap.createScaledBitmap(mainBitmap, 85, 70, false);
        canvas.drawBitmap(mainBitmap, 220, 230, null);




    }

}
