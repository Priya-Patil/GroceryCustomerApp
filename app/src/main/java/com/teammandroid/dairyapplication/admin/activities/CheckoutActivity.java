package com.teammandroid.dairyapplication.admin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.Network.AuthServices;
import com.teammandroid.dairyapplication.Network.OrderProductServices;
import com.teammandroid.dairyapplication.Network.OrderServices;
import com.teammandroid.dairyapplication.R;

import com.teammandroid.dairyapplication.activities.EditProfileActivity;
import com.teammandroid.dairyapplication.activities.HomepageActivity;
import com.teammandroid.dairyapplication.admin.adapters.CartAdapter;
import com.teammandroid.dairyapplication.admin.adapters.CheckoutCartAdapter;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.interfaces.ApiStatusCallBack;
import com.teammandroid.dairyapplication.model.Response;
import com.teammandroid.dairyapplication.model.UserModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import java.util.ArrayList;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.PMQUANTITY_4;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CheckoutActivity";
    ArrayList<ProductModel> productModelslist;
    DatabaseHelper dbHelper;
    private ImageView back_about;
    /**
     * Delivery
     */
    private TextView tv_delivery;
    /**
     * Payment
     */
    private TextView tv_payment;
    private LinearLayout ll_navigation;
    private ImageView iv_edit;
    /**
     * Delivery Address
     */
    private TextView tv_txt_address;
    /**
     * Delivery
     */
    private TextView tv_name;
    /**
     * Delivery
     */
    private TextView tv_address;
    /**
     * Mobile :
     */
    private TextView tv_mobile;
    private LinearLayout ll_details;
    /**
     * Home
     */
    private RadioButton radio_id1;
    /**
     * Work
     */
    private RadioButton radio_id2;
    /**
     * My Location
     */
    private RadioButton radio_id3;
    private RadioGroup groupradio;
    private CardView cv_list;
    /**
     * Current Location
     */
    private TextView tv_pick_address;
    /**
     * Update Location
     */
    private Button btn_pick;
    private LinearLayout ll_address;
    private CardView cv_pick_location;
    /**
     * Have a Promo Code ?
     */
    private TextView txt_promo;
    private ImageView iv_refresh;
    private TextView tv_promocode;
    /**
     * Apply
     */
    private Button btn_apply_promo;
    private RelativeLayout ll_promocode;
    private CardView cv_promocode;
    /**
     * Product Name
     */
    private TextView tv_product_name;
    /**
     * Qty
     */
    private TextView tv_qty;
    /**
     * Price
     */
    private TextView tv_price;
    /**
     * Subtotal
     */
    private TextView tv_amtsubtotal;
    /**
     * Subotal
     */
    private TextView tv_subtotal;
    /**
     * 0.0
     */
    private TextView tv_subtotalAmt;
    private RelativeLayout rl_one;
    /**
     * Tax
     */
    private TextView tv_total;
    /**
     * 0.0
     */
    private TextView tv_totalAmt;
    private RelativeLayout rl_three;
    /**
     * Delivery Charge
     */
    private TextView tv_deliveryCharge;
    /**
     * 0.0
     */
    private TextView tv_deliveryAmt;
    private RelativeLayout rl_two;
    /**
     * Total Rs 435
     */
    private TextView tv_finalTotalAmt;
    /**
     * Checkout
     */
    private TextView tv_finalTotal;
    private TextView tv_finalTotalAmt1;
    private TextView tv_proceed;
    private RelativeLayout rl_four,ll_layout2;
    private LinearLayout ll_layout1;
    PrefManager prefManager;
    int tquantity;

    ProgressDialog progressDialog;

    String deliveryAmt;
    ArrayList product_array_list;
    Activity  activity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initView();

        activity = CheckoutActivity.this;

        deliveryAmt= tv_deliveryAmt.getText().toString();
        tv_subtotalAmt.setText("₹ "+String.valueOf(prefManager.getgrandTotal()));
        tv_finalTotalAmt1.setText("₹ "+String.valueOf(prefManager.getgrandTotal()));

        tv_finalTotalAmt.setText("₹ "+String.valueOf(prefManager.getgrandTotal()+deliveryAmt));
        productModelslist = showProduct();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cart);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        CheckoutCartAdapter adapter = new CheckoutCartAdapter(this, productModelslist);
        recyclerView.setAdapter(adapter);

        prefManager.setSelect(0);

        Log.e("TAG","prefUserid "+prefManager.getUSER_ID());

        GetUser(prefManager.getUSER_ID());


        // productModelslist = getIntent().getParcelableExtra("productModelslist");
        //Log.d(TAG, "ffff " + productModelslist.getProductid());

    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            default:
                break;
            case R.id.radio_id1:
                cv_pick_location.setVisibility(View.GONE);

                break;

            case R.id.radio_id2:
                cv_pick_location.setVisibility(View.GONE);

                break;

            case R.id.radio_id3:
                Toast.makeText(getApplicationContext(),"fdgdg" ,Toast.LENGTH_LONG).show();
                //if (prefManager.getSelect()==0) {
                    cv_pick_location.setVisibility(View.VISIBLE);
                //}else {
                //    cv_pick_location.setVisibility(View.GONE);
               // }
                break;
        }
    }

    private ArrayList<ProductModel> showProduct() {

        int id = 0;
        int offer = 0;
        int subcategory = 0;
        int productId = 0;
        int isavailable = 0;
        int isactive = 0;
        String title = " ";
        String details = " ";
        String image = " ";
        String created = " ";
        String modified = " ";
        int createdby = 0;
        int modifiedby = 0;
        int RowCount = 0;
        double price = 0.0;
        double ourprice = 0.0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<ProductModel> list = new ArrayList<>();
        productModelslist = list;

        //SELECT sum(count) from quantity where productid = 21;
        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_PRODUCT, null);//+" WHERE "+ QUANTITY_2 +" = "+ item.getProductid(), null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(dbHelper.CUST_1);
            int index2 = cursor.getColumnIndex(dbHelper.CUST_2);
            int index3 = cursor.getColumnIndex(dbHelper.CUST_3);
            int index4 = cursor.getColumnIndex(dbHelper.CUST_4);
            int index5 = cursor.getColumnIndex(dbHelper.CUST_5);
            int index6 = cursor.getColumnIndex(dbHelper.CUST_6);
            int index7 = cursor.getColumnIndex(dbHelper.CUST_7);
            int index8 = cursor.getColumnIndex(dbHelper.CUST_8);
            int index9 = cursor.getColumnIndex(dbHelper.CUST_9);
            int index10 = cursor.getColumnIndex(dbHelper.CUST_10);
            int index11 = cursor.getColumnIndex(dbHelper.CUST_11);
            int index12 = cursor.getColumnIndex(dbHelper.CUST_12);
            int index13 = cursor.getColumnIndex(dbHelper.CUST_13);
            int index14 = cursor.getColumnIndex(dbHelper.CUST_14);
            int index15 = cursor.getColumnIndex(dbHelper.CUST_15);
            int index16 = cursor.getColumnIndex(dbHelper.CUST_16);

            int productid = cursor.getInt(index2);
            title = cursor.getString(index3);
            details = cursor.getString(index4);
            price = cursor.getDouble(index5);
            ourprice = cursor.getDouble(index6);
            offer = cursor.getInt(index7);
            isavailable = cursor.getInt(index8);
            subcategory = cursor.getInt(index9);
            image = cursor.getString(index10);
            isactive = cursor.getInt(index11);
            created = cursor.getString(index12);
            createdby = cursor.getInt(index13);
            modified = cursor.getString(index14);
            modifiedby = cursor.getInt(index15);
            RowCount = cursor.getInt(index16);


            //DataBean bean = new DataBean(id,userId,serviceName,cityName,workingHours,monthlyCharges);
            //list.add(bean);

            ProductModel productModel = new ProductModel(productid, title,
                    details, price, ourprice, offer, isavailable, subcategory, image, isactive, created, createdby, modified
                    , modifiedby, RowCount,0,0);

            list.add(productModel);

        }
        return list;
    }

    void getQuantity(int pid) {
        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

        Cursor cursor = db2.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " + dbHelper.TABLE_PLUS_MINUS_QUANTITY
                + " WHERE " + PMQUANTITY_2 + " = " + pid + " AND " + dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            tquantity = cursor.getInt(0);//to get id, 0 is the column index
            Log.e("getQuantity: ", String.valueOf(tquantity));

        }
        //tv.setText(String.valueOf(id));
        // double price =  item * item.getOurprice();

        //  Log.e( "getQuantity: ", String.valueOf(tquantity));
    }

    private void GetUser(int UserId) {
        try {
            if (Utility.isNetworkAvailable(getApplicationContext())) {

                progressDialog.setTitle("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Log.e(TAG, "GetUser: " + UserId);
                AuthServices.getInstance(getApplicationContext()).
                        GetUserDetails(UserId, new ApiStatusCallBack<ArrayList<UserModel>>() {
                            @Override
                            public void onSuccess(ArrayList<UserModel> response) {
                                progressDialog.dismiss();
                                Log.e("UserModel "," "+response.get(0).getUserid());

                                tv_address.setText("Address : "+response.get(0).getAddress());
                                tv_mobile.setText("Mobile :"+ response.get(0).getMobile());
                                tv_name.setText("Name :"+response.get(0).getFullname());
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e("anErrorOTP",anError.getMessage());
                                Utility.showErrorMessage(CheckoutActivity.this, "Check your network Connection !");
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Utility.showErrorMessage(CheckoutActivity.this, e.getMessage());
                            }
                        });
            } else {
                Utility.showErrorMessage(CheckoutActivity.this, "Could not connect to the internet");
            }
        } catch (Exception ex) {
            //  lyt_progress_reg.setVisibility(View.GONE);
            progressDialog.dismiss();
            //Toast.makeText(EntryProfileActivity.this,"ex "+ex.getMessage() ,Toast.LENGTH_SHORT).show();
            Utility.showErrorMessage(CheckoutActivity.this, ex.getMessage());
        }
    }

    private void initView() {
        tv_finalTotalAmt1 =  findViewById(R.id.tv_finalTotalAmt1);
        back_about = (ImageView) findViewById(R.id.back_about);
        back_about.setOnClickListener(this);
        ll_layout2 =  findViewById(R.id.rl_layout2);
        ll_layout2.setOnClickListener(this);
        ll_layout1 =  findViewById(R.id.ll_layout1);
        ll_layout1.setOnClickListener(this);
        tv_proceed =  findViewById(R.id.tv_proceed);
        tv_proceed.setOnClickListener(this);
        tv_delivery = (TextView) findViewById(R.id.tv_delivery);
        tv_delivery.setOnClickListener(this);
        tv_payment = (TextView) findViewById(R.id.tv_payment);
        tv_payment.setOnClickListener(this);
        ll_navigation = (LinearLayout) findViewById(R.id.ll_navigation);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        tv_txt_address = (TextView) findViewById(R.id.tv_txt_address);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        ll_details = (LinearLayout) findViewById(R.id.ll_details);
        radio_id1 = (RadioButton) findViewById(R.id.radio_id1);
        radio_id1.setOnClickListener(this);
        radio_id2 = (RadioButton) findViewById(R.id.radio_id2);
        radio_id2.setOnClickListener(this);
        radio_id3 = (RadioButton) findViewById(R.id.radio_id3);
        radio_id3.setOnClickListener(this);
        groupradio = (RadioGroup) findViewById(R.id.groupradio);
        groupradio.setOnClickListener(this);
        cv_list = (CardView) findViewById(R.id.cv_list);
        tv_pick_address = (TextView) findViewById(R.id.tv_pick_address);
        tv_pick_address.setOnClickListener(this);
        btn_pick = (Button) findViewById(R.id.btn_pick);
        btn_pick.setOnClickListener(this);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        cv_pick_location = (CardView) findViewById(R.id.cv_pick_location);
        txt_promo = (TextView) findViewById(R.id.txt_promo);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        iv_refresh.setOnClickListener(this);
        tv_promocode = (TextView) findViewById(R.id.tv_promocode);
        btn_apply_promo = (Button) findViewById(R.id.btn_apply_promo);
        btn_apply_promo.setOnClickListener(this);
        ll_promocode = (RelativeLayout) findViewById(R.id.ll_promocode);
        cv_promocode = (CardView) findViewById(R.id.cv_promocode);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_qty = (TextView) findViewById(R.id.tv_qty);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_amtsubtotal = (TextView) findViewById(R.id.tv_amtsubtotal);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_subtotalAmt = (TextView) findViewById(R.id.tv_subtotalAmt);
        rl_one = (RelativeLayout) findViewById(R.id.rl_one);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_totalAmt = (TextView) findViewById(R.id.tv_totalAmt);
        rl_three = (RelativeLayout) findViewById(R.id.rl_three);
        tv_deliveryCharge = (TextView) findViewById(R.id.tv_deliveryCharge);
        tv_deliveryAmt = (TextView) findViewById(R.id.tv_deliveryAmt);
        rl_two = (RelativeLayout) findViewById(R.id.rl_two);
        tv_finalTotalAmt = (TextView) findViewById(R.id.tv_finalTotalAmt);
        tv_finalTotal = (TextView) findViewById(R.id.tv_finalTotal);
        rl_four = (RelativeLayout) findViewById(R.id.rl_four);
        rl_four.setOnClickListener(this);
        tv_finalTotal.setOnClickListener(this);

        progressDialog = new ProgressDialog(CheckoutActivity.this);
        dbHelper = new DatabaseHelper(CheckoutActivity.this);
        prefManager = new PrefManager(CheckoutActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.back_about:
                break;
            case R.id.tv_finalTotal:
                    ll_layout1.setVisibility(View.GONE);
                    ll_layout2.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_payment:
                break;
            case R.id.iv_edit:
                Utility.launchActivity(CheckoutActivity.this, EditProfileActivity.class,false);
                break;
            case R.id.radio_id1:
                break;
            case R.id.radio_id2:
                break;
            case R.id.radio_id3:
                break;
            case R.id.groupradio:
                break;
            case R.id.tv_pick_address:
                break;
            case R.id.btn_pick:
                break;
            case R.id.tv_proceed:
                updateDialog();
                break;
            case R.id.iv_refresh:
                break;
            case R.id.btn_apply_promo:
                break;
            case R.id.rl_four:
                break;
        }
    }

    public void onRadioButtonClicked1(View view) {
    }

    private void updateDialog() {
        final Dialog resultbox = new Dialog(CheckoutActivity.this);
        resultbox.setContentView(R.layout.staus_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_resume = (Button) resultbox.findViewById(R.id.btn_resume);

        TextView tv_title = resultbox.findViewById(R.id.tv_title);
        TextView tv_totalAmt = resultbox.findViewById(R.id.tv_totalAmt);
        TextView txt_ourprice = resultbox.findViewById(R.id.txt_ourprice);
        TextView txt_savedprice = resultbox.findViewById(R.id.txt_savedprice);
        TextView tv_deliveryAmt = resultbox.findViewById(R.id.tv_deliveryAmt);
        RadioGroup radioGroup = resultbox.findViewById(R.id.groupradio);
        RadioButton radio_id1 = resultbox.findViewById(R.id.radio_id1);
        RadioButton radio_id2 = resultbox.findViewById(R.id.radio_id2);
        RadioButton radio_id3 = resultbox.findViewById(R.id.radio_id3);
        EditText et1_address = resultbox.findViewById(R.id.et1_address);
        LinearLayout btn_all = resultbox.findViewById(R.id.btn_all);
        LinearLayout ll_price = resultbox.findViewById(R.id.ll_price);

        tv_totalAmt.setText(tv_deliveryAmt.getText().toString()+prefManager.getour_price());
        txt_ourprice.setText(""+prefManager.getour_price());
        txt_savedprice.setText(""+prefManager.getsaved_price());
        tv_title.setText("Delivery Address");
        radio_id1.setText("Use Profile Address");
        radio_id2.setText("Change Address");

        radio_id3.setVisibility(View.GONE);
        radio_id1.setVisibility(View.VISIBLE);
        ll_price.setVisibility(View.VISIBLE);
        //  et_address.setVisibility(View.GONE);
        btn_finish.setVisibility(View.VISIBLE);
        btn_resume.setVisibility(View.VISIBLE);
        btn_all.setVisibility(View.VISIBLE);

        //  radio_id2.isChecked();

        radio_id1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (prefManager.getaddress() == null) {
                    Toast.makeText(CheckoutActivity.this, "Profile not having address", Toast.LENGTH_SHORT).show();
                } else {
                    et1_address.setVisibility(View.VISIBLE);
                    Log.e("onClick: ", prefManager.getaddress());
                    et1_address.setText(prefManager.getaddress());

                }


            }
        });


        radio_id2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(CheckoutActivity.this)) {

                    et1_address.setVisibility(View.VISIBLE);

                }
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(CheckoutActivity.this)) {

                    // String address  =  et1_address.getText().toString();

                    if (et1_address.getText().toString().equals("")) {

                        Toast.makeText(CheckoutActivity.this, "Enter address", Toast.LENGTH_SHORT).show();
                        //  Utility.showErrorMessage(CheckoutActivity.this,"Please enter all the details !!");
                    } else {
                        placeOrder(0, prefManager.getUSER_ID(), 0, 0,
                                Utility.getCurrentDate(),
                                et1_address.getText().toString(), 0, Double.parseDouble(prefManager.getour_price()),
                                Double.parseDouble(prefManager.getsaved_price()));

                        resultbox.cancel();

                        // updateUserProfile(fullname, address, mobileNo, email);
                    }

                }
                // Utility.launchActivity(EntryProfileActivity.this,HomepageActivity.class,true);
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }


    private void placeOrder(int Orderid, int Userid, int Status, int Deliveryboyid, String Deliverydate,
                            String Address, int Paymentmode, double Totalprice, double Savedprice) {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OrderServices.getInstance(getApplicationContext()).
                createOrder(Orderid, Userid, Status, Deliveryboyid, Deliverydate,
                        Address, Paymentmode, Totalprice, Savedprice, new ApiStatusCallBack<Response>() {
                            @Override
                            public void onSuccess(Response response) {
                                progressDialog.dismiss();
                                Log.e(TAG, "response " + response.getMessage());



                                Log.e("chk: ", product_array_list.toString());
                                for (int i = 0; i < product_array_list.size(); i++) {

                                    Log.e("productlist: ", product_array_list.get(i).toString());
                                    getQuantity(Integer.parseInt(product_array_list.get(i).toString()), response.getResult());
                                  /*  insertOrderProduct(0,response.getResult(),
                                            Integer.parseInt(product_array_list.get(i).toString()),tquantity,1,1);
*/
                                }
                                // imp
                                Utility.launchActivity(CheckoutActivity.this, OrderHistoryActivity.class, true);
                                //Utility.launchActivity(CheckoutActivity.this, EnquiryListActivity.class,false);
                                //  Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Log.e(TAG, "ANError " + anError.getMessage());
                                Utility.showErrorMessage(CheckoutActivity.this, "Server Error", Snackbar.LENGTH_SHORT);
                            }

                            @Override
                            public void onUnknownError(Exception e) {
                                progressDialog.dismiss();
                                Log.e(TAG, "exc " + e.getMessage());
                                Utility.showErrorMessage(CheckoutActivity.this, "Server Error", Snackbar.LENGTH_SHORT);
                            }

                        });

    }

    void insertOrderProduct(int Orderproductid, int Orderid, int Productid, int Quantity, double Totalamount, int LogedinUserId) {
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OrderProductServices.getInstance(CheckoutActivity.this).insertOrderProduct(
                Orderproductid, Orderid, Productid, Quantity, Totalamount, LogedinUserId,
                new ApiStatusCallBack<Response>() {
                    @Override
                    public void onSuccess(Response userModels) {
                        Log.e("ChkOrderdetails:", userModels.getMessage());
                        Log.e("ChkOrderdetails:", userModels.toString());

                        if (userModels.getHasError() == 1) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, userModels.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Utility.showErrorMessage(activity, userModels.getMessage());
                            Utility.launchActivity(CheckoutActivity.this, HomepageActivity.class,true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Utility.showErrorMessage(activity, "Server Error", Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onUnknownError(Exception e) {
                        progressDialog.dismiss();
                        Utility.showErrorMessage(activity, "Server Error", Snackbar.LENGTH_SHORT);
                    }
                });
    }

    void getQuantity(int pid, int oid) {
        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

        Cursor cursor = db2.rawQuery("SELECT SUM (" + PMQUANTITY_4 + ") FROM " + dbHelper.TABLE_PLUS_MINUS_QUANTITY
                + " WHERE " + PMQUANTITY_2 + " = " + pid + " AND " + dbHelper.PMQUANTITY_3 + " = "
                + prefManager.getUSER_ID(), null);

        if (cursor.moveToNext()) {

            tquantity = cursor.getInt(0);//to get id, 0 is the column index
            Log.e("getQuantity: ", String.valueOf(tquantity));

        }
        //tv.setText(String.valueOf(id));
        // double price =  item * item.getOurprice();

        insertOrderProduct(0, oid,
                pid, tquantity, 0, 1);

        //  Log.e( "getQuantity: ", String.valueOf(tquantity));
    }

}
