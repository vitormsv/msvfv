package br.com.microserv.framework.msvutil;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by msvbe on 02/10/2019.
 */

public class MSVMaps {

    public static void navigationOnWaze(Context context, String enderecoCompleto) throws ActivityNotFoundException {

        String _url = null;
        Intent _intent = null;


        if (MSVUtil.isPackageManeger(context, "com.waze")) {

            _url = "https://waze.com/ul?q=" + Uri.encode(enderecoCompleto) + "&navigate=yes";

            _intent = new Intent( Intent.ACTION_VIEW, Uri.parse( _url ) );
            _intent.setPackage("com.waze");

            context.startActivity( _intent );

        } else {
            throw new ActivityNotFoundException("O aplicativo WAZE não está instalado neste dispositivo");
        }

    }


    public static void navigationOnGoogleMaps(Context context, String enderecoCompleto) throws ActivityNotFoundException {

        String _url = null;
        Intent _intent = null;


        if (MSVUtil.isPackageManeger(context, "com.google.android.apps.maps")) {

            _url = "google.navigation:q=" + Uri.encode(enderecoCompleto);

            _intent = new Intent( Intent.ACTION_VIEW, Uri.parse( _url ) );
            _intent.setPackage("com.google.android.apps.maps");

            context.startActivity( _intent );

        } else {
            throw new ActivityNotFoundException("O aplicativo GOOGLE MAPS não está instalado neste dispositivo");
        }

    }

}
