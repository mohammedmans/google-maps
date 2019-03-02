package senior.com.locationandmaps.Base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

public class BaseFragment extends Fragment {

    protected BaseActivity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;

    }

    public MaterialDialog ShowMessage(String title, String message) {
        return activity.ShowMessage(title, message);
    }
    public MaterialDialog ShowConfirmationDialog(String title, String message, String posText, String negText
            , MaterialDialog.SingleButtonCallback pos,MaterialDialog.SingleButtonCallback neg){
        return activity.ShowConfirmationDialog(title,message,posText,negText,
        pos,neg);
    }
    public MaterialDialog ShowProgressBar(){
        return activity.ShowProgressBar();

    }

    public void HideProgressBar(){
       activity.HideProgressBar();

    }



}
