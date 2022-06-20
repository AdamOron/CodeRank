package com.example.coderank.entry;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import androidx.annotation.NonNull;
import com.example.coderank.R;

/**
 * Responsible for fetching an image from either the camera or the gallery.
 * Enters the relevant Activity and eventually returns to the calling Activity.
 * Dismisses itself when finished.
 */
public class ImagePickerDialog extends Dialog
{
	/* Store calling Activity so we can use it's 'startActivityForResult' function */
	private final Activity caller;

	public ImagePickerDialog(@NonNull Activity context)
	{
		super(context);
		/* Save caller Activity */
		this.caller = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_picker_dialog);

		/* Our Dialog has rounded corners, so this statement ensures there will be no white boxy corners. */
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		/* Initialize Dialog's variables */
		initVars();
	}

	private void initVars()
	{
		/* Listen to clicking on camera button, get image from camera if clicked */
		View bCamera = findViewById(R.id.bCamera);
		bCamera.setClickable(true);
		bCamera.setOnClickListener($ -> getImageFromCamera());

		/* Listen to clicking on gallery button, get image from gallery if clicked */
		View bGallery = findViewById(R.id.bGallery);
		bGallery.setClickable(true);
		bGallery.setOnClickListener($ -> getImageFromGallery());
	}

	/**
	 * Gets image from camera.
	 * Enters built-in camera Activity, on result returns to calling Activity.
	 * Dismisses the Dialog.
	 */
	private void getImageFromCamera()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		caller.startActivityForResult(intent, RegisterActivity.CAMERA_REQUEST_CODE);

		dismiss();
	}

	/**
	 * Gets image from gallery.
	 * Enters built-in gallery Activity, on result returns to calling Activity.
	 * Dismisses the Dialog.
	 */
	private void getImageFromGallery()
	{
		Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		caller.startActivityForResult(galleryIntent, RegisterActivity.GALLERY_REQUEST_CODE);

		dismiss();
	}
}
