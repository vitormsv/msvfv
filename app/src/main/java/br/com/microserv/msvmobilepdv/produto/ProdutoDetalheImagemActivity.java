package br.com.microserv.msvmobilepdv.produto;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.msvmobilepdv.R;

public class ProdutoDetalheImagemActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_ITEM_INDEX = "ItemIndex";
    static final String _KEY_TP_PRODUTO = "tpProduto";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final String _PRODUTO_DETALHE_IMAGEM_VALUE = "ProdutoDetalheImagemActivity";

    // endregion


    // region Declarando as variáveis que vinculam elementos do layout

    // TextView
    TextView _txtEan13 = null;
    TextView _txtCodigo = null;
    TextView _txtDescricao = null;
    TextView _txtGrupo = null;

    // ImageView
    ImageView _imgProdutoImagem = null;

    // endregion


    // region Declarando objetos e variaveis de uso da activity

    // Object
    tpProduto _tpProduto = null;

    // String
    String _sourceActivity = "";

    // int
    int _metodoEdicao = 0;
    int _itemIndex = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe_imagem);
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
                        ProdutoDetalheImagemActivity.this,
                        "O parâmetro _KEY_METODO_EDICAO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ITEM_INDEX)) {
                _itemIndex = _extras.getInt(_KEY_ITEM_INDEX);
            } else {
                Toast.makeText(
                        ProdutoDetalheImagemActivity.this,
                        "O parâmetro _KEY_ITEM_INDEX não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_SOURCE_ACTIVITY)) {
                _sourceActivity = _extras.getString(_KEY_SOURCE_ACTIVITY);
            } else {
                Toast.makeText(
                        ProdutoDetalheImagemActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_TP_PRODUTO)) {
                _tpProduto = (tpProduto) _extras.getSerializable(_KEY_TP_PRODUTO);
            } else {
                Toast.makeText(
                        ProdutoDetalheImagemActivity.this,
                        "O parâmetro _KEY_TP_PRODUTO não foi informado",
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
        _txtEan13 = (TextView) findViewById(R.id.txtEan13);
        _txtCodigo = (TextView) findViewById(R.id.txtCodigo);
        _txtDescricao = (TextView) findViewById(R.id.txtDescricao);
        _txtGrupo = (TextView) findViewById(R.id.txtGrupo);

        // ImageView
        _imgProdutoImagem = (ImageView) findViewById(R.id.imgProdutoImagem);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

    }
    // endregion


    // region initialize
    public void initialize() {

        this.showCabecalho();
        this.showImage();

    }
    // endregion


    // region showCabecalho
    private void showCabecalho() {

        if (_tpProduto != null) {

            _txtEan13.setText(_tpProduto.Ean13);
            _txtCodigo.setText(_tpProduto.Codigo);
            _txtDescricao.setText(_tpProduto.Descricao);

            if (_tpProduto.Grupo != null) {
                _txtGrupo.setText(_tpProduto.Grupo.Descricao);
            }

        }

    }
    // endregion


    // region showImage
    private void showImage() {

        // region Limpando o campo de imagem
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

                Uri _uri = Uri.fromFile(_img);
                _imgProdutoImagem.setImageURI(_uri);

            }
            // endregion

        }
        // endregion

    }
    // endregion

}
