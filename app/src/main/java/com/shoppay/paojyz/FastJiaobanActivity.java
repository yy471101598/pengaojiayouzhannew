package com.shoppay.paojyz;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.shoppay.paojyz.bean.JiaobanMsg;
import com.shoppay.paojyz.bean.VipInfoMsg;
import com.shoppay.paojyz.tools.ActivityStack;
import com.shoppay.paojyz.tools.BluetoothUtil;
import com.shoppay.paojyz.tools.DayinUtils;
import com.shoppay.paojyz.tools.DialogUtil;
import com.shoppay.paojyz.tools.LogUtils;
import com.shoppay.paojyz.tools.PreferenceHelper;
import com.shoppay.paojyz.tools.StringUtil;
import com.shoppay.paojyz.tools.UrlTools;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import sunmi.sunmiui.dialog.LoadingDialog;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class FastJiaobanActivity extends Activity {
    @Bind(R.id.img_left)
    ImageView imgLeft;
    @Bind(R.id.rl_left)
    RelativeLayout rlLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_jiaobanNum)
    TextView tvJiaobanNum;
    @Bind(R.id.tv_jiaobanTime)
    TextView tvJiaobanTime;
    @Bind(R.id.tv_tixianhuizong)
    TextView tvTixianhuizong;
    @Bind(R.id.tv_qitayingxiaohuizong)
    TextView tvQitayingxiaohuizong;
    @Bind(R.id.tv_yueyingxiaohuizong)
    TextView tvYueyingxiaohuizong;
    @Bind(R.id.tv_zengsongyingxiaohuizong)
    TextView tvZengsongyingxiaohuizong;
    @Bind(R.id.tv_xianjinyingxiaohuizong)
    TextView tvXianjinyingxiaohuizong;
    @Bind(R.id.tv_wxyingxiaohuizong)
    TextView tvWxyingxiaohuizong;
    @Bind(R.id.tv_yinlianyingxiaohuizong)
    TextView tvYinlianyingxiaohuizong;
    @Bind(R.id.tv_aliyingxiaohuizong)
    TextView tvAliyingxiaohuizong;
    @Bind(R.id.tv_youhuiyingxiaohuizong)
    TextView tvYouhuiyingxiaohuizong;
    @Bind(R.id.tv_jfdkyingxiaohuizong)
    TextView tvJfdkyingxiaohuizong;
    @Bind(R.id.et_qitaxianjinshouru)
    EditText etQitaxianjinshouru;
    @Bind(R.id.et_qitaxianjinzhichu)
    EditText etQitaxianjinzhichu;
    @Bind(R.id.tv_zongyingyeehuizong)
    TextView tvZongyingyeehuizong;
    @Bind(R.id.tv_yinshouxianjinhuizong)
    TextView tvYinshouxianjinhuizong;
    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_xianjinhuizong)
    EditText etXianjinhuizong;
    @Bind(R.id.rl_jiaoban)
    RelativeLayout rlJiaoban;
    private Activity ac;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiaoban);
        ac = this;
        ButterKnife.bind(this);
        dialog= DialogUtil.loadingDialog(ac, 1);
        tvTitle.setText("快速交班");
        obtainJiaobanInfo();
    }

    @OnClick({R.id.rl_left, R.id.rl_jiaoban})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_left:
                finish();
                break;
            case R.id.rl_jiaoban:
                if(null==etAccount.getText().toString()||etAccount.getText().toString().equals("")){
                    Toast.makeText(ac,"请输入账号",Toast.LENGTH_SHORT).show();
                }else  if(null==etXianjinhuizong.getText().toString()||etXianjinhuizong.getText().toString().equals("")) {
                    Toast.makeText(ac, "请输入实收现金汇总", Toast.LENGTH_SHORT).show();
                }else  if(null==etQitaxianjinzhichu.getText().toString()||etQitaxianjinzhichu.getText().toString().equals("")) {
                    Toast.makeText(ac, "请输入其他现金支出", Toast.LENGTH_SHORT).show();
                }else  if(null==etQitaxianjinshouru.getText().toString()||etQitaxianjinshouru.getText().toString().equals("")) {
                    Toast.makeText(ac, "请输入其他现金收入", Toast.LENGTH_SHORT).show();
                }else {
                    jiaoban();
                }
                break;
        }
    }

    private void obtainJiaobanInfo() {
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        RequestParams params = new RequestParams();
        params.put("UserID",  PreferenceHelper.readString(ac, "shoppay", "UserID",""));
        params.put("UserShopID", PreferenceHelper.readString(ac, "shoppay", "ShopID",""));
        LogUtils.d("xxparams", params.toString());
        String url = UrlTools.obtainUrl(ac, "?Source=3", "GetUserWork");
        LogUtils.d("xxurl", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    dialog.dismiss();
                    LogUtils.d("xxJiaobaninfoS", new String(responseBody, "UTF-8"));
                    JSONObject jso = new JSONObject(new String(responseBody, "UTF-8"));
                    if (jso.getInt("flag") == 0) {
                        Gson gson = new Gson();
                        JiaobanMsg infomsg = gson.fromJson(jso.getString("vdata"), JiaobanMsg.class);
                        tvTixianhuizong.setText(StringUtil.twoNum(infomsg.getDrawMoneySum()+""));
                        tvQitayingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyOthePaymentSum()+""));
                        tvYueyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyChangeBalanceSum()+""));
                        tvZengsongyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyChangeGiveMoneySum()+""));
                        tvXianjinyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyChangeCashSum()+""));
                        tvWxyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyWeChatPaySum()+""));
                        tvYinlianyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyChangeUnionPaySum()+""));
                        tvAliyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyAlipaySum()+""));
                        tvYouhuiyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyChangeCouponMoneySum()+""));
                        tvJfdkyingxiaohuizong.setText(StringUtil.twoNum(infomsg.getMoneyPointMoneySum()+""));
                        tvZongyingyeehuizong.setText(StringUtil.twoNum(infomsg.getTotalTurnover()+""));
                        tvYinshouxianjinhuizong.setText(StringUtil.twoNum(infomsg.getCashReceivable()+""));
                        tvJiaobanNum.setText(infomsg.getShiftChangeAccount());
                        tvJiaobanTime.setText(infomsg.getMoneyChangeCreateTime());
                    } else {
                        Toast.makeText(ac,jso.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(ac,"获取信息失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Toast.makeText(ac,"获取信息失败，请重试",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void jiaoban() {
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        RequestParams params = new RequestParams();
        params.put("UserID",  PreferenceHelper.readString(ac, "shoppay", "UserID",""));
        params.put("UserShopID", PreferenceHelper.readString(ac, "shoppay", "ShopID",""));
        params.put("OtherCashIn",etQitaxianjinshouru.getText().toString());
        params.put("OtherCashOut",etQitaxianjinzhichu.getText().toString());
        params.put("Account",etAccount.getText().toString());
        params.put("password",etPassword.getText().toString());
        params.put("TotalCashMoney",etXianjinhuizong.getText().toString());
        LogUtils.d("xxparams", params.toString());
        String url = UrlTools.obtainUrl(ac, "?Source=3", "SubmitUserWork");
        LogUtils.d("xxurl", url);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    dialog.dismiss();
                    LogUtils.d("xxJiaobaninfoS", new String(responseBody, "UTF-8"));
                    JSONObject jso = new JSONObject(new String(responseBody, "UTF-8"));
                    if (jso.getInt("flag") == 1) {
                        Toast.makeText(ac,jso.getString("msg"),Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = (JSONObject) jso.getJSONArray("print").get(0);
                        if (jsonObject.getInt("printNumber") == 0) {
                           finish();
                        } else {
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (bluetoothAdapter.isEnabled()) {
                                BluetoothUtil.connectBlueTooth(MyApplication.context);
                                BluetoothUtil.sendData(DayinUtils.dayin(jsonObject.getString("printContent")), jsonObject.getInt("printNumber"));
                              finish();
                            } else {
                               finish();
                            }
                        }
                    } else {
                        Toast.makeText(ac,jso.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(ac,"交班失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Toast.makeText(ac,"交班失败，请重试",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
