package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpSincronizacaoItem;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class SincronizacaoDadosListaAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpSincronizacaoItem> _items = null;

    int _pos = -1;

    public SincronizacaoDadosListaAdapter(Context context, ArrayList<tpSincronizacaoItem> items) {

        super();

        _context = context;
        _items = items;
    }

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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View _view = null;
        TextView _txtTabela = null;
        TextView _txtRegistro = null;
        TextView _txtOk = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_sincronizacao_lista, parent, false);
        } else {
            _view = convertView;
        }


        // region Tabela
        _txtTabela = (TextView) _view.findViewById(R.id.txtTabela);
        _txtTabela.setText(_items.get(position).Tabela);
        // endregion

        // region Registro
        _txtRegistro = (TextView) _view.findViewById(R.id.txtRegistro);
        _txtRegistro.setText(_items.get(position).Registro + " registro(s)");
        // endregion

        // region Ok
        _txtOk = (TextView) _view.findViewById(R.id.txtOk);
        if (_items.get(position).Ok) {
            _txtOk.setText("Ok");
        } else {
            _txtOk.setText("");
        }
        // endregion


        return _view;

    }
}
