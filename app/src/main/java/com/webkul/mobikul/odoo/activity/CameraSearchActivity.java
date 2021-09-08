package com.webkul.mobikul.odoo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.extra.CameraSearchResultAdapter;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.custom.CameraSource;
import com.webkul.mobikul.odoo.custom.CameraSourcePreview;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.custom.GraphicOverlay;
import com.webkul.mobikul.odoo.custom.ImageLabelingProcessor;
import com.webkul.mobikul.odoo.custom.TextRecognitionProcessor;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_CAMERA;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class CameraSearchActivity extends AppCompatActivity {

    public static final String TAG = "CameraSearchActivity";

    public static final String TEXT_DETECTION = "Text Detection";
    public static final String IMAGE_LABEL_DETECTION = "Product Detection";
    private static final int PERMISSION_REQUESTS = 1;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private CameraSource cameraSource = null;
    private RecyclerView resultSpinner;
    private List<FirebaseVisionImageLabel> resultList;
    private ArrayList<String> displayList;
    private CameraSearchResultAdapter displayAdapter;
    private TextView resultNumberTv;
    private LinearLayout resultContainer;
    private boolean hasFlash, showResults;
    private String selectedModel = IMAGE_LABEL_DETECTION;


    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "CameraSearchActivity isPermissionGranted Permission granted : " + permission);
            return true;
        }
        Log.d(TAG, "CameraSearchActivity isPermissionGranted: Permission NOT granted -->" + permission);
        return false;
    }

    @Override
    public void onBackPressed() {
        CameraSearchActivity.this.setResult(RESULT_CANCELED);
        CameraSearchActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_search);

        if (getIntent().hasExtra(BundleConstant.CAMERA_SELECTED_MODEL)) {
            Log.i(TAG, "onCreate: 1");
            selectedModel = getIntent().getStringExtra(BundleConstant.CAMERA_SELECTED_MODEL);
            Log.i(TAG, "onCreate: 2"+ selectedModel);
        }

        if (getSupportActionBar() != null) {
            Log.i(TAG, "onCreate: 2");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(selectedModel);
        }
        showResults = false;

        resultList = new ArrayList<>();
        displayList = new ArrayList<>();
        resultSpinner = (RecyclerView) findViewById(R.id.results_spinner);
        resultSpinner.setLayoutManager(new LinearLayoutManager(CameraSearchActivity.this, RecyclerView.VERTICAL, false));
        displayAdapter = new CameraSearchResultAdapter(CameraSearchActivity.this, displayList);
        resultSpinner.setAdapter(displayAdapter);
        resultContainer = (LinearLayout) findViewById(R.id.resultsContainer);
        resultContainer.getLayoutParams().height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels* 0.50);



        resultNumberTv = (TextView) findViewById(R.id.resultsMessageTv);
        resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "CameraSearchActivity onCreate Preview is null ");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "CameraSearchActivity onCreate graphicOverlay is null ");

        }

        ToggleButton facingSwitch = (ToggleButton) findViewById(R.id.facingswitch);
        if (Camera.getNumberOfCameras() == 2) {
            facingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG, "onCheckedChanged: 1");
                    if (cameraSource != null) {
                        if (isChecked) {
                            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
                        } else {
                            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
                        }
                    }
                    preview.stop();
                    displayList.clear();
                    displayAdapter.notifyDataSetChanged();
                    startCameraSource();
                }
            });
        } else {
            facingSwitch.setEnabled(false);
            facingSwitch.setChecked(false);
            facingSwitch.setVisibility(View.GONE);
        }



        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        ToggleButton flashButton = (ToggleButton) findViewById(R.id.flashSwitch);

        if (hasFlash) {
            flashButton.setVisibility(View.VISIBLE);
            flashButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG, "onCheckedChanged: 2");
                    if (cameraSource != null) {
                        Camera camera = cameraSource.getCamera();
                        Camera.Parameters parameters = camera.getParameters();
                        if (isChecked) {
                            parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
                        } else {
                            parameters.setFlashMode(parameters.FLASH_MODE_OFF);
                        }
                        camera.setParameters(parameters);
                    } else {
                        Toast.makeText(CameraSearchActivity.this,  getString(R.string.error_while_using_flash), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            flashButton.setVisibility(View.GONE);
        }

        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        } else {
            getRuntimePermissions();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "CameraSearchActivity onResume: ");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private void startCameraSource() {
        Log.i(TAG, "startCameraSource: 1");
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "CameraSearchActivity startCameraSource resume: Preview is null ");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "CameraSearchActivity startCameraSource resume: graphOverlay is null ");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.d(TAG, "CameraSearchActivity startCameraSource : Unable to start camera source." + e.getMessage());
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void createCameraSource(String selectedModel) {
        Log.i(TAG, "createCameraSource: 1");
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            switch (selectedModel) {
                case TEXT_DETECTION:
                    cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor(CameraSearchActivity.this));
                    break;
                case IMAGE_LABEL_DETECTION:
                default:
                    cameraSource.setMachineLearningFrameProcessor(new ImageLabelingProcessor(CameraSearchActivity.this));
                    break;
            }
        } catch (Exception e) {
            Log.d(TAG, "CameraSearchActivity createCameraSource can not create camera source: " + e.getCause());
            e.printStackTrace();
        }
    }

    private String[] getRequiredPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                DialogInterface.OnClickListener listener = (dialog, which) -> {
                    ActivityCompat.requestPermissions(
                            this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
                    dialog.dismiss();
                };
                AlertDialogHelper.showPermissionDialog(this, this.getResources().getString(R.string.permission_confirmation), this.getResources().getString(R.string.camera_search), listener);
                return;
            }
           
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (allPermissionsGranted()) {
            Log.i(TAG, "onRequestPermissionsResult: ");
            createCameraSource(selectedModel);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateSpinnerFromResults(List<FirebaseVisionImageLabel> labelList) {
        Log.i(TAG, "updateSpinnerFromResults: 1");
        for (FirebaseVisionImageLabel visionLabel : labelList) {
            displayList.add(visionLabel.getText());
            resultList.add(visionLabel);
        }

        if (!showResults){
            resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
            displayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;

        }
        return true;

    }

    public void updateSpinnerFromTextResults(FirebaseVisionText textresults) {
        List<FirebaseVisionText.TextBlock> blocks = textresults.getTextBlocks();
        for (FirebaseVisionText.TextBlock eachBlock : blocks) {
            for (FirebaseVisionText.Line eachLine : eachBlock.getLines()) {
                if (!displayList.contains(eachLine.getText())){
                    displayList.add(eachLine.getText());
                }
            }
        }

        if (!showResults){
            resultNumberTv.setText(getString(R.string.x_results_found, displayList.size()));
            displayAdapter.notifyDataSetChanged();
        }
    }

    public void sendResultBack(int position) {
        Log.i(TAG, "sendResultBack: 1");
        Intent resultIntent = new Intent();
        resultIntent.putExtra(BundleConstant.CAMERA_SEARCH_HELPER, displayList.get(position));
        CameraSearchActivity.this.setResult(RESULT_OK, resultIntent);
        CameraSearchActivity.this.finish();
    }


    public void showHideResults(View view) {
        Log.i(TAG, "showHideResults: 1");
        showResults = !showResults;
        if (showResults) {

            resultContainer.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                resultNumberTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(CameraSearchActivity.this, R.drawable.ic_up_arrow_grey), null);
            }

        } else {
            resultContainer.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                resultNumberTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(CameraSearchActivity.this, R.drawable.ic_down_arrow_grey), null);
            }
        }

    }

}
