/**
 * Auteure : Benoit-Lynx Hamel & Julien Canuel
 * Fichier : Conifere1.java
 * Date    : 12 décembre 2016
 * Cours   : 420-254-MO (TP3 Android)
 */

/**
 * Classe contenant les données et les méthodes pour lactivité trois de conifère.
 */
package com.montmo.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Conifere3 extends AppCompatActivity {
    private Resources res;
    private Intent intent;
    private String nom;
    private String web;
    private WebView webView;
    private WebSettings webSettings;

    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conifere3);
        res = getResources();
        //Recuperer l'objet intent precedent
        intent = getIntent();
        //Recupere la ressource Extra
        nom = intent.getStringExtra(Conifere1.CLE_CONIFERE_NOM);
        web = intent.getStringExtra(Conifere1.CLE_CONIFERE_WEB);

        menuDrawerLayour();
        creerWebView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barre_actions, menu);
        return true;
    }

    private void creerWebView() {
        //Recupêrer le composant WebView
        webView = (WebView) findViewById(R.id.id_web_view);
        //acceder aux parametre du webview
        webSettings = webView.getSettings();
        //activer javascript
        webSettings.setJavaScriptEnabled(true);
        //afficher zoom et pincer
        webSettings.setBuiltInZoomControls(true);
        //gestionnaire pendant le chargement
        webView.setWebChromeClient(ecouterChargementWeb);
        //gestionnaire en cas d'erreur ou fin du chargement
        webView.setWebViewClient(ecouterFinChargement);
        //charger l'adresse url
        webView.loadUrl(web);
    }

    //Traitement pendant chargement du contenu web
    WebChromeClient ecouterChargementWeb = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int progress){
            Toast.makeText(getApplicationContext(), Integer.toString(progress) + " %",
                    Toast.LENGTH_SHORT).show();
        }
    };

    //Traitement en cas d'erreur ou fin du chargement
    WebViewClient ecouterFinChargement = new WebViewClient(){
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error){
            String messErreur = res.getString(R.string.erreur_chargement_web);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                messErreur = messErreur.concat(error.getDescription().toString());
            }
            Toast.makeText(getApplicationContext(), messErreur, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPageFinished(WebView view, String url){
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        }

    };


    //Méthode pour tester la création d'un menu glissant à gauche de l'application
    private void menuDrawerLayour() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.list_navigation);

        //Lier le drawerLayout à la barred'actions.
        //Utilisation de la flèche de remontée pour afficher ;e menu.
        // Le constructeur prend en paramètres : l'activité qui accueille le drawer, le drawerLayout
        // et deux chaines utilisées à l'ouvertures et à la fermeture du menu (pour l'accessibilité).
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.ouvrir_menu,
                R.string.fermer_menu);

        //Basculer entre l'icone hamburger et l'icone de la fleche de remontée.
        drawerToggle.setDrawerIndicatorEnabled(true);

        //Lier le drawerToggle au drawerLayout
        drawerLayout.addDrawerListener(drawerToggle);

        //Récuperer une référence à la bare avec Appcompat
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(nom);

        //Ajouter la flèche de remonté et la rendre cliquable
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Défénir l'écouteur de la liste dans une variable
        listView.setOnItemClickListener(ecouterListView);
    }

    //Définition de la variable qui écoute la liste listeNavigation du menu glissant
    private AdapterView.OnItemClickListener ecouterListView =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    //parent.getItemAtPosition(position) permet de
                    //recuperer l'item qui a été sélectionné.
                    String itemChoisi = parent.getItemAtPosition(position).toString();
                    String message = "Item : " + itemChoisi;

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    //Exemple d'intention explicite.
                    //La nouvelle intention contient le contexte de l'activité
                    // appelant et le nom de l'activite
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (position == 0) {
                        intent = new Intent(Conifere3.this, Pret1.class);
                        startActivity(intent);
                    } else if (position == 1) {
                        intent = new Intent(Conifere3.this, Conifere1.class);
                        startActivity(intent);
                    } else if (position == 2){
                        intent = new Intent(Conifere3.this, Dinosaurs1.class);
                        startActivity(intent);
                    }

                    //Fermer le draerLayout après une selection
                    drawerLayout.closeDrawers();

                }
            };

    //Méthode appelée après les méthodes onStart et onRestoreInstnceState
    //pour finaliser les initialisation.
    //Ici, on veut synchroniser le drawerToggle pour qu'il bascule entre l'icone hamburger
    //et l'icone de la fleche de remontee dans la barre d'actions.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    //Gestionnaire d'événements pour la barre d'actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Return false by default
        boolean traiter = super.onOptionsItemSelected(item);

        // item.getItemid() contient l'identifiant de l'item cliqué
        switch (item.getItemId()) {
            //Traitement de chaque item de la barre d'actions
            case R.id.menu_accueil:
                Toast.makeText(getApplicationContext(), R.string.action_accueil,
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                traiter = true;
                break;
            case R.id.menu_aide:
                AlertDialog.Builder boiteAlert = new AlertDialog.Builder(Conifere3.this);
                boiteAlert.setTitle(R.string.action_aide);
                boiteAlert.setIcon(R.drawable.ic_info_aide);
                boiteAlert.setMessage(R.string.aide_web_conifere);

                //Écouteur pour le bouton qui se trouvera tout à droite.
                boiteAlert.setPositiveButton(R.string.txt_alertdialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int whichButton ){
                        //Traitement pour le bouton tout à droite.
                        Toast.makeText(getApplicationContext(), R.string.txt_alertdialog_ok,
                                Toast.LENGTH_LONG).show();
                    }
                });
                //Créer un AlertDialog
                AlertDialog boiteAlertDialog = boiteAlert.create();
                //Afficher la boite de dialog
                boiteAlertDialog.show();


                traiter = true;
                break;

            default:
                break;
        }

        //Deleguer le clic de l'icone hamburger ou de la fleche de remontee au DrawerLayout
        //et non a la barre d'action
        if (drawerToggle.onOptionsItemSelected(item)) {
            traiter = true;

        }

        //Return true si bien gere
        return traiter;
    }
}
