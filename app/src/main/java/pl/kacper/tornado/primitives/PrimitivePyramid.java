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
public class PrimitivePyramid {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array
    private ByteBuffer indexBuffer;    // Buffer for index-array
    private FloatBuffer textureBuffer; //Buffer for texture-array

    private float[] vertices = { // 5 vertices of the pyramid in (x,y,z)
            -1.0f, -1.0f, -1.0f,  // 0. left-bottom-back
            1.0f, -1.0f, -1.0f,  // 1. right-bottom-back
            1.0f, -1.0f,  1.0f,  // 2. right-bottom-front
            -1.0f, -1.0f,  1.0f,  // 3. left-bottom-front
            0.0f,  1.0f,  0.0f   // 4. top
    };


    private byte[] indices = { // Vertex indices of the 4 Triangles
            2, 4, 3,   // front face (CCW)
            1, 4, 2,   // right face
            0, 4, 1,   // back face
            4, 0, 3    // left face
    };
    int[] textureIDs = new int[2];

    float[] textureCoords = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    // Constructor - Set up the buffers
    public PrimitivePyramid() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind

        // Setup index-array buffer. Indices in byte.
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4 * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();
        // All the 6 faces have the same texture coords, repeat 6 times
        for (int face = 0; face < 4; face++) {
            textureBuffer.put(textureCoords);
        }
        textureBuffer.position(0);
    }

    // Draw the shape
    public void draw(GL10 gl, int textureID) {
        // Enable arrays and define their buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glBindTexture(GL10.GL_TEXTURE_2D,textureID);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE,
                indexBuffer);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void drawTexture(GL10 gl, Context context) {
        gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Get bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}
