package br.com.maboo.neext.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import br.com.maboo.neext.R;

public class LinedTextView extends TextView
{
    private int dividerColor;
    private Paint paint;
    
    private boolean check = false;

    public LinedTextView(Context context)
    {
        super(context);
        init(context);
    }

    public LinedTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public LinedTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {
        Resources resources = context.getResources();
        //replace with your color
        dividerColor = resources.getColor(R.color.branco);

        paint = new Paint();
        paint.setColor(dividerColor);
        //replace with your desired width
        paint.setStrokeWidth(resources.getDimension(getWidth()));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawLine(0, getHeight(), getWidth(), 0, paint);
    }
}