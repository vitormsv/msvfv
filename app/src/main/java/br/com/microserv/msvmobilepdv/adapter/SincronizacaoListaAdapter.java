package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvdto.tpSincronizacaoItem;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class SincronizacaoListaAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpSincronizacao> _items = null;

    int _pos = -1;

    public SincronizacaoListaAdapter(Context context, ArrayList<tpSincronizacao> items) {

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
        TextView _txtData = null;
        TextView _txtDia = null;
        TextView _txtPeriodo = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_sincronizacoes_lista, parent, false);
        } else {
            _view = convertView;
        }


        // region Data
        _txtData = (TextView) _view.findViewById(R.id.txtData);
        _txtData.setText(_items.get(position).InicioDataHora);
        // endregion

        // region Dia
        _txtDia = (TextView) _view.findViewById(R.id.txtDia);
        _txtDia.setText("(" + _items.get(position).InicioDataHora + ")");
        // endregion

        // region Periodo
        _txtPeriodo = (TextView) _view.findViewById(R.id.txtPeriodo);
        _txtPeriodo.setText(
                "Início: " + _items.get(position).InicioDataHora + " | " +
                "Termino: " + _items.get(position).TerminoDataHora
        );
        // endregion

        return _view;

    }
}
