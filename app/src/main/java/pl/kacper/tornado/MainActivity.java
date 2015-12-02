package pl.kacper.tornado;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_view);
        glSurfaceView.setRenderer(new MyGLRenderer(this));
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
