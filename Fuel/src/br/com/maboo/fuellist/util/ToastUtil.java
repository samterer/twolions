package br.com.maboo.fuellist.util;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.fuellist.R;

public class ToastUtil {

	View view = null;

	ImageView image_toast;

	Context context;

	@SuppressWarnings("null")
	public ToastUtil(Context context) {
		this.context = context;

		// get your custom_toast.xml ayout
		LayoutInflater inflater = null;// getLayoutInflater();

		try {
			view = inflater.inflate(R.layout.custom_toast, (ViewGroup) context
					.getResources().getLayout(R.id.custom_toast_layout_id));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		image_toast = (ImageView) context.getResources().getLayout(
				R.id.image_toast);

	}

	public void show(int TIPO, String texto) {

		// set a dummy image
		// icon
		switch (TIPO) {
		case Constants.FUEL:
			image_toast.setImageResource(R.drawable.fuel);
			break;
		case Constants.EXPENSE:
			image_toast.setImageResource(R.drawable.expense);
			break;
		case Constants.NOTE:
			image_toast.setImageResource(R.drawable.note);
			break;
		case Constants.REPAIR:
			image_toast.setImageResource(R.drawable.repair);
			break;
		}

		// set a message
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(texto);

		// Toast...
		Toast toast = new Toast(context.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();

	}

	public void show(String texto) {
		// set a message
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(texto);

		// Toast...
		Toast toast = new Toast(context.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();

	}

}
