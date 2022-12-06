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
import androidx.core.graphics.ColorUtils;

import edu.wm.cs.cs301.amazebykenzieevan.gui.Constants;
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

        imageWidth = Constants.VIEW_WIDTH;
        imageHeight = Constants.VIEW_HEIGHT;

        mBitmap = Bitmap.createBitmap(imageWidth,imageHeight, Bitmap.Config.ARGB_8888);

        UIcanvas = new Canvas(mBitmap);


    }



    @Override
    protected void onDraw(Canvas UIcanvas) {
        super.onDraw(UIcanvas);

//        mBitmap = Bitmap.createScaledBitmap(mBitmap, 530, 530, false);
        UIcanvas.drawBitmap(mBitmap, 0, 0, paint);




    }

    public void testImage() {
//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_ball);

        addBackground(100);

        setColor(Color.RED);
        addFilledOval(50,50,100,100);

        setColor(Color.GREEN);
        addFilledOval(250,50,100,100);

        setColor(Color.YELLOW);
        addFilledRectangle(50,280, 200,370);

        setColor(Color.BLUE);
        int[] xpoints = {10,120,230,240,530};
        int[] ypoints = {110,220,330,440,150};

        addFilledPolygon(xpoints,ypoints,5);



        postInvalidate();
    }

    /**
     * Commits the changes into the Panel
     */
    @Override
    public void commit() {
        invalidate();
    }

    /**
     * Determine if able to draw or not
     * @return if canvas exists
     */
    @Override
    public boolean isOperational() {
        if (UIcanvas == null){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Sets the Color of the paint object
     * @param argb gives the alpha, red, green, and blue encoded value of the color
     */
    @Override
    public void setColor(int argb) {
        paint.setColor(argb);
    }

    /**
     * Gets the Color of the paint object
     * @return color of paint object
     */
    @Override
    public int getColor() {
        return paint.getColor();
    }

    /**
     * Helps to get what the current colors of the background should be relative to the percentToExit
     * @param percentToExit percent of maze completed
     * @param topHalf boolean to determine if on top half or bottom of background
     * @return Color to use for background
     */
    private int getCurrentColor(float percentToExit, boolean topHalf) {
        // General color settings across multiple screens from ColorTheme
        int greenWM = Color.parseColor("#115740");
        int goldWM = Color.parseColor("#916f41");
        int blackWM = Color.parseColor("#222222");

        // For top half from black to gold, for bottom half from grey to green
        return topHalf ? ColorUtils.blendARGB(blackWM, goldWM, percentToExit/100) :
                ColorUtils.blendARGB(Color.LTGRAY, greenWM, percentToExit/100);
    }

    /**
     * Creates the background for the maze to got through
     * @param percentToExit gives the distance to exit
     */
    @Override
    public void addBackground(float percentToExit) {
        setColor(getCurrentColor(percentToExit, true));
        addFilledRectangle(0,0, imageWidth,imageHeight/2);

        setColor(getCurrentColor(percentToExit, false));
        addFilledRectangle(0,imageHeight/2, imageWidth, imageHeight);



    }

    /**
     * Draws Rectangle of given parameters on canvas
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        UIcanvas.drawRect(x,y,width,height,paint);
    }

    /**
     * Draws Filled Polygon of given parameters on canvas.
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        // Adjust Paint and Create Path to use to draw Polygon
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.reset();

        // Move to Coordinate
        path.moveTo(xPoints[0],xPoints[1]);

        // Draw lines between the two Points
        for (int i=1; i<nPoints; i++) {
            path.lineTo(xPoints[i],yPoints[i]);
        }

        path.lineTo(xPoints[0], yPoints[0]);


        // Draw Path on Bitmap
        UIcanvas.drawPath(path,paint);

    }

    /**
     * Draws Polygon of given parameters on canvas
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        // Adjust Paint and Create Path to use to draw Polygon
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.reset();

        // Move to Coordinate
        path.moveTo(xPoints[0],xPoints[1]);

        // Draw lines between the two Points
        for (int i=1; i<nPoints; i++) {
            path.lineTo(xPoints[i],yPoints[i]);
        }
        path.lineTo(xPoints[0], yPoints[0]);

        // Draw Path on Bitmap
        UIcanvas.drawPath(path,paint);
    }

    /**
     * Draws line on canvas
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        UIcanvas.drawLine(startX, startY, endX, endY, paint);
    }

    /**
     * Draws Oval on canvas
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        UIcanvas.drawOval(x,y,x+width,y+height,paint);
    }

    /**
     * Draws Arc on canvas
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        UIcanvas.drawArc(x,y,width,height,startAngle,arcAngle,true,paint);
    }

    /**
     * Draws Text on canvas
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str) {
        UIcanvas.drawText(str,x,y,paint);
    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {

    }
}
