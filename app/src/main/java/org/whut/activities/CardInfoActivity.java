package org.whut.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.whut.beans.ResultPair;
import org.whut.gasmanagement.R;
import org.whut.utils.CardStrings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baisu on 15-5-29.
 */
public class CardInfoActivity extends Activity {

    private ListView infoList;

    private List<ResultPair> mList = new ArrayList<ResultPair>();
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String cardData = bundle.getString("cardData");

        parseData(cardData);
        initView();
    }

    /**
     * 解析IC卡
     *
     * GRK-3型 和 非GRK-3型维修2卡的区别：
     *      GRK-3型      75 存储单元存放补气次数， 65 存储单元存放 00
     *      非GRK-3型    65 存储单元存放补气次数， 75 存储单元存放 00
     * @param cardData
     */
    private void parseData(String cardData) {
        if (cardData.substring(0, 2).equals("DD")) {
            parseUserCard(cardData);
        } else if (cardData.substring(0, 2).equals("88") && cardData.substring(66, 68).equals("00")) {
            parseRe2GRK(cardData);
        } else {
            parseRe2NGRK(cardData);
        }
    }

    /**
     * 解析用户卡
     */
    private void parseUserCard(String cardData) {
        mList.add(new ResultPair(CardStrings.CARD_TYPE, CardStrings.USER_CARD));

        StringBuilder BH = new StringBuilder();
        int temp = Integer.parseInt(cardData.substring(2,4),16);
        if (temp < 10) {
            BH.append(0);
        }
        BH.append(temp);
        temp = Integer.parseInt(cardData.substring(4,6),16);
        if (temp < 10) {
            BH.append(0);
        }
        BH.append(temp);
        temp = Integer.parseInt(cardData.substring(6,8),16);
        if (temp < 10) {
            BH.append(0);
        }
        BH.append(temp);
        temp = Integer.parseInt(cardData.substring(8,10),16);
        if (temp < 10) {
            BH.append(0);
        }
        BH.append(temp);
        mList.add(new ResultPair(CardStrings.BH, BH.toString()));

        mList.add(new ResultPair(CardStrings.MM, cardData.substring(10, 16)));

        double SRQL = 0;
        SRQL = Integer.parseInt(cardData.substring(16,18),16) * 100;
        SRQL += Integer.parseInt(cardData.substring(18,20),16);
        SRQL += Integer.parseInt(cardData.substring(20,22),16) * 0.1;
        mList.add(new ResultPair(CardStrings.SRQL, String.valueOf(SRQL)));


        int SRZL = 0;
        SRZL = Integer.parseInt(cardData.substring(28,30),16) * 1000;
        SRZL += Integer.parseInt(cardData.substring(30,32),16) * 100;
        SRZL += Integer.parseInt(cardData.substring(32,34),16);
        mList.add(new ResultPair(CardStrings.SRZL, String.valueOf(SRZL)));


        if ("AA".equals(cardData.substring(36,38))) {
            mList.add(new ResultPair(CardStrings.MMCDBZ, "是"));
        } else {
            mList.add(new ResultPair(CardStrings.MMCDBZ, "否"));
        }


        int BQCS = Integer.parseInt(cardData.substring(38,40),16);
        mList.add(new ResultPair(CardStrings.BQCS, String.valueOf(BQCS)));


        if ("AA".equals(cardData.substring(56,58))) {
            mList.add(new ResultPair(CardStrings.BHXBZ, "回写"));
        } else {
            mList.add(new ResultPair(CardStrings.BHXBZ, "未回写"));
        }

		double BSYQL = 0;
		BSYQL += Integer.parseInt(cardData.substring(58,60),16) * 100;
		BSYQL += Integer.parseInt(cardData.substring(60,62),16);
		BSYQL += Integer.parseInt(cardData.substring(62,64),16) * 0.1;
		mList.add(new ResultPair(CardStrings.BSYQL, String.valueOf(BSYQL)));


		int BSRZL = 0;
		BSRZL += Integer.parseInt(cardData.substring(64,66),16) * 10000;
		BSRZL += Integer.parseInt(cardData.substring(66,68),16) * 100;
		BSRZL += Integer.parseInt(cardData.substring(68,70),16);
		mList.add(new ResultPair(CardStrings.BSRZL, String.valueOf(BSRZL)));


		mList.add(new ResultPair(CardStrings.KHBH, cardData.substring(76,78)));
		mList.add(new ResultPair(CardStrings.DQBH, cardData.substring(78,80)));
		mList.add(new ResultPair(CardStrings.JGBH, cardData.substring(80,82)));


		double ZHQQL = 0;
		ZHQQL += Integer.parseInt(cardData.substring(82,84),16) * 100;
		ZHQQL += Integer.parseInt(cardData.substring(84,86),16);
		ZHQQL += Integer.parseInt(cardData.substring(86,88),16) * 0.1;
		mList.add(new ResultPair(CardStrings.ZHQQL, String.valueOf(ZHQQL)));


		double ZHHQL = 0;
		ZHHQL += Integer.parseInt(cardData.substring(88,90),16) * 100;
		ZHHQL += Integer.parseInt(cardData.substring(90,92),16);
		ZHHQL += Integer.parseInt(cardData.substring(92,94),16) * 0.1;
		mList.add(new ResultPair(CardStrings.ZHHQL, String.valueOf(ZHHQL)));

		int TZTBZ = Integer.parseInt(cardData.substring(94,96), 16);
		if (0 == TZTBZ) {
			mList.add(new ResultPair(CardStrings.TZTBZ, "不透支"));
		} else if (1 == TZTBZ) {
			mList.add(new ResultPair(CardStrings.TZTBZ, "透支"));
		} else {
			mList.add(new ResultPair(CardStrings.TZTBZ, ""));
		}
    }

    /**
     * 解析GRK-3型维修2卡
     * @param cardData
     */
    private void parseRe2GRK(String cardData) {
        mList.add(new ResultPair(CardStrings.CARD_TYPE, CardStrings.RE2_GRK));

        int temp = Integer.parseInt(cardData.substring(32,34),16);
        switch (temp) {
            case 0:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.YHT));
                break;
            case 1:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.YST));
                break;
            case 2:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.JJYQT));
                break;
            case 3:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.AZWG));
                break;
            case 4:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.WXT));
                break;
            case 5:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.WXWG));
                break;
            case 6:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.WXLSBJ));
                break;
            case 7:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.TZT));
                break;
        }

        temp = Integer.parseInt(cardData.substring(34,36),16);
        switch (temp) {
            case 0:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.FMWZS));
                break;
            case 1:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.FMWCC));
                break;
            case 2:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.CGH));
                break;
            case 3:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.CGBJ));
                break;
            case 4:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.NDCD));
                break;
            case 5:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.WDCD));
                break;
            case 6:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.QLL));
                break;
            case 7:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.XTSJC));
                break;
        }

        double bsyql = 0;
        bsyql += Integer.parseInt(cardData.substring(40,42),16) * 100;
        bsyql += Integer.parseInt(cardData.substring(42,44),16);
        bsyql += Integer.parseInt(cardData.substring(44,46),16) * 0.1;
        mList.add(new ResultPair(CardStrings.BSYQL, String.valueOf(bsyql)));

        int srzl = 0;
        srzl += Integer.parseInt(cardData.substring(46,48),16) * 1000;
        srzl += Integer.parseInt(cardData.substring(48,50),16) * 100;
        srzl += Integer.parseInt(cardData.substring(50,52),16);
        mList.add(new ResultPair(CardStrings.SRZL, String.valueOf(srzl)));

        int bh = 0;
        bh += Integer.parseInt(cardData.substring(52,54),16) * 1000000;
        bh += Integer.parseInt(cardData.substring(54,56),16) * 10000;
        bh += Integer.parseInt(cardData.substring(56,58),16) * 100;
        bh += Integer.parseInt(cardData.substring(58,60),16);
        mList.add(new ResultPair(CardStrings.BH, String.valueOf(bh)));

        String mm = cardData.substring(60,66);
        mList.add(new ResultPair(CardStrings.MM, mm));
    }

    /**
     * 解析非GRK-3型维修2卡
     * @param cardData
     */
    private void parseRe2NGRK(String cardData) {
        mList.add(new ResultPair(CardStrings.CARD_TYPE, CardStrings.RE2_NGRK));

        int temp = Integer.parseInt(cardData.substring(32,34),16);
        switch (temp) {
            case 0:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.YHT));
                break;
            case 1:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.AZZX));
                break;
            case 4:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.WXT));
                break;
            case 6:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.AZT));
                break;
            case 7:
                mList.add(new ResultPair(CardStrings.CXJDBZ, CardStrings.TZT));
                break;
        }

        temp = Integer.parseInt(cardData.substring(34,36),16);
        switch (temp) {
            case 0:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.FMWZS));
                break;
            case 1:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.FMWCC));
                break;
            case 2:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.JLCGC));
                break;
            case 3:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.CQWJL));
                break;
            case 5:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.DCDYD));
                break;
            case 7:
                mList.add(new ResultPair(CardStrings.XTZTZJ, CardStrings.XTSJC));
                break;
        }

        double bsyql = 0;
        bsyql += Integer.parseInt(cardData.substring(40,42),16) * 100;
        bsyql += Integer.parseInt(cardData.substring(42,44),16);
        bsyql += Integer.parseInt(cardData.substring(44,46),16) * 0.1;
        mList.add(new ResultPair(CardStrings.BSYQL, String.valueOf(bsyql)));

        int srzl = 0;
        srzl += Integer.parseInt(cardData.substring(46,48),16) * 1000;
        srzl += Integer.parseInt(cardData.substring(48,50),16) * 100;
        srzl += Integer.parseInt(cardData.substring(50,52),16);
        mList.add(new ResultPair(CardStrings.SRZL, String.valueOf(srzl)));

        int bh = 0;
        bh += Integer.parseInt(cardData.substring(52,54),16) * 1000000;
        bh += Integer.parseInt(cardData.substring(54,56),16) * 10000;
        bh += Integer.parseInt(cardData.substring(56,58),16) * 100;
        bh += Integer.parseInt(cardData.substring(58,60),16);
        mList.add(new ResultPair(CardStrings.BH, String.valueOf(bh)));

        String mm = cardData.substring(60,66);
        mList.add(new ResultPair(CardStrings.MM, mm));

        int bqcs = Integer.parseInt(cardData.substring(60,62),16);
        mList.add(new ResultPair(CardStrings.BQCS, String.valueOf(bqcs)));


    }

    private void initView() {
        adapter = new CardAdapter(this);
        infoList = (ListView) findViewById(R.id.list_info);
        infoList.setAdapter(adapter);
    }

    public class ViewHolder {
        public TextView name;
        public TextView value;
    }

    public class CardAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;

        public CardAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.card_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.card_item_name);
                holder.value = (TextView) convertView.findViewById(R.id.card_item_value);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.name.setText(mList.get(position).name);
            holder.value.setText(mList.get(position).value);
            return convertView;
        }
    }
}
