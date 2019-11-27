package br.com.microserv.framework.msvinterface;

import java.util.ArrayList;

import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 01/02/2017.
 */
public interface OnTaskCompleteListner {

    public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut);

}
