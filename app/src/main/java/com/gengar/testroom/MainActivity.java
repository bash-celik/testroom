package com.gengar.testroom;

import android.Manifest;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements  LoaderManager.LoaderCallbacks<List<AlbumSearch>> {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0 ;
    /**KAPTEN'S LOG
    * Aj sad da ga jebi Cicio, treba se snaci ovde:22.Sept '17 01:05AM
    * Kazu da su oni koji neuredno i ruzno pisu kreativni i inteligentni, da li to vazi i za kod? 03:05AM
    *sunce ti jebem idm da spavam pre nego sto cale ustane za posao 04:08AM
    * */


    /**
     * DON'T LOOK AT ME BANE, I'M HIDEOUS
     * ne pitaj, 4 i nesto je, ceo dan gledam javu, sasvim je normalno da ne budem normalan
     * kod nije nesto mnogo komentarisan jer jelte bitno je da radi
     * kad smo kod toga, ne radi
     * na telefonu ne mogu da ga komplajlujemm, dobijam error koji sam ti poslao na wa, mislim da on ne pokusava da fecuje, tj da ne moze da pristupi netu
     * na emulatoru radi al nzm dal se cuje muzika, jer jelte emulator je sjeban
     * probaj da kompajlujes i na fonu i na emulatoru, ako oce oce, ako ne opravicu ga sutra
     * moras da uneses u polje dole applicationID, saljem ti na wa da ga ne bi stavljao na git
     *
     * */

    ListView listView;
    Button test;

    //woohoo
    String applicationID = "252922";//ovde ovde
    //gore kljuc
    DeezerConnect deezerConnect;
    TextView testTxt;
    Button play;
    String Tag = "com.gengar.roomtest";
    TrackPlayer tp;
    ArrayList<AlbumSearch > testLista = new ArrayList<>();

    public static final String DEEZER_URL =
            "http://api.deezer.com/search?q=track:%22straight%20to%20the%20bank%22?";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        deezerConnect= new DeezerConnect(this, applicationID);


        getLoaderManager().initLoader(0,null,this);

        //testTxt = findViewById(R.id.text);
        /** test lista, proverava da li radi album adapter i list view*/
       /* testLista.add(new AlbumSearch(11,123,123,"TEST1","test1"));
        testLista.add(new AlbumSearch(11,123,123,"TEST1","test1"));
        testLista.add(new AlbumSearch(11,123,123,"TEST1","test1"));
        testLista.add(new AlbumSearch(11,123,123,"TEST1","test1"));
        //definisanje list view, u ovom se prikazuje album_itam.xm;*/


       // instanciramo plejer jebeni
        try{
            tp = new TrackPlayer(getApplication(), deezerConnect, new WifiAndMobileNetworkStateChecker());
        }catch (TooManyPlayersExceptions e){
            Log.e("LOG_TAG","Player error to many players",e);


        }catch (DeezerError e){
            Log.e("LOG_TAG","deezer error",e);
        }



        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };





        // Launches the authentication process
        SessionStore sessionStore = new SessionStore();

        //deezerConnect.authorize(this, permissions, listener);


        //sessionStore.save(deezerConnect, this);





    }


    @Override
    public Loader<List<AlbumSearch>> onCreateLoader(int i, Bundle bundle) {
        return new AlbumLoader(this,DEEZER_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<AlbumSearch>> loader, List<AlbumSearch> albumSearches) {
        updateUI(albumSearches);

    }

    @Override
    public void onLoaderReset(Loader<List<AlbumSearch>> loader) {

    }

    public static class AlbumLoader extends AsyncTaskLoader<List<AlbumSearch>>{

        String url;

        public AlbumLoader(Context context,String url) {
            super(context);
            this.url = url;
        }


        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<AlbumSearch> loadInBackground() {
            if(url.length() < 1){
                return null;
            }
            List<AlbumSearch> list = FetchUtils.fetchAlbumData(url);
            return list;
        }

    }
    private void updateUI(List<AlbumSearch> list){
        listView = findViewById(R.id.list);
        //novi album adapter
        final AlbumAdapter adapter = new AlbumAdapter(this,list);
        //setovanje novog album adaptera
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // stopiramo prethodnu
                tp.stop();

                // pustamo novu
                AlbumSearch current = adapter.getItem(i);
                System.out.println(current.getAlbumId());
                tp.playTrack(current.getAlbumId());

            }
        });

    }


}
