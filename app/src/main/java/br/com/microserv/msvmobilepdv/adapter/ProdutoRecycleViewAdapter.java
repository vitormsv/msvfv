package br.com.microserv.msvmobilepdv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.msvmobilepdv.R;

/**
 * Created by msvbe on 18/03/2019.
 */

public class ProdutoRecycleViewAdapter extends RecyclerView.Adapter<ProdutoRecycleViewAdapter.ProdutoRecycleViewHolder> {

    private List<tpProduto> _lstProduto = null;

    public ProdutoRecycleViewAdapter(List<tpProduto> lstProduto) {
        this._lstProduto = lstProduto;
    }

    @Override
    public ProdutoRecycleViewAdapter.ProdutoRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produto_lista, parent, false);

        ProdutoRecycleViewHolder _hoder = new ProdutoRecycleViewHolder(view);

        return _hoder;
    }

    @Override
    public void onBindViewHolder(ProdutoRecycleViewAdapter.ProdutoRecycleViewHolder holder, int position) {

        tpProduto _tp = _lstProduto.get(position);

        holder.txtCodigo.setText(_tp.Codigo);
        holder.txtEan13.setText(_tp.Ean13);
        holder.txtDescricao.setText(_tp.Descricao);
        holder.txtUnidadeMedida.setText(_tp.UnidadeMedida);
        holder.txtProdutoPreco.setText("0,00");

    }

    @Override
    public int getItemCount() {
        return _lstProduto.size();
    }

    public class ProdutoRecycleViewHolder extends RecyclerView.ViewHolder {

        View _v = null;
        public TextView txtCodigo = null;
        public TextView txtEan13 = null;
        public TextView txtDescricao = null;
        public TextView txtUnidadeMedida = null;
        public TextView txtProdutoPreco = null;

        public ProdutoRecycleViewHolder(View itemView) {
            super(itemView);
            _v = itemView;

            txtCodigo = (TextView) _v.findViewById(R.id.txtCodigo);
            txtEan13 = (TextView) _v.findViewById(R.id.txtEan13);
            txtDescricao = (TextView) _v.findViewById(R.id.txtDescricao);
            txtUnidadeMedida = (TextView) _v.findViewById(R.id.txtUnidadeMedida);
            txtProdutoPreco = (TextView) _v.findViewById(R.id.txtProdutoPreco);
        }
    }
}
