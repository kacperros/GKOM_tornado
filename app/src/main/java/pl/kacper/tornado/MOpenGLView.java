package pl.kacper.tornado;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import pl.kacper.tornado.renderer.TornadoRenderer;

/**
 * Created by kacper on 09.12.15.
 */
public class MOpenGLView extends GLSurfaceView {

    private final TornadoRenderer renderer;
    private ScaleGestureDetector scaleGestureDetector;

    public MOpenGLView(Context context) {
        super(context);
        renderer = new TornadoRenderer(context);
        setRenderer(renderer);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        scaleGestureDetector.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;
                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }
                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }
                renderer.setFlatAngle(
                        renderer.getFlatAngle() +
                                ((dx) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                renderer.setHeight(
                        renderer.getHeight() +
                                ((dy) * 0.05f));
                requestRender();
        }
        previousX = x;
        previousY = y;
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            renderer.setZ(renderer.getZ()*detector.getScaleFactor());
            invalidate();
            return true;
        }
    }

}
