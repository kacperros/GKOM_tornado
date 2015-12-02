package pl.kacper.tornado;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.kacper.tornado.shapes.Circle;
import pl.kacper.tornado.shapes.Square;
import pl.kacper.tornado.shapes.Triangle;

/**
 * Created by kacper on 01.12.15.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    Triangle triangle;     // ( NEW )
    Circle circle;
    Square square;

    public MyGLRenderer(Context context) {
        this.context = context;
        triangle = new Triangle();   // ( NEW )
        circle = new Circle();
        square = new Square();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;
        float aspect = (float)width / height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();                 // Reset model-view matrix ( NEW )
        gl.glTranslatef(-1.5f, 0.0f, -6.0f); // Translate left and into the screen ( NEW )
        //gl.glScalef(1.0f, 2.0f, -1.0f);
        triangle.draw(gl);                   // Draw triangle ( NE

        gl.glTranslatef(3.0f, 0.0f, 0.0f);
        circle.draw(gl);

        gl.glTranslatef(3.0f, 0.0f, 0.0f);
        square.draw(gl);
    }
}
