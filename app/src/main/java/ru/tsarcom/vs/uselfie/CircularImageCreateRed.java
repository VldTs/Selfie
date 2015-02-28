package ru.tsarcom.vs.uselfie;

/**
 * Created by USRSLM on 12.02.2015.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImageCreateRed extends ImageView {
    private int borderWidth = 2;
    private int viewWidth;
    private int viewHeight;
    private Bitmap image;
    private Paint paint;
    private Paint paintBorder;
    private BitmapShader shader;
    public CircularImageCreateRed(Context context) {
        super(context);
        setup();
    }
    public CircularImageCreateRed(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }
    public CircularImageCreateRed(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }
    private void setup()
    {
// init paint
        paint = new Paint();
        paint.setAntiAlias(true);
        paintBorder = new Paint();
        setBorderColor(Color.WHITE);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        paintBorder.setShadowLayer(4.0f, 0.0f, 4.0f, Color.BLACK);
        paintBorder.setAntiAlias(true);
    }
    public void setBorderWidth(int borderWidth)
    {
        this.borderWidth = borderWidth;
        this.invalidate();
    }
    public void setBorderColor(int borderColor)
    {
        if(paintBorder != null)
            paintBorder.setColor(borderColor);
        this.invalidate();
    }
    private void loadBitmap()
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if(bitmapDrawable != null)
            image = bitmapDrawable.getBitmap();
    }
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas)
    {
//load the bitmap
        loadBitmap();
// init shader
        if(image !=null)
        {
            Matrix m;
            m = new Matrix();

            int width = image.getWidth();
            int height = image.getHeight();

            float sx = canvas.getWidth() / (float) width;
            float sy = canvas.getHeight() / (float) height;
            Bitmap dstBmp;
            if (image.getWidth() >= image.getHeight()) {

                m.setScale(sy, sy);
                dstBmp = Bitmap.createBitmap(
                        image,
                        image.getWidth() / 2 - image.getHeight() / 2,
                        0,
                        image.getHeight(),
                        image.getHeight(), m, false
                );

            } else {

                m.setScale(sx, sx);
                dstBmp = Bitmap.createBitmap(
                        image,
                        0,
                        image.getHeight() / 2 - image.getWidth() / 2,
                        image.getWidth(),
                        image.getWidth(), m, false
                );
            }

            shader = new BitmapShader(dstBmp,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            int circleCenter = viewWidth / 2;
// circleCenter is the x or y of the view's center
// radius is the radius in pixels of the cirle to be drawn
// paint contains the shader that will texture the shape
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth, paintBorder);
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paint);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);
        viewWidth = width - (borderWidth *2)-5;
        viewHeight = height - (borderWidth*2)-5;
        setMeasuredDimension(width, height);
    }
    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
// We were told how big to be
            result = specSize;
        } else {
// Measure the text
            result = viewWidth;
        }
        return result;
    }
    private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);
        if (specMode == MeasureSpec.EXACTLY) {
// We were told how big to be
            result = specSize;
        } else {
// Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }
        return result;
    }
}
