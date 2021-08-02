
package com.mauriciomartinscruz.CybersourceDeviceFingerprint;

import android.app.Application;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.threatmetrix.TrustDefender.TMXProfiling;
import com.threatmetrix.TrustDefender.TMXConfig;
import com.threatmetrix.TrustDefender.TMXStatusCode;
import com.threatmetrix.TrustDefender.TMXProfilingOptions;
import com.threatmetrix.TrustDefender.TMXEndNotifier;
import com.threatmetrix.TrustDefender.TMXProfilingHandle.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class RNCybersourceDeviceFingerprintModule extends ReactContextBaseJavaModule {

    private static final String CYBERSOURCE_SDK = "RNCybersourceDeviceFingerprint";
    private TMXProfiling _defender = null;

    public RNCybersourceDeviceFingerprintModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return CYBERSOURCE_SDK;
    }

    @ReactMethod
    public void configure(final String orgId, final String serverURL, final Promise promise) {
        if (_defender != null) {
            promise.reject(CYBERSOURCE_SDK, "CyberSource SDK is already initialised");
            return;
        }

        _defender = TMXProfiling.getInstance();

        try {
            TMXConfig config = new TMXConfig()
                    .setOrgId(orgId)
                    .setFPServer(serverURL)
                    .setContext(getReactApplicationContext());

            _defender.init(config);
        } catch (IllegalArgumentException exception) {
            promise.reject(CYBERSOURCE_SDK, "Invalid parameters");
        }
        promise.resolve(true);
    }

    @ReactMethod
    public void getSessionID(final String merchantId, final Promise promise) {
        if (_defender == null) {
            promise.reject(CYBERSOURCE_SDK, "CyberSource SDK is not yet initialised");
            return;
        }

        String sessionId = merchantId + new Date().getTime();
        TMXProfilingOptions options = new TMXProfilingOptions().setCustomAttributes(null);
        options.setSessionID(sessionId);

        TMXProfiling.getInstance().profile(options, new CompletionNotifier(promise));
    }

    private class CompletionNotifier implements TMXEndNotifier {
        private final Promise _promise;

        CompletionNotifier(Promise promise) {
            super();
            _promise = promise;
        }

        @Override
        public void complete(Result result) {
            WritableMap map = new WritableNativeMap();
            map.putString("sessionId", result.getSessionID());
            map.putInt("status", result.getStatus().ordinal());
            _promise.resolve(map);
        }
    }

}
