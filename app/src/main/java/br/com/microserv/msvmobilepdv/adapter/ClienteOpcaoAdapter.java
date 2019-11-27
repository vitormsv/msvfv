package br.com.microserv.msvmobilepdv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpClienteOpcao;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 20/11/2016.
 */

public class ClienteOpcaoAdapter extends BaseAdapter {

    Context _context;
    ArrayList<tpPedidoMobile> _items;


    // region Construtor
    public ClienteOpcaoAdapter(Context context, ArrayList<tpPedidoMobile> items){

        super();

        this._context = context;
        this._items = items;

    }
    // endregion


    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return _items.get(position)._id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("getView", "Entrou nesta opção");

        View _view;
        ImageView _imgEhConfirmado = null;
        ImageView _imgEhSincronizado = null;
        TextView _txtNumero = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_cliente_opcao_lista, parent, false);
        } else {
            _view = convertView;
        }


        // region Cuidando do número do pedido
        String _texto = "";
        _texto += _items.get(position).EmissaoDataHora;
        _texto += " - ";
        _texto += _items.get(position).Numero;


        _txtNumero = (TextView)_view.findViewById(R.id.txtNumero);
        _txtNumero.setText(_texto);
        // endregion


        return _view;

    }
}
