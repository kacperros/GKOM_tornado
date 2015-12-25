package pl.kacper.tornado.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kacper on 02.12.15.
 */
public class PrimitiveCylinder {

    private FloatBuffer vertexBuffer;
    private FloatBuffer outerVertexBuffer;

    private int vertexCount = 31;
    private int facesNumber = vertexCount - 2;
    private float centerX = 0.0f;
    private float centerY = 0.0f;
    private float centerZ = 0.0f;
    private float vertices[] = new float[vertexCount *3 *2];
    private int outerVertexCount = vertexCount - 1;
    int idx = 0;
    private float sideVertices[] = new float[facesNumber * 4 * 3];

    private float height;
    private float radius;
    private float[] color;

    public PrimitiveCylinder(float radius, float height, float[] color) {
        this.radius = radius;
        this.height = height;
        this.color = color;


        for(int i = 0; i < 2; i++ ) {
            vertices[idx++] = centerX;
            vertices[idx++] = centerY;
            vertices[idx++] = centerZ + i*this.height;
            for (int j = 0; j < outerVertexCount; ++j) {
                float percent = (j / (float) (outerVertexCount - 1));
                float rad = (float) (percent * 2 * Math.PI);
                //Vertex position
                float outer_x = (float) (centerX + this.radius * Math.cos(rad));
                float outer_y = (float) (centerY + this.radius * Math.sin(rad));
                float outer_z = centerZ + i*this.height;

                vertices[idx++] = outer_x;
                vertices[idx++] = outer_y;
                vertices[idx++] = outer_z;
            }
        }



        int idt = 0;
        int isx = 0;
        int k = 0;
        while(k < facesNumber) {
            for(int i = 0 ; i<6; i++, idt++, isx++){
                sideVertices[idt] = vertices[isx+3];
            }
            for(int i = 0; i < 6; i++, idt++){
                if(i==2 || i == 5)
                    sideVertices[idt] = (sideVertices[idt - 6]) + this.height;
                else
                    sideVertices[idt] = sideVertices[idt-6];
            }
            isx-=3;
            ++k;
        }


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        ByteBuffer vbb2 = ByteBuffer.allocateDirect(sideVertices.length * 4);
        vbb2.order(ByteOrder.nativeOrder());
        outerVertexBuffer = vbb2.asFloatBuffer();
        outerVertexBuffer.put(sideVertices);
        outerVertexBuffer.position(0);

    }

    public void draw(GL10 gl){

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColor4f(color[0], color[1], color[2], color[3]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertices.length / 6);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, vertices.length / 6, vertices.length / 6);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, outerVertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, sideVertices.length/3);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
