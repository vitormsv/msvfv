package br.com.microserv.msvmobilepdv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpFinanceiro;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by notemsv01 on 26/11/2016.
 */

public class ClienteFinanceiroParcelaAdapter extends BaseAdapter {

    Context _context = null;
    ArrayList<tpFinanceiro> _items = null;


    public ClienteFinanceiroParcelaAdapter(Context context, ArrayList<tpFinanceiro> items) {

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
        TextView _txtNumero = null;
        TextView _txtParcela = null;
        TextView _txtValor = null;
        TextView _txtEmissaoData = null;
        TextView _txtVencimentoData = null;

        try {

            // region Inflando ou aproveitando a view
            if (convertView == null) {
                LayoutInflater _li = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                _view = _li.inflate(R.layout.row_cliente_financeiro_lista, parent, false);
            } else {
                _view = convertView;
            }
            // endregion

            // region Selecionando o objeto tpFinanceiro em questão
            tpFinanceiro _tp = _items.get(position);
            // endregion

            // region Imprimindo o número da documento
            _txtNumero = (TextView) _view.findViewById(R.id.txtNumero);
            _txtNumero.setText(_tp.Numero);
            // endregion

            // region Imprimindo o número da parcela
            _txtParcela = (TextView) _view.findViewById(R.id.txtParcela);
            _txtParcela.setText(_tp.Parcela);
            // endregion

            // region Imprimindo o valor da parcela
            _txtValor = (TextView) _view.findViewById(R.id.txtValor);
            _txtValor.setText(MSVUtil.doubleToText(_tp.Valor));
            // endregion

            // region Imprimindo a data de emissão da parcela
            _txtEmissaoData = (TextView) _view.findViewById(R.id.txtEmissaoData);
            _txtEmissaoData.setText(MSVUtil.ymdhmsTOdmy(_tp.EmissaoData));
            // endregion

            // region Imprimindo a data de vencimento da parcela
            _txtVencimentoData = (TextView) _view.findViewById(R.id.txtVencimentoData);
            _txtVencimentoData.setText(MSVUtil.ymdhmsTOdmy(_tp.VencimentoData));
            // endregion

        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

        return _view;
    }
}
