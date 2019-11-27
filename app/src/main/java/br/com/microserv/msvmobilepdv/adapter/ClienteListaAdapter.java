package br.com.microserv.msvmobilepdv.adapter;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteListaRow;
import br.com.microserv.msvmobilepdv.R;

import static android.R.attr.resource;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class ClienteListaAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpClienteListaRow> _items = null;


    public ClienteListaAdapter(Context context, ArrayList<tpClienteListaRow> items) {

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

        View _view = null;
        TextView _txtRow1 = null;
        TextView _txtRow2 = null;
        TextView _txtRow3 = null;
        ImageView _imvPedido = null;
        ImageView _imvHistorico = null;

        String _row1 = "";
        String _row2 = "";
        String _row3 = "";


        if (convertView == null) {
            LayoutInflater _li = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _view = _li.inflate(R.layout.row_cliente_lista, parent, false);
        } else {
            _view = convertView;
        }


        // region Cuidando da apresentação das imagens de pedido e histórico
        _imvPedido = (ImageView) _view.findViewById(R.id.imvPedido);
        _imvPedido.setVisibility(View.GONE);

        if (_items.get(position).PedidoQuantidade > 0) {
            _imvPedido.setVisibility(View.VISIBLE);
        }

        _imvHistorico = (ImageView) _view.findViewById(R.id.imvHistorico);
        _imvHistorico.setVisibility(View.GONE);

        if (_items.get(position).HistoricoQuantidade > 0) {
            _imvHistorico.setVisibility(View.VISIBLE);
        }
        // endregion


        // region Compondo as rows da lista de clientes
        _row1 = _items.get(position).NomeFantasia;

        _row2 += _items.get(position).Codigo;
        _row2 += " - ";
        _row2 += _items.get(position).RazaoSocial;

        _row3 += _items.get(position).Cep;
        _row3 += " - ";
        _row3 += _items.get(position).CidadeNome;
        _row3 += " - ";
        _row3 += _items.get(position).EstadoSigla;
        // endregion


        // region Buscando os elementos da lista e preenchendo com os valores
        _txtRow1 = (TextView)_view.findViewById(R.id.txtRow1);
        _txtRow1.setText(_row1);

        _txtRow2 = (TextView)_view.findViewById(R.id.txtRow2);
        _txtRow2.setText(_row2);

        _txtRow3 = (TextView)_view.findViewById(R.id.txtRow3);
        _txtRow3.setText(_row3);
        // endregion

        return _view;

    }
}
