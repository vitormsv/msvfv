package br.com.microserv.framework.msvutil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.msvmobilepdv.R;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

/**
 * Created by notemsv01 on 20/04/2017.
 */

public class MSVMsgBox {

    // region Declarando delegates
    private OnCloseDialog _onCloseDialog;
    // endregion


    // region showMsgBoxInfo
    public static void showMsgBoxInfo(Context context, String message) {

        showMsgBoxInfo(context, message, "");

    }

    public static void showMsgBoxInfo(Context context, String message, String description) {

        // region Inflando o layout customizado para o AlertDialog
        // inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_alert, null);

        // cuidando do título da janela
        TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText("INFORMAÇÃO");
        _txtDialogTitle.setBackgroundResource(R.color.indigo_500);

        // cuidando do icone que deverá ser apresentado ao usuário
        ImageView _imvDialogMessage = (ImageView) _v.findViewById(R.id.imvDialogMessage);
        _imvDialogMessage.setImageResource(R.drawable.img_info_48);

        // cuidando da mensagem que será apresentada na janela
        TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // cuidando da mensagem de complemento que poderá ser apresentada na janela
        TextView _txtDialogComplement = (TextView) _v.findViewById(R.id.txtDialogComplement);
        _txtDialogComplement.setVisibility(View.GONE);

        if (MSVUtil.isNullOrEmpty(description) == false) {
            _txtDialogComplement.setVisibility(View.VISIBLE);
            _txtDialogComplement.setText(description);
        }
        // endregion

        // region Criando a janela modal AlertDialog
        final AlertDialog.Builder _builder = new AlertDialog.Builder(context);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // não faz nada
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.show();
        // endregion

    }
    // endregion


    // region showMsgBoxWarning
    public static void showMsgBoxWarning(Context context, String message) {

        showMsgBoxWarning(context, message, "");

    }

    public static void showMsgBoxWarning(Context context, String message, String description) {

        // region Inflando o layout customizado para o AlertDialog
        // inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_alert, null);

        // cuidando do título da janela
        TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText("ATENÇÃO");
        _txtDialogTitle.setBackgroundResource(R.color.orange_500);

        // cuidando do icone que deverá ser apresentado ao usuário
        ImageView _imvDialogMessage = (ImageView) _v.findViewById(R.id.imvDialogMessage);
        _imvDialogMessage.setImageResource(R.drawable.img_warning_48);

        // cuidando da mensagem que será apresentada na janela
        TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // cuidando da mensagem de complemento que poderá ser apresentada na janela
        TextView _txtDialogComplement = (TextView) _v.findViewById(R.id.txtDialogComplement);
        _txtDialogComplement.setVisibility(View.GONE);

        if (MSVUtil.isNullOrEmpty(description) == false) {
            _txtDialogComplement.setVisibility(View.VISIBLE);
            _txtDialogComplement.setText(description);
        }
        // endregion

        // region Criando a janela modal AlertDialog
        final AlertDialog.Builder _builder = new AlertDialog.Builder(context);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // não faz nada
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.show();
        // endregion

    }
    // endregion


    // region showMsgBoxQuestion
    public static void showMsgBoxQuestion(Context context, String message, OnCloseDialog onCloseDialog) {

        showMsgBoxQuestion(context, message, "", onCloseDialog);

    }

    public static void showMsgBoxQuestion(Context context, String message, String description, final OnCloseDialog onCloseDialog) {

        // region Inflando o layout customizado para o AlertDialog
        // inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_alert, null);

        // cuidando do título da janela
        TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText("PERGUNTA");
        _txtDialogTitle.setBackgroundResource(R.color.colorAccent);

        // cuidando do icone que deverá ser apresentado ao usuário
        ImageView _imvDialogMessage = (ImageView) _v.findViewById(R.id.imvDialogMessage);
        _imvDialogMessage.setImageResource(R.drawable.img_question_48);

        // cuidando da mensagem que será apresentada na janela
        TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // cuidando da mensagem de complemento que poderá ser apresentada na janela
        TextView _txtDialogComplement = (TextView) _v.findViewById(R.id.txtDialogComplement);
        _txtDialogComplement.setVisibility(View.GONE);

        if (MSVUtil.isNullOrEmpty(description) == false) {
            _txtDialogComplement.setVisibility(View.VISIBLE);
            _txtDialogComplement.setText(description);
        }
        // endregion

        // region Criando a janela modal AlertDialog
        final AlertDialog.Builder _builder = new AlertDialog.Builder(context);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(true, "");
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.show();
        // endregion

    }
    // endregion


    // region showMsgBoxError
    public static void showMsgBoxError(Context context, String message) {

        showMsgBoxError(context, message, "");

    }

    public static void showMsgBoxError(Context context, String message, String description) {

        // region Inflando o layout customizado para o AlertDialog
        // inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_alert, null);

        // cuidando do título da janela
        TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText("ERRO");
        _txtDialogTitle.setBackgroundResource(R.color.red_500);

        // cuidando do icone que deverá ser apresentado ao usuário
        ImageView _imvDialogMessage = (ImageView) _v.findViewById(R.id.imvDialogMessage);
        _imvDialogMessage.setImageResource(R.drawable.img_error_48);

        // cuidando da mensagem que será apresentada na janela
        TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // cuidando da mensagem de complemento que poderá ser apresentada na janela
        TextView _txtDialogComplement = (TextView) _v.findViewById(R.id.txtDialogComplement);
        _txtDialogComplement.setVisibility(View.GONE);

        if (MSVUtil.isNullOrEmpty(description) == false) {
            _txtDialogComplement.setVisibility(View.VISIBLE);
            _txtDialogComplement.setText(description);
        }
        // endregion

        // region Criando a janela modal AlertDialog
        final AlertDialog.Builder _builder = new AlertDialog.Builder(context);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // não faz nada
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.show();
        // endregion

    }
    // endregion


