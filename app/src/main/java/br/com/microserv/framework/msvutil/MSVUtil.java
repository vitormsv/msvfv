package br.com.microserv.framework.msvutil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.microserv.framework.msvdal.dbSincronizacao;
import br.com.microserv.framework.msvdto.tpAndroidContact;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvhelper.SQLiteHelper;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.content.pm.PackageManager.*;

/**
 * Created by Ricardo on 27/11/2015.
 */
public class MSVUtil {

    public static final String WHATSAPP_INTENT = "com.whatsapp";
    public static final String GMAIL_INTENT = "com.google.android.gm";


    // region Metodos para cuidar de strings

    // region isNullOrBlank
    // Verifica se a String é null ou vazia ou só tem espaços em branco
    public static boolean isNullOrBlank(String s) {
        return (s == null || s.trim().equals(""));
    }
    // endregion

    // region isNullOrEmpty
    // Verifica se a String é null ou vazia
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.equals(""));
    }
    // endregion


    // region repeat
    // Repete o valor informado no parâmetro [character] a quantidade de
    // vezes definida no parâmetro [length]
    public static String repeat(String character, int length) {

        String _out = "";

        for (int i = 0; i <= length; i++) {
            _out += character;
        }

        return _out;
    }
    // endregion

    // region padLeft
    // Alinha o texto a esquerda e completa os espaços com
    // oaracter indicado pelo desenvolvedor
    public static String padLeft(String value, String character, int length){

        String _out = "";

        if (value.length() > length) {
            _out = value.substring(0, length);
        } else {
            _out = value + repeat(character, length - value.length());
        }

        return _out;

    }
    // endregion

    // region padRight
    // Alinha o texto a direita e completa os espaços com
    // oaracter indicado pelo desenvolvedor
    public static String padRight(String value, String character, int length){

        String _out = "";

        if (value.length() > length) {
            _out = value.substring(0, length);
        } else {
            _out = repeat(character, length - value.length()) + value;
        }

        return _out;

    }
    // endregion

    // endregion


    // region intToBool
    public static boolean intToBool(int value) {

        return value == 0 ? false : true;
    }
    // endregion


    // region onlyNumber
    public static String onlyNumber(String in){

        if (in == null){
            return "";
        }


        String _out = "";

        for(char c : in.toCharArray()){
            if (Character.isDigit(c)) {
                _out += String.valueOf(c);
            }
        }

        return _out;
    }
    // endregion


    // region hojeToLong
    public static long hojeToLong() {

        long _out;

        _out = System.currentTimeMillis();

        return _out;

    }

    // endregion


    // region hojeToText
    public static String hojeToText() {

        String _out = null;

        long hoje = System.currentTimeMillis();

        SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");
        _out = _sdf.format(hoje);

        return _out;

    }
    // endregion


    // region timeMillesToDayOfWeek
    public static int timeMillesToDayOfWeek(long timeMilles) {

        int _dayOfWeek;

        try {

            GregorianCalendar _gc = new GregorianCalendar();
            _gc.setTimeInMillis(timeMilles);

            _dayOfWeek = _gc.get(Calendar.DAY_OF_WEEK);

        } catch(Exception e) {
            _dayOfWeek = 0;
        }

        return _dayOfWeek;
    }
    // endregion


    // region booleanToSimNao
    public static String booleanToSimNao(boolean value) {

        String _out;

        _out = value ? "SIM" : "NÃO";

        return _out;
    }
    // endregion


    // region intToSimNao
    public static String intToSimNao(int value) {

        String _out;

        switch (value) {
            case 0:
                _out = "NÃO";
                break;

            case 1:
                _out = "SIM";
                break;

            default:
                _out = "";
        }

        return _out;
    }
    // endregion


    // region parseInt
    public static int parseInt(String value) {

        int _out = 0;

        try{
            _out = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            _out = 0;
        }

        return  _out;

    }
    // endregion


    // region parseInt
    public static int parseInt(double value) {

        int _out = 0;

        try{
            _out = Double.valueOf(value).intValue();
        } catch (Exception e) {
            _out = 0;
        }

        return  _out;

    }
    // endregion


    // region parseDouble
    public static double parseDouble(String value) {

        double _out = 0;

        try{
            _out = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            _out = 0;
        }

        return _out;

    }
    // endregion


    // region Metodos para conversão de dados em tipos SQL

    // region Metodo toSqlText
    public static String toSqlText(Object value){

        return toSqlText(value, "", "");
    }


    public static String toSqlText(Object value, String prefix, String sufix){

        if (prefix == null){
            prefix = "";
        }

        if (sufix == null){
            sufix = "";
        }

        return "'" + prefix + String.valueOf(value) + sufix + "'";
    }
    // endregion


    // region Metodo toSqlInt
    public static String toSqlInt(Object value){

        return String.valueOf(value);

    }
    // endregion


    // region Metodo toSqlDate
    public static String toSqlDate(Object value) throws ParseException{

        String _out;

        SimpleDateFormat _sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date _date = (Date)_sdf.parse(String.valueOf(value));
        _out = String.valueOf(_date.getTime());

        return _out;

    }
    // endregion


    // region Metodo toSqlDecimal
    public static String toSqlDecimal(Object value) throws ParseException{

        String _out;

        String _aux = String.valueOf(value);
        _aux = _aux.replaceAll(".", "");
        _aux = _aux.replaceAll(",", ".");

        _out = String.valueOf(Double.parseDouble(_aux));

        return _out;

    }
    // endregion

    // endregion


    // region Metodos para trabalhar com data e hora

    // region Método tryParseDate
    public Date tryParseDate(String value, String format) {

        Date _out = null;
        SimpleDateFormat _sdf = null;

        try {
            _sdf = new SimpleDateFormat(format);
            _out = _sdf.parse(value);
        } catch (Exception e) {
            _out = null;
        }

        return _out;
    }
    // endregion

    // endregion


    // region Metodos para formatação de valores e demonstação em texto

    // region alignLeft
    public static String alignLeft(String value, int length) throws Exception {

        String _out = "";

        try {

            String _aux = "%-" + String.valueOf(length) + "." + String.valueOf(length) + "s";

            _out = String.format(_aux, value);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return _out;
    }
    // endregion


    // region alignRight
    public static String alignRight(String value, int length) throws Exception {

        String _out = "";

        try {

            String _aux = "%" + String.valueOf(length) + "." + String.valueOf(length) + "s";

            _out = String.format(_aux, value);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return _out;
    }
    // endregion


    // region timeMillisToText
    public static String timeMillisToText(long value) {

        String _out = "";

        Date _data = new Date(value);

        SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");
        _out = _sdf.format(_data);

        return _out;

    }
    // endregion


    // region timeMillisToHour
    public static String timeMillisToHour(long value) {

        String _out = "";

        Date _data = new Date(value);

        SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm:ss");
        _out = _sdf.format(_data);

        return _out;

    }
    // endregion


    // region timeMillisToSqLiteNow
    public static String timeMillisToSqLiteNow(long value) {

        String _out = "";

        Date _data = new Date(value);

        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        _out = _sdf.format(_data);

        return _out;

    }
    // endregion


    // region timeMillisToWeekDay
    public static String timeMillisToWeekDay(long value) {

        String _out = "";

        Locale.setDefault(new Locale("pt", "BR"));

        Date _data = new Date(value);

        SimpleDateFormat _sdf = new SimpleDateFormat("EEEE");
        _out = _sdf.format(_data);

        return _out;

    }
    // endregion


    // region doubleToText
    public static String doubleToText(double value) {

        String _out = "";

        DecimalFormat _df = new DecimalFormat(",##0.00");
        _out = _df.format(value);

        return _out;

    }
    // endregion


    // region doubleToText
    public static String doubleToText(String prefix, double value) {

        String _out = "";

        DecimalFormat _df = new DecimalFormat(",##0.00");
        _out = prefix + " " + _df.format(value);

        return _out;

    }
    // endregion


    // region formatCep
    public static String formatCep(String cep){

        if (TextUtils.isEmpty(cep)){
            return "";
        }


        String _out = "";

        _out = MSVUtil.onlyNumber(cep);


        if (_out.length() == 8) {
            _out = getValueMaskFormat("##.###-###", cep, true);
        }

        return _out;
    }
    // endregion


    // region formatCnpj
    public static String formatCnpj(String cnpj){

        if (TextUtils.isEmpty(cnpj)){
            return "";
        }


        String _out = "";

        _out = MSVUtil.onlyNumber(cnpj);


        if (_out.length() == 14) {
            _out = getValueMaskFormat("##.###.###/####-##", _out, true);
        }

        return _out;
    }
    // endregion


    // region formatCpf
    public static String formatCpf(String cpf){

        if (TextUtils.isEmpty(cpf)){
            return "";
        }


        String _out = "";

        _out = MSVUtil.onlyNumber(cpf);


        if (_out.length() == 11) {
            _out = getValueMaskFormat("###.###.###-##", _out, true);
        }

        return _out;
    }
    // endregion


    // region formatTelefoneFixo
    public static String formatTelefoneFixo(String telefone) {

        String _aux = onlyNumber(telefone);
        String _out = "";

        if (TextUtils.isEmpty(_aux)){
            return _out;
        }

        if (_aux.length() == 10) {
            _out = getValueMaskFormat("(##) ####-####", _aux, true);
        }

        return _out;
    }
    // endregion


    // region formatTelefoneCelular
    public static String formatTelefoneCelular(String telefone) {

        String _aux = onlyNumber(telefone);
        String _out = "";

        if (TextUtils.isEmpty(_aux)){
            return _out;
        }

        if (_aux.length() == 11) {
            _out = getValueMaskFormat("(##) #####-####", _aux, true);
        }

        return _out;
    }
    // endregion


    // region formatIP
    public static String formatIP(String grupo1, String grupo2, String grupo3, String grupo4){

        String _out = "";
        String _aux1 = "";
        String _aux2 = "";
        String _aux3 = "";
        String _aux4 = "";

        _aux1 = MSVUtil.onlyNumber(grupo1);
        _aux2 = MSVUtil.onlyNumber(grupo2);
        _aux3 = MSVUtil.onlyNumber(grupo3);
        _aux4 = MSVUtil.onlyNumber(grupo4);

        _out = _aux1 + "." + _aux2 + "." + _aux3 + "." + _aux4;

        return _out;
    }
    // endregion


    // region getValueMaskFormat
    public static String getValueMaskFormat(String mask, String value, boolean returnEmpty){
		/*
		 * Verifica se se foi configurado para nao retornar a
		 * mascara se a string for nulo ou vazia se nao
		 * retorna somente a mascara.
		 */
        if (returnEmpty == true
                && (value == null || value.trim().equals("")))
            return "";
		/*
		 * Formata valor com a mascara passada
		 */
        for(int i = 0; i < value.length(); i++){
            mask = mask.replaceFirst("#", value.substring(i, i + 1));
        }
		/*
		 * Subistitui por string vazia os digitos restantes da mascara
		 * quando o valor passado é menor que a mascara
		 */
        return mask.replaceAll("#", "");
    }
    // endregion

    // endregion


    // region Metodos que cuidam das preferencias do usuário

    // region savePreference
    public static void savePreference(Context context, String companyInitial, String key, String value) {

        key = companyInitial + "_" + key;

        SharedPreferences _pref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor _editor = _pref.edit();
        _editor.putString(key, value);
        _editor.commit();

    }
    // endregion


    // region readPreference
    public static String readPreference(Context context, String companyInitial, String key, String defaultValue) {

        String _out = defaultValue;

        key = companyInitial + "_" + key;

        SharedPreferences _pref = PreferenceManager.getDefaultSharedPreferences(context);

        if (_pref.contains(key)) {
            _out = _pref.getString(key, defaultValue);
        }

        return _out;

    }
    // endregion

    // endregion


    // region isConnected
    public static boolean isConnected(Context context) {

        boolean _out = false;


        ConnectivityManager _cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo _ni = _cm.getActiveNetworkInfo();

        if ((_ni != null) && (_ni.isConnectedOrConnecting()) && (_ni.isAvailable())) {
            _out = true;
        }


        return _out;

    }
    // endregion


    // region Metodos para validação de informações

    // region isCnpjValid
    public static boolean isCnpjValid(String cnpj) {

        // region Declarando variáveis locais
        boolean _out = false;

        int[] _dv1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] _dv2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int _digito1 = 0;
        int _digito2 = 0;
        int _aux = 0;
        // endregion


        // region Recuperando somente os números informados no parametro
        cnpj = onlyNumber(cnpj);
        // endregion


        // region Se existir exatamente 14 caracteres no parametro então...
        if (cnpj.length() == 14) {

            // region O primeiro passo é calcular o digito 1 e digito 2
            for (int i = 0; i <= 11; i++)
            {
                _aux = Integer.parseInt(cnpj.substring(i, i + 1));

                _digito1 += (_aux * _dv1[i]);
                _digito2 += (_aux * _dv2[i]);
            }
            // endregion


            // region Validando o digito 1
            _digito1 = _digito1 % 11;

            if (_digito1 < 2) {
                _digito1 = 0;
            } else {
                _digito1 = 11 - _digito1;
            }
            // endregion


            // region Validando o digito 2 somente se o 1 for verdadeiro
            if (_digito1 == Integer.parseInt(cnpj.substring(12, 13))) {

                _digito2 += (_digito1 * _dv2[12]);
                _digito2 = _digito2 % 11;

                if (_digito2 < 2) {
                    _digito2 = 0;
                } else {
                    _digito2 = 11 - _digito2;
                }


                if (_digito2 == Integer.parseInt(cnpj.substring(13, 14))) {
                    _out = true;
                }

            }
            // endregion


        }
        // endregion


        // region Entregando o retorno para quem ivocou o método
        return _out;
        // endregion

    }
    // endregion


    // region isCpfValid
    public static boolean isCpfValid(String cpf) {

        // region Declarando variáveis locais
        boolean _out = false;

        int[] _dv1 = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] _dv2 = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

        int _digito1 = 0;
        int _digito2 = 0;
        int _aux = 0;
        // endregion


        // region Separando somente a parte numerica do parametro
        cpf = onlyNumber(cpf);
        // endregion


        // region Validando o cpf se o mesmo possuir 11 caracteres
        if (cpf.length() == 11) {

            // region Calculando os valores para os digitos 1 e2
            for (int i = 0; i <= 8; i++)
            {
                _aux = Integer.parseInt(cpf.substring(i, i + 1));

                _digito1 += (_aux * _dv1[i]);
                _digito2 += (_aux * _dv2[i]);
            }
            // endregion


            // region Validando o digito 1
            _digito1 = _digito1 % 11;

            if (_digito1 < 2) {
                _digito1 = 0;
            } else {
                _digito1 = 11 - _digito1;
            }
            // endregion


            // region Validando o digito 2 caso o digito 1 estiver ok
            if (_digito1 == Integer.parseInt(cpf.substring(9, 10))) {

                _digito2 += (_digito1 * _dv2[9]);
                _digito2 = _digito2 % 11;

                if (_digito2 < 2) {
                    _digito2 = 0;
                } else {
                    _digito2 = 11 -_digito2;
                }

                if (_digito2 == Integer.parseInt(cpf.substring(10, 11))) {
                    _out = true;
                }

            }
            // endregion

        }
        // endregion


        // region Devolvendo o valor para quem invocou o método
        return _out;
        // endregion

    }
    // endregion


    // region isIpValid
    public static boolean isIpValid(String ip) {

        // region Declarando variáveis
        boolean _out = false;
        int _teste = 0;
        // endregion


        // region Montando a primeira explressão regular
        Pattern _p1 = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Matcher _m1 = _p1.matcher(ip);
        // endregion

        // region Montando a segunda expressão regular
        Pattern _p2 = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\:[0-9]{1,5}");
        Matcher _m2 = _p2.matcher(ip);
        // endregion


        // region Combinando a primeira expressão (somente endereço ip)
        if (_m1.find() == true) {
            _teste += 1;
        }
        // endregion

        // region Combinando a segunda expressão (ip e porta)
        if (_m2.find() == true) {
            _teste += 1;
        }
        // endregion


        // region Calculando a resposta
        if (_teste > 0) {
            _out = true;
        }
        // endregion


        // region Devolvendo o resultado
        return _out;
        // endregion

    }
    // endregion

    // endregion


    // region contactExist
    public static tpAndroidContact contactExist(Context context, String phoneNumber) throws Exception {

        // region Declarando variáveis
        tpAndroidContact _out = null;
        // endregion

        // region Normalizando o número do telefone
        phoneNumber = onlyNumber(phoneNumber);
        // endregion

        // region Se o número do telefone for válido então...
        if ((phoneNumber.length() == 10) || (phoneNumber.length() == 11)) {

            Uri lookupUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(phoneNumber)
            );

            String[] aQuery = {
                    ContactsContract.PhoneLookup._ID,
                    ContactsContract.PhoneLookup.NUMBER,
                    ContactsContract.PhoneLookup.DISPLAY_NAME
            };

            Cursor _c = null;

            try {

                _c = context.getContentResolver().query(lookupUri, aQuery, null, null, null);

                if ((_c != null) && (_c.moveToFirst())) {

                    _out = new tpAndroidContact();
                    _out._ID = _c.getLong(_c.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                    _out.DISPLAY_NAME = _c.getString(_c.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    _out.NUMBER = _c.getString(_c.getColumnIndexOrThrow(ContactsContract.PhoneLookup.NUMBER));

                }

            } catch (Exception e) {
                throw new Exception("MSVUtil | contactExist | " + e.getMessage());
            } finally {
                if (_c != null) {
                    _c.close();
                }
            }

        }
        // endregion

        // region Retornando o valor para quem invocou o método
        return _out;
        // endregion

    }
    // endregion


    //region checkPermission

    public static void checkPermission(Context context, String... permissions)
    {

                ActivityCompat.requestPermissions((Activity)context,
                        permissions,
                        0);


    }
    //endregion


    // region checkSinchronization
    public static boolean checkSinchronization(Context context) {

        boolean _out = false;

        /*
        Date _today = new Date();
        Date _currentDate = null;

        SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");

        SQLiteHelper _sqh = null;
        dbSincronizacao _dbSincronizacao;
        tpSincronizacao _tpSincronizacao;


        try {

            _sqh = new SQLiteHelper(context);
            _sqh.open(false);

            _dbSincronizacao = new dbSincronizacao(_sqh);
            _tpSincronizacao = _dbSincronizacao.getLast();

            if (_tpSincronizacao != null) {
                _currentDate = new Date(_tpSincronizacao.TerminoDataHora);

                if (_sdf.format(_today).equalsIgnoreCase(_sdf.format(_currentDate))) {
                    _out = true;
                }
            }

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    context,
                    "Erro no método checkSinchronization da classe MSVUtil",
                    e.getMessage()
            );

        } finally {
            if (_sqh != null) {
                if (_sqh.isOpen()) {
                    _sqh.close();
                }
            }
        }
        */


        /*
         * O método todo foi comentado para devolver a informação padrão
         * de que o dispositivo está sempre em dia com a sincronização
         * Isso foi feito a pedido do Biazon e por mim (Ricardo) alterado em
         * 13/11/219 as 13:02
         */
        _out = true;

        return _out;

    }
    // endregion


    // region Método vibrate
    public static void vibrate(Context context) {
        ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
    }
    // endregion


    // region Novos metodos de data (utilizados para gravar em formato texto)

    // region Método sqliteHojeHora
    public static String sqliteHojeHora() {

        String _out = null;

        long hoje = System.currentTimeMillis();

        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        _out = _sdf.format(hoje);

        return _out;

    }
    // endregion


    // region Método sqliteHoje
    public static String sqliteHoje() {

        String _out = null;

        long hoje = System.currentTimeMillis();

        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd");
        _out = _sdf.format(hoje);

        return _out;

    }
    // endregion


    // region Método sqliteHora
    public static String sqliteHora() {

        String _out = null;

        long hoje = System.currentTimeMillis();

        SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm:ss");
        _out = _sdf.format(hoje);

        return _out;

    }
    // endregion


    // region Método ymdhmsTOdmy
    public static String ymdhmsTOdmy(String value) throws Exception {

        String _out = null;

        Date _hojeHora = null;
        SimpleDateFormat _ymdhms = null;
        SimpleDateFormat _dma = null;

        try {

            _ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            _dma = new SimpleDateFormat("dd/MM/yyyy");

            _hojeHora = _ymdhms.parse(value);

            _out = _dma.format(_hojeHora);

        } catch (Exception e) {
            throw new Exception("[MSVUtil|ymdhmsTOdmy] " + e.getMessage());
        }

        return _out;
    }
    // endregion


    // region Método ymdhmsTOymd
    public static String ymdhmsTOymd(String value) throws Exception {

        String _out = null;

        Date _hoje = null;
        SimpleDateFormat _ymdhms = null;
        SimpleDateFormat _dma = null;

        try {

            _ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            _dma = new SimpleDateFormat("yyyy-MM-dd");

            _hoje = _ymdhms.parse(value);

            _out = _dma.format(_hoje);

        } catch (Exception e) {
            throw new Exception("[MSVUtil|ymdhmsTOymd] " + e.getMessage());
        }

        return _out;
    }
    // endregion


    // region Método ymdhmsTOhms
    public static String ymdhmsTOhms(String value) throws Exception {

        String _out = null;

        Date _hojeHora = null;
        SimpleDateFormat _ymdhms = null;
        SimpleDateFormat _hms = null;

        try {

            _ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            _hms = new SimpleDateFormat("HH:mm:ss");

            _hojeHora = _ymdhms.parse(value);

            _out = _hms.format(_hojeHora);

        } catch (Exception e) {
            throw new Exception("[MSVUtil|ymdhmsTOhms] " + e.getMessage());
        }

        return _out;
    }
    // endregion

    // endregion


    // region Método isPackageManager
    public static boolean isPackageManeger(Context context, String packageName) {

        PackageManager _packageManeger = null;

        try {

            _packageManeger = context.getPackageManager();
            _packageManeger.getPackageInfo(packageName, 0);

            return true;

        } catch (NameNotFoundException e) {
            return false;
        }

    }
    // endregion

}
