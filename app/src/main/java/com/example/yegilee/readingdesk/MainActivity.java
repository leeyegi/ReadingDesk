package com.example.yegilee.readingdesk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yegilee.readingdesk.MainTab1;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

//메인엑티비티
public class MainActivity extends AppCompatActivity {

    //탭 기능 - 변수선언
    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //다른 클래스의 메소드를 호출하기 위한 인스턴스
    public static MainActivity mContext;
    private SM_Database db;
    private SM_Database2 db2;

    //블루투스
    TextView mConnectionStatus;

    //블루투스 관련 인스턴스 선언
    //블루투스가 현재 연결가능한지 판단
    private final int REQUEST_BLUETOOTH_ENABLE = 2;
    ConnectedTask mConnectedTask = null;                        //블루투스 연결된후 수행 클래스 객체
    static BluetoothAdapter mBluetoothAdapter;                   //현재의 상태를 알 수 있는 블루투스 어댑터
    private String mConnectedDeviceName = null;              //연결된 장치의 이름을 표시
    private ArrayAdapter<String> mConversationArrayAdapter;  //다른 장치와 통신
    static boolean isConnectionError = false;                  //현재의 블루투스 연결상태의 에러여부
    private static final String TAG = "BluetoothClient";
    String surrent_status="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //다른 클래스의 메소드 사용을 위한 mContext선언
        mContext=this;

        //데이터베이스의 insert 와 query를 위한 코드
        db=new SM_Database(getApplicationContext());
        db2=new SM_Database2(getApplicationContext());


        //탭 기능
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //블루투스 기능
        mConnectionStatus=(TextView)findViewById(R.id.connection_status_textview);

        ListView mMessageListview = (ListView) findViewById(R.id.message_listview);

        mConversationArrayAdapter = new ArrayAdapter<>( this,
                android.R.layout.simple_list_item_1 );
        mMessageListview.setAdapter(mConversationArrayAdapter);


        Log.d( TAG, "Initalizing Bluetooth adapter...");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        }
        else {
            Log.d(TAG, "Initialisation successful.");

            showPairedDevicesListDialog();
        }
    }

    //----------------------------------------------------------------------------

    //블루투스 연결을 Destroy시키는 메소드
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if ( mConnectedTask != null ) {

            mConnectedTask.cancel(true);
            Log.e("connect","connect fail");

        }
    }
