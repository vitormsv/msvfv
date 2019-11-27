package br.com.microserv.msvmobilepdv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 18/11/2016.
 */

public class PedidoEnviarResultadoAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpPedidoMobile> _items = null;


    public PedidoEnviarResultadoAdapter(Context context, ArrayList<tpPedidoMobile> items) {

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
        return _items.get(position)._id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // region Declarando variaveis para bind de elementos da tela

        // View
        View _view = null;

        // LinearLayout
        LinearLayout _pnlBorda = null;

        // TextView
        TextView _txtNumero = null;
        TextView _txtDataHora = null;
        TextView _txtCliente = null;
        TextView _txtTipoPedido = null;
        TextView _txtValorTotalLiquido = null;

        // endregion

        // region Infalando o layout o recebendo um layout já inflado
        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_pedido_enviar_resultado_adapter, parent, false);
        } else {
            _view = convertView;
        }
        // endregion

        // region Obtendo o objeto específico contido na lista
        tpPedidoMobile _tp = _items.get(position);
        // endregion

        // region Imprimindo os valores nos elementos

        // NUMERO
        _txtNumero = (TextView) _view.findViewById(R.id.txtNumero);
        _txtNumero.setText(_tp.Numero);

        // DATA & HORA
        _txtDataHora = (TextView) _view.findViewById(R.id.txtDataHora);
        _txtDataHora.setText(_tp.EmissaoDataHora);

        // CLIENTE
        _txtCliente = (TextView) _view.findViewById(R.id.txtCliente);
        _txtCliente.setText(_tp.Cliente.NomeFantasia);

        // TIPO de PEDIDO
        _txtTipoPedido = (TextView) _view.findViewById(R.id.txtTipoPedido);
        _txtTipoPedido.setText(_tp.TipoPedido.Descricao);

        // VALOR TOTAL LIQUIDO
        _txtValorTotalLiquido = (TextView) _view.findViewById(R.id.txtValorTotalLiquido);
        _txtValorTotalLiquido.setText(MSVUtil.doubleToText("R$", _tp.TotalValorLiquido));

        // BORDA
        _pnlBorda = (LinearLayout) _view.findViewById(R.id.pnlBorda);
        _pnlBorda.setBackgroundResource(R.color.green_500);

        if (_tp.EhSincronizado == 0) {
            _pnlBorda.setBackgroundResource(R.color.red_600);
        }

        // endregion

        // region Retornando a view preenchida
        return _view;
        // endregion

    }
}
