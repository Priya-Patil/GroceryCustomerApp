package com.teammandroid.dairyapplication.admin.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.squareup.picasso.Picasso;
import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.admin.activities.AddProductActivity;
import com.teammandroid.dairyapplication.admin.model.ProductModel;
import com.teammandroid.dairyapplication.offline.DatabaseHelper;
import com.teammandroid.dairyapplication.utils.Constants;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.teammandroid.dairyapplication.offline.DatabaseHelper.CUST_16;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.CUST_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_1;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_2;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.QUANTITY_4;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.TABLE_PRODUCT;
import static com.teammandroid.dairyapplication.offline.DatabaseHelper.TABLE_QUANTITY;
import static com.teammandroid.dairyapplication.utils.Constants.URL_PRODUCT_IMG;


public class ProductViewListAdapter extends RecyclerView.Adapter<ProductViewListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ProductModel>   HoldersArrayList;
    private  ItemClickListener itemClickListener;
    String categoryName;
    private Activity activity;
    ProgressDialog progressDialog;

    ProductModel expenseCategoryHolder;
    DatabaseHelper dbHelper;
    PrefManager prefManager;

    public ProductViewListAdapter(Activity activity, ArrayList<ProductModel>   HoldersArrayList,
                                  ItemClickListener itemClickListener){

        this.activity = activity;
        this.HoldersArrayList =   HoldersArrayList;
        this.expenseCategoryHolder=expenseCategoryHolder;
        this.itemClickListener=itemClickListener;
    }


    public interface ItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_product, null);

        progressDialog = new ProgressDialog(activity);
        dbHelper = new DatabaseHelper(activity);
        prefManager = new PrefManager(activity);

        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductModel item = HoldersArrayList.get(position);

        //holder.ll_add.setVisibility(View.VISIBLE);

        Log.e("CategoryListAdapter"," "+item.toString());
        holder.tv_title.setText(HoldersArrayList.get(position).getTitle());
        holder.tv_amount.setText(String.valueOf(HoldersArrayList.get(position).getDetails()));
        //holder.tv_reason.setText("Date: "+ExpenseHoldersArrayList.get(position).getDetails());
        Picasso.with(activity)
                .load(URL_PRODUCT_IMG + item.getImagename())
                .into(holder.img_title);

        holder.iv_add.setVisibility(View.GONE);
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*    Bundle bundle=new Bundle();
                   bundle.putParcelable("ProductModel", item);
                   Utility.launchActivity(activity, EditCategoryActivity.class, true, bundle);
                */
            }
        });

         holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                //   bundle.putString("categoryName",categoryName);
                bundle.putParcelable("ProductModel", item);
                Utility.launchActivity(activity, AddProductActivity.class, false,bundle);

            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("CatAdapter ",item.getProductid()+" "+item.getImagename()+" "+item.getTitle()+" "+item.getDetails());
                dialogForDeleteImage(item.getProductid(),item.getTitle(), item.getDetails(),item.getPrice(),
                      item.getOurprice(),item.getOffer(),item.getIsavailable(),item.getSubcategory(),
                        item.getImagename());

            }
        });

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*addProduct(item.getProductid(), item.getTitle(),item.getDetails(),item.getPrice(),item.getOurprice(),
                        item.getOffer(),item.getIsavailable(),item.getSubcategory(),item.getImagename(),item.getIsactive(),
                        item.getCreated(),item.getCreatedby(),item.getModified(),item.getModifiedby(),item.getRowCount());
