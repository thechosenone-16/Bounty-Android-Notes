package bounty_note_test1;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.widget.EditText;
import android.widget.TextView;

@RunWith(AndroidTestRunner.class)
public class MainActivityTest {

	@Test
	public void test() {
		NotesHomeActivity  activity= new NotesHomeActivity();
		
		activity.onCreate(null);
		
		EditText editText= (EditText) activity.findViewById(R.id.quicknoteedittext);
		
		String text= editText.getText().toString();
		
		assertTrue(text.isEmpty());
		
	}

}
