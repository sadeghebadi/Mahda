package ir.rayacell.mahda;

import ir.rayacell.mahda.manager.Container;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

public class CustomMapTileProvider implements TileProvider {
	private static final int TILE_WIDTH = 256;
	private static final int TILE_HEIGHT = 256;
	private static final int BUFFER_SIZE = 16 * 1024;

	private AssetManager mAssets;
	HashMap<String, Bitmap> hashMap;

	public CustomMapTileProvider(AssetManager assets) {
		mAssets = assets;
		this.hashMap = hashMap;
	}

	@Override
	public Tile getTile(int x, int y, int zoom) {
		Tile tile = null;
		byte[] image = readTileImage(x, y, zoom);
		if (image != null) {
			tile = new Tile(TILE_WIDTH, TILE_HEIGHT, image);
		} else {
			Drawable d = Container.activity
					.getResources().getDrawable(R.drawable.ic_launcher);
			Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] bitmapdata = stream.toByteArray();
			tile = new Tile(TILE_WIDTH, TILE_HEIGHT, bitmapdata);
		}
		return tile;
	}

	private byte[] readTileImage(int x, int y, int zoom) {
		// InputStream in = null;
		FileInputStream in = null;
		ByteArrayOutputStream buffer = null;

		try {

			// in = mAssets.open(getTileFilename(x, y, zoom));
			// in = new
			// FileInputStream(Environment.getExternalStorageDirectory()
			// .toString()
			// + "sdsdsdpd/0/GoogleMaps/"
			// + zoom
			// + '/'
			// + x
			// + '/' + y + ".png");
			// File f_path = new File(getTileFilename(x, y, zoom));
			// InputStream in = null;
			in = new FileInputStream(getTileFilename(x, y, zoom));
			buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[BUFFER_SIZE];

			while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();

			return buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception ignored) {
				}
			if (buffer != null)
				try {
					buffer.close();
				} catch (Exception ignored) {
				}
		}
	}
	private String getTileFilename(int x, int y, int zoom) {
		String path = Environment.getExternalStorageDirectory().toString()
				+ "/.tilesMahda/" + zoom + '/' + x + '/' + y + ".png";
		return path;
	}
}