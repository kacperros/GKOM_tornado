package pl.kacper.tornado.threeDshapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kacper on 02.12.15.
 */
public class Cylinder {

    private FloatBuffer vertexBuffer;
    private FloatBuffer outerVertexBuffer;
    private int vertexCount = 31;
    private int facesNumber = vertexCount - 2;
    float radius = 1.0f;
    private float centerX = 0.0f;
    private float centerY = 0.0f;
    private float centerZ = 0.0f;
    private float vertices[] = new float[vertexCount *3 *2];
    private int outerVertexCount = vertexCount - 1;
    int idx = 0;
    private float sideVertices[] = new float[facesNumber * 4 * 3];

    private float[][] colors = {  // Colors of the 6 faces
            {1.0f, 0.5f, 0.0f, 1.0f},  // 0. orange
            {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
            {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
            {0.0f, 0.0f, 1.0f, 1.0f},  // 3. blue
            {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
            {1.0f, 1.0f, 0.0f, 1.0f}   // 5. yellow
    };
    {
        for(int i = 0; i < 2; i++) {
            vertices[idx++] = centerX;
            vertices[idx++] = centerY;
            vertices[idx++] = centerZ+i;
            float z = 0.0f + i;
            for (int j = 0; j < outerVertexCount; ++j) {
                float percent = (j / (float) (outerVertexCount - 1));
                float rad = (float) (percent * 2 * Math.PI);

                //Vertex position
                float outer_x = (float) (centerX + radius * Math.cos(rad));
                float outer_y = (float) (centerY + radius * Math.sin(rad));
                float outer_z = z;

                vertices[idx++] = outer_x;
                vertices[idx++] = outer_y;
                vertices[idx++] = outer_z;
            }
        }
    }
    {
        int idx = 0;
        int isx = 0;
        int k = 0;
        while(k < facesNumber) {
            for(int i = 0 ; i<6; i++, idx++, isx++){
                sideVertices[idx] = vertices[isx+3];
            }
            for(int i = 0; i < 6; i++, idx++){
                if(i==2 || i == 5)
                    sideVertices[idx] = (sideVertices[idx - 6]) + 1.0f;
                else
                    sideVertices[idx] = sideVertices[idx-6];
            }
            isx-=3;
            ++k;
        }
    }

    public Cylinder() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // Use native byte order
        vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
        vertexBuffer.put(vertices);         // Copy data into buffer
        vertexBuffer.position(0);
        ByteBuffer vbb2 = ByteBuffer.allocateDirect(sideVertices.length * 4);
        vbb2.order(ByteOrder.nativeOrder()); // Use native byte order
        outerVertexBuffer = vbb2.asFloatBuffer(); // Convert from byte to float
        outerVertexBuffer.put(sideVertices);         // Copy data into buffer
        outerVertexBuffer.position(0);
    }

    public void draw(GL10 gl){
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertices.length / 6);
        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, vertices.length / 6, vertices.length / 6);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, outerVertexBuffer);
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, sideVertices.length/3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
