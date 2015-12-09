package pl.kacper.tornado.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.kacper.tornado.R;
import pl.kacper.tornado.primitives.PrimitiveCube;
import pl.kacper.tornado.primitives.PrimitiveLeaf;
import pl.kacper.tornado.primitives.PrimitivePyramid;
import pl.kacper.tornado.primitives.PrimitiveSurface;

/**
 * Created by kacper on 06.12.15.
 */
public class TornadoRenderer implements GLSurfaceView.Renderer{

    private Context context;
    private PrimitiveLeaf[] leaves;
    private PrimitiveCube cube;
    private PrimitivePyramid pyramid;
    private PrimitiveSurface sceneFloor = new PrimitiveSurface();
    private PrimitiveSurface sceneWall1 = new PrimitiveSurface();
    private PrimitiveSurface sceneWall2 = new PrimitiveSurface();
    private PrimitiveSurface sceneWall3 = new PrimitiveSurface();
    private PrimitiveSurface sceneWall4 = new PrimitiveSurface();
    private PrimitiveSurface sceneCeiling = new PrimitiveSurface();
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
    private float tornadoBaseRadius = 0.6f;
    private float tornadoTopRadius = 2.0f;
    private float tornadoHeight = 6.0f;
    private float tornadoBase = 0.0f;
    private int numberOfLeaves = 200;
    private int textureIDs[] = new int[5];

    private float flatAngle = 0.0f;
    private float height = -5.0f;
    private float z = 5.0f;
    private final float maxZ = 10.0f;
    private final float minZ = 1.0f;
    private final float minHeight = -7.0f;
    private final float maxHeight = 0.0f;
    private final float maxAngle = 360.0f;


    private Random random = new Random();

    public TornadoRenderer(Context context) {
        this.context = context;

        leaves = new PrimitiveLeaf[numberOfLeaves];
        cube = new PrimitiveCube();
        pyramid = new PrimitivePyramid();
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

    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightPosition = {5.0f, 5.0f, 5.0f, 1.0f};

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);

        loadTextures(gl);
        gl.glEnable(GL10.GL_TEXTURE_2D);
//
//        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient, 0);
//        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse, 0);
//        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition, 0);
//        gl.glEnable(GL10.GL_LIGHT1);   // Enable Light 1
//        gl.glEnable(GL10.GL_LIGHT0);   // Enable the default Light 0
//        gl.glEnable(GL10.GL_COLOR_MATERIAL);
    }

    private void loadTextures(GL10 gl) {
        gl.glGenTextures(5, textureIDs, 0); // Generate texture-ID array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        // Get bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pw_bricks1);
        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        // Get bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);
        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[2]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

       // gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
       // gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);


        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass);
        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[3]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        // Get bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.autumn_wall);
        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[4]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        // Get bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
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
        //gl.glEnable(GL10.GL_LIGHTING);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 7, 7, 7, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        //GLU.gluLookAt(gl, 3, 3, 7, 0.0f, 0.0f, 0.0f, 0f, 1.0f, 0.0f);

        //Transform from inputs
        gl.glRotatef(flatAngle, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(0.0f, height, 0.0f);
        //Scene setup
        gl.glPushMatrix();
            gl.glScalef(12.0f, 1.0f, 12.0f);
            gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            sceneFloor.draw(gl, textureIDs[2]);
        gl.glPopMatrix();

        gl.glPushMatrix();
            gl.glTranslatef(0.0f, 12.0f, 0.0f);
            gl.glScalef(12.0f, 1.0f, 12.0f);
            gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            sceneCeiling.draw(gl, textureIDs[4]);
        gl.glPopMatrix();

        gl.glPushMatrix();
            gl.glTranslatef(0.0f, 6.0f, -12.0f);
            gl.glScalef(12.0f, 12.0f, 1.0f);
            sceneWall1.draw(gl, textureIDs[3]);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(12.0f, 6.0f, 0.0f);
        gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        gl.glScalef(12.0f, 12.0f, 1.0f);
        sceneWall2.draw(gl, textureIDs[3]);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 6.0f, 12.0f);
        gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        gl.glScalef(12.0f, 12.0f, 1.0f);
        sceneWall3.draw(gl, textureIDs[3]);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-12.0f, 6.0f, 0.0f);
        gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
        gl.glScalef(12.0f, 12.0f, 1.0f);
        sceneWall4.draw(gl, textureIDs[3]);
        gl.glPopMatrix();


        //The snow covered tower, as pitiful as it may look
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.0f, 0.0f);

        gl.glPushMatrix();
        gl.glTranslatef(4.0f, 0.0f, -4.0f);
        gl.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
            gl.glPushMatrix();
        gl.glScalef(1.0f, 2.0f, 1.0f);
        cube.draw(gl, textureIDs[0]);
            gl.glPopMatrix();
            gl.glPushMatrix();
            gl.glTranslatef(0.0f, 3.0f, 0.0f);
        pyramid.draw(gl, textureIDs[1]);
        gl.glPopMatrix();
        gl.glPopMatrix();

        //Leaf Tornado!!!
        gl.glPushMatrix();
        gl.glTranslatef((float) Math.cos(angleTornado / 50.0f), 0.0f, (float) Math.sin(angleTornado / 50.0f)); //Tornado moves in a circle
        gl.glRotatef(-angleTornado*3.0f, 0.0f, 1.0f, 0.0f);//Remember to increase the multiplier for hand in
        for(int i = 0; i < numberOfLeaves; i++){
            gl.glPushMatrix();
            gl.glTranslatef((float) Math.sin(leafHeight[i] + angleTornado / 30.0f), 0.0f, 0.0f); //Tornado sinuses
            gl.glRotatef(-leafAngle[i]-angleTornado*leafAngle[i]/360.0f, 0.0f, 1.0f, 0.0f);
            gl.glTranslatef(leafRadius[i], leafHeight[i], 0.0f);
            gl.glScalef(0.1f, 0.1f, 0.1f);
            gl.glRotatef(leafRotationAngle[3 * i] + angleTornado*leafRotationRates[3*i], 1.0f, 0.0f, 0.0f);
            gl.glRotatef(leafRotationAngle[3*i + 1] + angleTornado*leafRotationRates[3*i+1] ,0.0f, 1.0f, 0.0f);
            gl.glRotatef(leafRotationAngle[3*i + 2] + angleTornado*leafRotationRates[3*i +2],0.0f, 0.0f,1.0f);
            gl.glRotatef(leafAngle[i], 0.0f, 0.0f, 1.0f);
            gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            gl.glTranslatef(0.0f, 0.0f, -3.0f);
            leaves[i].draw(gl);
            gl.glPopMatrix();
        }
        angleTornado += speedTornado;
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    private float[] createRotationRates(){
        float[] ret = { random.nextFloat()*3.0f + 1.0f, random.nextFloat()*3.0f+1.0f, random.nextFloat()*3.0f+1.0f};
        return ret;
    }

    public float getFlatAngle() {
        return flatAngle;
    }

    public float getHeight(){
        return height;
    }

    public float getZ() {
        return z;
    }

    public void setFlatAngle(float flatAngle) {
        this.flatAngle = flatAngle%360;
    }

    public void setHeight(float height){
        if(height <= maxHeight && height >= minHeight)
            this.height = height;
        if(height < minHeight)
            this.height = minHeight;
        if(height > maxHeight)
            this.height = maxHeight;
    }

    public void setZ(float z) {
        if(z <= maxZ && z >= minZ)
            this.z = z;
        if(z < minZ)
            this.z = minZ;
        if(z > maxZ)
            this.z = maxZ;
    }

}