    // region showMsgBoxList
    public static void showMsgBoxList(
            Context context,
            String title,
            final BaseAdapter baseAdapter,
            final OnCloseDialog onCloseDialog) {

        // region Inflando o layout a ser utilizado

        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

        _lv.setAdapter(baseAdapter);

        // endregion


        // region Construindo a janela de alerta para seleção do item

        AlertDialog.Builder _builder = new AlertDialog.Builder(context);

        _builder.setView(_v);
        _builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onCloseDialog.onCloseDialog(true, "");

            }
        });


        AlertDialog _dialog = _builder.create();
        _dialog.show();

        // endregion

    }
    // endregion


    // region Dialog para preenchimento de valores

    // region getIntValue
    public static void getIntValue(Context context, String title, String message, int oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_inteiro, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        _edtDialogNewValue.setFocusableInTouchMode(true);
        _edtDialogNewValue.requestFocus();
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getLongValue
    public static void getIntValue(Context context, String title, String message, long oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_inteiro, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getDoubleValue
    public static void getDoubleValue(Context context, String title, String message, double oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_decimal, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getStringValue
    public static void getStringValue(Context context, String title, String message, String oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_texto, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getStringNumberValue
    public static void getStringNumberValue(Context context, String title, String message, String oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_texto, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        _edtDialogNewValue.setRawInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_NORMAL);
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        TextView _edtNewItem =(TextView) _v.findViewById(R.id.edtDialogNewValue);
        _edtNewItem.requestFocus();

        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(_edtNewItem, InputMethodManager.SHOW_IMPLICIT);

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getEmailValue
    public static void getEmailValue(Context context, String title, String message, String oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_texto, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        _edtDialogNewValue.setRawInputType( TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getPhoneValue
    public static void getPhoneValue(Context context, String title, String message, String oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_texto, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // preenchendo o valor corrente
        final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
        _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        _edtDialogNewValue.setInputType(InputType.TYPE_CLASS_PHONE);
        // endregion

        // atribuindo o evento do botão de cópia
        final ImageView _imgCopyOldValue = (ImageView) _v.findViewById(R.id.imgCopyOldValue);
        _imgCopyOldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText(_txtDialogCurrentValue.getText().toString());
            }
        });

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion


    // region getPhoneValue
    public static void getPasswordValue(Context context, String title, String message, String oldValue, final OnCloseDialog onCloseDialog) {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_texto, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText(message);

        // selecionando o painel de valor corrente
        final LinearLayout _llyDialogCurrentValue = (LinearLayout) _v.findViewById(R.id.llyDialogCurrentValue);
        _llyDialogCurrentValue.setVisibility(View.GONE);

        if (!oldValue.isEmpty()) {

            _llyDialogCurrentValue.setVisibility(View.VISIBLE);

            // preenchendo o valor corrente
            final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
            _txtDialogCurrentValue.setText(String.valueOf(oldValue));

        }

        // cuidando o campo que permitie a digitação do novo valor
        final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
        _edtDialogNewValue.setText("");
        _edtDialogNewValue.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // endregion

        // atibuindo o evento do botão que limpa o novo valor
        final ImageView _imgClearNewValue = (ImageView) _v.findViewById(R.id.imgClearNewValue);
        _imgClearNewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _edtDialogNewValue.setText("");
            }
        });

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String _newValue = _edtDialogNewValue.getText().toString();

                onCloseDialog.onCloseDialog(true, _newValue);
            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCloseDialog.onCloseDialog(false, "");
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        _dialog.show();
        // endregion

    }
    // endregion

    // endregion


    // region Dialog para seleção de itens em lista

    public static void getValueFromList(
            Context context,
            String title,
            final BaseAdapter baseAdapter,
            final OnSelectedItem onSelectedItem,
            final OnCloseDialog onCloseDialog) {


        // region Inflando o layout a ser utilizado

        LayoutInflater _inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(title);

        ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

        _lv.setAdapter(baseAdapter);

        _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // recuperando o objeto tp selecionado na lista
                tpBase _tp = (tpBase)baseAdapter.getItem(position);

                // atualizando o título da janela de lookup
                String _title = _tp.getListIdentifierValue();

                if (_title != "") {
                    _txtDialogTitle.setText(_tp.getListIdentifierValue());
                }

                // invocando o evento para ser tratado no local onde foi
                // chamado este lookup
                onSelectedItem.onSelectedItem(position, _tp);

            }
        });

        // endregion


        // region Construindo a janela de alerta para seleção do item

        AlertDialog.Builder _builder = new AlertDialog.Builder(context);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onCloseDialog.onCloseDialog(true, "");

            }
        });
        _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // não faz nada
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.show();

        // endregion

    }

    // endregion
}
