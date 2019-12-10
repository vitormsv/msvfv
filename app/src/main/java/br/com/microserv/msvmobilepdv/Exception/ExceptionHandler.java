package br.com.microserv.msvmobilepdv.pedido;

import java.io.*;
import android.content.*;

import android.os.Bundle;
import android.os.Process;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

    private Context myContext;
    private Bundle savedInstance;

    public ExceptionHandler(Context context, Bundle savedInstance) {
        this.myContext = context;
        this.savedInstance = savedInstance;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);

        Intent intent = new Intent(myContext, this.myContext.getClass());
        intent.putExtras(this.savedInstance);
        myContext.startActivity(intent);

        Process.killProcess(Process.myPid());
        System.exit(10);

    }
}
