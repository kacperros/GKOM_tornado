package pl.kacper.tornado.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kacper on 02.12.15.
 */
public class Circle {

    private FloatBuffer vertexBuffer;

    int vertexCount = 30;
    float radius = 1.0f;
    float center_x = 0.0f;
    float center_y = 0.0f;
    float center_z = 0.0f;

    // Create a buffer for vertex data
    float buffer[] = new float[vertexCount*3]; // (x,y) for each vertex
    int idx = 0;
    int outerVertexCount = vertexCount - 1;

// Center vertex for triangle fan
    {
        buffer[idx++] = center_x;
        buffer[idx++] = center_y;
        buffer[idx++] = center_z;

        // Outer vertices of the circle
        for (int i = 0; i < outerVertexCount; ++i) {
            float percent = (i / (float) (outerVertexCount - 1));
            float rad = (float) (percent * 2 * Math.PI);

            //Vertex position
            float outer_x = (float) (center_x + radius * Math.cos(rad));
            float outer_y = (float) (center_y + radius * Math.sin(rad));
            float outer_z = 0.0f;

            buffer[idx++] = outer_x;
            buffer[idx++] = outer_y;
            buffer[idx++] = outer_z;
        }
    }

    public Circle() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(buffer.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(buffer);         // Copy data into buffer
        vertexBuffer.position(0);
    }

    public void draw(GL10 gl){
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        //gl.glDrawArrays(GL10.GL_LINE_LOOP, 1, outerVertexCount);
        //just the circle
        gl.glColor4f(1.0f, 0.2f, 0.2f, 1.0f); //!Remember this sets only the current color!!!

        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 1, vertexCount);
        //filled circle
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }


}
