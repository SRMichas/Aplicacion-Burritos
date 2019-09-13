package com.sorezel.burritos.BD;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.sorezel.burritos.Objetos.Burrito;
import com.sorezel.burritos.Objetos.Orden;
import com.sorezel.burritos.Objetos.Usuario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LocalHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    String DB_PATH = null;
    private static String DB_NAME = "data.sqlite";
    private static SQLiteDatabase myDataBase;
    private static SQLiteDatabase myDataBase2;
    private final Context myContext;
    private static Context mContext2;

    public LocalHelper(Context context)
    {
        super(context, DB_NAME, null, 1);// 1? Its database Version
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
        mContext2 = context;
        this.createDataBase();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */

    public void createDataBase(){
        try {
            boolean dbExist = checkDataBase();
            if(dbExist){
                //do nothing - database already exist
            }else{
                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getWritableDatabase();
                copyDataBase();
            }
        }
        catch (Exception e) { }
    }
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            //database does't exist yet.
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */

    private void copyDataBase(){
        try{
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }catch (Exception e) { /*catch exception*/ }
    }
    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    //Consultas
    public static Usuario retUser(int id,String pass){
        Usuario us = null;
        Cursor c = myDataBase.rawQuery("select * from Usuario where UsID = ? and Uscontrasena = ?",new String[]{""+id,pass});
        if( c.moveToFirst() ) {

            us = new Usuario(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4)
            ,c.getString(5),c.getString(6));
            c.close();
        }
        return us;
    }
    public static Usuario retUser2(String con,String pass){
        Usuario us = null;
        Cursor c = myDataBase.rawQuery("select * from Usuario where Uscorreo = ? and Uscontrasena = ?",new String[]{con,pass});
            if( c.moveToFirst()) {
            us = new Usuario(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4)
                    ,c.getString(5),c.getString(6));
            c.close();
        }
        return us;
    }
    public static ArrayList<Burrito> retBurros(){
        ArrayList<Burrito> menu = null;
        Burrito us = null;
        Cursor c = myDataBase.rawQuery("select * from Burrito",null);
        if( c.moveToFirst() ) {

            menu = new ArrayList<>();
            while(!c.isAfterLast()){
                int i = c.getInt(0);
                us = new Burrito(i,c.getString(1),c.getString(2),c.getInt(3),
                        c.getInt(4),c.getInt(5),c.getInt(6));
                menu.add(us);
                c.moveToNext();
            }
            c.close();
        }
        return menu;
    }
    public static ArrayList<Burrito> retTopBurros(){
        ArrayList<Burrito> menu = null;
        Burrito us = null;
        Cursor c = myDataBase.rawQuery("select * from Burrito order by BPopularidad desc",null);
        if( c.moveToFirst() ) {

            menu = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                us = new Burrito(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),
                        c.getInt(4),c.getInt(5),c.getInt(6));
                menu.add(us);
                c.moveToNext();
            }
            c.close();
        }
        return menu;
    }
    public static ArrayList<Burrito> retFavoritos(int id){
        ArrayList<Burrito> menu = null;
        Burrito us = null;
        Cursor c = myDataBase.rawQuery("select b.* from Favoritos f " +
                                        "inner join Burrito b on f.BID = b.BId " +
                                        "inner join Usuario u on f.UsID = u.UsID " +
                                        "where u.UsID = ?",new String[]{""+id});
        if( c.moveToFirst() ) {
            menu = new ArrayList<>();
            while(!c.isAfterLast()){
                us = new Burrito(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),
                        c.getInt(4),c.getInt(5),c.getInt(6));
                menu.add(us);
                c.moveToNext();
            }
            c.close();
        }
        return menu;
    }
    public static ArrayList<Integer> canti = null;
    public static ArrayList<Burrito> retCarrito(int id){
        ArrayList<Burrito> menu = null;
        Burrito us = null;
        Cursor c = myDataBase.rawQuery("SELECT b.*,c.Cant FROM Carrito c " +
                "INNER JOIN Burrito b ON c.BID = b.BId " +
                "INNER JOIN Usuario u ON c.UsID = u.UsID " +
                "WHERE c.UsID = ?",new String[]{""+id});
        if( c.moveToFirst()) {

            menu = new ArrayList<>();
            canti = new ArrayList<>();
            while(!c.isAfterLast()){
                us = new Burrito(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),
                        c.getInt(4),c.getInt(5),c.getInt(6));
                menu.add(us);
                canti.add(c.getInt(7));
                c.moveToNext();
            }
            c.close();
        }
        return menu;
    }
    public static int algo(int a){
        int size = -1;
        ArrayList<Burrito> menu = null;
        Burrito us = null;
        Cursor c = myDataBase.rawQuery("select count(*) from Carrito where UsID = ?",new String[]{""+a});
        if( c.moveToFirst()) {
            size = c.getInt(0);
            c.close();
        }
        return size;
    }
    public static ArrayList<Orden> retOrdenes(int ID,int edo){
        ArrayList<Orden> ord = null;
        ArrayList<Integer> canr = null;
        ArrayList<Burrito> burr = null;
        int fol = 0, fol2 = 0;
        String fch = null;
        Cursor c = myDataBase.rawQuery(
                "select do.Folio,do.Catidad,o.fecha,b.* " +
                "from DetalleOrden do " +
                "inner join Orden o on do.Folio = o.Folio " +
                "inner join Burrito b on do.BID = b.BId " +
                "where o.USID = ? and o.edo = ?",new String[]{""+ID,""+edo});
        if(c.moveToFirst()){
            ord = new ArrayList<>();
            canr = new ArrayList<>();
            burr = new ArrayList<>();
            while(!c.isAfterLast()){
                do{
                    fol = c.getInt(0);
                    canr.add(c.getInt(1));
                    fch = c.getString(2);
                    burr.add(new Burrito(c.getInt(3),c.getString(4),c.getString(5),c.getInt(6),
                            c.getInt(7),c.getInt(8),c.getInt(9)));


                    c.moveToNext();
                    try {
                        fol2 = c.getInt(0);
                    }catch (CursorIndexOutOfBoundsException e){
                        fol2++;
                    }


                }while(fol == fol2);
                ord.add(new Orden(fol,fch,burr,canr));
                //c.moveToNext();
            }
            c.close();
        }
        return  ord;
    }
    public static int retUltimoIdUsuario(){
        Cursor c = myDataBase.rawQuery("select UsId from Usuario",null);
        if(c.moveToLast())
            return c.getInt(0);
        return 0;
    }
    public static boolean busqBurrCarr(int a,int b){
        Cursor c = myDataBase.rawQuery("select 0 from Carrito where UsID = ? and BID = ?",new String[]{""+a,""+b});
        if(c.moveToFirst())
            return true;
        return false;
    }
    public static boolean busqBurrFav(int Uid,int Bid){
        Cursor c = myDataBase.rawQuery("select 0 from Favoritos where UsID = ? and BID = ?",new String[]{""+Uid,""+Bid});
        if(c.moveToFirst())
            return true;
        return false;
    }
    public static boolean busqBurrCarr2(Context con,int a){
        Cursor c = myDataBase.rawQuery("select 0 from Carrito ",null);
        Toast.makeText( con,""+c.getCount(),Toast.LENGTH_SHORT).show();
        //if(c.moveToFirst())
          //  return true;
        return false;
    }
    public static boolean busqBurrCarr3(Context con,int a){
        Cursor c = myDataBase.rawQuery("select Usnombre from Usuario",null);
        String algo = "\n";
        if(c.moveToFirst())
            while (!c.isAfterLast()){
                algo += c.getString(0)+"\n";
                c.moveToNext();
            }

        Toast.makeText( con,""+c.getCount()+algo,Toast.LENGTH_SHORT).show();
        //if(c.moveToFirst())
        //  return true;
        return false;
    }
    public static void totalUser(){
        Cursor c = myDataBase.rawQuery("select count(*) from usuario",null);
        c.moveToFirst();
        Toast.makeText(mContext2,"Usuarios: "+c.getCount()+"\nCa: "+c.getInt(0),Toast.LENGTH_SHORT).show();
    }
    public static int ultimaOrden(){
        Cursor c = myDataBase.rawQuery("select Folio from Orden",null);
        if(c.moveToLast())
            return c.getInt(0);
        return -1;
    }
    public static boolean esFavorito(int U,int B){
        Cursor c = myDataBase.rawQuery("SELECT 0 FROM Favoritos WHERE UsID = ? and BID = ?",new String[]{""+U,""+B});
        if(c.moveToFirst())
            return true;
        return false;
    }
    //Insersiones
    public static void registraUsuario(Object[] d){
        myDataBase.execSQL("insert into Usuario values (?,?,?,?,?,?,?)",d);
    }
    public static void insertaCarrito(Object[] da){
        myDataBase.execSQL("INSERT INTO Carrito VALUES (?,?,?)",da);
    }
    public static void insertaFavorito(String[] data){
        myDataBase.execSQL("INSERT INTO Favoritos VALUES (?,?)",data);
    }
    public static void insertaOrden(String[] d){
        myDataBase.execSQL("insert into Orden values (?,?,?,?)",d);
        //c = myDataBase.rawQuery("insert into DetalleOrden values ()",d)
    }
    public static void insertaOrdenD(String[] d){
        myDataBase.execSQL("insert into DetalleOrden values (?,?,?)",d);
    }
    //Actualizaciones
    public static void udpCarrito(String[] d){
        myDataBase.execSQL("update Carrito set Cant = ? where UsID = ? and BID = ?",d);
    }
    public static void udpOrden(String[] d){
        Cursor c = myDataBase.rawQuery("update Orden set edo = ? where Folio = ? and USID = ?",d);
        c.close();
    }
    //Eliminaciones
    public static void delCarrito(String[] d,boolean tipo){
        Cursor c = null;
        if(tipo){
            myDataBase.execSQL("delete from Carrito where UsID = ? and BID = ?",d);
        }else{
            myDataBase.execSQL("delete from Carrito where UsID = ?",d);
        }
       // c.close();
    }
    public static void borraUsuarios(){
        myDataBase.execSQL("DELETE FROM usuario where UsID > 1");
    }
    public static void borraCarr(){
        myDataBase.execSQL("DELETE FROM Carrito");
    }
    public static void borraFavorito(String[] data){
        myDataBase.execSQL("DELETE FROM Favoritos WHERE UsID = ? and BID = ?",data);
    }

    public  void elimina(){
        this.myContext.deleteDatabase("data.sqlite");
    }
}
