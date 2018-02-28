package com.example.san.myapplication.NewDesign;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.san.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class NewHomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

  /*  private int[] tabIcons = {
            R.drawable.attend_cir,
            R.drawable.stud_cir,
            R.drawable.fees_cir,
            R.drawable.exam_cir
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tv_toolbar = (TextView)toolbar.findViewById(R.id.toolbar_title);
        tv_toolbar.setText("Baldwin girls high school");
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    startActivity(new Intent(StudentDeatilsActivity.this,NewHomeActivity.class));
                finish();
            }
        });



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons()
    {
        /*View view = (View) LayoutInflater.from(this).inflate(R.layout.custome_tab_new, null);
        TextView tabOne = (TextView) view.findViewById(R.id.tab);
        ImageView img = (ImageView) view.findViewById(R.id.icon_toolbar);
        tabOne.setText("Student Details");
        img.setImageDrawable(getResources().getDrawable(R.drawable.stud_cir));
    //    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.attend_cir, 0,0);
        view.setSelected(true);
        tabLayout.getTabAt(0).setCustomView(view);*/

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Student Details");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.details_new, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Events");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.coe_new, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Fees");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fees_new, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Attendance");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.attend_new, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText("Exam");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.exam_new, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new StudFragment(), "Student Details");
        adapter.addFrag(new EventsFragment(), "Events");
        adapter.addFrag(new FeesFragment(), "Fees");
        adapter.addFrag(new AttendanceFragment(), "Attendance");
        adapter.addFrag(new ExamFragment(), "Exam");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
