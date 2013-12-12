package br.com.maboo.node.nodemenubeta.tab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.maboo.node.nodemenubeta.adapter.ListFriendAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.FriendElement;
import com.facebook.model.GraphUser;

public class FragmentFriend extends SherlockFragment {

	private String TAG = "FragmentFriend";

	private ArrayList<FriendElement> itens;
	private List<GraphUser> graphUsers;

	private ListView listview_log;

	private View view;

	private ImageView img;

	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

	private File cacheDir;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		itens = new ArrayList<FriendElement>();

		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"LazyList");
		else
			cacheDir = getActivity().getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	private void queuePhoto(String url, Activity activity, ImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	private void stopThread() {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						Bitmap bmp = getBitmap(photoToLoad.url);
						cache.put(photoToLoad.url, bmp);
						if (((String) photoToLoad.imageView.getTag())
								.equals(photoToLoad.url)) {
							BitmapDisplayer bd = new BitmapDisplayer(bmp,
									photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView
									.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
			else
				imageView.setImageResource(R.drawable.fbprofile);
		}
	}

	private void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmentfriend, container, false);

		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setVisibility(View.INVISIBLE);

		// Bind onclick event handler
		listview_log.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView t = (TextView) view.findViewById(R.id.id);

				sendToProfilePage(t.getText().toString());
			}
		});

		montaLista();

		return view;
	}

	/**
	 * abre a pagina do facebook (profile do usuario clicado)
	 * @param id
	 */
	private void sendToProfilePage(String id) {
		String url = "http://www.facebook.com/" + id;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	/**
	 * recupera a lista de amigos dos sevidores do facebook
	 */
	private void montaLista() {

		Log.i(TAG, "montaLista...");

		Request request = Request.newMyFriendsRequest(
				Session.getActiveSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {

						Log.i(TAG, "onCompleted...");
						graphUsers = users;

						criaLista();

					}
				});
		request.executeAsync();

	}

	/**
	 * monta a lista de amigos de acordo com a lista recuperada
	 * dos servidores do facebook
	 */
	private void criaLista() {
		for (final GraphUser g : graphUsers) {

			Log.i(TAG, "get frind: " + g.getFirstName());

			try {
				itens.add(new FriendElement(null, g.getId().toString(), g
						.getName().toString().toString(), "noob") {
				});
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		}

		randomList();

	}
	
	/**
	 *  random list
	 */
	private void randomList() {

		// a lista de amigos usada tera apenas 10 pessoas conhecidas,s empre de
		// maneira randomica
		Collections.shuffle(itens);

		// this.itens = itens;
		List<FriendElement> friends = new ArrayList<FriendElement>();

		Log.i(TAG, "verificando a lista...");
		for (int i = 0; i < 10; i++) {

			FriendElement b = itens.get(i);

			friends.add(b);
		}
		
		setImgProfile(friends);
	}
	
	@SuppressWarnings("null")
	private void setImgProfile(List<FriendElement> friends) {

		for (int i = 0; i < friends.size(); i++) {
			FriendElement b = friends.get(i);

			// URL bitmapURL;
			Bitmap friendBitmap = null;
			try {
				String url = "https://graph.facebook.com/" + b.getId()
						+ "/picture?width=" + 40 + "&height=" + 40;

				// bitmapURL = new URL(url);
				friendBitmap = getBitmap(url);// BitmapFactory.decodeStream(bitmapURL.openConnection().getInputStream());

				// friendBitmap = getBitmap(url);

				ImageView img = null;
				img.setImageBitmap(friendBitmap);

				if (cache.containsKey(url))
					img.setImageBitmap(cache.get(url));
				else {
					queuePhoto(url, getActivity(), img);
					img.setImageResource(R.drawable.fbprofile);
				}

				b.setIcon(img);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		listview_log.setAdapter(new ListFriendAdapter(FragmentFriend.this,
				friends));
		
		listview_log.setVisibility(View.VISIBLE);

		ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar);
		
		progressBar.setVisibility(View.GONE);

	}

	private Bitmap getBitmap(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	private static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}
