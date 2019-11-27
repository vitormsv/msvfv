package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpClienteListaRow;
import br.com.microserv.framework.msvdto.tpProdutoPrecoRow;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class ProdutoDetalhePrecoAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpProdutoPrecoRow> _items = null;


    public ProdutoDetalhePrecoAdapter(Context context, ArrayList<tpProdutoPrecoRow> items) {

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
    public View getView(int position, View convertView, ViewGroup parent) {

        // region Declarando as variáveis locais
        View _view = null;
        TextView _txtEmpresaSigla = null;
        TextView _txtTabelaPrecoDescricao = null;
        TextView _txtProdutoPreco = null;
        // endregion


        // region Inflando o layout de cada linha que será preenchida
        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_produto_detalhe_preco, parent, false);
        } else {
            _view = convertView;
        }
        // endregion


        // region Buscando os elementos da lista e preenchendo com os valores
        _txtEmpresaSigla = (TextView)_view.findViewById(R.id.txtEmpresaSigla);
        _txtEmpresaSigla.setText(_items.get(position).EmpresaSigla);

        _txtTabelaPrecoDescricao = (TextView)_view.findViewById(R.id.txtTabelaPrecoDescricao);
        _txtTabelaPrecoDescricao.setText(_items.get(position).TabelaPrecoDescricao);

        _txtProdutoPreco = (TextView)_view.findViewById(R.id.txtProdutoPreco);
        _txtProdutoPreco.setText(MSVUtil.doubleToText("R$", _items.get(position).ProdutoPreco));
        // endregion

        return _view;

    }
}
