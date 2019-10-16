
/**
 * Reference code that can be used Android Application developer for integrating Veri5Digitals
 * VideoIdKyc SDK into their application
 *
 */

package com.khoslalabs.trialApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.khoslalabs.base.ViKycResults;
import com.khoslalabs.facesdk.FaceSdkModuleFactory;
import com.khoslalabs.ocrsdk.OcrSdkModuleFactory;
import com.khoslalabs.videoidkyc.ui.init.VideoIdKycInitActivity;
import com.khoslalabs.videoidkyc.ui.init.VideoIdKycInitRequest;
import com.khoslalabs.vikycapi.TimeUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * This is a reference Activity class that can be used by clients to implement their own Activity to invoke VideoIdKyc SDK.
 * MainActivity can be replaced by clients own Activity Name.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int VI_KYC_SDK_REQ_CODE = 7879;

    @BindView(R.id.smtButton)
    Button buton;

    @BindView(R.id.client_code)
    EditText etClientcode;

    @BindView(R.id.apiKey)
    EditText etAPIkey;

    @BindView(R.id.client_salt)
    EditText etSalt;

    @BindView(R.id.mob_No)
    EditText etMobNo;

    @BindView(R.id.email_Id)
    EditText etEmail;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    String salt;

    /**
     * Dummy activity to demonstrate the the required params for invoking SDK.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salt = etSalt.getText().toString();

                startSdk(
                        etClientcode.getText().toString(),
                        etAPIkey.getText().toString(),
                        salt,
                        getOtpType(),
                        etEmail.getText().toString(),
                        etMobNo.getText().toString(),
                        null,
                        true,
                        false,
                        false,
                        false
                );
            }
        });

    }

    /**
     * if client not opted for otp, return null
     * @return OTP method, if client is opted for mobile otp , return MOB_NO
     * if opted for email otp, return EMAIL
     */
    public String getOtpType() {
        if ("".equals(etMobNo.getText().toString())) {
            if ("".equals(etEmail.getText().toString())) {
                return null;
            }
            return "EMAIL";
        }
        return "MOB_NO";
    }



    /**
     *	This function is used to invoke the VideoIDKyc SDK.
     * 	For invoking SDK critical params like clientCode, apiKey,otpType( if OTP is not opted pass NULL),
     *  and other params.
     **/

    public void startSdk(
            String clientCode,  // Client Code shared during Onboarding
            String apiKey,    // Api Key shared during Onboarding
            String salt, // Salt shared during Onboarding
            String otpType, // OTP Type option opted by client it can be null, "MOB_NO" or "EMAIL"
            String email,   //emailId for getting OTP if otpType is EMAIL
            String mobNo,   //Moble No for getting OTP if otpType is MOB_NO
            String screenTitle,
            boolean ocrSdk,
            boolean faceSdk,
            boolean selfieSdk,
            boolean docSelectMin
    ) {

        try {

            /**
             * ReuestId is a unique Id that should be passed for each transaction request.
             * Clients can use their own mechanism for generating requestId. Below given is just for sample.
             */

            String requestId = clientCode + "-" + TimeUtil.getCurTimeMilisec() + "";

            // For request integrity check  Hash Digest is also passed. Refer below how to do the same.

            String hash = calculateHash(clientCode, requestId, apiKey, salt);

            if (hash == null) {
                throw new Exception("Hash cannot be generated.");
            }

            Intent myIntent = new Intent(MainActivity.this, VideoIdKycInitActivity.class);


            // Forming VideoIdKyc Request for invoking SDK.

            VideoIdKycInitRequest.Builder videoIdKycInitRequestBuilder =
                    new VideoIdKycInitRequest
                            .Builder(
                            clientCode,
                            apiKey,
                            "Testing Demo Application",
                            requestId,
                            hash
                    )
                            .otpType(otpType)
                            .email(email)
                            .mobile(mobNo)
                            .screenTitle(screenTitle)
                            .salt(salt);

            //include ocr module factory if your flow contains any scan purpose
            videoIdKycInitRequestBuilder.moduleFactory(OcrSdkModuleFactory.newInstance());

            //include face SDK module factory if your flow have selfie(liveness check)
            videoIdKycInitRequestBuilder.moduleFactory(FaceSdkModuleFactory.newInstance());

            myIntent.putExtra("init_request", videoIdKycInitRequestBuilder.build());

            startActivityForResult(myIntent, VI_KYC_SDK_REQ_CODE);

        } catch (Exception e) {
            Log.e(TAG, "startSdk: ", e);
        }
    }


    /**
     * This function handles response from SDK post processing
     *
     * UserId will be returned from veri5Digital android SDK result, which then client will use for getting the
     * KYCinfo of user by calling fetchKycInfo API.
     *
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == VI_KYC_SDK_REQ_CODE) {

            if (resultCode == ViKycResults.RESULT_OK
                    || resultCode == ViKycResults.RESULT_DOC_COMPLETE) {

                if (data != null) {

                    String userId = data.getStringExtra(ViKycConstants.KEY_USER_ID);  //here user id is taken, will be used by client to get the KYCinfo block of user

                    if (userId != null) {
/**
 * here we have are calling one method(startkycDataActivity) as demo to process the KYCinfo block
 */
                        startKycDataActivity(
                                userId,
                                etClientcode.getText().toString(),
                                etAPIkey.getText().toString(),
                                salt
                        );
                    }

                } else {
                    showToast("Video Id KYC was not completed!");
                }

            } else {

                if (data != null) {

                    int code = data.getIntExtra(ViKycConstants.KEY_ERROR_CODE, 333);
                    String msg = data.getStringExtra(ViKycConstants.KEY_ERROR_MESSAGE);

                    if (msg != null) {
                        showToast(code + " : " + msg);
                    }

                } else {
                    showToast("Video Id KYC was not completed!");
                }
            }
        }
    }


    /**
     * using userId client can implement their own mechanism to get KYCinfo block
     * StartKycDataActivity method pass transaction credentials(client code user id, api key, salt) to a activity(we have named "KycDataActivity")
     * where fetchKYCInfo Api has been implemented and responses are displayed on screen.
     */

    private void startKycDataActivity(String userId,
                                      String clientCode,
                                      String apiKey,
                                      String salt) {
        Intent i = new Intent(this, KycDataActivity.class);
        i.putExtra("user_id", userId);
        i.putExtra("client_code", clientCode);
        i.putExtra("api_key", apiKey);
        i.putExtra("salt", salt);
        startActivity(i);
    }

    private void showToast(String s) {
        Toast.makeText(MainActivity.this, s + "", Toast.LENGTH_LONG).show();
    }

    /**
     *
     * Function to calculate Hash Digest for initiating the SDK. It uses  sequence of clientCode|requestId|apiKey|salt
     *
     * @param clientCode
     * @param requestId
     * @param apiKey
     * @param salt
     * @return
     * @throws NoSuchAlgorithmException
     */

    private String calculateHash(String clientCode, String requestId, String apiKey, String salt)
            throws NoSuchAlgorithmException {

        MessageDigest digest;

        digest = MessageDigest.getInstance("SHA-256");
        if (digest != null) {

            byte[] hash =
                    digest
                            .digest(
                                    (clientCode + "|" + requestId + "|" + apiKey + "|" + salt)
                                            .getBytes()
                            );

            return bytesToHex(hash);
        }

        return null;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}

