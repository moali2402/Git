package dev.vision.rave.listeners;

public interface FileProgressListener {

	/**
	 * Is called when image loading progress changed.
	 *
	 * @param imageUri Image URI
	 * @param view     View for image. Can be <b>null</b>.
	 * @param current  Downloaded size in bytes
	 */
	void onProgressUpdate(int current);
}