package co.external.user;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContentProviderExternal extends Activity {

    Button insertButton;
    Button queryButton;
    Button updateButton;
    Button deleteButton;

    Button nextButton;
    Button previousButton;

    EditText firstname;
    EditText lastname;

    TextView idTv;
    TextView fnameTv;
    TextView lnameTv;

    Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);

        insertButton = (Button) findViewById(R.id.insertButton);
        queryButton = (Button) findViewById(R.id.queryButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        idTv = (TextView) findViewById(R.id.unique_id);
        fnameTv = (TextView) findViewById(R.id.fnameTextView);
        lnameTv = (TextView) findViewById(R.id.lnameTextView);

        nextButton = (Button) findViewById(R.id.nextButton);
        previousButton = (Button) findViewById(R.id.previousButton);

        insertButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri mNewUri;

                ContentValues mNewValues = new ContentValues();

                mNewValues.put("firstname", firstname.getText().toString().trim());
                mNewValues.put("lastname", lastname.getText().toString().trim());

                mNewUri = getContentResolver().insert(
                        Uri.parse("content://co.martinbrown.provider/namestable"), mNewValues);
                
                if (mNewUri == null) {
                	//--- No row inserted ---
                }

                clear();

            }
        });

        updateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ContentValues mUpdateValues = new ContentValues();

                mUpdateValues.put("firstname", firstname.getText().toString().trim());
                mUpdateValues.put("lastname", lastname.getText().toString().trim());

                String mSelectionClause = "firstname" +  " = ? " + " AND " +
                        "lastname" + " = ? ";

                String[] mSelectionArgs = {fnameTv.getText().toString().trim(), lnameTv.getText().toString().trim()};

                int mRowsUpdated = 0;

                mRowsUpdated = getContentResolver().update(
                        Uri.parse("content://co.martinbrown.provider/namestable"),
                        mUpdateValues,
                        mSelectionClause,
                        mSelectionArgs
                        );
                
                if (mRowsUpdated == 0) {
                	//--- No rows updated ---
                }

                clear();

            }
        });

        deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String mSelectionClause = "firstname" +  " = ? " + " AND " +
                        "lastname" + " = ? ";
                String[] mSelectionArgs = {fnameTv.getText().toString().trim(), lnameTv.getText().toString().trim()};

                int mRowsDeleted = 0;

                mRowsDeleted = getContentResolver().delete(
                        Uri.parse("content://co.martinbrown.provider/namestable"),
                        mSelectionClause,
                        mSelectionArgs
                        );
                
                if (mRowsDeleted == 0) {
                	//--- No rows deleted ---
                }

                clear();

            }
        });

        queryButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mCursor = getContentResolver().query(Uri.parse("content://co.martinbrown.provider/namestable"),
                        null, null, null, null);

                if(mCursor != null) {
                    if(mCursor.getCount() > 0) {
                        mCursor.moveToNext();
                        idTv.setText(mCursor.getString(0));
                        fnameTv.setText(mCursor.getString(1) + " ");
                        lnameTv.setText(mCursor.getString(2));
                    }
                }
            }
        });

        previousButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mCursor != null) {
                    if(!mCursor.moveToPrevious()){
                        mCursor.moveToLast();
                    }

                    setViews();
                }

            }
        });

        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mCursor != null) {
                    if(!mCursor.moveToNext()){
                        mCursor.moveToFirst();
                    }

                    setViews();
                }

            }
        });
    }

    private void setViews() {
        idTv.setText(mCursor.getString(0));
        fnameTv.setText(mCursor.getString(1) + " ");
        lnameTv.setText(mCursor.getString(2));
    }

    private void clear() {
        firstname.setText("");
        lastname.setText("");

        idTv.setText("");
        fnameTv.setText("");
        lnameTv.setText("");

        mCursor = null;
    }
}