//------------------------------------------------------------------------------------------

    //블루투스 연결을 위한 클래스
    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        //생성자 메소드
        ConnectTask(BluetoothDevice bluetoothDevice) {

            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();

            //SPP
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d( TAG, "create socket for "+mConnectedDeviceName);

            } catch (IOException e) {
                Log.e( TAG, "socket create failed " + e.getMessage());
            }

            mConnectionStatus.setText("connecting...");

        }


        //백그라운드에서 블루투스 연결 확인 메소드
        @Override
        protected Boolean doInBackground(Void... params) {

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mBluetoothSocket.connect();

                //Log.e("connection","log ");

                //블루투스가 연결이 되면 timer가 올라감
                Log.e("connect check","connect ok");
                ((MainTab1)MainTab1.mContext).start_timer();

            } catch (IOException e) {
                // Close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }

                return false;
            }

            return true;
        }

        //현재의 연결 상태를 화면과 로그에 표시
        @Override
        protected void onPostExecute(Boolean isSucess) {

            if ( isSucess ) {
                connected(mBluetoothSocket);
            }
            else{

                isConnectionError = true;
                Log.d( TAG,  "Unable to connect device");
                showErrorDialog("Unable to connect device");
            }
        }
    }


    //블루투스가 연결 되었을때 다음으로 통신을 하기 위해 ConnectedTask 객체 생성
    public void connected( BluetoothSocket socket ) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }


    //--------------------------------------------------------------------------------------------------
    //블루투스가 연결된 후 통신을 하기위한 클래스
    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;
        boolean check_blt_status=true;

        //생성자
        ConnectedTask(BluetoothSocket socket){

            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e );
            }

            Log.d( TAG, "connected to "+mConnectedDeviceName);
            mConnectionStatus.setText( "connected to "+mConnectedDeviceName);
        }


        //백그라운드에서 블루투스와 통신 하는 메소드
        @Override
        protected Boolean doInBackground(Void... params) {

            byte [] readBuffer = new byte[1024];
            int readBufferPosition = 0;

            while(true) {

                if(check_blt_status==false){
                    if (mBluetoothAdapter.isEnabled()) {
                        Log.e("connect status", "turning on");
                        publishProgress("reconnect");
                    }
                }


                while (check_blt_status == true) {

                    if (!mBluetoothAdapter.isEnabled()) {
                        Log.e("connect status", "turning off");
                        check_blt_status = false;
                        ((MainTab1)MainTab1.mContext).save_timer();
                    }

                    if (isCancelled()) return false;


                    try {

                        int bytesAvailable = mInputStream.available();

                        if (bytesAvailable > 0) {

                            byte[] packetBytes = new byte[bytesAvailable];

                            mInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {

                                byte b = packetBytes[i];
                                if (b == '*') {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                            encodedBytes.length);
                                    String recvMessage = new String(encodedBytes, "UTF-8");

                                    readBufferPosition = 0;

                                    //Log.e(TAG, "recv message: " + recvMessage);
                                    publishProgress(recvMessage);
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException e) {

                        Log.e(TAG, "disconnected", e);
                        return false;
                    }
                }
            }

        }

        //-------------------------------------------------------------------------------------------------
        //블루투스의 통신 상태를 어댑터를 통해 업데이트하는 메소드
        @Override
        protected void onProgressUpdate(String... recvMessage) {
            mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);

            int search;
            String search_data="";
            //받은 메세지로 타이머 상태 결정
            Toast.makeText(getApplicationContext(), recvMessage[0], Toast.LENGTH_LONG).show();
            Log.e("text", recvMessage[0]);

            //받은 메세지가 go이면 공부 집중중
            boolean str_go=recvMessage[0].contains("go");
            //Log.e("str_go", String.valueOf(str_go));

            if(str_go){
                //((MainTab1)MainTab1.mContext).start_timer();
                search=recvMessage[0].indexOf("go.");
                search_data=recvMessage[0].substring(search+3, search+13);
                search_data=search_data.trim();
                int tmp=Integer.parseInt(search_data);
                tmp=tmp/(200*200*3);
                if(tmp>10){
                    tmp=10;
                }
                search_data=String.valueOf(tmp);
                Log.e("search",search_data);
                db2.INSERT(search_data);

            }

            //받은 메세지가 stop이면 공부 집중력 떨어짐
            boolean str_stop=recvMessage[0].contains("stop");
            //Log.e("str_stop", String.valueOf(str_stop));

            if(str_stop){
                //((MainTab1)MainTab1.mContext).pause_timer();
                search=recvMessage[0].indexOf("op.");
                search_data=recvMessage[0].substring(search+3, search+13);
                search_data=search_data.trim();
                Log.e("search",search_data);
                search_data="0";
                db2.INSERT(search_data);

            }
            ((MainTab1)MainTab1.mContext).drawLinechart();

        }

        //디바이스 연결이 되지 않았을째 화면에 표시되는 포스트 메소드
        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);

            if ( !isSucess ) {

                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("Device connection was lost");
            }
        }

        //블루투스 연결을 취소하는 메소드
        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            closeSocket();
        }

        //연결 소켓을 취소하는 메소드
        void closeSocket(){

            try {

                mBluetoothSocket.close();
                Log.d(TAG, "close socket()");

            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }


        /*
        void write(String msg){

            msg += "\n";

            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during send", e );
            }

            //mInputEditText.setText(" ");
        }
        */
    }

    //----------------------------------------------------------------------------------------------


    //블루투스 연결을 위해 접근가능한 블루투스 장치를 보여주는 메소드
    public void showPairedDevicesListDialog()
    {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        //Log.e("connection","log1 ");


        if ( pairedDevices.length == 0 ){
            showQuitDialog( "No devices have been paired.\n"
                    +"You must pair it with another device.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for (int i=0;i<pairedDevices.length;i++) {
            items[i] = pairedDevices[i].getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ConnectTask task = new ConnectTask(pairedDevices[which]);
                task.execute();
            }
        });
        builder.create().show();
    }


    //통신중 에러가 있으면 에러를 표시해주는 메소드
    public void showErrorDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if ( isConnectionError  ) {
                    isConnectionError = false;
                    //finish();
                }
            }
        });
        builder.create().show();
    }

    //블루투스 연결을 종료하는 메소드
    public void showQuitDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
            }
        });
        builder.create().show();
    }

    /*
    void sendMessage(String msg){

        if ( mConnectedTask != null ) {
            mConnectedTask.write(msg);
            Log.d(TAG, "send message: " + msg);
            mConversationArrayAdapter.insert("Me:  " + msg, 0);
        }
    }*/

    //블루투스 연결이 가능하다면 관련 결과를 보여주는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_BLUETOOTH_ENABLE){
            if (resultCode == RESULT_OK){
                //BlueTooth is now Enabled
                showPairedDevicesListDialog();
            }
            if(resultCode == RESULT_CANCELED){
                showQuitDialog( "You need to enable bluetooth");

            }
        }
    }

}

