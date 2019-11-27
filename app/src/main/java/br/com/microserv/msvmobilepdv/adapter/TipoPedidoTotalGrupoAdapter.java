package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class TipoPedidoTotalGrupoAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpTipoPedido> _items = null;

    int _pos = -1;

    public TipoPedidoTotalGrupoAdapter(Context context, ArrayList<tpTipoPedido> items) {

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
        TextView _txtDescricao = null;
        TextView _txtPedidoQuantidade = null;
        TextView _txtValorTotal = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_tipo_pedido_total_grupo, parent, false);
        } else {
            _view = convertView;
        }


        // region Descricao
        _txtDescricao = (TextView) _view.findViewById(R.id.txtDescricao);
        _txtDescricao.setText(_items.get(position).Descricao);
        // endregion

        // region PedidoQuantidade
        _txtPedidoQuantidade = (TextView)_view.findViewById(R.id.txtPedidoQuantidade);
        _txtPedidoQuantidade.setText(String.valueOf(_items.get(position).PedidoQuantidade));
        // endregion

        // region ValorTotal
        _txtValorTotal = (TextView)_view.findViewById(R.id.txtValorTotal);
        _txtValorTotal.setText(MSVUtil.doubleToText("R$", _items.get(position).ValorTotal));
        // endregion


        return _view;

    }
}
