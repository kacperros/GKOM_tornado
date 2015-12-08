package pl.kacper.tornado.primitives;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.kacper.tornado.R;

/**
 * Created by kacper on 07.12.15.
 */
public class PrimitiveCube {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private FloatBuffer textureBuffer; //Buffer for texture-array
    private int numFaces = 6;
    private float[] vertices = {  // Vertices of the 6 faces
            // FRONT
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            // BACK
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            // LEFT
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            // RIGHT
            1.0f, -1.0f,  1.0f,  // 1. right-bottom-front
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            // TOP
            -1.0f,  1.0f,  1.0f,  // 2. left-top-front
            1.0f,  1.0f,  1.0f,  // 3. right-top-front
            -1.0f,  1.0f, -1.0f,  // 5. left-top-back
            1.0f,  1.0f, -1.0f,  // 7. right-top-back
            // BOTTOM
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f,  1.0f,  // 0. left-bottom-front
            1.0f, -1.0f,  1.0f   // 1. right-bottom-front
    };

    float[] textureCoords = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    int[] textureIDs = new int[1];

    public PrimitiveCube() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4 * 6);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        // All the 6 faces have the same texture coords, repeat 6 times
        for (int face = 0; face < 6; face++) {
            textureBuffer.put(textureCoords);
        }
        textureBuffer.position(0);     // Rewind
    }


    public void draw(GL10 gl, int textureID) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        for (int face = 0; face < numFaces; face++) {
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face*4, 4);
        }
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
