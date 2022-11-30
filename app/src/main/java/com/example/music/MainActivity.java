package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem songs,album;
    PagerAdapter pagerAdapter;

    public static final int REQUEST_CODE=1;
    static ArrayList<MusicFiles> musicFiles;
    static boolean shuffleBoolean=false,repeatBoolean=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission(); //调用获取权限的方法
    }

    //界面显示和控件绑定
    private void initViewPager() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager=findViewById(R.id.viewPager);
        mTabLayout=findViewById(R.id.tabLayout);

        songs=findViewById(R.id.songs);
        album=findViewById(R.id.album);

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        //添加监听事件
        navigationView.setNavigationItemSelectedListener(this);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        //创建适配器并设置
        pagerAdapter=new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mTabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        //tab 选择监听事件
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    //获取权限
    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }else {
            musicFiles=getAllAudio(this);
            initViewPager();
        }
    }

    //请求权限返回的结果事件
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //Do whatever you want permission related
                musicFiles=getAllAudio(this);
                initViewPager();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }

    //菜单栏点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.menuHome:
                Toast.makeText(this,"Home btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuFriend:
                Toast.makeText(this,"Friend btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuFollow:
                Toast.makeText(this,"Follow btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuTicket:
                Toast.makeText(this,"Ticket btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuGift:
                Toast.makeText(this,"Gift btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuScan:
                Toast.makeText(this,"Scan btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuClock:
                Toast.makeText(this,"Clock btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuLocation:
                Toast.makeText(this,"Location btn click",Toast.LENGTH_LONG).show();
                break;
            case R.id.menuSetting:
                Toast.makeText(this,"Setting btn click",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return false;
    }

    //获取手机上的歌曲信息
    public static ArrayList<MusicFiles> getAllAudio(Context context){
        ArrayList<MusicFiles> tempAudioList=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,  //FOR PATH
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID

        };
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String album=cursor.getString(0);
                String title=cursor.getString(1);
                String duration=cursor.getString(2);
                String path=cursor.getString(3);
                String artist=cursor.getString(4);
                String id=cursor.getString(5);

                MusicFiles musicFiles=new MusicFiles(path,title,artist,album,duration,id);
                //take log.e check
                Log.e("PATH:"+path,"Album:"+album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }

}