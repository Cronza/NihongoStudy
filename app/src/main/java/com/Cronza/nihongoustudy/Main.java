package com.Cronza.nihongoustudy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Random;

public class Main extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Data Sets
    private String[] activeStudyData;
    private String[] activeStudyOptions;

    //UI References
    TextView characterText;
    TextView romanjiText;
    TextView englishText;
    Spinner studyTablesDropdown;
    CheckBox randomizeButton;

    //State Variables
    boolean randomize = false;

    //Variables
    Resources res;
    int currentIndex = 0;
    String splitDelim = ":";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the global vars
        res = getResources();

        //Start in the main menu layout
        setContentView(R.layout.main_menu_layout);
    }

    /*Updates the active study table to the selected one in the tables dropdown, and displays the
    first element in that table. Requires case sensitivity */
    void UpdateStudyTable(String table) {

        //Reset the counter
        currentIndex = 0;

        //Set the active table to reflect the chosen table
        activeStudyData = GetStringArrayResource(table);

        //Grab the UI view references
        characterText = (TextView)findViewById(R.id.CharacterText);
        englishText = (TextView)findViewById(R.id.EnglishText);
        romanjiText = (TextView)findViewById(R.id.RomanjiText);

        //Split the retrieve text using the defined delimiter
        String[] splitText = activeStudyData[currentIndex].split(splitDelim);
        characterText.setText(splitText[0]);
        romanjiText.setText("*****");
        englishText.setText("*****");
    }

    /* ---------------------- UI CLICK EVENTS ----------------------*/

    /*Display the appropriate romanji text for the active text*/
    public void TranslateRomanji(View view)
    {
        romanjiText.setText(activeStudyData[currentIndex].split(splitDelim)[1]);
    }

    /*Display the appropriate english text for the active text*/
    public void Translate(View view)
    {
        englishText.setText(activeStudyData[currentIndex].split(splitDelim)[2]);
    }

    /*Toggles whether randomization is used when parsing the study lists*/
    public void ToggleRandomization(View view)
    {
        randomize = ((CheckBox) view).isChecked();
        Log.d("Test", "Randomized!");

    }

    /*Display the Next Element in the Study Table*/
    public void NextText(View view)
    {
        //Has the user chosen to randomize the list?
        if(randomize){
            //RANDOMIZATION LOGIC
            int randomInt = currentIndex;
            while(randomInt == currentIndex) {
                randomInt = new Random().nextInt(activeStudyData.length);
            }
            currentIndex = randomInt;
        }
        //No? That's okay, continue on sequentially
        else{
            currentIndex += 1;

            //Have we reached the end?
            if(currentIndex >= activeStudyData.length)
            {
                currentIndex = 0;
            }
        }
        String[] splitText = activeStudyData[currentIndex].split(splitDelim);
        characterText.setText(splitText[0]);
        romanjiText.setText("*****");
        englishText.setText("*****");
    }

    /*Returns to the main menu*/
    public void Back(View view)
    {
        currentIndex = 0;
        setContentView(R.layout.main_menu_layout);
    }

    /*Transitions to the study table menu*/
    public void TransitionToStudy(View view)
    {
        //Change to the study view
        setContentView(R.layout.study_layout);

        //Initialize U.I elements now that they are contextually existent
        studyTablesDropdown = findViewById(R.id.StudyTableDropdown);
        randomizeButton = findViewById(R.id.enableRandomButton);

        //Update the available study drop-downs based on which study topic the user choice
        // (Retrieves the elements within the array specified in the study button tag)
        activeStudyOptions = res.getStringArray(res.getIdentifier(view.getTag().toString(), "array" , getPackageName()));
        ArrayAdapter<String> studyTableAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, activeStudyOptions);
        studyTablesDropdown.setAdapter(studyTableAdapter);
        studyTablesDropdown.setOnItemSelectedListener(this);

        //Update the active study material based on the default study option chosen by the theme choice
        UpdateStudyTable(activeStudyOptions[0].toLowerCase());
    }

    /* ---------------------- INTERFACE EVENTS ----------------------*/

    /*Called when the user chooses an item on a drop-down*/
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        UpdateStudyTable(parent.getItemAtPosition(pos).toString().toLowerCase());
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
    /* ---------------------- UTILITY FUNCTIONS ----------------------*/

    //Returns the string array data using the resource name
    private String[] GetStringArrayResource(String array)
    {
        return res.getStringArray(res.getIdentifier(array, "array" , getPackageName()));
    }



}
