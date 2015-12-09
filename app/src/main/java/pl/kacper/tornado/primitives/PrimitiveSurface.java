package pl.kacper.tornado.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kacper on 09.12.15.
 * Class used for generating flat surfaces
 */
public class PrimitiveSurface {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private FloatBuffer textureBuffer; //Buffer for texture-array
    private float[] vertices = {  // Vertices
            // FRONT
            -1.0f, -1.0f,  0.0f,  // 0. left-bottom-front
            1.0f, -1.0f,  0.0f,  // 1. right-bottom-front
            -1.0f,  1.0f,  0.0f,  // 2. left-top-front
            1.0f,  1.0f,  0.0f,  // 3. right-top-front

    };

    float[] textureCoords = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    public PrimitiveSurface() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);
    }


    public void draw(GL10 gl, int textureID) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

}