*/
                addQuantity(item);

                //deleteQuantity(holder.totalview);
            }
        });


    }

    @Override
    public int getItemCount() {
        return   HoldersArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title, tv_amount, tv_reason,totalview;
        CircleImageView img_title;
        LinearLayout ll_add;
        ImageView iv_edit,iv_delete,iv_add,iv_remove,iv_addImg;
        Button btn_add;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_reason = (TextView) itemView.findViewById(R.id.tv_reason);
            totalview = (TextView) itemView.findViewById(R.id.totalview);
            iv_edit =  itemView.findViewById(R.id.iv_edit);
            iv_delete =  itemView.findViewById(R.id.iv_delete);
            iv_add =  itemView.findViewById(R.id.iv_add);
            iv_remove =  itemView.findViewById(R.id.iv_remove);
            iv_addImg =  itemView.findViewById(R.id.iv_addImg);
            ll_add =  itemView.findViewById(R.id.ll_add);
            btn_add =  itemView.findViewById(R.id.btn_add);

            img_title =  itemView.findViewById(R.id.img_title);
            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
        }
    }

    //region Search Filter (setFilter Code)
    public void setFilter(ArrayList<ProductModel> newList) {
        HoldersArrayList = new ArrayList<>();
        HoldersArrayList.addAll(newList);
        notifyDataSetChanged();
    }


    private void addProduct(int Productid, String title,String Details,double Price,double Ourprice,
                            int Offer,int Isavailable,int Subcategory,String imgpath,int isactive,
                            String  created, int createdby,String modified,int modifiedby,int RowCount) {


            boolean isInserted = dbHelper.
                    insertProductInfo(
                            Productid, title, Details, Price, Ourprice,
                            Offer, Subcategory, imgpath, Isavailable,
                            isactive, created, createdby, modified, modifiedby, RowCount);

            if (isInserted == true) {

                int id = 0;
                int productId = 0;


                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //SELECT sum(count) from quantity where productid = 21;
                Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_PRODUCT, null);//+" WHERE "+ QUANTITY_2 +" = "+ item.getProductid(), null);

                if (cursor.moveToNext()) {

                    id = cursor.getInt(0);//to get id, 0 is the column index
                    productId = cursor.getInt(1);
                    title = cursor.getString(2);
                }

                activity.finish();
                activity.overridePendingTransition( 0, 0);
                activity.startActivity(activity.getIntent());
                activity.overridePendingTransition( 0, 0);

                Log.d("ProductviewListAdapter", "productAdapter " + title +" "+productId);
                //Utility.launchActivity(getActivity(), HomepageActivity.class,false);

            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        //}


    }


    private void addQuantity(ProductModel item)
    {

        if (!dbHelper.alreadyExistProductEntry(item.getProductid()))
        {
            boolean isInserted = dbHelper.insertQuantity(
                    item.getProductid(),//prefManager.getUSER_ID()
                    0,
                    1
            );

            if (isInserted == true) {

                int id = 0;
                int productId = 0;
                int userid = 0;

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //SELECT sum(count) from quantity where productid = 21;
                Cursor cursor = db.rawQuery("SELECT SUM (" + QUANTITY_4 + ") FROM " + dbHelper.TABLE_QUANTITY + " WHERE " + QUANTITY_2 + " = " + item.getProductid(), null);

                if (cursor.moveToNext()) {

                    id = cursor.getInt(0);//to get id, 0 is the column index

                }
                //totalview.setText(String.valueOf(id));

                /*  activity.finish();
                activity.overridePendingTransition( 0, 0);
                activity.startActivity(activity.getIntent());
                activity.overridePendingTransition( 0, 0);
    */
                //prefManager.setCOUNT_ID(id);

                addProduct(item.getProductid(), item.getTitle(),item.getDetails(),item.getPrice(),item.getOurprice(),
                        item.getOffer(),item.getIsavailable(),item.getSubcategory(),item.getImagename(),item.getIsactive(),
                        item.getCreated(),item.getCreatedby(),item.getModified(),item.getModifiedby(),item.getRowCount());

                Toast.makeText(activity, "Added to Cart ", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } else
            {
                Toast.makeText(activity,"Go to Cart",Toast.LENGTH_LONG).show();
            }
    }

    private void dialogForDeleteImage(int pid,String title,String Details,double Price,
                                      double Ourprice,int Offer,int Isavailable, int Subcategory,String imgpath) {
        // this.correct = correct;
        final Dialog resultbox = new Dialog(activity);
        resultbox.setContentView(R.layout.delete_subject_dialog);
        // resultbox.setCanceledOnTouchOutside(false);
        Button btn_finish = (Button) resultbox.findViewById(R.id.btn_finish);
        Button btn_cancel = (Button) resultbox.findViewById(R.id.btn_resume);
        TextView text_title =  resultbox.findViewById(R.id.text_title);
        text_title.setText(" Are you sure you want to delete ? ");
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(activity)) {
                    /**********Delete Code****************/
                    AndroidNetworking.upload(Constants.URL_Product)
                            // .addFileToUpload("", "certificate") //Adding file
                            .addMultipartParameter("type", "1")
                            .addMultipartParameter("Action", "0")
                            .addMultipartParameter("Productid", String.valueOf(pid))
                            .addMultipartParameter("Title", title)
                            .addMultipartParameter("Details",Details)
                            .addMultipartParameter("Price", String.valueOf(Price))
                            .addMultipartParameter("Ourprice", String.valueOf(Ourprice))
                            .addMultipartParameter("Offer", String.valueOf(Offer))
                            .addMultipartParameter("Isavailable", String.valueOf(Isavailable))
                            .addMultipartParameter("Subcategory", String.valueOf(Subcategory))
                            .addMultipartParameter("Image", imgpath)
                            //.addMultipartParameter("Imagename", "path")
                            .addMultipartParameter("LogedinUserId", "1")


                            .setTag("uploadTest")
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    resultbox.cancel();
                                    Log.e("adapter1", "uploadImage: totalBytes: " + totalBytes);
                                    Log.e("adapter1", "uploadImage: bytesUploaded: " + bytesUploaded);
                                    progressDialog.setMessage("Deleting File, Please wait...");
                                    progressDialog.show();
                                    progressDialog.setCancelable(false);
                                }
                            })
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.hide();
                                    Log.e("adapter1", "FileonRes: " + response.toString());
                                    //Toast.makeText(activity,"Deleted Succesfully ",Toast.LENGTH_LONG).show();
                                    //Intent intent= new Intent(activity,AdvertisementListActivity.class);
                                    //startActivity(intent);
                                    //rl_img_delete.setVisibility(View.GONE);
                                    resultbox.cancel();
                                    activity.finish();
                                    activity.overridePendingTransition( 0, 0);
                                    activity.startActivity(activity.getIntent());
                                    activity.overridePendingTransition( 0, 0);
                                }

                                @Override
                                public void onError(ANError error) {
                                    resultbox.cancel();
                                    Log.e("adapter1", "FileonError: ", error);
                                    progressDialog.hide();
                                }
                            });
                } else {
                    resultbox.cancel();
                    Toast.makeText(activity, "No Internet Connection",Toast.LENGTH_SHORT).show();                }
            }

            ;

            /**************************************/
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                resultbox.cancel();
            }
        });

        resultbox.show();
    }






}
