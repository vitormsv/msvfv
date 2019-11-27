package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpRobinHood;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class RobinHoodListaAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpRobinHood> _items = null;

    int _pos = -1;

    public RobinHoodListaAdapter(Context context, ArrayList<tpRobinHood> items) {

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
        TextView _txtLancamentoData = null;
        TextView _txtDia = null;
        TextView _txtHistorico = null;
        TextView _txtClienteNomeFantasia = null;
        TextView _txtPedidoNumero = null;
        TextView _txtCreditoValor = null;
        TextView _txtDebitoValor = null;
        TextView _txtSaldoValor = null;

        double _creditoDebito = 0;
        String _sinal = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_robin_hood_lista, parent, false);
        } else {
            _view = convertView;
        }


        // region Data
        _txtLancamentoData = (TextView) _view.findViewById(R.id.txtLancamentoData);
        _txtLancamentoData.setText(MSVUtil.timeMillisToText(_items.get(position).LancamentoData));
        // endregion

        // region Dia
        _txtDia = (TextView) _view.findViewById(R.id.txtDia);
        _txtDia.setText("(" + MSVUtil.timeMillisToWeekDay(_items.get(position).LancamentoData).toUpperCase() + ")");
        // endregion

        // region Historico
        _txtHistorico = (TextView) _view.findViewById(R.id.txtHistorico);
        _txtHistorico.setText(_items.get(position).Historico.toUpperCase());
        // endregion

        // region ClienteNomeFantasia
        _txtClienteNomeFantasia = (TextView) _view.findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteNomeFantasia.setVisibility(View.GONE);

        if (_items.get(position).ClienteNomeFantasia.length() != 0) {
            _txtClienteNomeFantasia.setVisibility(View.VISIBLE);
            _txtClienteNomeFantasia.setText(_items.get(position).ClienteNomeFantasia);
        }
        // endregion

        // region PedidoNumero
        _txtPedidoNumero = (TextView) _view.findViewById(R.id.txtPedidoNumero);
        _txtPedidoNumero.setVisibility(View.GONE);

        if (_items.get(position).PedidoNumero.length() != 0) {
            _txtPedidoNumero.setVisibility(View.VISIBLE);
            _txtPedidoNumero.setText(_items.get(position).PedidoNumero);
        }
        // endregion

        // region CreditoValor
        _txtCreditoValor = (TextView) _view.findViewById(R.id.txtCreditoValor);
        _txtCreditoValor.setText("");

        if (_items.get(position).CreditoValor != 0 ) {
            _txtCreditoValor.setText(MSVUtil.doubleToText("R$", _items.get(position).CreditoValor));
        }
        // endregion

        // region DebitoValor
        _txtDebitoValor = (TextView) _view.findViewById(R.id.txtDebitoValor);
        _txtDebitoValor.setText("");

        if (_items.get(position).DebitoValor != 0 ) {
            _txtDebitoValor.setText(MSVUtil.doubleToText("R$", _items.get(position).DebitoValor));
        }
        // endregion

        // region SaldoValor
        _txtSaldoValor = (TextView) _view.findViewById(R.id.txtSaldoValor);

        if (_items.get(position).SaldoValor != 0 ) {
            _txtSaldoValor.setText(MSVUtil.doubleToText("R$", _items.get(position).SaldoValor));
        }
        // endregion

        return _view;

    }
}
