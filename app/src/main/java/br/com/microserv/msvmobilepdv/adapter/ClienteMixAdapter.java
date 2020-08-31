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

import br.com.microserv.framework.msvdto.tpClienteMix;
import br.com.microserv.framework.msvdto.tpClienteProdutoMixRow;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.cliente.ClienteMixActivity;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class ClienteMixAdapter extends BaseAdapter {

    // region Declarando variávies publicas da classe
    Context _context = null;
    ArrayList<tpClienteMix> _items = null;
    // endregion


    // region Método Construtor
    public ClienteMixAdapter(Context context, ArrayList<tpClienteMix> items) {

        super();

        _context = context;
        _items = items;
    }
    // endregion


    // region Métodos Override
    // region Método getCount
    @Override
    public int getCount() {
        return _items.size();
    }
    // endregion

    // region Método getItem
    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }
    // endregion

    // region Método getItemId
    @Override
    public long getItemId(int position) {
        return 0;
    }
    // endregion

    // region Método getView
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // region Declarando elementos de vinculo do layout

        // region View
        View _view = null;
        // endregion

        // region LinearLayout
        LinearLayout _llyPedidoDetalheCnt = null;
        // endregion

        // region ImageView
        ImageView _imvEhItemConfirmado = null;
        // endregion

        // region TextView
        TextView _txtProdutoCodigo = null;
        TextView _txtProdutoEan13 = null;
        TextView _txtProdutoDescricao = null;
        TextView _txtProdutoUnidadeMedida = null;
        TextView _txtProdutoPreco = null;
        TextView _txtEstoqueQuantidade = null;
        TextView _txtPedidoQuantidade = null;
        TextView _txtItemValorTotal = null;
        // endregion

        // endregion


        // region Declarando variáveis do método

        // region Object
        tpClienteMix _item = null;
        // endregion

        // endregion


        try {

            // region Inflando ou recuperando a view
            if (convertView == null) {
                LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                _view = _li.inflate(R.layout.row_cliente_mix_lista, parent, false);
            } else {
                _view = convertView;
            }
            // endregion


            // region Pegando o item em contexto do adapter
            _item = _items.get(position);
            // endregion


            // region Imprimindo os valores nos elementos da activity

            // region Codigo do Produto
            _txtProdutoCodigo = (TextView) _view.findViewById(R.id.txtProdutoCodigo);
            _txtProdutoCodigo.setText(_item.Produto.Codigo);
            // endregion

            // region Codigo de Barras do Produto
            _txtProdutoEan13 = (TextView) _view.findViewById(R.id.txtProdutoEan13);
            _txtProdutoEan13.setText(_item.Produto.Ean13);
            // endregion

            // region Descricao do Produto
            _txtProdutoDescricao = (TextView) _view.findViewById(R.id.txtProdutoDescricao);
            _txtProdutoDescricao.setText(_item.Produto.Descricao);
            // endregion

            // region Unidade de Medida do Produto
            _txtProdutoUnidadeMedida = (TextView) _view.findViewById(R.id.txtProdutoUnidadeMedida);
            _txtProdutoUnidadeMedida.setText(_item.Produto.UnidadeMedida + " - " + String.valueOf(_item.Produto.PackQuantidade));
            // endregion

            // region Unidade de ProdutoPreco
            String sProdutoPreco;
            double ProdutoPreco =  _item.TabelaPrecoProduto.Preco - ( _item.TabelaPrecoProduto.Preco * ( _item.DescontoPadrao / 100 ));

            sProdutoPreco = MSVUtil.doubleToText("R$", ProdutoPreco);

            //Desconto Padrão do Cliente
            if(_item.DescontoPadrao > 0)
            {
                sProdutoPreco = "(-" + MSVUtil.doubleToText(_item.DescontoPadrao) + "%)" + sProdutoPreco;
            }

            _txtProdutoPreco = (TextView) _view.findViewById(R.id.txtProdutoPreco);
            _txtProdutoPreco.setText(sProdutoPreco);
            // endregion

            // region Painel de informações do item confirmado

            // region Vinculando os elementos de tela
            _llyPedidoDetalheCnt = (LinearLayout) _view.findViewById(R.id.llyPedidoDetalheCnt);
            _imvEhItemConfirmado = (ImageView) _view.findViewById(R.id.imvEhItemConfirmado);
            _txtEstoqueQuantidade = (TextView) _view.findViewById(R.id.txtEstoqueQuantidade);
            _txtPedidoQuantidade = (TextView) _view.findViewById(R.id.txtPedidoQuantidade);
            _txtItemValorTotal = (TextView) _view.findViewById(R.id.txtItemValorTotal);
            // endregion

            if (_item.EhItemConfirmado == 1) {

                _imvEhItemConfirmado.setVisibility(View.VISIBLE);

                _llyPedidoDetalheCnt.setVisibility(View.VISIBLE);

                _txtEstoqueQuantidade.setText(String.valueOf(_item.EstoqueQuantidade));
                _txtPedidoQuantidade.setText(String.valueOf(_item.PedidoQuantidade));
                _txtItemValorTotal.setText(MSVUtil.doubleToText(_item.PedidoQuantidade * ProdutoPreco));

            } else {

                _imvEhItemConfirmado.setVisibility(View.INVISIBLE);
                _llyPedidoDetalheCnt.setVisibility(View.GONE);

            }
            // endregion

            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    _context,
                    "Erro dentro do adapter do mix de produto",
                    e.getMessage()
            );
        }


        return _view;
    }
    // endregion
    // endregion


    // region Métodos Personalizados

    // region Método updateItem
    public void updateItem(tpClienteMix item, int position) {

        if (item != null) {
            _items.set(position, item);
        }

        notifyDataSetChanged();

    }
    // endregion

    // region Método updateItems
    public void updateItems(ArrayList<tpClienteMix> items) {

        if ((items != null) && (items.size() > 0)) {
            _items = items;

            notifyDataSetChanged();
        }

    }
    // endregion

    // endregion


    // region Método ClienteMixHolder
    private class ClienteMixHolder {

        // region Declarando os elementos de vinculo com o xml
        LinearLayout _llyPedidoDetalheCnt = null;

        ImageView _imvEhItemConfirmado = null;

        TextView _txtProdutoCodigo = null;
        TextView _txtProdutoEan13 = null;
        TextView _txtProdutoDescricao = null;
        TextView _txtProdutoUnidadeMedida = null;
        TextView _txtProdutoPreco = null;
        TextView _txtEstoqueQuantidade = null;
        TextView _txtPedidoQuantidade = null;
        TextView _txtItemValorTotal = null;
        // endregion

        public ClienteMixHolder(View view) {

            if (view != null) {

                _llyPedidoDetalheCnt = (LinearLayout) view.findViewById(R.id.llyPedidoDetalheCnt);

                _imvEhItemConfirmado = (ImageView) view.findViewById(R.id.imvEhItemConfirmado);

                _txtProdutoCodigo = (TextView) view.findViewById(R.id.txtProdutoCodigo);
                _txtProdutoEan13 = (TextView) view.findViewById(R.id.txtProdutoEan13);
                _txtProdutoDescricao = (TextView) view.findViewById(R.id.txtProdutoDescricao);
                _txtProdutoUnidadeMedida = (TextView) view.findViewById(R.id.txtProdutoUnidadeMedida);
                _txtProdutoPreco = (TextView) view.findViewById(R.id.txtProdutoPreco);
                _txtEstoqueQuantidade = (TextView) view.findViewById(R.id.txtEstoqueQuantidade);
                _txtPedidoQuantidade = (TextView) view.findViewById(R.id.txtPedidoQuantidade);
                _txtItemValorTotal = (TextView) view.findViewById(R.id.txtItemValorTotal);

            }

        }

    }
    // endregion

}
