package br.com.microserv.framework.msvhold;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 02/02/2017.
 */

public class DialogAlertHold {

    // LinearLayout
    public LinearLayout llyDialogMessage;
    public LinearLayout llyDialogComplement;

    // ImageView
    public ImageView imvDialogMessage;

    // TextView
    public TextView txtDialogTitle;
    public TextView txtDialogMessage;
    public TextView txtDialogComplement;


    // region Construtor
    public DialogAlertHold(View v) {

        if (v != null) {

            // LinearLayout
            llyDialogMessage = (LinearLayout) v.findViewById(R.id.llyDialogMessage);
            llyDialogComplement = (LinearLayout) v.findViewById(R.id.llyDialogComplement);

            // ImageView
            imvDialogMessage = (ImageView) v.findViewById(R.id.imvDialogMessage);

            // TextView
            txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
            txtDialogMessage = (TextView) v.findViewById(R.id.txtDialogMessage);
            txtDialogComplement = (TextView) v.findViewById(R.id.txtDialogComplement);

        }

    }
    // endregion
}
