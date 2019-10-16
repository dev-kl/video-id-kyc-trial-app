package com.khoslalabs.trialApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.khoslalabs.vikycapi.api.ApiException;
import com.khoslalabs.vikycapi.api.ApiRequest;
import com.khoslalabs.vikycapi.api.ApiResponse;
import com.khoslalabs.vikycapi.api.model.FetchKycInfo;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * this activity implements fetchKYCInfo API.
 * and displays a response of fetchKYCInfo API.
 */
public class KycDataActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.docFaceIv)
    ImageView docFaceIv;

    @BindView(R.id.liveFaceIv)
    ImageView liveFaceIv;

    @BindView(R.id.matchStatusLabel)
    TextView matchStatusLabel;

    @BindView(R.id.matchStatusTv)
    TextView matchStatusTv;

    @BindView(R.id.agentStatusLabel)
    TextView agentStatusLabel;

    @BindView(R.id.agentStatusTv)
    TextView agentStatusTv;

    @BindView(R.id.docFrontIv)
    ImageView docFrontIv;

    @BindView(R.id.docBackIv)
    ImageView docBackIv;

    @BindView(R.id.vrfByLabelOrgTv)
    TextView vrfByLabelOrgTv;

    @BindView(R.id.vrfByOrgTv)
    TextView vrfByOrgTv;

    @BindView(R.id.vrfUsingLabelOrgTv)
    TextView vrfUsingLabelOrgTv;

    @BindView(R.id.vrfUsingOrgTv)
    TextView vrfUsingOrgTv;

    @BindView(R.id.docNo1LabelOrgTv)
    TextView docNo1LabelOrgTv;

    @BindView(R.id.docNo1OrgTv)
    TextView docNo1OrgTv;

    @BindView(R.id.nameLabelOrgTv)
    TextView nameLabelOrgTv;

    @BindView(R.id.nameOrgTv)
    TextView nameOrgTv;

    @BindView(R.id.fathersNameLabelOrgTv)
    TextView fathersNameLabelOrgTv;

    @BindView(R.id.fathersNameOrgTv)
    TextView fathersNameOrgTv;

    @BindView(R.id.mothersNameLabelOrgTv)
    TextView mothersNameLabelOrgTv;

    @BindView(R.id.mothersNameOrgTv)
    TextView mothersNameOrgTv;

    @BindView(R.id.genderLabelOrgTv)
    TextView genderLabelOrgTv;

    @BindView(R.id.genderOrgTv)
    TextView genderOrgTv;

    @BindView(R.id.dobLabelOrgTv)
    TextView dobLabelOrgTv;

    @BindView(R.id.dobOrgTv)
    TextView dobOrgTv;

    @BindView(R.id.emailLabelOrgTv)
    TextView emailLabelOrgTv;

    @BindView(R.id.emailOrgTv)
    TextView emailOrgTv;

    @BindView(R.id.mobileLabelOrgTv)
    TextView mobileLabelOrgTv;

    @BindView(R.id.mobileOrgTv)
    TextView mobileOrgTv;

    @BindView(R.id.addressLabelOrgTv)
    TextView addressLabelOrgTv;

    @BindView(R.id.addressOrgTv)
    TextView addressOrgTv;

    @BindView(R.id.docNo1LabelDclTv)
    TextView docNo1LabelDclTv;

    @BindView(R.id.docNo1DclTv)
    TextView docNo1DclTv;

    @BindView(R.id.docVerificationStatusLabelDclTv)
    TextView docVerificationStatusLabelDclTv;

    @BindView(R.id.docVerificationStatusDclTv)
    TextView docVerificationStatusDclTv;

    @BindView(R.id.docVerificationCodeLabelDclTv)
    TextView docVerificationCodeLabelDclTv;

    @BindView(R.id.docVerificationCodeDclTv)
    TextView docVerificationCodeDclTv;

    @BindView(R.id.docVerificationMsgLabelDclTv)
    TextView docVerificationMsgLabelDclTv;

    @BindView(R.id.docVerificationMsgDclTv)
    TextView docVerificationMsgDclTv;

    @BindView(R.id.docVerificationStatusLabelOrgTv)
    TextView docVerificationStatusLabelOrgTv;

    @BindView(R.id.docVerificationStatusOrgTv)
    TextView docVerificationStatusOrgTv;

    @BindView(R.id.docVerificationCodeLabelOrgTv)
    TextView docVerificationCodeLabelOrgTv;

    @BindView(R.id.docVerificationCodeOrgTv)
    TextView docVerificationCodeOrgTv;

    @BindView(R.id.docVerificationMsgLabelOrgTv)
    TextView docVerificationMsgLabelOrgTv;

    @BindView(R.id.docVerificationMsgOrgTv)
    TextView docVerificationMsgOrgTv;

    @BindView(R.id.vrfByLabelDclTv)
    TextView vrfByLabelDclTv;

    @BindView(R.id.vrfByDclTv)
    TextView vrfByDclTv;

    @BindView(R.id.vrfUsingLabelDclTv)
    TextView vrfUsingLabelDclTv;

    @BindView(R.id.vrfUsingDclTv)
    TextView vrfUsingDclTv;

    @BindView(R.id.nameLabelDclTv)
    TextView nameLabelDclTv;

    @BindView(R.id.nameDclTv)
    TextView nameDclTv;

    @BindView(R.id.fathersNameLabelDclTv)
    TextView fathersNameLabelDclTv;

    @BindView(R.id.fathersNameDclTv)
    TextView fathersNameDclTv;

    @BindView(R.id.mothersNameLabelDclTv)
    TextView mothersNameLabelDclTv;

    @BindView(R.id.mothersNameDclTv)
    TextView mothersNameDclTv;

    @BindView(R.id.genderLabelDclTv)
    TextView genderLabelDclTv;

    @BindView(R.id.genderDclTv)
    TextView genderDclTv;

    @BindView(R.id.dobLabelDclTv)
    TextView dobLabelDclTv;

    @BindView(R.id.dobDclTv)
    TextView dobDclTv;

    @BindView(R.id.emailLabelDclTv)
    TextView emailLabelDclTv;

    @BindView(R.id.emailDclTv)
    TextView emailDclTv;

    @BindView(R.id.mobileLabelDclTv)
    TextView mobileLabelDclTv;

    @BindView(R.id.mobileDclTv)
    TextView mobileDclTv;

    @BindView(R.id.addressLabelDclTv)
    TextView addressLabelDclTv;

    @BindView(R.id.addressDclTv)
    TextView addressDclTv;

    @BindView(R.id.loadingParentV)
    View loadingParentV;

    @BindView(R.id.loadingTv)
    TextView loadingTv;

    @BindView(R.id.doneBtn)
    Button doneBtn;

    @BindView(R.id.dclKycInfoTitleTv)
    TextView dclKycInfoTitleTv;

    @BindView(R.id.orgKycInfoTitleTv)
    TextView orgKycInfoTitleTv;

    @BindView(R.id.equalSignTv)
    TextView equalSignTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kyc_data);
        ButterKnife.bind(this);

        try {
            fetchKycInfo();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(KycDataActivity.this, MainActivity.class);
                startActivity(intentMain);
                finish();
            }
        });
    }

    @SuppressWarnings("unused")
    private void fetchKycInfo() throws NoSuchAlgorithmException {

        FetchKycInfo.Req req = new FetchKycInfo.Req(
                getIntent().getStringExtra("user_id"),
                calculateHash(
                        getIntent().getStringExtra("client_code"),
                        getIntent().getStringExtra("user_id"),
                        getIntent().getStringExtra("api_key"),
                        getIntent().getStringExtra("salt")
                ),
                getIntent().getStringExtra("api_key")
        );

        ApiRequest<FetchKycInfo.Req> apiRequest = new ApiRequest<>(
                new ApiRequest.Headers.Builder()
                        .clientCode(getIntent().getStringExtra("client_code"))
                        .build(),
                req
        );

        Disposable d = Util.apiService()
                .fetchKycInfo(apiRequest)
                .flatMap(
                        new Function<ApiResponse<FetchKycInfo.Res>, ObservableSource<ApiResponse<FetchKycInfo.Res>>>() {
                            @Override
                            public ObservableSource<ApiResponse<FetchKycInfo.Res>> apply(ApiResponse<FetchKycInfo.Res> resApiResponse) {
                                return Util.apiResponseInterceptor(resApiResponse);
                            }
                        }
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) {
                                showLoading("Fetching KYC Info...");
                            }
                        }
                )
                .subscribe(
                        new Consumer<ApiResponse<FetchKycInfo.Res>>() {
                            @Override
                            public void accept(ApiResponse<FetchKycInfo.Res> resApiResponse)
                                    throws Exception {
                                handleFetchKycInfoRes(resApiResponse.getResponseData());
                                hideLoading();
                            }
                        },
                        new Consumer<Throwable>() {
                            @SuppressWarnings("RedundantCast")
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                                hideLoading();
                                if (throwable instanceof ApiException) {
                                    showToast(((ApiException) throwable).getMessage());
                                    matchStatusTv.setText(((ApiException) throwable).getMessage());
                                } else {
                                    showToast(throwable.getMessage());
                                    matchStatusTv.setText(throwable.getMessage());
                                }

                                finish();

                            }
                        }
                );
    }

    @SuppressWarnings("SameParameterValue")
    private String calculateHash(String clientCode, String userId, String apiKey, String salt)
            throws NoSuchAlgorithmException {

        MessageDigest digest;

        digest = MessageDigest.getInstance("SHA-256");

        if (digest != null) {

            byte[] hash =
                    digest
                            .digest(
                                    (clientCode + "|" + userId + "|" + apiKey + "|" + salt)
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

    @SuppressWarnings("SameParameterValue")
    private String calculateResponseHash(String clientCode,
                                         String userId,
                                         String KycInfo,
                                         String apiKey,
                                         String salt)
            throws NoSuchAlgorithmException {

        MessageDigest digest;

        digest = MessageDigest.getInstance("SHA-256");

        if (digest != null) {

            byte[] hash =
                    digest
                            .digest(
                                    (clientCode + "|" + userId + "|" + KycInfo + "|" + apiKey + "|" + salt)
                                            .getBytes()
                            );

            return bytesToHex(hash);
        }

        return null;
    }

    private void handleFetchKycInfoRes(FetchKycInfo.Res apiResponse)
            throws Exception {

        String hash = calculateResponseHash(
                getIntent().getStringExtra("client_code"),
                getIntent().getStringExtra("user_id"),
                apiResponse.getKycInfo(),
                getIntent().getStringExtra("api_key"),
                getIntent().getStringExtra("salt")
        );

        Log.i(
                TAG,
                "handleFetchKycInfoRes: Api hash = "
                        + apiResponse.getHash() + ", Calculated hash = " + hash + "."
        );

        if (hash != null && !hash.equalsIgnoreCase(apiResponse.getHash())) {
            throw new IllegalArgumentException("Invalid hash!");
        }

        byte[] decodedResponse = Base64.decode(apiResponse.getKycInfo(), Base64.DEFAULT);

        if (apiResponse.getEncrypted().equalsIgnoreCase("yes")) {
            showPasswordDialog(decodedResponse);
        } else {
            refreshUi(decodedResponse);
        }
    }

    private void showPasswordDialog(final byte[] decodedResponse) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter encryption password");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {

                    AssetManager am = getAssets();
                    InputStream inputStream = am.open("KL123.p12");

                    char[] password = input.getText().toString().toCharArray();
                    String alias = "dummy";
                    KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
                    keyStore.load(inputStream, password);
                    PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);

                    ResponseByteArraySplitter splitter = new ResponseByteArraySplitter(decodedResponse);
                    byte[] sKey = DecryptionUtil.decryptUsingPrivateKey(privateKey, splitter.getEncryptedSymmetricKey());

                    refreshUi(DecryptionUtil.decryptUsingSymmetricKey(sKey, splitter.getEncryptedData()));

                } catch (Exception e) {
                    showToast("Exception occurred while decryption: " + e.getMessage());
                    KycDataActivity.this.finish();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showToast("Decryption cancelled!");
                KycDataActivity.this.finish();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    private void refreshUi(byte[] decodedResponse) {

        FetchKycInfo.Res.KycInfo kycInfo = new Gson().fromJson(
                new String(decodedResponse),
                FetchKycInfo.Res.KycInfo.class
        );

        String docFront = null;
        FetchKycInfo.Res.Document document = kycInfo.getDocument();

        if (document != null) {

            if (document.getPan() != null) {
                docFront = document.getPan();
            } else if (document.getAadhaarFront() != null) {
                docFront = document.getAadhaarFront();
            } else if (document.getPassportFront() != null) {
                docFront = document.getPassportFront();
            } else if (document.getVoterIdFront() != null) {
                docFront = document.getVoterIdFront();
            }

            if (docFront != null) {
                docFrontIv.setVisibility(View.VISIBLE);
                docFrontIv.setImageBitmap(base64ToBmp(docFront));
            }

            String docBack = null;

            if (document.getAadhaarBack() != null) {
                docBack = document.getAadhaarBack();
            } else if (document.getPassportBack() != null) {
                docBack = document.getPassportBack();
            } else if (document.getVoterIdBack() != null) {
                docBack = document.getVoterIdBack();
            }

            if (docBack != null) {
                docBackIv.setVisibility(View.VISIBLE);
                docBackIv.setImageBitmap(base64ToBmp(docBack));
            }
        }

        if (kycInfo.getPhoto() != null) {

            if (kycInfo.getPhoto().getDocFace() != null) {
                docFaceIv.setImageBitmap(base64ToBmp(kycInfo.getPhoto().getDocFace()));
            } else {
                docFaceIv.setBackgroundColor(Color.GRAY);
            }

            if (kycInfo.getPhoto().getLiveFace() != null) {
                liveFaceIv.setImageBitmap(base64ToBmp(kycInfo.getPhoto().getLiveFace()));
            } else {
                liveFaceIv.setBackgroundColor(Color.GRAY);
            }

            matchStatusTv.setText(
                    kycInfo.getPhoto().getMatchStatus() != null
                            ? kycInfo.getPhoto().getMatchStatus().toString()
                            : "NOT FOUND"
            );

            equalSignTv.setText(
                    kycInfo.getPhoto().getMatchStatus() != null
                            ? getEqualChar(kycInfo.getPhoto().getMatchStatus())
                            : "?"
            );

            matchStatusTv.setTextColor(
                    kycInfo.getPhoto().getMatchStatus() != null
                            ? getStatusColor(kycInfo.getPhoto().getMatchStatus())
                            : Color.BLACK
            );

        } else {
            docFaceIv.setBackgroundColor(Color.GRAY);
            liveFaceIv.setBackgroundColor(Color.GRAY);
            matchStatusTv.setText("NOT FOUND");
            equalSignTv.setText("?");
            matchStatusTv.setTextColor(Color.BLACK);
        }

        FetchKycInfo.Res.KycData orgKycData = kycInfo.getOriginalKycData();

        if (orgKycData != null) {

            toggleField(
                    orgKycData.getDocumentVerificationStatus(),
                    docVerificationStatusLabelOrgTv,
                    docVerificationStatusOrgTv
            );

            toggleField(
                    orgKycData.getDocumentVerificationCode(),
                    docVerificationCodeLabelOrgTv,
                    docVerificationCodeOrgTv
            );

            toggleField(
                    orgKycData.getDocumentVerificationMessage(),
                    docVerificationMsgLabelOrgTv,
                    docVerificationMsgOrgTv
            );

            toggleField(orgKycData.getVerifiedBy(), vrfByLabelOrgTv, vrfByOrgTv);
            toggleField(orgKycData.getVerifiedUsing(), vrfUsingLabelOrgTv, vrfUsingOrgTv);

            docNo1LabelOrgTv.setText(
                    String.format("%s Number", orgKycData.getDocType())
            );

            toggleField(orgKycData.getDocumentId(), docNo1LabelOrgTv, docNo1OrgTv);
            toggleField(orgKycData.getName(), nameLabelOrgTv, nameOrgTv);
            toggleField(orgKycData.getFatherName(), fathersNameLabelOrgTv, fathersNameOrgTv);
            toggleField(orgKycData.getMotherName(), mothersNameLabelOrgTv, mothersNameOrgTv);
            toggleField(orgKycData.getGender(), genderLabelOrgTv, genderOrgTv);
            toggleField(orgKycData.getDob(), dobLabelOrgTv, dobOrgTv);
            toggleField(orgKycData.getEmail(), emailLabelOrgTv, emailOrgTv);
            toggleField(orgKycData.getMobile(), mobileLabelOrgTv, mobileOrgTv);
            toggleField(orgKycData.getAddress(), addressLabelOrgTv, addressOrgTv);
        }

        FetchKycInfo.Res.KycData dclKycData = kycInfo.getDeclaredKycData();

        if (dclKycData != null) {

            dclKycInfoTitleTv.setVisibility(View.VISIBLE);

            docNo1LabelDclTv.setText(
                    String.format("%s Number", dclKycData.getDocType())
            );

            toggleField(dclKycData.getDocumentId(), docNo1LabelDclTv, docNo1DclTv);

            toggleField(
                    dclKycData.getDocumentVerificationStatus(),
                    docVerificationStatusLabelDclTv,
                    docVerificationStatusDclTv
            );

            toggleField(
                    dclKycData.getDocumentVerificationCode(),
                    docVerificationCodeLabelDclTv,
                    docVerificationCodeDclTv
            );

            toggleField(
                    dclKycData.getDocumentVerificationMessage(),
                    docVerificationMsgLabelDclTv,
                    docVerificationMsgDclTv
            );

            toggleField(dclKycData.getVerifiedBy(), vrfByLabelDclTv, vrfByDclTv);
            toggleField(dclKycData.getVerifiedUsing(), vrfUsingLabelDclTv, vrfUsingDclTv);
            toggleField(dclKycData.getName(), nameLabelDclTv, nameDclTv);
            toggleField(dclKycData.getFatherName(), fathersNameLabelDclTv, fathersNameDclTv);
            toggleField(dclKycData.getMotherName(), mothersNameLabelDclTv, mothersNameDclTv);
            toggleField(dclKycData.getGender(), genderLabelDclTv, genderDclTv);
            toggleField(dclKycData.getDob(), dobLabelDclTv, dobDclTv);
            toggleField(dclKycData.getEmail(), emailLabelDclTv, emailDclTv);
            toggleField(dclKycData.getMobile(), mobileLabelDclTv, mobileDclTv);
            toggleField(dclKycData.getAddress(), addressLabelDclTv, addressDclTv);

        } else {

            dclKycInfoTitleTv.setVisibility(View.GONE);

            toggleField(null, docNo1LabelDclTv, docNo1DclTv);
            toggleField(null, docVerificationStatusLabelDclTv, docVerificationStatusDclTv);
            toggleField(null, docVerificationCodeLabelDclTv, docVerificationCodeDclTv);
            toggleField(null, docVerificationMsgLabelDclTv, docVerificationMsgDclTv);
            toggleField(null, vrfByLabelDclTv, vrfByDclTv);
            toggleField(null, vrfUsingLabelDclTv, vrfUsingDclTv);
            toggleField(null, nameLabelDclTv, nameDclTv);
            toggleField(null, fathersNameLabelDclTv, fathersNameDclTv);
            toggleField(null, mothersNameLabelDclTv, mothersNameDclTv);
            toggleField(null, genderLabelDclTv, genderDclTv);
            toggleField(null, dobLabelDclTv, dobDclTv);
            toggleField(null, emailLabelDclTv, emailDclTv);
            toggleField(null, mobileLabelDclTv, mobileDclTv);
            toggleField(null, addressLabelDclTv, addressDclTv);

        }

        FetchKycInfo.Res.VerifiedAgent verifiedAgent = kycInfo.getVerifiedAgent();

        if (verifiedAgent != null) {

            toggleField(
                    verifiedAgent.getStatus() != null
                            ? verifiedAgent.getStatus().toString()
                            : "NOT FOUND",
                    agentStatusLabel,
                    agentStatusTv
            );

            agentStatusTv.setTextColor(
                    kycInfo.getVerifiedAgent().getStatus() != null
                            ? getStatusColor(kycInfo.getVerifiedAgent().getStatus())
                            : Color.BLACK
            );

        } else {
            toggleField(null, agentStatusLabel, agentStatusTv);
        }
    }

    private String getEqualChar(FetchKycInfo.Res.MatchStatus matchStatus) {
        switch (matchStatus) {
            case SUCCESS:
                return "=";
            case FAIL:
                return "\u2260";
            case UNCERTAIN:
            default:
                return "~";
        }
    }

    private int getStatusColor(FetchKycInfo.Res.MatchStatus matchStatus) {
        switch (matchStatus) {
            case SUCCESS:
                return ContextCompat.getColor(this, R.color.vikycGreen);
            case FAIL:
                return ContextCompat.getColor(this, R.color.vikycRed);
            case UNCERTAIN:
            default:
                return ContextCompat.getColor(this, R.color.vikycOrange);
        }
    }

    private int getStatusColor(FetchKycInfo.Res.VerifiedAgentStatus matchStatus) {
        switch (matchStatus) {
            case APPROVED:
                return ContextCompat.getColor(this, R.color.vikycGreen);
            case REJECTED:
                return ContextCompat.getColor(this, R.color.vikycRed);
            case NA:
                return ContextCompat.getColor(this, R.color.vikycBlack);
            case PENDING:
            default:
                return ContextCompat.getColor(this, R.color.vikycOrange);
        }
    }

    private void toggleField(String value, TextView labelTv, TextView valTv) {

        valTv.setText("");

        if (value == null) {
            labelTv.setVisibility(View.GONE);
            valTv.setVisibility(View.GONE);
        } else {
            labelTv.setVisibility(View.VISIBLE);
            valTv.setVisibility(View.VISIBLE);
            valTv.setText(getAppropriateValue(value));
        }
    }

    private String getAppropriateValue(String val) {
        return val.equals("NULL") ? " " : val;
    }

    private Bitmap base64ToBmp(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @SuppressWarnings("SameParameterValue")
    private void showLoading(String message) {
        loadingTv.setText(message);
        loadingParentV.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingParentV.setVisibility(View.GONE);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
