package com.example.furniture.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.furniture.R;

import java.util.Locale;

public class SepayScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sepay_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        Button btnDone = findViewById(R.id.btn_done);
        if (btnDone != null) {
            btnDone.setOnClickListener(v -> finish());
        }

        ImageView ivQrCode = findViewById(R.id.iv_qr_code);
        TextView tvBank = findViewById(R.id.tv_bank);
        TextView tvAccountNumber = findViewById(R.id.tv_account_number);
        TextView tvAmount = findViewById(R.id.tv_amount);
        TextView tvTransferContent = findViewById(R.id.tv_transfer_content);

        String qrCodeUrl = getIntent().getStringExtra("qrCodeUrl");
        String bank = getIntent().getStringExtra("bank");
        String accountNumber = getIntent().getStringExtra("accountNumber");
        String transferContent = getIntent().getStringExtra("transferContent");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        int orderId = getIntent().getIntExtra("orderId", 0);

        if (qrCodeUrl != null && !qrCodeUrl.trim().isEmpty()) {
            if (ivQrCode != null) {
                Glide.with(this)
                        .load(qrCodeUrl)
                        .placeholder(R.drawable.icon_sepay)
                        .error(R.drawable.icon_sepay)
                        .into(ivQrCode);
            }
        }

        if (tvBank != null && bank != null) {
            tvBank.setText("Bank: " + bank);
        }
        if (tvAccountNumber != null && accountNumber != null) {
            tvAccountNumber.setText("Account Number: " + accountNumber);
        }
        if (tvAmount != null) {
            tvAmount.setText(String.format(Locale.US, "Amount: $%,.2f", totalAmount));
        }
        if (tvTransferContent != null && transferContent != null) {
            tvTransferContent.setText("Transfer Content: " + transferContent);
        }
    }
}
