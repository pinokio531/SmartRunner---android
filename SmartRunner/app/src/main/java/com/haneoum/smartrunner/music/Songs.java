package com.haneoum.smartrunner.music;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.haneoum.smartrunner.R;

import java.util.ArrayList;

public class Songs extends Fragment {

    //조성현, 김성민

    private TextView tv;
    private LocationManager lm;
    private LocationListener ll;
    double mySpeed, maxSpeed;

    private ListView listView;
    public static ArrayList<MusicDto> list;

    Location location;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_song,null);
        tv = (TextView)view.findViewById(R.id.speed);

        getMusicList();

        listView = (ListView)view.findViewById(R.id.listview);
        MyAdapter adapter = new MyAdapter(getActivity(),list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MusicActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("playlist",list);
                startActivity(intent);
            }
        });

        location = null;
        SpeedoActionListener speedoActionListener = new SpeedoActionListener();
        speedoActionListener.onLocationChanged(location);

        maxSpeed = mySpeed = 0;

        lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        ll = new SpeedoActionListener();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
        return view;
    }

    private class SpeedoActionListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                Log.d("테스트", "ㅎㅇㅎㅇ");

                mySpeed = location.getSpeed();
                if (mySpeed > maxSpeed) {
                    maxSpeed = mySpeed;
                }
                tv.setText("현재속도 : " + mySpeed + " km/h, 최고속도: "
                        + maxSpeed + " km/h");
            }
            else{

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }

    public  void getMusicList() {
        if (mySpeed <=3 ) {
            list = new ArrayList<>();
            //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
            String[] projection = {MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST
            };
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                return;
                // do your stuff..
            }

            Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);

            while (cursor.moveToNext()) {
                MusicDto musicDto = new MusicDto();
                musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                list.add(musicDto);
            }
            cursor.close();
        }
        else if(mySpeed > 3){

            list = new ArrayList<>();
            //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
            String[] projection = {MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST
            };
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                return;
                // do your stuff..
            }

            Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                    projection, null, null, null);

            while (cursor.moveToNext()) {
                MusicDto musicDto = new MusicDto();
                musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                list.add(musicDto);
            }
            cursor.close();

        }
    }
}
