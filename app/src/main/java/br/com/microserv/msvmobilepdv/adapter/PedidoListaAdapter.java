package br.com.microserv.msvmobilepdv.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 18/11/2016.
 */

public class PedidoListaAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpPedidoMobile> _items = null;


    public PedidoListaAdapter(Context context, ArrayList<tpPedidoMobile> items) {

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

        View _view = null;

        ImageView _imvConfirmado = null;
        ImageView _imvSincronizado = null;

        TextView _txtNumero = null;
        TextView _txtEmissaoDataHora = null;
        TextView _txtClienteNomeFantasia = null;
        TextView _txtPedidoTipoDescricao = null;
        TextView _txtCondicaoPagamentoDescricao = null;
        TextView _txtItensQuantidade = null;
        TextView _txtTotalLiquidoValor = null;


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_pedido_lista, parent, false);
        } else {
            _view = convertView;
        }

        tpPedidoMobile _tp = _items.get(position);


        // region Confirmado
        _imvConfirmado = (ImageView) _view.findViewById(R.id.imvConfirmado);
        _imvConfirmado.setVisibility(View.INVISIBLE);

        if (_tp.EhConfirmado == 1) {
            _imvConfirmado.setVisibility(View.VISIBLE);
        }
        // endregion

        // region Sincronizado
        _imvSincronizado = (ImageView) _view.findViewById(R.id.imvSincronizado);
        _imvSincronizado.setVisibility(View.INVISIBLE);

        if (_tp.EhSincronizado == 1) {
            _imvSincronizado.setVisibility(View.VISIBLE);
        }
        // endregion


        // region Numero
        _txtNumero = (TextView) _view.findViewById(R.id.txtNumero);
        _txtNumero.setText(_tp.Numero);
        // endregion

        // region EmissaoDataHora
        _txtEmissaoDataHora = (TextView) _view.findViewById(R.id.txtEmissaoDataHora);
        _txtEmissaoDataHora.setText(_tp.EmissaoDataHora);
        // endregion

        // region Cliente
        _txtClienteNomeFantasia = (TextView) _view.findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteNomeFantasia.setText(_tp.Cliente.NomeFantasia);
        // endregion

        // region Tipo de pedido
        _txtPedidoTipoDescricao = (TextView) _view.findViewById(R.id.txtTipoPedidoDescricao);
        _txtPedidoTipoDescricao.setText(_tp.TipoPedido.Descricao);
        // endregion

        // region Condicao de pagamento
        _txtCondicaoPagamentoDescricao = (TextView) _view.findViewById(R.id.txtCondicaoPagamentoDescricao);
        _txtCondicaoPagamentoDescricao.setText(_tp.CondicaoPagamento.Descricao);
        // endregion

        // region Quantidade de itens
        _txtItensQuantidade = (TextView) _view.findViewById(R.id.txtItensQuantidade);
        _txtItensQuantidade.setText(String.valueOf(_tp.ItensQuantidade) + " ITENS");
        // endregion

        // region Valor total (liquido)
        _txtTotalLiquidoValor = (TextView) _view.findViewById(R.id.txtTotalValorLiquido);
        _txtTotalLiquidoValor.setText(MSVUtil.doubleToText("(R$)", _tp.TotalValorLiquido));
        // endregion


        return _view;

    }
}
