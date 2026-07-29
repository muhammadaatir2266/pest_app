package com.pestdetect.app.ui.preview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.pestdetect.app.databinding.ActivityImagePreviewBinding;
import com.pestdetect.app.ui.analyzing.AnalyzingActivity;
import com.pestdetect.app.ui.camera.CameraActivity;
import com.pestdetect.app.utils.Constants;
import com.pestdetect.app.utils.LocaleHelper;
import java.io.File;

public class ImagePreviewActivity extends AppCompatActivity {

    private ActivityImagePreviewBinding binding;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityImagePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imagePath = getIntent().getStringExtra(Constants.EXTRA_IMAGE_PATH);

        if (imagePath != null) {
            if (imagePath.startsWith("content://")) {
                Glide.with(this).load(Uri.parse(imagePath)).into(binding.ivPreview);
            } else {
                Glide.with(this).load(new File(imagePath)).into(binding.ivPreview);
            }
        }

        binding.btnRetake.setOnClickListener(v -> {
            startActivity(new Intent(this, CameraActivity.class));
            finish();
        });

        binding.btnAnalyze.setOnClickListener(v -> {
            Intent intent = new Intent(this, AnalyzingActivity.class);
            intent.putExtra(Constants.EXTRA_IMAGE_PATH, imagePath);
            startActivity(intent);
            finish();
        });
    }
}
