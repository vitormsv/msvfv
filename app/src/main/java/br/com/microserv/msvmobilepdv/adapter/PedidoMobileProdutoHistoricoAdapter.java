package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpPedidoMobileItemHistorico;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class PedidoMobileProdutoHistoricoAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpPedidoMobileItemHistorico> _items = null;

    int _pos = -1;

    public PedidoMobileProdutoHistoricoAdapter(Context context, ArrayList<tpPedidoMobileItemHistorico> items) {

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

        // region Recuperando o item na lista
        tpPedidoMobileItemHistorico _tp = _items.get(position);
        // endregion

        // region Declarando variáveis auxiliares
        String _aux = null;
        // endregion

        // region Declarando variaveis vinculadas a elementos da view
        View _view = null;

        TextView _txtProdutoCodigo = null;
        TextView _txtProdutoDescricao = null;
        TextView _txtEmbalagemQuantidade = null;
        TextView _txtValorUnitario = null;
        TextView _txtDescontoPercentual = null;
        TextView _txtDescontoValor = null;
        TextView _txtValorUnitarioLiquido = null;
        TextView _txtVendaQuantidade = null;
        TextView _txtValorTotal = null;
        // endregion


        // region Inflando ou recuperando a view
        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_pedido_mobile_produto_historico_adapter, parent, false);
        } else {
            _view = convertView;
        }
        // endregion


        // region Código do produto
        _txtProdutoCodigo = (TextView) _view.findViewById(R.id.txtProdutoCodigo);
        _txtProdutoCodigo.setText(_tp.ProdutoCodigo);
        // endregion

        // region Descrição do produto
        _txtProdutoDescricao = (TextView) _view.findViewById(R.id.txtProdutoDescricao);
        _txtProdutoDescricao.setText(_tp.ProdutoDescricao);
        // endregion

        // region Quantidade do produto por embalagem
        _aux = _tp.UnidadeMedida + " - " + String.valueOf(_tp.PackQuantidade);

        _txtEmbalagemQuantidade = (TextView) _view.findViewById(R.id.txtEmbalagemQuantidade);
        _txtEmbalagemQuantidade.setText(_aux);
        // endregion

        // region Valor unitário do item
        _txtValorUnitario = (TextView) _view.findViewById(R.id.txtValorUnitario);
        _txtValorUnitario.setText(MSVUtil.doubleToText(_tp.UnidadeValor) + " ($)");
        // endregion

        // region Percentual de desconto do item
        _txtDescontoPercentual = (TextView) _view.findViewById(R.id.txtDescontoPercentual);
        _txtDescontoPercentual.setText(MSVUtil.doubleToText(_tp.UnidadeDescontoPercentual) + " (%)");
        // endregion

        // region Valor de desconto do item
        _txtDescontoValor = (TextView) _view.findViewById(R.id.txtDescontoValor);
        _txtDescontoValor.setText(MSVUtil.doubleToText(_tp.UnidadeDescontoValor) + " ($)");
        // endregion

        // region Valor unitário líquido
        _txtValorUnitarioLiquido = (TextView) _view.findViewById(R.id.txtValorUnitarioLiquido);
        _txtValorUnitarioLiquido.setText(MSVUtil.doubleToText(_tp.UnidadeValorLiquido) + " ($)");
        // endregion

        // region Quantidade vendida do item
        _txtVendaQuantidade = (TextView) _view.findViewById(R.id.txtVendaQuantidade);
        _txtVendaQuantidade.setText(String.valueOf(_tp.UnidadeVendaQuantidade));
        // endregion

        // region Valor total do item (Quantidade * ValorUnitarioLiquido)
        _txtValorTotal = (TextView) _view.findViewById(R.id.txtValorTotal);
        _txtValorTotal.setText(MSVUtil.doubleToText("R$", _tp.UnidadeValorTotal) + " ($)");
        // endregion


        return _view;

    }
}
