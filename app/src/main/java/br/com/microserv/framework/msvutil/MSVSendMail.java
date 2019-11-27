package br.com.microserv.framework.msvutil;

import android.content.Intent;

/**
 * Created by msvbe on 28/08/2017.
 */

public class MSVSendMail {

    public void send(String to, String subject, StringBuilder content) {

        Intent _i = new Intent(Intent.ACTION_SEND);
        _i.setType("plain/text");
        _i.putExtra(Intent.EXTRA_EMAIL, "");
        _i.putExtra(Intent.EXTRA_SUBJECT, "");
        _i.putExtra(Intent.EXTRA_TEXT, "");



    }

}
