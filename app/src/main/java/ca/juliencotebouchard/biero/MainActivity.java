// tutoriel à fait par Hisham Muneer
// https://www.youtube.com/watch?v=X2mY3zfAMfM

package ca.juliencotebouchard.biero;

//importation des modules
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.os.AsyncTask;
        import android.os.Bundle;

        import android.support.v7.app.ActionBarActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import ca.juliencotebouchard.biero.models.BiereModel;
        import com.nostra13.universalimageloader.core.DisplayImageOptions;
        import com.nostra13.universalimageloader.core.ImageLoader;
        import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
        import com.nostra13.universalimageloader.core.assist.FailReason;
        import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class MainActivity extends ActionBarActivity {

    ListView lvBieres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        // va chercher mon champs où je vais mettre ma liste de bière
        lvBieres =  (ListView) findViewById(R.id.lvBieres);
        // part toute la poutine à partir des données de mon webservice
        new RestOperation().execute("http://www.juliencotebouchard.ca/webservice/php/biere/");
    }



    private class RestOperation extends AsyncTask<String, String, List<BiereModel>> {

        @Override
        protected List<BiereModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader =null;

            try{
                URL url = new URL(params[0]);
                //
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // le stream est les données recues
                InputStream stream = connection.getInputStream();
                // decode le stream de la façon demander (utf8 pour ma part)
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine())!= null){
                    buffer.append(line);
                }
                //met le buffer en string
                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray jsonArray = parentObject.optJSONArray("data");
                List<BiereModel> biereModelList = new ArrayList<>();

                // parse chaque biere, les met dans le model
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject child = jsonArray.getJSONObject(i);
                    BiereModel biereModel = new BiereModel();
                    biereModel.setNom(child.getString("nom"));
                    biereModel.setId_biere(child.getString("id_biere"));
                    biereModel.setDescription(child.getString("description"));
                    biereModel.setMoyenne(child.getString("moyenne"));
                    biereModel.setBrasserie(child.getString("brasserie"));
                    biereModel.setNombre(child.getString("nombre"));
                    biereModel.setMoyenne(child.getString("moyenne"));
                    biereModel.setImage(child.getString("image"));

                    // ajoute le model final dans la liste
                    biereModelList.add(biereModel);
                }
                return biereModelList;
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            } finally{
                if(connection != null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            return null;

        }

        //quand tout est télécharger et mis dans le modèle
        @Override
        protected void onPostExecute( List<BiereModel> result) {
            super.onPostExecute(result);
            // TODO doit mettre le data dans la liste

            BiereAdapter adapter = new BiereAdapter(getApplicationContext(), R.layout.ligne, result);
            lvBieres.setAdapter(adapter);


        }


    }

    public class BiereAdapter extends ArrayAdapter{

        //quelque chose de très flou
        public List<BiereModel> biereModelList;
        private int resource;
        private LayoutInflater inflater;
        public BiereAdapter( Context context,  int resource,  List<BiereModel> objects) {
            super(context, resource, objects);
            biereModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {

            if(convertView==null){
                convertView=inflater.inflate(resource, null);
            }
            //initie les variables
            ImageView ivBiereIcon;
            TextView tvNomBiere;
            TextView tvBrasserie;
            TextView tvNombre;

            RatingBar rbNote;
            TextView tvDescription;
            //rempli les variables
            ivBiereIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
            tvNomBiere = (TextView)convertView.findViewById(R.id.nomBiere);
            tvBrasserie = (TextView)convertView.findViewById(R.id.brasserie);
            tvNombre = (TextView)convertView.findViewById(R.id.nombre);

            rbNote = (RatingBar) convertView.findViewById(R.id.rbBiere);
            tvDescription = (TextView)convertView.findViewById(R.id.description);
            final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);

            //met une image ou un symbole de téléchargement si elle n'est pas encore téléchargé
            ImageLoader.getInstance().displayImage(biereModelList.get(position).getImage(), ivBiereIcon, new ImageLoadingListener() {
                //   ImageLoader.getInstance().displayImage("http://findicons.com/files/icons/85/kids/128/thumbnail.png", ivBiereIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);

                }
            }); // Default options will be used

            //rempli le modèle
            tvNomBiere.setText(biereModelList.get(position).getNom());
            tvBrasserie.setText("Par: " + biereModelList.get(position).getBrasserie());
            tvNombre.setText("Nombre de votes: " +biereModelList.get(position).getNombre());

            rbNote.setRating(Float.parseFloat(biereModelList.get(position).getMoyenne())/2);
            tvDescription.setText(biereModelList.get(position).getDescription());

            return convertView;
        }
    }




}
