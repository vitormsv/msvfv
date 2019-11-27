package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class KeyValueDialogSearchAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpKeyValueRow> _items = null;

    int _pos = -1;

    public KeyValueDialogSearchAdapter(Context context, ArrayList<tpKeyValueRow> items) {

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
        ImageView _imgValue = null;
        TextView _txtValue = null;

        String _row1 = "";

        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_key_value_dialog, parent, false);
        } else {
            _view = convertView;
        }


        // region Compondo as rows da lista de itens
        _row1 += _items.get(position).Value;
        // endregion


        // region Buscando os elementos da lista e preenchendo com os valores
        _txtValue = (TextView)_view.findViewById(R.id.txtValue);
        _txtValue.setText(_row1);
        // endregion


        // region Verificando a necessidade de mostrar a imagem
        _imgValue = (ImageView) _view.findViewById(R.id.imgValue);
        _imgValue.setVisibility(View.GONE);

        if (_items.get(position).ImageResource != 0) {
            _imgValue.setImageResource(_items.get(position).ImageResource);
            _imgValue.setVisibility(View.VISIBLE);
        }
        // endregion

        return _view;

    }
}
