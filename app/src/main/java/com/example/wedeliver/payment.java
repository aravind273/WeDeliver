package com.example.wedeliver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class payment extends AppCompatActivity implements PaymentResultListener {
    public static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    TextView msg,amount;
    Button pay;
    public static String status;
    long sendAmount;
    Uri uri;
    ArrayList<String> ApplicationsPackageNames;
    boolean success=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
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

                if (pay.getText().equals("PAY")) {


                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.payment_dailog);
                    ImageView paytm = dialog.findViewById(R.id.Paytm);
                    ImageView Razorpay = dialog.findViewById(R.id.Razorpay);
                    dialog.show();
                    paytm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            if (amount != null) {
                                if (sendAmount > 0) {
                                    uri = getPayTmUri("sanjeev", "8008915140@paytm", "Thanks", "" + sendAmount);
                                    payWithPayTm(PAYTM_PACKAGE_NAME);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Amount should be greater than 0", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    });
                    Razorpay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startPayment();
                            dialog.dismiss();
                        }
                    });
                }
                else if (pay.getText().equals("Paid")) {
                    Toast.makeText(getApplicationContext(), "AMOUNT PAID", Toast.LENGTH_SHORT).show();
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


    //RazorPay
    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_vcHcOB3jB9VQ2G");
       checkout.setImage(R.drawable.app_logo1);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "We Deliver");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", ""+(sendAmount*100));//pass amount in currency subunits
            options.put("prefill.email", "wedeliver@gmail.com");
            options.put("prefill.contact","7981081830");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Toast.makeText(getApplicationContext(),"Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Transaction successful." +s, Toast.LENGTH_SHORT).show();
        msg.setText("Transaction successful of ₹" + sendAmount);
        pay.setText("Paid");
        msg.setTextColor(GREEN);
        orderDetails orderDetails=(orderDetails) getIntent().getSerializableExtra("object");
        String emaill= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String newemail=emaill.replace(".","");
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference().child("order").child(newemail).child(orderDetails.getDate()+orderDetails.getTime());
        databaseReference.child("payment_mode").setValue("paid through RazorPay");
        success=true;
    }
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show();
        msg.setText("Transaction Failed of ₹" + sendAmount);
        msg.setTextColor(RED);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(success)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
}