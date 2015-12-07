package pl.kacper.tornado.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.kacper.tornado.primitives.PrimitiveCube;
import pl.kacper.tornado.primitives.PrimitiveLeaf;
import pl.kacper.tornado.primitives.PrimitivePyramid;

/**
 * Created by kacper on 06.12.15.
 */
public class TornadoRenderer implements GLSurfaceView.Renderer{

    private Context context;
    private PrimitiveLeaf[] leaves;
    private PrimitiveCube cube;
    private PrimitivePyramid pyramid;
    private float[] leafHeight;
    private float[] leafRadius;
    private float[] leafAngle;
    private float[] leafRotationAngle;
    private float[] leafRotationRates;
    private float colors[] = {
            42.0f/255.0f, 156.0f/255.0f, 46.0f/255.0f, 1.0f,
            18.0f/255.0f, 227.0f/255.0f, 25.0f/255.0f, 1.0f,
            150.0f/255.0f, 227.0f/255.0f, 18.0f/255.0f, 1.0f,
            214.0f/255.0f, 200.0f/255.0f, 47.0f/255.0f, 1.0f,
            214.0f/255.0f, 184.0f/255.0f, 47.0f/255.0f, 1.0f,
            214.0f/255.0f, 133.0f/255.0f, 47.0f/255.0f, 1.0f,
            214.0f/255.0f, 89.0f/255.0f, 47.0f/255.0f, 1.0f,
            153.0f/255.0f, 73.0f/255.0f, 46.0f/255.0f, 1.0f,
            214.0f/255.0f, 21.0f/255.0f, 21.0f/255.0f, 1.0f,
    };
    private float angleTornado = 0.0f;
    private float speedTornado = 1.5f;
    private float tornadoBaseRadius = 0.3f;
    private float tornadoTopRadius = 1.0f;
    private float tornadoHeight = 2.0f;
    private float tornadoBase = 0.0f;
    private int numberOfLeaves = 100;


    private Random random = new Random();

    public TornadoRenderer(Context context) {
        this.context = context;
        leaves = new PrimitiveLeaf[numberOfLeaves];
        cube = new PrimitiveCube(getRandomLeafColor());
        pyramid = new PrimitivePyramid(getRandomLeafColor());
        leafHeight = new float[numberOfLeaves];
        leafRadius = new float[numberOfLeaves];
        leafAngle = new float[numberOfLeaves];
        leafRotationAngle = new float[numberOfLeaves * 3];
        leafRotationRates = new float[numberOfLeaves * 3];
        for(int i = 0; i < numberOfLeaves; i++){
            leaves[i] = new PrimitiveLeaf(getRandomLeafColor());
            leafHeight[i] = random.nextFloat() * (tornadoHeight - tornadoBase) + tornadoBase;
            leafRadius[i] = ((leafHeight[i] * tornadoTopRadius)/(tornadoHeight - tornadoBase)) + tornadoBaseRadius + randomRadius();
            leafAngle[i] = random.nextFloat() * 360.0f;
            leafRotationAngle[i] = random.nextFloat() * 360.0f + 20.0f;
            leafRotationAngle[i+1] = random.nextFloat() * 360.0f + 20.0f;
            leafRotationAngle[i+2] = random.nextFloat() * 360.0f + 20.0f;
            float leafRates[] = createRotationRates();
            leafRotationRates[i] = leafRates[0];
            leafRotationRates[i+1] = leafRates[1];
            leafRotationRates[i+2] = leafRates[2];
        }
    }

    private float[] getRandomLeafColor(){
        int colorIdx = random.nextInt(colors.length/4);
        float[] ret = {colors[colorIdx*4], colors[colorIdx*4+1], colors[colorIdx*4+2], colors[colorIdx*4+3]};
        return ret;
    }

    private float randomRadius() {
        return random.nextFloat()*0.3f-0.15f;
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
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 3, 3, 5, 0.0f, 0.0f, 0.0f, 0f, 1.0f, 0.0f);
        gl.glPushMatrix();
        gl.glTranslatef((float) Math.cos(angleTornado / 50.0f), 0.0f, (float) Math.sin(angleTornado / 50.0f)); //Tornado moves in a circle
        gl.glRotatef(-angleTornado*3.0f, 0.0f, 1.0f, 0.0f);
        for(int i = 0; i < numberOfLeaves; i++){
            gl.glPushMatrix();
            gl.glTranslatef((float) Math.sin(leafHeight[i] + angleTornado / 30.0f), 0.0f, 0.0f); //Tornado sinuses
            gl.glRotatef(-leafAngle[i]-angleTornado*leafAngle[i]/360.0f, 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(leafRadius[i], leafHeight[i], 0.0f);
            gl.glScalef(0.05f, 0.05f, 0.05f);
            gl.glRotatef(leafRotationAngle[3 * i] + angleTornado*leafRotationRates[3*i], 1.0f, 0.0f, 0.0f);
            gl.glRotatef(leafRotationAngle[3*i + 1] + angleTornado*leafRotationRates[3*i+1] ,0.0f, 1.0f, 0.0f);
            gl.glRotatef(leafRotationAngle[3*i + 2] + angleTornado*leafRotationRates[3*i +2],0.0f, 0.0f,1.0f);
            gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            gl.glTranslatef(0.0f, 0.0f, -3.0f);
            leaves[i].draw(gl);
            gl.glPopMatrix();
        }
        angleTornado += speedTornado;
        gl.glPopMatrix();
        gl.glPushMatrix();
            gl.glTranslatef(3.0f, 0.0f, -6.0f);
            gl.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            gl.glPushMatrix();
                gl.glScalef(0.5f, 1.0f, 0.5f);
                cube.draw(gl);
            gl.glPopMatrix();
            gl.glPushMatrix();
                gl.glTranslatef(0.0f, 1.5f, 0.0f);
                gl.glScalef(0.5f, 0.5f, 0.5f);
                pyramid.draw(gl);
            gl.glPopMatrix();
        gl.glPopMatrix();

    }

    private float[] createRotationRates(){
        float[] ret = { random.nextFloat()*3.0f + 1.0f, random.nextFloat()*3.0f+1.0f, random.nextFloat()*3.0f+1.0f};
        return ret;
    }

}
