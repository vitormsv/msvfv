package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpClienteHistoricoCompra;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class HistoricoCompraDialogAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpClienteHistoricoCompra> _items = null;

    int _pos = -1;

    public HistoricoCompraDialogAdapter(Context context, ArrayList<tpClienteHistoricoCompra> items) {

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

        // region Declarando as variáveis de elementos do layout
        View _view = null;

        TextView _txtPedidoNumero = null;
        TextView _txtData = null;
        TextView _txtQuantidade = null;
        TextView _txtValorUnitarioLiquido = null;
        TextView _txtValorTotal = null;
        // endregion


        // region Inflando o layout
        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_historico_compra_dialog, parent, false);
        } else {
            _view = convertView;
        }
        // endregion


        // region Preenchendo um objeto de historico de compra
        tpClienteHistoricoCompra _tp = _items.get(position);
        // endregion


        // region Preenchendo os elementos do layout

        // Número do pedido
        _txtPedidoNumero = (TextView)_view.findViewById(R.id.txtPedidoNumero);
        _txtPedidoNumero.setText(_tp.PedidoNumero);

        // Data
        _txtData = (TextView)_view.findViewById(R.id.txtData);
        _txtData.setText(_tp.Data);

        // Quantidade
        _txtQuantidade = (TextView)_view.findViewById(R.id.txtQuantidade);
        _txtQuantidade.setText(MSVUtil.doubleToText(_tp.Quantidade));

        // Valor unitário líquido
        _txtValorUnitarioLiquido = (TextView)_view.findViewById(R.id.txtValorUnitarioLiquido);
        _txtValorUnitarioLiquido.setText(MSVUtil.doubleToText("R$", _tp.ValorUnitarioLiquido));

        // Valor total
        _txtValorTotal = (TextView)_view.findViewById(R.id.txtValorTotal);
        _txtValorTotal.setText(MSVUtil.doubleToText("R$", _tp.ValorTotal));

        // endregion


        return _view;

    }
}
