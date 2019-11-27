package br.com.microserv.msvmobilepdv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpProdutoSearch;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 18/11/2016.
 */

public class ProdutoSearchAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpProdutoSearch> _items = null;


    public ProdutoSearchAdapter(Context context, ArrayList<tpProdutoSearch> items) {

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
        TextView _txtDescricao = null;
        TextView _txtCodigo = null;
        TextView _txtEan13 = null;
        TextView _txtUnidadeMedida = null;
        TextView _txtProdutoPreco = null;

        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_produto_lista, parent, false);
        } else {
            _view = convertView;
        }

        tpProdutoSearch _tp = _items.get(position);

        // Descricao
        _txtDescricao = (TextView) _view.findViewById(R.id.txtDescricao);
        _txtDescricao.setText(_tp.Descricao);

        // Codigo
        _txtCodigo = (TextView) _view.findViewById(R.id.txtCodigo);
        _txtCodigo.setText(_tp.Codigo);

        // Ean13 (codigo de barras)
        _txtEan13 = (TextView) _view.findViewById(R.id.txtEan13);
        _txtEan13.setText(_tp.Ean13);

        // Unidade de medida
        _txtUnidadeMedida = (TextView) _view.findViewById(R.id.txtUnidadeMedida);
        _txtUnidadeMedida.setText(_tp.UnidadeMedida);

        // Preço do produto
        _txtProdutoPreco = (TextView) _view.findViewById(R.id.txtProdutoPreco);
        _txtProdutoPreco.setText(MSVUtil.doubleToText("R$", _tp.Preco));

        return _view;

    }
}
