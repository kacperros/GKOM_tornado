package pl.kacper.tornado;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.kacper.tornado.renderer.LeafRenderer;
import pl.kacper.tornado.renderer.My3dRenderer;
import pl.kacper.tornado.renderer.My3dRenderer2;
import pl.kacper.tornado.renderer.MyGLRenderer;
import pl.kacper.tornado.renderer.TornadoRenderer;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_view);
//        glSurfaceView.setRenderer(new TornadoRenderer(this));
//        //glSurfaceView.setRenderer(new My3dRenderer(this));
        //glSurfaceView.setRenderer(new LeafRenderer(this));
        //glSurfaceView.setRenderer(new MyGLRenderer(this));
        glSurfaceView = new MOpenGLView(this);
        setContentView(glSurfaceView);

    }

    @Override
    protected void onPause(){
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        glSurfaceView.onResume();
    }
}
