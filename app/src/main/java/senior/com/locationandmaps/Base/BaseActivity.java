package senior.com.locationandmaps.Base;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

public class BaseActivity extends AppCompatActivity {


    protected AppCompatActivity activity;

   public BaseActivity(){
        activity = this;
    }

    MaterialDialog dialog;
    public MaterialDialog ShowMessage(String title, String message){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                /*.positiveText("yes")
                .negativeText("No")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })*/
                .show();

        return dialog;
    }
    public MaterialDialog ShowConfirmationDialog(String title, String message, String posText, String negText
            , MaterialDialog.SingleButtonCallback pos,MaterialDialog.SingleButtonCallback neg){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(posText)
                .negativeText(negText)
                .onNegative(neg)
                .onPositive(pos)
                .show();

        return dialog;
    }
    public MaterialDialog ShowProgressBar(){
        dialog =   new MaterialDialog.Builder(this)
                .content("loading...")
                .progress(true,0)
                .cancelable(false)
                .show();

        return dialog;

    }

    public void HideProgressBar(){
        if(dialog !=null && dialog.isShowing())
            dialog.dismiss();

    }

    public void SaveData(String key,String value){
       SharedPreferences.Editor editor =
               getSharedPreferences("NewsPref",MODE_PRIVATE)
               .edit();
       editor.putString(key,value);
       editor.apply();

    }

    public String getData(String key){
        return getSharedPreferences("NewsPref",MODE_PRIVATE)
                .getString(key,null);
    }




}
