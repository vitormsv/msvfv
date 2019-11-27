package br.com.microserv.msvmobilepdv.produto;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbGrupo;
import br.com.microserv.framework.msvdal.dbLinha;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdto.tpGrupo;
import br.com.microserv.framework.msvdto.tpLinha;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpProdutoPrecoRow;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;

public class ProdutoDetalheActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_ITEM_INDEX = "ItemIndex";
    static final String _KEY_ID_PRODUTO = "IdProduto";
    static final String _KEY_TP_PRODUTO = "tpProduto";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final String _PRODUTO_LISTA_VALUE = "ProdutoListaActivity";

    // endregion


    // region Declarando as variáveis que vinculam elementos do layout

    // TextView
    TextView _txtCodigo = null;
    TextView _txtEan13 = null;
    TextView _txtDescricao = null;
    TextView _txtLinhaDescricao = null;
    TextView _txtGrupoDescricao = null;
    TextView _txtUnidadeMedida = null;
    TextView _txtPackQuantidade = null;
    TextView _txtPesoBruto = null;
    TextView _txtPrecoDescricao = null;
    TextView _txtPrecoValor = null;

    // ImageView
    ImageView _imgProdutoImagem = null;

    // LinearLayout
    LinearLayout _pnlProdutoImagemCnt = null;
    LinearLayout _pnlPrecosCnt = null;
    LinearLayout _pnlPrecoCnt = null;

    // endregion


    // region Declarando objetos e variaveis de uso da activity

    // Object
    tpProduto _tpProduto = null;
    tpGrupo _tpGrupo = null;
    tpLinha _tpLinha = null;
    ArrayList<tpProdutoPrecoRow> _lstProdutoPreco;

    // String
    String _sourceActivity = "";

    // int
    int _metodoEdicao = 0;
    int _itemIndex = 0;

    long _idProduto = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);
        // endregion


        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion


        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            if (_extras.containsKey(_KEY_METODO_EDICAO)) {
                _metodoEdicao = _extras.getInt(_KEY_METODO_EDICAO);
            } else {
                Toast.makeText(
                        ProdutoDetalheActivity.this,
                        "O parâmetro _KEY_METODO_EDICAO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ITEM_INDEX)) {
                _itemIndex = _extras.getInt(_KEY_ITEM_INDEX);
            } else {
                Toast.makeText(
                        ProdutoDetalheActivity.this,
                        "O parâmetro _KEY_ITEM_INDEX não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_SOURCE_ACTIVITY)) {
                _sourceActivity = _extras.getString(_KEY_SOURCE_ACTIVITY);
            } else {
                Toast.makeText(
                        ProdutoDetalheActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ID_PRODUTO)) {
                _idProduto = _extras.getLong(_KEY_ID_PRODUTO);
            } else {
                Toast.makeText(
                        ProdutoDetalheActivity.this,
                        "O parâmetro _KEY_ID_PRODUTO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }
        // endregion


        // region Invocando os metodos de inicialização
        this.bindElements();
        this.bindEvents();
        this.initialize();
        // endregion

    }
    // endregion


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_produto_detalhe, menu);
        return true;

    }
    // endregion


    // region onOptionItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;


        }


        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // TextView
        _txtCodigo = (TextView) findViewById(R.id.txtCodigo);
        _txtEan13 = (TextView) findViewById(R.id.txtEan13);
        _txtDescricao = (TextView) findViewById(R.id.txtDescricao);
        _txtLinhaDescricao = (TextView) findViewById(R.id.txtLinhaDescricao);
        _txtGrupoDescricao = (TextView) findViewById(R.id.txtGrupoDescricao);
        _txtUnidadeMedida = (TextView) findViewById(R.id.txtUnidadeMedida);
        _txtPackQuantidade = (TextView) findViewById(R.id.txtPackQuantidade);
        _txtPesoBruto = (TextView) findViewById(R.id.txtPesoBruto);

        // ImageView
        _imgProdutoImagem = (ImageView) findViewById(R.id.imgProdutoImagem);

        // LinearLayout
        _pnlProdutoImagemCnt = (LinearLayout) findViewById(R.id.pnlProdutoImagemCnt);
        _pnlPrecosCnt = (LinearLayout) findViewById(R.id.pnlPrecosCnt);
        _pnlPrecoCnt = null;

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        _imgProdutoImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle _extras = new Bundle();
                _extras.putInt(_KEY_METODO_EDICAO, _DETAIL_VALUE);
                _extras.putInt(_KEY_ITEM_INDEX, 0);
                _extras.putString(_KEY_SOURCE_ACTIVITY, _PRODUTO_LISTA_VALUE);
                _extras.putSerializable(_KEY_TP_PRODUTO, _tpProduto);

                Intent _i = new Intent(ProdutoDetalheActivity.this, ProdutoDetalheImagemActivity.class);
                _i.putExtras(_extras);

                startActivity(_i);

            }
        });

    }
    // endregion


    // region initialize
    public void initialize() {

        this.loadProduto();
        this.showProduto();
        this.showImage();

    }
    // endregion


    // region loadProduto
    private void loadProduto() {

        SQLiteHelper _sqh = null;
        dbGrupo _dbGrupo = null;
        dbLinha _dbLinha = null;

        try {

            // region Instânciando o objeto de conexão com o banco
            _sqh = new SQLiteHelper(ProdutoDetalheActivity.this);
            _sqh.open(false);
            // endregion

            // region Buscando as informações do produto
            dbProduto _dbProduto = new dbProduto(_sqh);
            _tpProduto = (tpProduto) _dbProduto.getBySourceId(_idProduto);
            // endregion

            // region Buscando demais informações
            if (_tpProduto != null) {

                // region Buscando as informações do grupo de produto
                _dbGrupo = new dbGrupo(_sqh);
                _tpProduto.Grupo = (tpGrupo) _dbGrupo.getBySourceId(_tpProduto.IdGrupo);
                // endregion

                // region Buscando informações da linha do produto
                if (_tpProduto.Grupo != null) {
                    _dbLinha = new dbLinha(_sqh);
                    _tpProduto.Linha = (tpLinha) _dbLinha.getBySourceId(_tpProduto.Grupo.IdLinha);
                }
                // endregion

                // region Buscando os preços para o produto
                _lstProdutoPreco = (ArrayList<tpProdutoPrecoRow>) _dbProduto.getPreco(_tpProduto.IdProduto);
                // endregion

            }
            // endregion

        } catch (Exception e) {

            Toast.makeText(
                    ProdutoDetalheActivity.this,
                    "Ocorreu um erro ao selecionar as informações do produto",
                    Toast.LENGTH_SHORT
            ).show();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }

    }
    // endregion


    // region showProduto
    private void showProduto() {

        // region Limpando o conteúdo dos elementos graficos da tela
        _txtCodigo.setText("");
        _txtEan13.setText("");
        _txtDescricao.setText("");
        _txtLinhaDescricao.setText("");
        _txtGrupoDescricao.setText("");
        _txtUnidadeMedida.setText("");
        _txtPackQuantidade.setText("");
        _txtPesoBruto.setText("");
        // endregion


        // region Apresentando as informações do produtona tela
        if (_tpProduto != null) {

            // region Apresentando as informações basicas
            _txtCodigo.setText(_tpProduto.Codigo);
            _txtEan13.setText(_tpProduto.Ean13);
            _txtDescricao.setText(_tpProduto.Descricao);
            _txtLinhaDescricao.setText(_tpProduto.Linha.Descricao);
            _txtGrupoDescricao.setText(_tpProduto.Grupo.Descricao);
            _txtUnidadeMedida.setText(_tpProduto.UnidadeMedida);
            _txtPackQuantidade.setText(String.valueOf(_tpProduto.PackQuantidade));
            _txtPesoBruto.setText(MSVUtil.doubleToText(_tpProduto.PesoBruto) + " KG");
            // endregion

            // region Apresentando os preços do produto
            if ((_lstProdutoPreco != null) && (_lstProdutoPreco.size() > 0)) {

                for (tpProdutoPrecoRow _tp : _lstProdutoPreco) {

                    // region Inflando o layout
                    View _view = null;

                    LayoutInflater _li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    _view = (View) _li.inflate(R.layout.include_activity_produto_detalhe_preco, null);
                    // endregion

                    // region Apresentando o nome da tabela de preço
                    _txtPrecoDescricao = (TextView) _view.findViewById(R.id.txtPrecoDescricao);

                    if (_txtPrecoDescricao != null) {
                        _txtPrecoDescricao.setText(_tp.TabelaPrecoDescricao);
                    }
                    // endregion

                    // region Apresentando o valor do produto
                    _txtPrecoValor = (TextView) _view.findViewById(R.id.txtPrecoValor);

                    if (_txtPrecoValor != null) {
                        _txtPrecoValor.setText(MSVUtil.doubleToText("R$", _tp.ProdutoPreco));
                    }
                    // endregion

                    // region Adicionando a view na tela
                    _pnlPrecosCnt.addView(_view);
                    // endregion

                }

            }

        }
        // endregion

    }
    // endregion


    // region showImage
    private void showImage() {

        // region Limpando o campo de imagem
        _pnlProdutoImagemCnt.setVisibility(View.GONE);
        _imgProdutoImagem.setImageResource(android.R.color.transparent);
        // endregion

        // region Se a imagem existir então vamos mostrar
        if (_tpProduto != null) {

            // region Formatando o nome do arquivo
            String _nome = _tpProduto.Codigo + ".png";
            // endregion

            // region Se existir o arquivo então vamos mostrar
            File _img = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), _nome);

            if (_img.exists()) {

                _pnlProdutoImagemCnt.setVisibility(View.VISIBLE);

                Uri _uri = Uri.fromFile(_img);
                _imgProdutoImagem.setImageURI(_uri);

            }
            // endregion

        }
        // endregion

    }
    // endregion

}
