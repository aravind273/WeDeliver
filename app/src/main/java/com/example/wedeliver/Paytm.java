package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.*;

public class Paytm extends AppCompatActivity {
    public static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    TextView msg,amount;
    Button pay;
    public static String status;
    long sendAmount;
    Uri uri;
    ArrayList<String> ApplicationsPackageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        msg=findViewById(R.id.msg);
        amount=findViewById(R.id.amount);
        pay=findViewById(R.id.pay);
        ApplicationsPackageNames=new ArrayList<>();
        getInstalledAppsPackageNames();
        sendAmount=getIntent().getLongExtra("Amount",0);
        amount.setText(""+sendAmount);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pay.getText().equals("PAY")) {
                    if (amount != null) {
                        if (sendAmount > 0) {
                            uri = getPayTmUri("sanjeev", "8008915140@paytm", "Thanks", "" + 1);
                            payWithPayTm(PAYTM_PACKAGE_NAME);

                        } else {
                            Toast.makeText(getApplicationContext(), "Amount should be greater than 0", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else if(pay.getText().equals("PAID"))
                {
                    Toast.makeText(getApplicationContext(),"AMOUNT PAID",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static Uri getPayTmUri(String name, String upiId, String note, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }
    public  void getInstalledAppsPackageNames()
    {
        List<PackageInfo> applicationInfoList=getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for(PackageInfo x:applicationInfoList)
        {
            if(x.packageName.charAt(4)=='a') {
                Log.d("hello", x.packageName);
            }
            ApplicationsPackageNames.add(x.packageName);
        }
    }

    private void payWithPayTm(String packageName) {

//        if (isAppInstalled(this, packageName)) {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(uri);
            i.setPackage(packageName);
            startActivityForResult(i, 0);

//        } else {
//            Toast.makeText(this, "Paytm is not installed Please install and try again.", Toast.LENGTH_SHORT).show();
//        }


    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }
        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(getApplicationContext(), "Transaction successful." + data.getStringExtra("ApprovalRefNo"), Toast.LENGTH_SHORT).show();
            msg.setText("Transaction successful of ₹" + sendAmount);
            pay.setText("Paid");
            msg.setTextColor(GREEN);

        } else {
            Toast.makeText(getApplicationContext(), "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show();
            msg.setText("Transaction Failed of ₹" + sendAmount);
            msg.setTextColor(RED);
        }


    }
}