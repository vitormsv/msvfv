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

import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 20/11/2016.
 */

public class PedidoMobileListaAdapter extends BaseAdapter {

    Context _context;
    ArrayList<tpPedidoMobile> _items;


    // region Construtor
    public PedidoMobileListaAdapter(Context context, ArrayList<tpPedidoMobile> items){

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

        View _view = null;
        ImageView _imvConfirmado = null;
        ImageView _imvSincronizado = null;
        TextView _txtNumero = null;
        TextView _txtEmissaoDataHora = null;
        TextView _txtTipoPedidoDescricao = null;
        TextView _txtCondicaoPagamentoDescricao = null;
        TextView _txtItensQuantidade = null;
        TextView _txtTotalValorLiquido = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_pedido_mobile_lista_adapter, parent, false);
        } else {
            _view = convertView;
        }

        try {

            // region Cuidando do campo EhConfirmado
            _imvConfirmado = (ImageView) _view.findViewById(R.id.imvConfirmado);
            _imvConfirmado.setVisibility(View.INVISIBLE);

            if (_items.get(position).EhConfirmado == 1) {
                _imvConfirmado.setVisibility(View.VISIBLE);
            }
            // region

            // region Cuidando do campo EhSincronizado
            _imvSincronizado = (ImageView) _view.findViewById(R.id.imvSincronizado);
            _imvSincronizado.setVisibility(View.INVISIBLE);

            if (_items.get(position).EhSincronizado == 1) {
                _imvSincronizado.setVisibility(View.VISIBLE);
            }
            // region


            // region Cuidando do número do pedido
            _txtNumero = (TextView) _view.findViewById(R.id.txtNumero);
            _txtNumero.setText(_items.get(position).Numero);
            // endregion

            // region Cuidando da data e hora de emissão
            _txtEmissaoDataHora = (TextView) _view.findViewById(R.id.txtEmissaoDataHora);
            _txtEmissaoDataHora.setText(MSVUtil.ymdhmsTOdmy(_items.get(position).EmissaoDataHora));
            // endregion


            // region Cuidando do campo TipoPedidoDescricao
            _txtTipoPedidoDescricao = (TextView) _view.findViewById(R.id.txtTipoPedidoDescricao);
            _txtTipoPedidoDescricao.setText(_items.get(position).TipoPedido.Descricao);
            // endregion


            // region Cuidando do campo CondicaoPagamentoDescricao
            _txtCondicaoPagamentoDescricao = (TextView) _view.findViewById(R.id.txtCondicaoPagamentoDescricao);
            _txtCondicaoPagamentoDescricao.setText(_items.get(position).CondicaoPagamento.Descricao);
            // endregion


            // region Cuidando da quantidade de itens do pedido
            _txtItensQuantidade = (TextView) _view.findViewById(R.id.txtItensQuantidade);

            if (_items.get(position).ItensQuantidade == 1) {
                _txtItensQuantidade.setText(String.valueOf(_items.get(position).ItensQuantidade) + " ITEM");
            } else {
                _txtItensQuantidade.setText(String.valueOf(_items.get(position).ItensQuantidade) + " ITENS");
            }
            // endregion


            // region Cuidando do campo TotalValorLiquido
            _txtTotalValorLiquido = (TextView) _view.findViewById(R.id.txtTotalValorLiquido);
            _txtTotalValorLiquido.setText(MSVUtil.doubleToText("(R$)", _items.get(position).TotalValorLiquido));
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    _context,
                    "Erro ao tentar montar a lista de pedidos",
                    e.getMessage()
            );

        }


        return _view;

    }

}
