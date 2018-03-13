package edu.nus.trailblazelearn.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.helper.LearningTrailHelper;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.utility.DateUtil;


public class CreateLearningTrailActivity extends AppCompatActivity {

    private static final String TAG = "CreateLearningTrailActivity";
    private EditText edTrailName,edTrailDescription,edTrailStartDate,edTrailEndDate,edTrailCode;
    private StringBuilder trailStartDate;
    private String trailCodeStr;
    DatePickerDialog datePickerDialog;
    Toolbar toolBarLearningActivity;





    @Override protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started: ");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trail);
        toolBarLearningActivity = findViewById(R.id.tb_trail_header);
        toolBarLearningActivity.setTitle(getString(R.string.page_heading_learning));

        edTrailName = findViewById(R.id.et_trail_name);
        edTrailDescription = findViewById(R.id.et_trail_description);
        edTrailStartDate = findViewById(R.id.et_trail_startdate);
        edTrailEndDate = findViewById(R.id.et_trail_enddate);


        /**Invoke Date Picker for selecting start date of Trail**/
            edTrailEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateLearningTrailActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                edTrailEndDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                Log.d(TAG, "edTrailEndDate set to : "+edTrailEndDate.getText().toString());

                              }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        /**Invoke Date Picker for selecting start date of Trail**/
        edTrailStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateLearningTrailActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                               // set day of month , day and year value in the edit text

                                edTrailStartDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                trailStartDate = new StringBuilder();
                                trailStartDate.append(year);
                                trailStartDate.append(monthOfYear+1);
                                trailStartDate.append(dayOfMonth);

                                Log.d(TAG, "edTrailStartDate set to : "+edTrailStartDate.getText().toString());
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (isValid()) {
                    Log.d(TAG, "Fields value are validated to true");
                    trailCodeStr= constructTrailCode(trailStartDate.toString(),edTrailName.getText().toString());

                    LearningTrail pouplatedTrailObj= pouplateTrail();

                    /**Construct Helper to call DB and persist data**/
                    LearningTrailHelper trailHelper = new LearningTrailHelper();
                    trailHelper.createTrail(pouplatedTrailObj);

                    Toast.makeText(CreateLearningTrailActivity.this, getString(R.string.trail_save_successful),
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        Log.d(TAG, "onCreate method invoke finished");
    }


    /**
     * API to validate each field on trail learn page
     * @return
     */
    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(edTrailName.getText().toString().trim())) {
            edTrailName.setError(getString(R.string.trail_name_validation_msg));
            isValid = false;
        }
        if (TextUtils.isEmpty(edTrailDescription.getText().toString().trim())) {
            edTrailDescription.setError(getString(R.string.trail_description_validation_msg));
            isValid = false;
        }
        if (TextUtils.isEmpty(edTrailStartDate.getText().toString().trim())) {
            edTrailStartDate.setError(getString(R.string.trail_startDate_validation_msg));
            isValid = false;
        }
        if (TextUtils.isEmpty(edTrailEndDate.getText().toString().trim())) {
            edTrailEndDate.setError(getString(R.string.trail_endDate_validation_msg));
            isValid = false;
        }
        return isValid;
    }

    /**
     * API pouplating Learning Trail
     * from the UI fields
     * @return
     */
    private LearningTrail pouplateTrail(){

        Log.d(TAG, "pouplateTrail method invoke started");
        //Populate learning trail
        LearningTrail trailObj = new LearningTrail();
        trailObj.setTrailName(edTrailName.getText().toString());
        trailObj.setTrailDescription(edTrailDescription.getText().toString());

        Log.d(TAG,"Value of startDate:"+DateUtil.constructDateFromString(edTrailStartDate.getText().toString()));

        trailObj.setStartDate(DateUtil.constructDateFromString(edTrailStartDate.getText().toString()));
        trailObj.setEndDate(DateUtil.constructDateFromString(edTrailEndDate.getText().toString()));
        trailObj.setTrailCode(trailCodeStr);
        trailObj.setUserId("ms.romila@gmail.com");
        Log.d(TAG, "pouplateTrail method invoke stopped");
        return trailObj;
    }


    /**
     * API to form trail code
     * @param startDate
     * @param trailName
     * @return
     */
    private String constructTrailCode(String startDate,String trailName){
        StringBuilder trailCodeStr = new StringBuilder();
        trailCodeStr.append(startDate);
        trailCodeStr.append("_");
        trailCodeStr.append(trailName);

        return trailCodeStr.toString();


    }

}