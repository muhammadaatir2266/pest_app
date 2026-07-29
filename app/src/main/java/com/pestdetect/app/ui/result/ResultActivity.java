package com.pestdetect.app.ui.result;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.pestdetect.app.R;
import com.pestdetect.app.data.models.Crop;
import com.pestdetect.app.data.models.Pesticide;
import com.pestdetect.app.databinding.ActivityResultBinding;
import com.pestdetect.app.ui.adapters.AffectedCropAdapter;
import com.pestdetect.app.ui.adapters.PesticideAdapter;
import com.pestdetect.app.utils.Constants;
import com.pestdetect.app.utils.LocaleHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onAttach(this);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        String imagePath = getIntent().getStringExtra(Constants.EXTRA_IMAGE_PATH);
        if (imagePath != null) {
            if (imagePath.startsWith("content://")) {
                Glide.with(this).load(Uri.parse(imagePath)).into(binding.ivPestResult);
            } else {
                Glide.with(this).load(new File(imagePath)).into(binding.ivPestResult);
            }
        } else {
            Glide.with(this)
                    .load("https://images.unsplash.com/photo-1590740880194-e6fae853ca6c?w=500")
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivPestResult);
        }

        // Set pest info
        binding.tvPestName.setText("Aphids (Greenflies)");
        binding.tvScientificName.setText("Myzus persicae");
        binding.tvConfidenceScore.setText(getString(R.string.confidence_score, 94));
        binding.tvPestDescription.setText("Small sap-sucking insects that cause leaf curling, stunting, and honeydew mold growth on wheat and vegetable crops.");

        // Harmful badge configuration
        binding.tvHarmfulBadge.setText(R.string.badge_harmful);
        binding.tvHarmfulBadge.setBackgroundResource(R.drawable.bg_badge_harmful);
        binding.tvHarmfulBadge.setTextColor(getColor(R.color.harmful_red));

        // Populate Affected Crops
        binding.rvAffectedCrops.setLayoutManager(new LinearLayoutManager(this));
        List<Crop> dummyCrops = new ArrayList<>();
        dummyCrops.add(createCrop("Wheat", "Sucks sap from tillers and ears, causing severe yield reduction.", "High"));
        dummyCrops.add(createCrop("Tomato", "Transmits plant viruses and secretes honeydew causing black mold.", "Moderate"));
        binding.rvAffectedCrops.setAdapter(new AffectedCropAdapter(dummyCrops));

        // Populate Recommended Pesticides
        binding.rvPesticides.setLayoutManager(new LinearLayoutManager(this));
        List<Pesticide> dummyPesticides = new ArrayList<>();
        dummyPesticides.add(createPesticide(
                "Neem Oil Botanical Extract",
                "Azadirachtin 0.15% EC",
                "organic",
                "5 ml per liter water",
                "Foliar spray early morning or late evening. Repeat every 7 days.",
                "Safe for bees and humans. Eco-friendly organic option."
        ));
        dummyPesticides.add(createPesticide(
                "Imidacloprid 200 SL",
                "Imidacloprid 17.8% SL",
                "chemical",
                "0.5 ml per liter water (50-100 ml/acre)",
                "Foliar spray at early infestation threshold.",
                "Wear protective gloves and mask. Avoid spraying near bees during bloom."
        ));
        binding.rvPesticides.setAdapter(new PesticideAdapter(dummyPesticides));

        // WhatsApp / SMS Share Intent
        binding.btnShare.setOnClickListener(v -> {
            String shareMessage = "Pest Alert from PestDetect App!\nDetected: Aphids (Greenflies)\nHarmful: Yes\nRecommended Pesticide: Neem Oil / Imidacloprid";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share scan result via"));
        });

        // Consult Expert stub
        binding.btnConsultExpert.setOnClickListener(v -> {
            Toast.makeText(this, "Expert consultation feature coming soon! Connecting to Helpline...", Toast.LENGTH_LONG).show();
        });
    }

    private Crop createCrop(String name, String damage, String severity) {
        Crop c = new Crop();
        try {
            java.lang.reflect.Field fName = Crop.class.getDeclaredField("cropName");
            fName.setAccessible(true);
            fName.set(c, name);

            java.lang.reflect.Field fDamage = Crop.class.getDeclaredField("damageDescription");
            fDamage.setAccessible(true);
            fDamage.set(c, damage);

            java.lang.reflect.Field fSev = Crop.class.getDeclaredField("severity");
            fSev.setAccessible(true);
            fSev.set(c, severity);
        } catch (Exception ignored) {}
        return c;
    }

    private Pesticide createPesticide(String name, String ingredient, String type, String dosage, String method, String safety) {
        Pesticide p = new Pesticide();
        try {
            java.lang.reflect.Field fName = Pesticide.class.getDeclaredField("name");
            fName.setAccessible(true);
            fName.set(p, name);

            java.lang.reflect.Field fIng = Pesticide.class.getDeclaredField("activeIngredient");
            fIng.setAccessible(true);
            fIng.set(p, ingredient);

            java.lang.reflect.Field fType = Pesticide.class.getDeclaredField("type");
            fType.setAccessible(true);
            fType.set(p, type);

            java.lang.reflect.Field fDos = Pesticide.class.getDeclaredField("dosage");
            fDos.setAccessible(true);
            fDos.set(p, dosage);

            java.lang.reflect.Field fMeth = Pesticide.class.getDeclaredField("applicationMethod");
            fMeth.setAccessible(true);
            fMeth.set(p, method);

            java.lang.reflect.Field fSaf = Pesticide.class.getDeclaredField("safetyNotes");
            fSaf.setAccessible(true);
            fSaf.set(p, safety);
        } catch (Exception ignored) {}
        return p;
    }
}
