package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpTransportadora;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class TransportadoraDialogSearchAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpTransportadora> _items = null;

    int _pos = -1;

    public TransportadoraDialogSearchAdapter(Context context, ArrayList<tpTransportadora> items) {

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
        TextView _TextView1 = null;

        String _row1 = "";

        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_one_dialog, parent, false);
        } else {
            _view = convertView;
        }


        // region Compondo as rows da lista de clientes
        _row1 += _items.get(position).Descricao;
        // endregion


        // region Buscando os elementos da lista e preenchendo com os valores
        _TextView1 = (TextView)_view.findViewById(R.id.textView1);
        _TextView1.setText(_row1);
        // endregion


        return _view;

    }
}
