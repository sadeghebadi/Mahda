package ir.rayacell.mahdaclient.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.*;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WifiApManager {

    private static final String LOG_TAG = "WifiApManager";
    public static final int TYPE_NO_PASSWD = 0x11;
    public static final int TYPE_WEP = 0x12;
    public static final int TYPE_WPA = 0x13;
    public static final int DEFAULT_TYPE = TYPE_WPA;
    public static String SSID_PREFIX = "_MAHADA_";
    public static  String DEFAULT_SSID = SSID_PREFIX + Build.MODEL + Math.random()*1000;
    public static  String DEFAULT_PASSWORD = "ttpod123";
    private static final int DEFAULT_PRIORITY = 10000;
    private static int mWifiApStateDisabled;
    private static int mWifiApStateDisabling;
    private static int mWifiApStateEnabled;
    private static int mWifiApStateEnabling;
    private static int mWifiApStateFailed;
    

    private static String mWifiApStateChangedAction;

    private static String mExtraWifiApState;
    private static String mExtraPreviousWifiApState;

    static {
        try {
            mWifiApStateDisabled = WifiManager.class.getField("WIFI_AP_STATE_DISABLED").getInt(WifiManager.class);
            mWifiApStateDisabling = WifiManager.class.getField("WIFI_AP_STATE_DISABLING").getInt(WifiManager.class);
            mWifiApStateEnabled = WifiManager.class.getField("WIFI_AP_STATE_ENABLED").getInt(WifiManager.class);
            mWifiApStateEnabling = WifiManager.class.getField("WIFI_AP_STATE_ENABLING").getInt(WifiManager.class);
            mWifiApStateFailed = WifiManager.class.getField("WIFI_AP_STATE_FAILED").getInt(WifiManager.class);
            mWifiApStateChangedAction = (String)WifiManager.class.getField("WIFI_AP_STATE_CHANGED_ACTION").get(WifiManager.class);
            mExtraWifiApState = (String)WifiManager.class.getField("EXTRA_WIFI_AP_STATE").get(WifiManager.class);
            mExtraPreviousWifiApState = (String)WifiManager.class.getField("EXTRA_PREVIOUS_WIFI_AP_STATE").get(WifiManager.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final int WIFI_AP_STATE_DISABLED = mWifiApStateDisabled;
    public static final int WIFI_AP_STATE_DISABLING = mWifiApStateDisabling;
    public static final int WIFI_AP_STATE_ENABLED = mWifiApStateEnabled;
    public static final int WIFI_AP_STATE_ENABLING = mWifiApStateEnabling;
    public static final int WIFI_AP_STATE_FAILED = mWifiApStateFailed;
    public static final String WIFI_AP_STATE_CHANGED_ACTION = mWifiApStateChangedAction;
    public static final String EXTRA_WIFI_AP_STATE = mExtraWifiApState;
    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = mExtraPreviousWifiApState;

    private WifiManager mWifiManager;
    private WifiStateListener mWifiStateListener;
    private boolean mIsNetworkEnabled = false;
    private WifiManager.WifiLock mWifiLock;

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, action);
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> results = mWifiManager.getScanResults();
                scanFinishedEvent(results);

                if (mWifiStateListener != null) {
                    mWifiStateListener.onScanFinished(results);
                }
            } else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onRSSIChanged(intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0));
                }
            } else if (WifiManager.NETWORK_IDS_CHANGED_ACTION.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onNetworkIdsChanged();
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiInfo wifiInfo = (WifiInfo)intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (mWifiStateListener != null) {
                    if (networkInfo.isAvailable() && networkInfo.isConnected() && wifiInfo != null) {
                        mWifiStateListener.onConnectNetworkSucceeded(networkInfo, wifiInfo);
                    } else {
                        mWifiStateListener.onConnectNetworkFailed(networkInfo);
                    }
                }
            } else if (WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onSupplicantConnectionChanged(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false));
                }
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onSupplicantStateChanged((SupplicantState)intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)
                        , intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, WifiManager.ERROR_AUTHENTICATING));
                }
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onWifiStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                        , intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
                }
            } else if (WifiManager.ACTION_PICK_WIFI_NETWORK.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onPickWifiNetwork();
                }
            } else if (WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
                if (mWifiStateListener != null) {
                    mWifiStateListener.onWifiApStateChanged(intent.getIntExtra(EXTRA_WIFI_AP_STATE, WIFI_AP_STATE_FAILED));
                }
            }

        }
    };

    public WifiApManager(Context context, WifiStateListener listener) {
        mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        mWifiStateListener = listener;

        mWifiLock = mWifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, SSID_PREFIX);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);
        if (!TextUtils.isEmpty(WIFI_AP_STATE_CHANGED_ACTION)) {
            intentFilter.addAction(WIFI_AP_STATE_CHANGED_ACTION);
        }

        context.registerReceiver(mWifiReceiver, intentFilter);
    }

    public boolean setWifiApEnabled(WifiConfiguration configuration, boolean enabled) {
        try {
            if (enabled) {
                mWifiManager.setWifiEnabled(false);
            }

            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (Boolean)method.invoke(mWifiManager, configuration, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getWifiApState() {
        try {
            Method method = mWifiManager.getClass().getMethod("getWifiApState");
            return (Integer)method.invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mWifiApStateFailed;
    }

    public WifiConfiguration getWifiApConfiguration() {
        try {
            Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration config = (WifiConfiguration)method.invoke(mWifiManager);
            loadWifiConfigurationFromProfile(config);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean setWifiApConfiguration(WifiConfiguration configuration) {
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            return (Boolean)method.invoke(mWifiManager, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isWifiApEnabled() {
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            return (Boolean)method.invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean startWifi() {
        try {
            Method method = mWifiManager.getClass().getMethod("startWifi");
            return (Boolean)method.invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean stopWifi() {
        try {
            Method method = mWifiManager.getClass().getMethod("stopWifi");
            return (Boolean)method.invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean startWifiAp() {
        return openWifiAp();
    }

    public void stopWifiAp() {
        closeWifiAp();
        openWifi();
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }
    public void destroy(Context context) {
        stopWifiAp();
        removeNetwork(SSID_PREFIX);
        context.unregisterReceiver(mWifiReceiver);
    }

    private void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    private void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    private void closeWifiAp() {
        if (isWifiApEnabled()) {
            setWifiApEnabled(getWifiApConfiguration(), false);
        }
    }

    private boolean openWifiAp() {
        closeWifi();
        closeWifiAp();
        mWifiLock.acquire();

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = DEFAULT_SSID;
        wifiConfig.preSharedKey = DEFAULT_PASSWORD;
        setWifiConfigAsWPA(wifiConfig);
        return setWifiApEnabled(wifiConfig, true);
    }

    private void setWifiConfigAsWPA(WifiConfiguration wifiConfig) {
        wifiConfig.hiddenSSID = false;
        wifiConfig.status = WifiConfiguration.Status.ENABLED;
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        setWifiConfigurationProfile(wifiConfig);
    }

    private boolean scanFinishedEvent(List<ScanResult> scanResults) {
        List<ScanResult> filter = fileterAccessPoint(scanResults);

        return filter != null
                && !filter.isEmpty()
                && connectToHotspot(filter.get(0));
    }

    private List<ScanResult> fileterAccessPoint(List<ScanResult> results) {
        if (results != null) {
            List<ScanResult> filteredResult = new ArrayList<ScanResult>();
            for (ScanResult result : results) {
                if (result.SSID.contains(SSID_PREFIX)) {
                    filteredResult.add(result);
                }
            }

            return filteredResult;
        }

        return null;
    }

    private boolean connectToHotspot(ScanResult result) {
        if (mIsNetworkEnabled) {
            return true;
        }

        if (mWifiStateListener != null) {
            mWifiStateListener.onConnectionPreparing(result.SSID);
        }
        removeNetwork(SSID_PREFIX);
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        if (!result.SSID.equals(connectionInfo.getSSID())) {
            mWifiManager.disableNetwork(connectionInfo.getNetworkId());
            mWifiManager.disconnect();
        }

        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (!config.SSID.equals("\"" + result.SSID + "\"")) {
                config.priority = 0;
                mWifiManager.updateNetwork(config);
            }
        }

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + result.SSID + "\"";
        wifiConfig.preSharedKey = "\"" + DEFAULT_PASSWORD + "\"";
        wifiConfig.BSSID = result.BSSID;
        wifiConfig.priority = DEFAULT_PRIORITY;

        setWifiConfigAsWPA(wifiConfig);

        try {
            Field field = wifiConfig.getClass().getField("ipAssignment");
            field.set(wifiConfig, Enum.valueOf((Class<Enum>)field.getType(), "DHCP"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int networkId = mWifiManager.addNetwork(wifiConfig);

        mIsNetworkEnabled = mWifiManager.enableNetwork(networkId, true);

        if (mWifiStateListener != null) {
            mWifiStateListener.onConnectionPrepared(mIsNetworkEnabled, result.SSID);
        }

        return mIsNetworkEnabled;
    }

    public void startScan() {
        closeWifiAp();
        openWifi();
        mWifiLock.acquire();

        mWifiManager.startScan();
        mIsNetworkEnabled = false;
    }

    private void setWifiConfigurationProfile(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration != null) {
            try {
                Field wifiApProfileField = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
                wifiApProfileField.setAccessible(true);
                Object wifiApProfile = wifiApProfileField.get(wifiConfiguration);
                wifiApProfileField.setAccessible(false);

                if (wifiApProfile != null) {
                    Field ssidField = wifiApProfile.getClass().getDeclaredField("SSID");
                    ssidField.setAccessible(true);
                    ssidField.set(wifiApProfile, wifiConfiguration.SSID);
                    ssidField.setAccessible(false);

                    Field bssidField = wifiApProfile.getClass().getDeclaredField("BSSID");
                    bssidField.setAccessible(true);
                    bssidField.set(wifiApProfile, wifiConfiguration.BSSID);
                    bssidField.setAccessible(false);

                    Field dhcpField = wifiApProfile.getClass().getDeclaredField("dhcpEnable");
                    dhcpField.setAccessible(true);
                    dhcpField.set(wifiApProfile, 1);
                    dhcpField.setAccessible(false);

                    Field keyField = wifiApProfile.getClass().getDeclaredField("key");
                    keyField.setAccessible(true);
                    keyField.set(wifiApProfile, wifiConfiguration.preSharedKey);
                    keyField.setAccessible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadWifiConfigurationFromProfile(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration != null) {
            if (TextUtils.isEmpty(wifiConfiguration.SSID) || TextUtils.isEmpty(wifiConfiguration.BSSID)) {
                try {
                    Field wifiApProfileField = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
                    wifiApProfileField.setAccessible(true);
                    Object wifiApProfile = wifiApProfileField.get(wifiConfiguration);
                    wifiApProfileField.setAccessible(false);

                    if (wifiApProfile != null) {
                        Field ssidField = wifiApProfile.getClass().getDeclaredField("SSID");
                        ssidField.setAccessible(true);
                        Object value2 = ssidField.get(wifiApProfile);
                        if (value2 != null) {
                            wifiConfiguration.SSID = (String)value2;
                        }
                        ssidField.setAccessible(false);

                        Field bssidField = wifiApProfile.getClass().getDeclaredField("BSSID");
                        bssidField.setAccessible(true);
                        Object value3 = bssidField.get(wifiApProfile);
                        if (value3 != null) {
                            wifiConfiguration.BSSID = (String)value3;
                        }
                        bssidField.setAccessible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    public WifiConfiguration getWifiConfiguration(String ssid) {
        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            loadWifiConfigurationFromProfile(config);
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }

        return null;
    }

    public void removeConnection() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        mWifiManager.removeNetwork(wifiInfo.getNetworkId());
        mWifiManager.saveConfiguration();
    }

    public WifiInfo getConnectionInfo() {
        return mWifiManager.getConnectionInfo();
    }
    public void removeNetwork(String ssid) {
        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
        if (configs != null) {
            for (WifiConfiguration config : configs) {
                loadWifiConfigurationFromProfile(config);
                if (config.SSID.contains(ssid)) {
                    mWifiManager.disableNetwork(config.networkId);
                    mWifiManager.removeNetwork(config.networkId);
                }
            }
        }
        mWifiManager.saveConfiguration();
    }
    public void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = connectivityManager.getClass().getMethod("setMobileDataEnabled", boolean.class);
            method.invoke(connectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getMobileDataEnabled(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = connectivityManager.getClass().getMethod("getMobileDataEnabled");

            return (Boolean)method.invoke(connectivityManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static interface WifiStateListener {
        public void onScanFinished(List<ScanResult> scanResults);
        public void onSupplicantStateChanged(SupplicantState state, int supplicantError);
        public void onSupplicantConnectionChanged(boolean connected);
        public void onWifiStateChanged(int wifiState, int prevWifiState);
        public void onWifiApStateChanged(int wifiApState);
        public void onNetworkIdsChanged();

        public void onRSSIChanged(int rssi);

        public void onPickWifiNetwork();

        public void onConnectionPreparing(String ssid);
        public void onConnectionPrepared(boolean success, String ssid);

        public void onConnectNetworkSucceeded(NetworkInfo networkInfo, WifiInfo wifiInfo);

        public void onConnectNetworkFailed(NetworkInfo networkInfo);
    }
}
