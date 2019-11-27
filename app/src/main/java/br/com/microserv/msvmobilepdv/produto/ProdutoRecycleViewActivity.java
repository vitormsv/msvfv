package br.com.microserv.msvmobilepdv.produto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;


import java.util.List;

import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvutil.DividerItemDecoration;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.ProdutoRecycleViewAdapter;

public class ProdutoRecycleViewActivity extends AppCompatActivity {

    RecyclerView _rcvProduto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_recycle_view);

        _rcvProduto = (RecyclerView) findViewById(R.id.rcvProduto);
        _rcvProduto.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        _rcvProduto.setHasFixedSize(true);

        SQLiteHelper _sqh = new SQLiteHelper(ProdutoRecycleViewActivity.this);
        _sqh.open(false);

        try {
            dbProduto _dbProduto = new dbProduto(_sqh);

            List<tpProduto> _lstProduto = (List<tpProduto>) _dbProduto.getList(tpProduto.class);

            LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            _rcvProduto.setLayoutManager(layout);

            ProdutoRecycleViewAdapter _adp = new ProdutoRecycleViewAdapter(_lstProduto);
            _rcvProduto.setAdapter(_adp);

            //_rcvProduto.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
