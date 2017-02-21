package cn.exz.xugaung.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activiy_dialog)
public class DiaLogActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.title)
    private TextView title;
    private Context c = DiaLogActivity.this;

    @ViewInject(R.id.confirm)

    private TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText(getIntent().getStringExtra("message"));
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                setResult(210);
                finish();
                break;

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(210);
    }
}
