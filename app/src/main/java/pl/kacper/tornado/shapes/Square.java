package pl.kacper.tornado.shapes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
/*
 * A square drawn in 2 triangles (using TRIANGLE_STRIP).
 */
public class Square {
    private FloatBuffer vertexBuffer;  // Buffer for vertex-array

    private float[] vertices = {  // Vertices for the square
            -1.0f, -1.0f,  0.0f,  // 0. left-bottom
            1.0f, -1.0f,  0.0f,  // 1. right-bottom
            -1.0f,  1.0f,  0.0f,  // 2. left-top
            1.0f,  1.0f,  0.0f   // 3. right-top
    };

    // Constructor - Setup the vertex buffer
    public Square() {
        // Setup vertex array buffer. Vertices in float. A float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);           // Rewind
    }

    // Render the shape
    public void draw(GL10 gl) {
        // Enable vertex-array and define its buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        // Draw the primitives from the vertex-array directly
        gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);      // Set the current color (NEW)
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        // glDrawArrays(int mode, int first, int count)
        //   mode: GL_POINTS, GL_LINE_STRIP, GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, or GL_TRIANGLES
        //   first: the starting index in the enabled arrays.
        //   count: the number of indices to be rendered.


        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}