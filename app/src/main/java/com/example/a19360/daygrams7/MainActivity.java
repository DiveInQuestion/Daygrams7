package com.example.a19360.daygrams7;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends Activity {
    private ArrayList<Grams>gramsCollection;
    private List<String> Year=new ArrayList<>();
    ListViewAdapter theListAdapter;
    ListView listView;
    private Calendar c = Calendar.getInstance();
    private int selected_month, selected_year,current_month,current_year, current_day;
    private Button btn_month,btn_year,btn_add,btn_change;
    private int temp;
    private boolean flag=true;
    private RecyclerView recyclerView_month;
    private MonthAdapter monthAdapter;
    private RecyclerView recyclerView_year;
    private YearAdapter yearAdapter;
    private GramsCollectionOperator operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        recyclerView_month=(RecyclerView)findViewById(R.id.monthSelect);
        recyclerView_year=(RecyclerView)findViewById(R.id.yearSelect);
        listView =(ListView)this.findViewById(R.id.MyListView);
        btn_month = (Button)findViewById(R.id.month);
        btn_year = (Button)findViewById(R.id.year);
        btn_add=(Button)findViewById(R.id.add);
        btn_change=(Button)findViewById(R.id.change);

        btn_month.setText(getMonth(c.get(Calendar.MONTH)+1));
        btn_year.setText(""+c.get(Calendar.YEAR));

        current_month=c.get(Calendar.MONTH)+1;
        current_year=c.get(Calendar.YEAR);
        current_day =c.get(Calendar.DAY_OF_MONTH);
        selected_month=c.get(Calendar.MONTH)+1;
        selected_year =c.get(Calendar.YEAR);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_month.setLayoutManager(layoutManager);


        initYearNames();
        LinearLayoutManager layoutManager_year = new LinearLayoutManager(this);
        layoutManager_year.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_year.setLayoutManager(layoutManager_year);


        operator=new GramsCollectionOperator();
        operator.setFile(""+current_month,""+current_year);
        gramsCollection=operator.load(getBaseContext());
        if(gramsCollection==null) {
            gramsCollection = new ArrayList<Grams>();
            reactToSelectedDate();
        }
        theListAdapter= new ListViewAdapter();
        listView.setAdapter(theListAdapter);

        /*
        * 点击月份选择，并处理选择结果
        * */
        btn_month.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                recyclerView_month.setVisibility(recyclerView_month.VISIBLE);
                monthAdapter = new MonthAdapter(selected_month);
                recyclerView_month.setAdapter(monthAdapter);
                monthAdapter.setOnItemClickListener(new MonthAdapter.OnItemClickListener(){
                    public void onItemClick(View v,int position){
                        selected_month = position+1;
                        //Toast.makeText(v.getContext(),""+selected_month,Toast.LENGTH_SHORT).show();
                        if(selected_month>current_month&&selected_year==current_year) {

                        }
                        else {
                            btn_month.setText(getMonth(position + 1));
                            recyclerView_month.setVisibility(recyclerView_month.INVISIBLE);
                            operator.setFile("" + selected_month, "" + selected_year);
                            gramsCollection = operator.load(getBaseContext());
                            if (gramsCollection == null) {
                                gramsCollection = new ArrayList<Grams>();
                                reactToSelectedDate();
                            }
                            theListAdapter = new ListViewAdapter();
                            listView.setAdapter(theListAdapter);
                            monthAdapter.notifyDataSetChanged();
                            // recyclerView_month.setAdapter(monthAdapter);
                        }

                    }
                });
        }});

        /*
        * 点击年份选择，并处理选择结果
        * */
        btn_year.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                recyclerView_year.setVisibility(recyclerView_year.VISIBLE);
                yearAdapter = new YearAdapter(Year,selected_year);
                recyclerView_year.setAdapter(yearAdapter);
                yearAdapter.setOnItemClickListener(new YearAdapter.OnItemClickListener(){
                    public void onItemClick(View v,String data){
                        //Toast.makeText(v.getContext(),data,Toast.LENGTH_SHORT).show();
                        selected_year = Integer.parseInt(data);
                        btn_year.setText(data);
                        recyclerView_year.setVisibility(recyclerView_year.INVISIBLE);
                        operator.setFile(""+selected_month,""+selected_year);
                        gramsCollection=operator.load(getBaseContext());
                        if(gramsCollection==null) {
                            gramsCollection = new ArrayList<Grams>();
                            reactToSelectedDate();
                        }
                        theListAdapter= new ListViewAdapter();
                        listView.setAdapter(theListAdapter);
                        yearAdapter.notifyDataSetChanged();
                        // recyclerView_year.setAdapter(yearAdapter);
                    }
                });
            }});

        btn_add.setOnClickListener(new addClick());
        btn_change.setOnClickListener(new changeClick());
        listView.setOnItemClickListener(new ItemClick());
        listView.setOnItemLongClickListener(new TextLongClickListener());
    }


    @Override
    public void onBackPressed() {
        if(recyclerView_month.getVisibility() == View.VISIBLE){
            recyclerView_month.setVisibility(View.INVISIBLE);
        }
        else if(recyclerView_year.getVisibility() == View.VISIBLE){
            recyclerView_year.setVisibility(View.INVISIBLE);
        }
        else
            super.onBackPressed();
    }

    /*
        * 显示多种View的适配器，所有的数据都从gramsCollection加载
        * */
    public class ListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if((selected_year ==current_year)&&(selected_month==current_month))
                return current_day;
            else
                return getMonthOfDay(selected_month, selected_year);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =null;
            int type = getItemViewType(position);
            switch (type){
                case 0:
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.blackdot_item,parent,false);
                    break;
                case 1:
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.reddot_item,parent,false);
                    break;
                case 2:
                    view= LayoutInflater.from(MainActivity.this).inflate(R.layout.listview_item,parent,false);
                    TextView textView1 = (TextView) view.findViewById(R.id.week);
                    TextView textView2 = (TextView) view.findViewById(R.id.day);
                    TextView textView3 = (TextView) view.findViewById(R.id.text);
                    textView1.setText(getAbbrWeek(gramsCollection.get(position).getWeek()));
                    textView2.setText(String.valueOf(gramsCollection.get(position).getDay()));
                    textView3.setText(gramsCollection.get(position).getEditText());
                    if(gramsCollection.get(position).getWeek()==1)
                        textView2.setTextColor(android.graphics.Color.RED);
                    break;
                case 3:
                    if(gramsCollection.get(position).getEditText()==null){
                        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_text_item2, parent, false);
                        break;
                    }
                    else {
                        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_text_item, parent, false);
                        TextView View1 = (TextView) view.findViewById(R.id.list_text_day);
                        TextView View2 = (TextView) view.findViewById(R.id.list_text_week);
                        TextView View3 = (TextView) view.findViewById(R.id.list_text_content);
                        View2.setText(getWeek(gramsCollection.get(position).getWeek()));
                        View1.setText(String.valueOf(gramsCollection.get(position).getDay()));
                        View3.setText(gramsCollection.get(position).getEditText());
                        if (gramsCollection.get(position).getWeek() == 1)
                            View2.setTextColor(android.graphics.Color.RED);
                        break;
                    }
            }
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 4;//共4种布局
        }

        public int getItemViewType(int position) {
            if(!flag)
                return 3;
            else {
                if ((gramsCollection.get(position).getEditText() != null)
                        && (gramsCollection.get(position).getEditText().length() != 0))
                    return 2;
                else if (gramsCollection.get(position).getWeek() == 1)
                    return 1;
                else
                    return 0;
            }
        }
    }

    /*
    * 将数据传进编辑页面进行编辑
    * */
    class ItemClick implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            temp = arg2;
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,EditActivity.class);
            intent.putExtra("week",""+gramsCollection.get(arg2).getWeek());
            intent.putExtra("month",""+gramsCollection.get(arg2).getMonth());
            intent.putExtra("day",""+gramsCollection.get(arg2).getDay());
            intent.putExtra("year",""+gramsCollection.get(arg2).getYear());
            intent.putExtra("text",gramsCollection.get(arg2).getEditText());
            startActivityForResult(intent,1);
        }
    }
    /*
     * 设置点击“+”的事件处理
     * */
    class addClick implements OnClickListener{
        public void onClick(View v){
            operator = new GramsCollectionOperator();
            gramsCollection = new ArrayList<Grams>();
            operator.setFile(""+current_month,""+current_year);
            gramsCollection=operator.load(getBaseContext());
            //Toast.makeText(getApplicationContext(), ""+gramsCollection.size(), Toast.LENGTH_SHORT).show();
            if(gramsCollection==null) {
                gramsCollection = new ArrayList<Grams>();
                reactToCurrentDate();
            }
            int position = current_day;
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,EditActivity.class);
            intent.putExtra("week",""+gramsCollection.get(position-1).getWeek());
            intent.putExtra("month",""+gramsCollection.get(position-1).getMonth());
            intent.putExtra("day",""+gramsCollection.get(position-1).getDay());
            intent.putExtra("year",""+gramsCollection.get(position-1).getYear());
            intent.putExtra("text",gramsCollection.get(position-1).getEditText());

            startActivityForResult(intent,2);
        }
    }
    /*
     * 设置点击“==”的事件处理,实现页面样式转换
     * */
    class changeClick implements OnClickListener{
        public void onClick(View v){
            if(flag==true) {
                flag=false;
                theListAdapter = new ListViewAdapter();
                listView.setAdapter(theListAdapter);
            }
            else if(flag==false){
                flag=true;
                theListAdapter = new ListViewAdapter();
                listView.setAdapter(theListAdapter);
            }
        }
    }
    /*
    * 接收回传信息
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Toast.makeText(getApplicationContext(), ""+temp, Toast.LENGTH_SHORT).show();
        if (requestCode==1)
        {
            if (resultCode==EditActivity.RESULT_OK)
            {
                Bundle bundle=data.getExtras();
                String str = bundle.getString("editText");
                if(str.length()!=0) {
                    gramsCollection.get(temp).setEditText(str);
                    GramsCollectionOperator operator = new GramsCollectionOperator();
                    operator.setFile("" + selected_month, "" + selected_year);
                    operator.save(MainActivity.this.getBaseContext(), gramsCollection);
                    theListAdapter.notifyDataSetChanged();
                }
            }
        }
        else if(requestCode==2){
            if (resultCode==EditActivity.RESULT_OK)
            {
                btn_month.setText(getMonth(current_month));
                btn_year.setText(""+current_year);
                int position =current_day;
                Bundle bundle=data.getExtras();
                String str = bundle.getString("editText");
                selected_month =current_month;
                selected_year =current_year;
                //Toast.makeText(getApplicationContext(), str.length(), Toast.LENGTH_SHORT).show();
                if(str!=null&&str.length()!=0) {
                    gramsCollection.get(position-1).setEditText(str);
                    operator = new GramsCollectionOperator();
                    operator.setFile("" + current_month, "" + current_year);
                    operator.save(MainActivity.this.getBaseContext(), gramsCollection);
                }
                theListAdapter.notifyDataSetChanged();
            }
        }
    }

    /*
    * 设置长摁跳出删除或取消删除事件
    * */
    class TextLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final Grams selectedItem = gramsCollection.get(position);
            if (null != selectedItem.getEditText()) {
                final int textYear = selectedItem.getYear();

                final int textMonth = selectedItem.getMonth();
                final int textDay = selectedItem.getDay();
                String monthText;
                String dayText;
                if (10 > textMonth)
                    monthText = "0" + Integer.toString(textMonth);
                else
                    monthText = Integer.toString(textMonth);
                if (10 > textDay)
                    dayText = "0" + Integer.toString(textDay);
                else
                    dayText = Integer.toString(textDay);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.app_name);
                dialog.setMessage(getString(R.string.dialog_text, textYear, monthText, dayText));
                dialog.setCancelable(true);
                dialog.setPositiveButton(R.string.dialog_select_text2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gramsCollection.get(position).setEditText(null);
                        GramsCollectionOperator operator = new GramsCollectionOperator();
                        operator.setFile("" + textMonth, "" + textYear);
                        operator.save(MainActivity.this.getBaseContext(), gramsCollection);
                        theListAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton(R.string.dialog_select_text1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
            return true;
        }
    };

    public void initYearNames(){
        for(int i=2010;i<=c.get(Calendar.YEAR);i++){
            Year.add(""+i);
        }
    }
    public static int getMonthOfDay(int month,int year){
        int day = 0;
        if(year%4==0&&year%100!=0||year%400==0){
            day = 29;
        }else{
            day = 28;
        }
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;

        }

        return 0;
    }

    public int getDateWeek(String pTime) {
        int Week=-1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 1:
                Week =1;
                break;
            case 2:
                Week =2;
                break;
            case 3:
                Week =3;
                break;
            case 4:
                Week =4;
                break;
            case 5:
                Week =5;
                break;
            case 6:
                Week =6;
                break;
            case 7:
                Week =7;
                break;
            default:
                break;
        }
        return Week;
    }

    public void reactToSelectedDate(){

            for(int i = 1; i<=getMonthOfDay(selected_month, selected_year); i++) {
                Grams grams=new Grams();
                String string = selected_year +"-"+selected_month+"-"+i;
                //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
                grams.setYear(selected_year);
                grams.setMonth(selected_month);
                grams.setDay(i);
                grams.setWeek(getDateWeek(string));
                gramsCollection.add(grams);
                //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
            }
    }
    public void reactToCurrentDate(){
        for(int i = 1; i<=getMonthOfDay(current_month, current_year); i++) {
            Grams grams=new Grams();
            String string = current_year +"-"+current_month+"-"+i;

            grams.setYear(current_year);
            grams.setMonth(current_month);
            grams.setDay(i);
            grams.setWeek(getDateWeek(string));
            gramsCollection.add(grams);
            //Toast.makeText(getApplicationContext(), getDateWeek(string), Toast.LENGTH_SHORT).show();
        }
    }
    public boolean SundayOrNot(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        } else
            return false;
    }

    public String getTime(String week,String month,int day,int year){
        String Time;
        Time = week+" / "+month+" "+day+" / "+year;
        return Time;
    }
    private String getAbbrWeek(int week) {

        String Week="";
        if (week == 1) {
            Week += "SUN";
        }
        if (week == 2) {
            Week += "MON";
        }
        if (week == 3) {
            Week += "TUE";
        }
        if (week == 4) {
            Week += "WED";
        }
        if (week == 5) {
            Week += "THU";
        }
        if (week == 6) {
            Week += "FRI";
        }
        if (week == 7) {
            Week += "SAT";
        }
        return Week;
    }
    private String getWeek(int week) {

        String Week="";
        if (week == 1) {
            Week += "SUNDAY";
        }
        if (week == 2) {
            Week += "MONDAY";
        }
        if (week == 3) {
            Week += "TUESDAY";
        }
        if (week == 4) {
            Week += "WEDNESDAY";
        }
        if (week == 5) {
            Week += "THURSDAY";
        }
        if (week == 6) {
            Week += "FRIDAY";
        }
        if (week == 7) {
            Week += "SATURDAY";
        }
        return Week;
    }
    public String getMonth(int month) {

        String Month="";
        if (month == 1) {
            Month += "JANUARY";
        }
        if (month == 2) {
            Month += "FEBRUARY";
        }
        if (month == 3) {
            Month += "MARCH";
        }
        if (month == 4) {
            Month += "APRIL";
        }
        if (month == 5) {
            Month += "MAY";
        }
        if (month == 6) {
            Month += "JUNE";
        }
        if (month == 7) {
            Month += "JULY";
        }
        if (month == 8) {
            Month += "AUGUST";
        }
        if (month == 9) {
            Month += "SEPTEMBER";
        }
        if (month == 10) {
            Month += "OCTOBER";
        }
        if (month == 11) {
            Month += "NOVEMBER";
        }
        if (month == 12) {
            Month += "DECEMBER";
        }
        return Month;
    }
}
