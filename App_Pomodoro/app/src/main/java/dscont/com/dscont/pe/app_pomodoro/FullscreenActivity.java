package dscont.com.dscont.pe.app_pomodoro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.dwfox.dwrulerviewlibrary.utils.DWUtils;
import com.tistory.dwfox.dwrulerviewlibrary.view.ObservableHorizontalScrollView;
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    ScrollingValuePicker myScrollingValuePicker;
    Button btn_Iniciar;
    TextView tv_Valor;

    int Valor_Pomodoro;
    Float MAX_VALUE = new Float(100.0);
    Float MIN_VALUE = new Float(0.0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        final AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.8F);

        myScrollingValuePicker = (ScrollingValuePicker) findViewById(R.id.myScrollingValuePicker);

        myScrollingValuePicker.setViewMultipleSize(4.5f);
        myScrollingValuePicker.setMaxValue(MIN_VALUE, MAX_VALUE);
        myScrollingValuePicker.setValueTypeMultiple(5);
        myScrollingValuePicker.getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    myScrollingValuePicker.getScrollView().startScrollerTask();
                }
                return false;
            }
        });

        tv_Valor = (TextView)findViewById(R.id.tv_Valor);

        myScrollingValuePicker.setOnScrollChangedListener(new ObservableHorizontalScrollView.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {}

            @Override
            public void onScrollStopped(int l, int t) {
                tv_Valor.setText(DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView() // set TextView
                        , l
                        , t
                        , MAX_VALUE
                        , MIN_VALUE
                        , myScrollingValuePicker.getViewMultipleSize())+" Minutos");

                Valor_Pomodoro = DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView() // set TextView
                        , l
                        , t
                        , MAX_VALUE
                        , MIN_VALUE
                        , myScrollingValuePicker.getViewMultipleSize());
            }
        });
        btn_Iniciar = (Button)findViewById(R.id.btn_Iniciar);
        btn_Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent intent = new Intent(FullscreenActivity.this, MainActivity.class);
                intent.putExtra("Tiempo_Pomodoro",Valor_Pomodoro);
                startActivity(intent);

            }
        });
    }

}
