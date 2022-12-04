package edu.wm.cs.cs301.amazebykenzieevan.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import edu.wm.cs.cs301.amazebykenzieevan.gui.P7PanelF22;

/**
 * @author kenzieevan
 * Used for MazePanel in Play Manual Activity
 *
 */
public class MazePanel extends View  implements P7PanelF22 {
    private Bitmap mBitmap;
    private Paint paint;
    private Path mPath;
    private Canvas UIcanvas;

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
        // Initializing Paint, Bitmap, and Canvas to draw Bitmap
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = Bitmap.createBitmap(530,530, Bitmap.Config.ARGB_8888);

        UIcanvas = new Canvas(mBitmap);
    }


    @Override
    protected void onDraw(Canvas UIcanvas) {
        super.onDraw(UIcanvas);

        mBitmap = Bitmap.createScaledBitmap(mBitmap, 530, 530, false);
        UIcanvas.drawBitmap(mBitmap, 0, 0, paint);


    }

    public void testImage(Canvas c) {

//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_ball);
        paint.setColor(Color.BLUE);
        addFilledRectangle(0,0,100,100);

        int[] xarray;



        postInvalidate();
    }

    @Override
    public void commit() {
        postInvalidate();
    }

    @Override
    public boolean isOperational() {
        return false;
    }

    @Override
    public void setColor(int argb) {
        paint.setColor(argb);
    }

    @Override
    public int getColor() {
        return paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit) {

    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        UIcanvas.drawRect(x,y,width,height,paint);
        postInvalidate();
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        // Adjust Paint and Create Path to use to draw Polygon
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.reset();

        // Move to Coordinate
        path.moveTo(xPoints[0],xPoints[1]);

        // Draw lines between the two Points
        path.lineTo(xPoints[0], yPoints[0]);
        for (int i=1; i<nPoints; i++) {
            path.lineTo(xPoints[i],yPoints[i]);
        }



        UIcanvas.drawPath(path,paint);



    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        // Adjust Paint and Create Path to use to draw Polygon
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.reset();

        path.moveTo(xPoints[0],xPoints[1]);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {

    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {

    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void addMarker(float x, float y, String str) {

    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {

    }
}
